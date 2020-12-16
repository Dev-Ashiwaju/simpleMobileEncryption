package com.example.mobileencryption;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class DecryptActivity extends AppCompatActivity {

    private TextInputEditText messageTitle;
    private TextInputEditText messageContent;
    private TextInputEditText secretKey;
    private AppCompatButton encryptButton;
    private AppCompatButton backButton;

    int trig = 0;
    String msgTitle, Content, EncMessage, Seckey;
    private static final String ALGORITHM = "AES";
    DatabaseHelper dbHelper;

    String encText;
    public static String Decrypt(String encryptedValue, String passkey) throws Exception {
        SecretKeySpec key = generateKey(passkey);
        Cipher c = Cipher.getInstance("AES" );
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = Base64.decode(encryptedValue, Base64.DEFAULT);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }
    private static SecretKeySpec generateKey(String password) throws Exception {
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0, bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
        return secretKeySpec;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt);
        messageTitle = (TextInputEditText) findViewById(R.id.messageTitle);
        messageContent = (TextInputEditText) findViewById(R.id.messageContent);
        secretKey = (TextInputEditText) findViewById(R.id.secretKey);
        encryptButton = (AppCompatButton) findViewById(R.id.appCompatButtonLogin);
        backButton = (AppCompatButton) findViewById(R.id.backtoDash);

        dbHelper = new DatabaseHelper(DecryptActivity.this);
        Bundle extras = getIntent().getExtras();

        if(extras !=null) {
            String Value = extras.getString("id");

            if(Value != ""){
                Cursor rs = dbHelper.getData(Value);
                rs.moveToFirst();

                String title = rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_TASK_NAME));
                String mesageContent = rs.getString(rs.getColumnIndex(DatabaseHelper.COLUMN_TASK_CONTENT));

                if (!rs.isClosed())  {
                    rs.close();
                }

                messageTitle.setText(title);
                messageTitle.setFocusable(false);
                messageTitle.setClickable(false);

                messageContent.setText(mesageContent);
                messageContent.setFocusable(false);
                messageContent.setClickable(false);

                secretKey.setFocusable(true);
                secretKey.setClickable(true);

            }
        }
        encText = messageContent.getText().toString();
        encryptButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                String secretKeyString = secretKey.getText().toString();
                String msgContent = messageContent.getText().toString();
                if (secretKeyString.length() > 0 && secretKeyString.length() == 6) {
                    try {
                        String result = Decrypt(encText, secretKeyString);
                        messageContent.setText(result);
                    } catch (Exception e) {
                        messageContent.setText("Message Cannot Be Decrypted, Wrong Secret key provided!");
                    }


                } else
                    Toast.makeText(getBaseContext(),"You must provide a 6-character secret key!",
                            Toast.LENGTH_SHORT).show();

            }

        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentLogin = new Intent(DecryptActivity.this, DashboardActivity.class);
                startActivity(intentLogin);
            }
        });
    }
}
