package com.petclump.petclump;
import android.support.annotation.NonNull;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.widget.Toast;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.CallbackManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import com.petclump.petclump.models.Specie;

public class MainActivity extends AppCompatActivity {

    /*** Google Sign In set-up field ***/
    // Google Sign In button
    private SignInButton mGoogleBtn;
    private Button mGoogleSignOutBtn;
    // Google Sign In code
    private static final int RC_SIGN_IN = 1;
    // Google Sign In Client
    GoogleSignInClient mGoogleSignInClient;
    // Firebase Authentication
    FirebaseAuth mAuth;
    // TAG used for Google signIn debug
    private static final String TAG_Google_LogIn = "Google_LogIn";

    /*** Facebook Sign In set-up field ***/
    // facebook Signin button
    LoginButton loginButton;
    // facebook SignIn callbackmanager
    CallbackManager callbackManager;

    /***other fields***/
    Button pickButton, settingsButton;
    TextView hello;
    Context c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setup facebook sdk
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        c = getApplicationContext();

        pickButton = findViewById(R.id.button_go_upload_photo);
        pickButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UploadPhotoActivity.class);
            startActivity(nextScreen);
        });

        settingsButton = findViewById(R.id.button_settings);
        settingsButton.setOnClickListener(v -> {
            //Starting a new Intent
            Intent nextScreen = new Intent(c, UserInfoActivity.class);
            startActivity(nextScreen);
        });

        hello = findViewById(R.id.hello);


        Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        for (Specie s: Specie.values()) {
                            Thread.sleep(1000);
                            runOnUiThread( ()-> hello.setText(s.getName(c)));
                        }
                    }
                } catch (InterruptedException e) {
                    Log.d("Interrupted", "run: " + e.getMessage());
                }
            }
        };
        t.start();
        // setup google buttons and mAuth
        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);
        mAuth = FirebaseAuth.getInstance();
        mGoogleSignOutBtn = (Button) findViewById(R.id.mGoogleSignOut);

        // setup facebook SignIn button and callback
        loginButton = (LoginButton)findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();

        // setup facebook callback
        // Callback registration
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(MainActivity.this, R.string.User_Signed_In, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(MainActivity.this, R.string.Login_Cancelled, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(MainActivity.this, R.string.Authentication_failed, Toast.LENGTH_SHORT).show();
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Create Google Sign In Client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // use the Google signIn button to sign in
        // call signIn()
        mGoogleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mAuth.getCurrentUser() != null){
                    Toast.makeText(MainActivity.this, R.string.User_has_logined_in, Toast.LENGTH_SHORT).show();
                }else {
                    GoogleSignIn();
                }
            }
        });

        // Google Sign Out
        mGoogleSignOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    // to enable next loging in to choose user
                    mGoogleSignInClient.signOut();
                    if(mAuth.getCurrentUser() == null)
                        Toast.makeText(MainActivity.this, R.string.User_Signed_Out, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // Google Sign In Method
    private void GoogleSignIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    // Google Sign In Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // facebook callback
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        // google result
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, an error message would pop up
                Toast.makeText(MainActivity.this ,e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
    }
    // Google authentication with Firebase
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG_Google_LogIn, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
                            Log.d(TAG_Google_LogIn, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, R.string.User_Signed_In, Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG_Google_LogIn, "signInWithCredential:failure", task.getException());
                            Toast.makeText(MainActivity.this, R.string.Authentication_failed, Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }

}
