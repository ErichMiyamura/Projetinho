package com.example.user.projetinho.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.user.projetinho.Home.HomeActivity;
import com.example.user.projetinho.R;
import com.example.user.projetinho.Register.RegisterActivity;
import com.example.user.projetinho.Splash.SplashActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

public class LoginActivity extends AppCompatActivity {

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";

    private TextInputEditText textInputEditLogin;
    private TextInputEditText textInputEditPassword;
    private CircleImageView imageViewProfile;
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private FirebaseAuth firebaseAuth;
    private Button btnEntrar;
    private Button btnCadastrarSe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();

        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));
        firebaseAuth = firebaseAuth.getInstance();
        btnEntrar = findViewById(R.id.btn_entrar);
        btnCadastrarSe = findViewById(R.id.btn_cadastrar_se);

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.i("LOG", "onSuccess: " + loginResult);
                //getUserProfile();
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.i("LOG", "onCancel: Cancelei");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i("LOG", "onError: " + error.getMessage());
            }
        });

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();

        if (isLoggedIn){
            //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            Toast.makeText(this, "Já está logado", Toast.LENGTH_SHORT).show();
        }

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });

        btnCadastrarSe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initViews() {
        textInputEditLogin = findViewById(R.id.textInputEditText_login_email);
        textInputEditPassword = findViewById(R.id.textInputEditText_login_senha);
        loginButton = findViewById(R.id.btn_login_facebook_default);
        imageViewProfile = findViewById(R.id.imageview_user_login);
    }

    private void getUserProfile() {
        ProfileTracker profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {

                // Agora que temos o token do usuario podemos requisitar os dados
                Profile.fetchProfileForCurrentAccessToken();
                if (currentProfile != null) {
                    String fbUserId = currentProfile.getId();
                    String profileUrl = currentProfile.getProfilePictureUri(200, 200).toString();
                    Log.d("FB profile", "got new/updated profile from thread " + fbUserId);
                    Picasso.get().load(profileUrl).into(imageViewProfile);
                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
            }
        };
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        //updateUI(currentUser);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("LOG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("LOG", "signInWithCredential:success");
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("LOG", "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}
