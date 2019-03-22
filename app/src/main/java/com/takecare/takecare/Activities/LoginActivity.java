package com.takecare.takecare.Activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.takecare.takecare.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText loginEmail, loginPassword;
    private Button button_login;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private Intent HomeActivity;
    private Intent SideNavActivity;
    private ImageView loginPhoto;

    CheckBox chkbx;
    TextView tvForgotPass, tvNoAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        button_login = findViewById(R.id.button_login);
        progressBar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        HomeActivity = new Intent(this,NavigationActivity.class);
        chkbx = (CheckBox) findViewById(R.id.chkbx);

        chkbx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    loginPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    loginPassword.setSelection(loginPassword.getText().length());
                }
                else {
                    loginPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    loginPassword.setSelection(loginPassword.getText().length());
                }
            }
        });

        progressBar.setVisibility(View.INVISIBLE);
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                button_login.setVisibility(View.INVISIBLE);

                final String email = loginEmail.getText().toString();
                final String password = loginPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    showMessage("Please Verify All Field");
                    button_login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
                else
                {
                    signIn(email,password);
                }
            }
        });

        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
            }
        });

        initViews();
        initListeners();
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
    }

    private void signIn(String email, String password) {

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    progressBar.setVisibility(View.INVISIBLE);
                    button_login.setVisibility(View.VISIBLE);
                    showMessage("Successfully logged-in");
                    updateUI();

                }
                else {
                    showMessage(task.getException().getMessage());
                    button_login.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void updateUI() {
        startActivity(HomeActivity);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvForgotPass:
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.tvNoAccount:
                Intent intentRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
                break;
        }
    }

    private void initViews() {
        tvNoAccount = (TextView) findViewById(R.id.tvNoAccount);
        tvForgotPass = (TextView) findViewById(R.id.tvForgotPass);
    }

    private void initListeners(){
        tvNoAccount.setOnClickListener(this);
        tvForgotPass.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user != null) {
            //user is already connected  so we need to redirect him to home page
            updateUI();

        }
    }

}
