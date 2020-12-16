package com.example.mobileencryption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class EncryptActivity extends AppCompatActivity {

    private TextInputEditText messageTitle;
    private TextInputEditText messageContent;
    private TextInputEditText secretKey;
    private AppCompatButton encryptButton, backButton;

    int trig = 0;
    String msgTitle, Content, EncMessage, Seckey;
    private static final String ALGORITHM = "AES";
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        messageTitle =  findViewById(R.id.messageTitle);
        messageContent =  findViewById(R.id.messageContent);
        secretKey =  findViewById(R.id.secretKey);
        encryptButton =  findViewById(R.id.appCompatButtonLogin);
        backButton =  findViewById(R.id.backtoDash);

        dbHelper = new DatabaseHelper(EncryptActivity.this);

        messageTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                msgTitle = messageTitle.getText().toString().trim();
                int cm = msgTitle.length();
                if (!hasFocus) {
                    //Toast.makeText(EncryptActivity.this, "Total length "+cm,Toast.LENGTH_LONG).show();
                }
            }
        });

        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Content = messageContent.getText().toString().trim();
                Seckey = secretKey.getText().toString().trim();
                if (trig == 0){
                    try {
                        EncMessage = Encrypt(Content,Seckey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    messageContent.setText(EncMessage);
                    encryptButton.setText("Save Message");
                    trig = 1;
                }else{
                    dbHelper.insertNewTask(msgTitle,EncMessage);
                    Toast.makeText(EncryptActivity.this, "Message Saved Successfully!!!",Toast.LENGTH_LONG).show();
                }

            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(EncryptActivity.this, DashboardActivity.class);
                startActivity(intentLogin);
            }
        });

    }

    public String Encrypt(String valueToEnc, String passkey) throws Exception {
        SecretKeySpec key = generateKey(passkey);
        Cipher c = Cipher.getInstance(ALGORITHM);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encValue = c.doFinal(valueToEnc.getBytes());
        String encryptedValue = Base64.encodeToString(encValue, Base64.DEFAULT);
        return encryptedValue;
    }

    private SecretKeySpec generateKey( String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, ALGORITHM);
        return secretKeySpec;
    }

}
