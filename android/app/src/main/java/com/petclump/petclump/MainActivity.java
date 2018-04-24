package com.petclump.petclump;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.petclump.petclump.models.Specie;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    // Google Sign-ins
    private static final int RC_SIGN_IN = 9001;
    private GoogleApiClient mGoogleApiClient;


//    // Facebook Sign-ins
//    LoginButton loginButton;
//    CallbackManager callbackManager;

    // UI
    TextView animalText, uidText;
    Context c;
    private static final String TAG = "Entry Point";


    private void setupUI(){
        // Init
        c = getApplicationContext();
        uidText = findViewById(R.id.uidText);
        animalText = findViewById(R.id.animalText);
        Button pickButton = findViewById(R.id.button_go_upload_photo);
        Button settingsButton = findViewById(R.id.button_settings);

        // Upload photo activity
        pickButton.setOnClickListener(v -> {
            Intent nextScreen = new Intent(c, UploadPhotoActivity.class);
            startActivity(nextScreen);
        });

        // Setting page activity
        settingsButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UserInfoActivity.class);
            startActivity(nextScreen);
        });

        // Animal text on screen
        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        for (Specie s: Specie.values()) {
                            Thread.sleep(1000);
                            runOnUiThread( ()-> animalText.setText(s.getName(c)));
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d("Interrupted", "run: " + e.getMessage());
                }
            }
        };
        t.start();
    }

    private void setupGoogleLogin(){

        GoogleSignInOptions gos = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.default_web_client_id))
                .build();
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gos)
                .build();

        SignInButton mGoogleSignInBtn = findViewById(R.id.googleBtn);
        mGoogleSignInBtn.setOnClickListener(v -> {
            Intent doSignIn = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            startActivityForResult(doSignIn, RC_SIGN_IN);
        });

        Button mGoogleSignOutBtn = findViewById(R.id.mGoogleSignOut);
        mGoogleSignOutBtn.setOnClickListener(v ->
                Auth.GoogleSignInApi.signOut(mGoogleApiClient)
                        .setResultCallback(status -> uidText.setText("Signed out")
                        ));

        OptionalPendingResult<GoogleSignInResult> optionalPendingResult = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (optionalPendingResult.isDone()) {
            GoogleSignInResult googleSignInResult = optionalPendingResult.get();
            handleSignInResult(googleSignInResult);
        } else {
            optionalPendingResult.setResultCallback(result -> handleSignInResult(result));
        }
        //FirebaseAuth.getInstance().addAuthStateListener(authListener);

    }
    @Override
    protected void onStart() {
        super.onStart();
        setContentView(R.layout.activity_main);
        setupUI();
        setupGoogleLogin();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            this.handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result){
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            if (account != null) {
                firebaseAuthWithGoogle(account);
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                if (mAuth.getCurrentUser() != null) {
                    this.uidText.setText(mAuth.getCurrentUser().getUid());
                } else {
                    Log.d(TAG, "handleSignInResult: No current user");
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getIdToken());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }

                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed: Sign in connection failed");
    }
}
