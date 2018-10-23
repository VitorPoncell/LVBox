package com.LVBoxAndroid.activity.Login;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.LVBoxAndroid.R;
import com.LVBoxAndroid.activity.Main.MainActivity;
import com.LVBoxAndroid.activity.SignUp.SignUpActivity;
import com.LVBoxAndroid.model.User;
import com.LVBoxAndroid.util.Base64Custom;
import com.LVBoxAndroid.util.Connection;
import com.LVBoxAndroid.util.FirebaseConfig;
import com.LVBoxAndroid.util.PreferencesCustom;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.prefs.Preferences;

public class LoginActivity extends AppCompatActivity {

    private LoginPresenter presenter;

    private TextView signup;
    private TextView forget;
    private EditText email;
    private EditText password;
    private Button login;
    private User user;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListenerUser;
    private String userIdentifier;

    @Override
    protected void onResume() {
        super.onResume();

        isUserLoged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        isUserLoged();

        signup = (TextView) findViewById(R.id.tv_signup);
        forget = (TextView) findViewById(R.id.tv_forget);
        email = (EditText) findViewById(R.id.edt_email);
        password = (EditText) findViewById(R.id.edt_password);
        login = (Button) findViewById(R.id.btn_login);

        presenter = new LoginPresenter(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Connection.verifyNet(LoginActivity.this)) {
                    presenter.login(email,password);
                }else {
                    Toast.makeText(LoginActivity.this,"Sem conecxao com internet",Toast.LENGTH_LONG).show();
                }
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this,R.style.AppTheme);

                builder.setTitle("Recuperar senha");
                builder.setMessage("Entre com seu emeial");

                final EditText recoverEmail = new EditText(LoginActivity.this);
                builder.setView(recoverEmail);

                builder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        presenter.passwordRecover(recoverEmail);
                    }
                });

                builder.create();
                builder.show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSignup();
            }
        });

    }

    public void showError(String error){
        Toast.makeText(LoginActivity.this,error,Toast.LENGTH_LONG).show();
    }

    public void confirmRecover(){
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this, R.style.AppTheme);

        builder.setTitle("Pronto!");
        builder.setMessage("Verifique seu email");
        builder.setCancelable(false);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }

    private void isUserLoged(){
        firebaseAuth = FirebaseConfig.getFirebaseAuth();
        if(firebaseAuth.getCurrentUser()!=null){
            openMain();
        }
    }

    public void openMain(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void openSignup(){
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
    }
}
