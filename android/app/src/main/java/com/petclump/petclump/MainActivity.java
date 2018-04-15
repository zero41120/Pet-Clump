package com.petclump.petclump;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


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

public class MainActivity extends AppCompatActivity {

    /*** Sign In set-up field ***/
    // Google Sign In button
    private SignInButton mGoogleBtn;
    // Google Sign In code
    private static final int RC_SIGN_IN = 1;
    // Google Sign In Client
    GoogleSignInClient mGoogleSignInClient;
    // Firebase Authentication
    FirebaseAuth mAuth;
    // TAG used for Google signIn debug
    private static final String TAG_Google_LogIn = "Google_LogIn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGoogleBtn = (SignInButton) findViewById(R.id.googleBtn);
        mAuth = FirebaseAuth.getInstance();


        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // Create Google Sign In Client
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        /*** program entry ***/
        // use the Google signIn button to sign in
        // call signIn()
        mGoogleBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn();
            }
        });
    }

    // Google Sign In Method
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    /*** if signed-in go to next activity ***/

    @Override
    protected void onStart(){
        super.onStart();

        if(mAuth.getCurrentUser() != null){
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }
    }
    // Google Sign In Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
