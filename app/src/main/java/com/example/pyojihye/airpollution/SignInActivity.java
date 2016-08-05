package com.example.pyojihye.airpollution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        Button buttonLogin = (Button) findViewById(R.id.buttonLogin);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            EditText editTextEmail = (EditText) findViewById(R.id.editTextEmail);
            EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

            @Override
            public void onClick(View view) {
                String email = editTextEmail.getText().toString();
                String password = editTextPassword.getText().toString();

                if (email.equals("") || password.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill in all information", Toast.LENGTH_LONG).show();
                    return;
                } else {
//                    HttpConnection httpConnection = new HttpConnection(getApplicationContext());
//                    httpConnection.execute(email,password);
                    Intent IntentSettingDevice = new Intent(SignInActivity.this, SettingDeviceActivity.class);
                    startActivity(IntentSettingDevice);
                }
            }
        });
    }

    public void textViewSignInClick(View v) {
        if (v.isClickable()) {
            Intent signUpIntent = new Intent(SignInActivity.this, SignUpActivity.class);
            startActivity(signUpIntent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
