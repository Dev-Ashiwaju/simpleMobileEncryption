package com.example.mobileencryption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    NestedScrollView nestedScrollView;

    TextInputLayout textInputLayoutName;
    TextInputLayout textInputLayoutEmail;
    TextInputLayout textInputLayoutPassword;
    TextInputLayout textInputLayoutConfirmPassword;

    TextInputEditText textInputEditTextName;
    TextInputEditText textInputEditTextEmail;
    TextInputEditText textInputEditTextPassword;
    TextInputEditText textInputEditTextConfirmPassword;

    TextView loglink;
    Button signup;

    private InputValidation inputValidation;
    private DatabaseHelper databaseHelper;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();

        nestedScrollView = findViewById(R.id.nestedScrollViewRegister);

        textInputLayoutName = findViewById(R.id.userIDTextInputLayout);
        textInputLayoutEmail = findViewById(R.id.userEmail);
        textInputLayoutPassword = findViewById(R.id.userPassword);
        textInputLayoutConfirmPassword = findViewById(R.id.confirmPassword);

        textInputEditTextName = (TextInputEditText) findViewById(R.id.fullname);
        textInputEditTextEmail = (TextInputEditText) findViewById(R.id.editUserEmail);
        textInputEditTextPassword = (TextInputEditText) findViewById(R.id.editUserPassword);
        textInputEditTextConfirmPassword = (TextInputEditText) findViewById(R.id.editConfirmPassword);

        loglink = findViewById(R.id.backToLogin);
        signup = findViewById(R.id.registerButton);

        inputValidation = new InputValidation(RegisterActivity.this);
        databaseHelper = new DatabaseHelper(RegisterActivity.this);
        user = new User();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataToSQLite();
            }
        });
        loglink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(intentLogin);
            }
        });
    }

    public void postDataToSQLite() {

        if (!inputValidation.isInputEditTextFilled(textInputEditTextName, textInputLayoutName, getString(R.string.name_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextEmail(textInputEditTextEmail, textInputLayoutEmail, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextFilled(textInputEditTextPassword, textInputLayoutPassword, getString(R.string.email_error_message))) {
            return;
        }
        if (!inputValidation.isInputEditTextMatches(textInputEditTextPassword, textInputEditTextConfirmPassword,
                textInputLayoutConfirmPassword, getString(R.string.password_not_match))) {
            return;
        }

        if (!databaseHelper.checkUser(textInputEditTextEmail.getText().toString().trim())) {

            user.setName(textInputEditTextName.getText().toString().trim());
            user.setEmail(textInputEditTextEmail.getText().toString().trim());
            user.setPassword(textInputEditTextPassword.getText().toString().trim());

            databaseHelper.addUser(user);

            // Snack Bar to show success message that record saved successfully
            Snackbar.make(nestedScrollView, getString(R.string.success_message), Snackbar.LENGTH_LONG).show();
            emptyInputEditText();


        } else {
            // Snack Bar to show error message that record already exists
            Toast.makeText(RegisterActivity.this, getString(R.string.email__exist_error_message), Toast.LENGTH_LONG).show();
            //Snackbar.make(nestedScrollView, getString(R.string.email__exist_error_message), Snackbar.LENGTH_LONG).show();
        }
    }

    private void emptyInputEditText() {
        textInputEditTextName.setText(null);
        textInputEditTextEmail.setText(null);
        textInputEditTextPassword.setText(null);
        textInputEditTextConfirmPassword.setText(null);
    }
}