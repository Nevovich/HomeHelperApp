package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityLoginBinding;
import com.example.saturdayapp.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private String loginInputEmail, loginInputPassword;
    private ColorStateList defaultTintColor;
    private FirebaseAuth mAuth;
    ColorStateList errorTintColor;
    private static String loggedInUserUID = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        errorTintColor = getBaseContext().getColorStateList(
                com.google.android.material.R.color.design_default_color_error);
        defaultTintColor = binding.loginPasswordInput.getBackgroundTintList();

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                loginInputEmail = String.valueOf(binding.loginEmailInput.getText());
                loginInputPassword = String.valueOf(binding.loginPasswordInput.getText());
                if (!loginInputPassword.isEmpty() &
                        !loginInputEmail.isEmpty()) {
//                    Вход via fb
                    mAuth.signInWithEmailAndPassword(loginInputEmail,loginInputPassword)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        loggedInUserUID = task.getResult().getUser().getUid();
                                        startActivity(intent);
                                        finish();
                                    } else { // Ошибочные данные входа
                                        binding.loginPasswordInput.setBackgroundTintList(errorTintColor);
                                        binding.loginEmailInput.setBackgroundTintList(errorTintColor);
                                    }
                                }
                            });

                } else { // Введены пустые значения
                    if (loginInputPassword.isEmpty()) {
                        binding.loginPasswordInput.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.loginPasswordInput.setBackgroundTintList(defaultTintColor);
                    }
                    if (loginInputEmail.isEmpty()) {
                        binding.loginEmailInput.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.loginEmailInput.setBackgroundTintList(defaultTintColor);
                    }
                }

            }
        });
//      Нажатие на плашку "зарегистрироваться"
        binding.createAccountNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                finish();
            }
        });
        Objects.requireNonNull(getSupportActionBar())
                .setDisplayHomeAsUpEnabled(true);
    }
    //    Реализация кнопки назад в actionBar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void setLoggedInUserUID(String loggedInUserUID) {
        LoginActivity.loggedInUserUID = loggedInUserUID;
    }

    public static String getLoggedInUserUID() {
        return loggedInUserUID;
    }
    public static boolean isUserLoggedIn() {
        return !loggedInUserUID.isEmpty();
    }
}