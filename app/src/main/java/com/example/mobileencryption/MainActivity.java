package com.example.mobileencryption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {
    private final AppCompatActivity activity = MainActivity.this;
    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    private TextInputEditText textInputEditTextEmail;
    private TextInputEditText textInputEditTextPassword;

    private Button appCompatButtonLogin;

    TextView reglink;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        textInputLayoutEmail =  findViewById(R.id.userIDTextInputLayout);
        textInputLayoutPassword =  findViewById(R.id.passwordTextInputLayout);

        textInputEditTextEmail =  findViewById(R.id.userIDTextInputEditText);
        textInputEditTextPassword =  findViewById(R.id.passwordTextInputEditText);

        appCompatButtonLogin = findViewById(R.id.appCompatButtonLogin);

        reglink =   findViewById(R.id.TextViewLinkRegister);

        databaseHelper = new DatabaseHelper(activity);
        inputValidation = new InputValidation(activity);

        appCompatButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyFromSQLite();
            }
        });

        reglink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentRegister = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intentRegister);
            }
        });

    }
    private void verifyFromSQLite(){
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))){
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))){
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.password_error_message))){
            return;
        }

        if (databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim()
                , textInputEditTextPassword.getText().toString())){
            Intent accountsIntent = new Intent(activity, DashboardActivity.class);
            accountsIntent.putExtra("EMAIL", textInputEditTextEmail.getText().toString().trim());
            emptyInputEditText();
            startActivity(accountsIntent);
        }else{
            Toast.makeText(activity, getString(R.string.invalid_email_password), Toast.LENGTH_LONG).show();
            //Snackbar.make(nestedScrollView, getString(R.string.invalid_email_password), Snackbar.LENGTH_LONG).show();

        }
    }

    private void emptyInputEditText(){
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
    }
}
