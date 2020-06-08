package com.example.hw522;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText usernameEditText;
    EditText passwordEditText;
    Button loginButton;
    Button registerButton;
    String usernameStr;
    String loginFile;
    CheckBox saveCheck;
    SharedPreferences prefCheckbox;
    SharedPreferences.Editor editor;
    String PREF_FILE;
    String Is_Check;
    private static final int REQUEST_PERMISSION_WRITE = 1001;
    private boolean permissionGranted;


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
        return(new File(Environment.getExternalStorageDirectory(), loginFile));
    }

    private void init() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        registerButton = findViewById(R.id.register);
        loginFile = "login_file.txt";
        saveCheck = findViewById(R.id.checkBox);
        PREF_FILE = "CheckboxPos";
        Is_Check = "isCheck";
    }

    public void registerClick(View view) {
        FileOutputStream fileOutputStream = null;
        if (!saveCheck.isChecked()) {
            try {
                usernameStr = usernameEditText.getText().toString() + "\n" + passwordEditText.getText().toString();
                if (usernameStr.equals("")) {
                    Toast.makeText(MainActivity.this, "Error login or password", Toast.LENGTH_SHORT).show();
                } else {
                    fileOutputStream = openFileOutput(loginFile, MODE_PRIVATE);
                    fileOutputStream.write(usernameStr.getBytes());
                    Toast.makeText(MainActivity.this, "register done!", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            if(!permissionGranted){
                checkPermissions();
                return;
            }
                FileOutputStream fos = null;
                try {
                    usernameStr = usernameEditText.getText().toString() + "\n" + passwordEditText.getText().toString();
                    fos = new FileOutputStream(getExternalPath());
                    fos.write(usernameStr.getBytes());
//                Toast.makeText(MainActivity.this, "register !", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    if (fos != null) {
                        try {
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    }

    public void loginClick(View view) {

        FileInputStream fileInputStream = null;
        if (!saveCheck.isChecked()){
        try {
            usernameStr = usernameEditText.getText().toString();
            fileInputStream = openFileInput(loginFile);
            byte[] bytesLogin = new byte[fileInputStream.available()];
            fileInputStream.read(bytesLogin);
            String openLogin = new String(bytesLogin);
            String [] equalsLogin = openLogin.split("\n");
            if (equalsLogin[0].equals(usernameStr) && equalsLogin[1].equals(passwordEditText.getText().toString())) {
                Toast.makeText(MainActivity.this, "welcome", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Error login or password", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            }
        }
        else{

        }
    }

    public boolean isExternalStorageWriteable(){
        String state = Environment.getExternalStorageState();
        return  Environment.MEDIA_MOUNTED.equals(state);
    }

    public boolean isExternalStorageReadable(){
        String state = Environment.getExternalStorageState();
        return  (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    private boolean checkPermissions(){
        if(!isExternalStorageReadable() || !isExternalStorageWriteable()){
            Toast.makeText(this, "Внешнее хранилище не доступно", Toast.LENGTH_LONG).show();
            return false;
        }
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionCheck!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_WRITE);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case REQUEST_PERMISSION_WRITE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    permissionGranted = true;
                    Toast.makeText(this, "Разрешения получены", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(this, "Необходимо дать разрешения", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

}
