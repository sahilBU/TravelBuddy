package com.example.firebaselogindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.codelab.friendlychat.model.FirebaseModel;

public class SignupActivity extends AppCompatActivity {

    private TextView textView1;
    private TextView textView2;
    private EditText uemail;
    private EditText password;
    private Button submit_btn;

    private FirebaseModel model = new FirebaseModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_login);
        uemail = findViewById(R.id.uemail);
        password = findViewById(R.id.password);
        submit_btn = findViewById(R.id.submit_btn);

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        String email = uemail.getText().toString();
                        if (email.indexOf("@") <0){
                            Toast.makeText(SignupActivity.this, "Please Enter Valid Email Address", Toast.LENGTH_LONG).show();
                        }
                        else {
                            final String user_email = uemail.getText().toString();
                            final String pwd = password.getText().toString();
                            model.mFirebaseAuth.createUserWithEmailAndPassword(user_email, pwd)
                                    .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (!task.isSuccessful()) {
                                                Toast.makeText(
                                                        SignupActivity.this,
                                                        "Authentication Failed",
                                                        Toast.LENGTH_LONG).show();
                                                Log.v("error", task.getResult().toString());
                                            } else {
                                                Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(SignupActivity.this, "Sign Up Failed", Toast.LENGTH_LONG).show();
                    }
                }
        });
    }
}