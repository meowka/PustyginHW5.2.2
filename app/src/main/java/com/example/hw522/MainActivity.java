package com.example.hw522;


import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private String usernameStr;
    private String passwordStr;
    private String loginFile;
    private CheckBox saveCheck;
    private SharedPreferences prefCheckbox;
    private SharedPreferences.Editor editor;
    private String PREF_FILE;
    private String Is_Check;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        prefCheckbox = getSharedPreferences(PREF_FILE, MODE_PRIVATE);
        saveCheck.setChecked(prefCheckbox.getBoolean(Is_Check, false));
        saveCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                editor = prefCheckbox.edit();
                editor.putBoolean(Is_Check, b);
                editor.apply();
            }
        });
    }

    private File getExternalPath() {
        return (new File(getExternalFilesDir("myFileDir"), loginFile));
    }

    private void init() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginFile = "login_file.txt";
        saveCheck = findViewById(R.id.checkBox);
        PREF_FILE = "CheckboxPos";
        Is_Check = "isCheck";
    }

    public void registerClick(View view) {
        if (!saveCheck.isChecked()) {
            usernameStr = usernameEditText.getText().toString() + "\n" + passwordEditText.getText().toString();
            passwordStr = passwordEditText.getText().toString();
            try (FileOutputStream fileOutputStream = openFileOutput(loginFile, MODE_PRIVATE)) {
                if (usernameStr.equals("") && passwordStr.equals("")) {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                } else {
                    fileOutputStream.write(usernameStr.getBytes());
                    Toast.makeText(MainActivity.this, R.string.RegDone, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            usernameStr = usernameEditText.getText().toString() + "\n" + passwordEditText.getText().toString();
            try (FileOutputStream fos = new FileOutputStream(getExternalPath())) {
                fos.write(usernameStr.getBytes());
                Toast.makeText(MainActivity.this, R.string.RegDone, Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void loginClick(View view) {

        if (!saveCheck.isChecked()) {
            usernameStr = usernameEditText.getText().toString();
            passwordStr = passwordEditText.getText().toString();
            try (FileInputStream fileInputStream = openFileInput(loginFile)) {
                byte[] bytesLogin = new byte[fileInputStream.available()];
                fileInputStream.read(bytesLogin);
                String openLogin = new String(bytesLogin);
                String[] equalsLogin = openLogin.split("\n");
                if (equalsLogin[0].equals(usernameStr) && equalsLogin[1].equals(passwordStr)) {
                    Toast.makeText(MainActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            usernameStr = usernameEditText.getText().toString();
            passwordStr = passwordEditText.getText().toString();
            try (FileInputStream fileInputStream = new FileInputStream(getExternalPath());) {
                byte[] bytesLogin = new byte[fileInputStream.available()];
                fileInputStream.read(bytesLogin);
                String openLogin = new String(bytesLogin);
                String[] equalsLogin = openLogin.split("\n");
                if (equalsLogin[0].equals(usernameStr) && equalsLogin[1].equals(passwordStr)) {
                    Toast.makeText(MainActivity.this, R.string.welcome, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
