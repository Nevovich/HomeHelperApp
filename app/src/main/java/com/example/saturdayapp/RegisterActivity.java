package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private String registerInputEmail, registerInputPassword, registerInputPassword2, registerInputNickname, loginUID;
    private ColorStateList defaultTintColor;
    private FirebaseAuth mAuth;
    private DatabaseReference userDB;
    private String USER_KEY = "allUsers";
    ColorStateList errorTintColor;
    UserInfo newUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        errorTintColor = getBaseContext().getColorStateList(
                com.google.android.material.R.color.design_default_color_error);
        mAuth = FirebaseAuth.getInstance();

        binding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                registerInputEmail = String.valueOf(binding.registerEmailInput.getText());
                registerInputNickname = String.valueOf(binding.registerNicknameInput.getText());
                registerInputPassword = String.valueOf(binding.registerPasswordInput.getText());
                registerInputPassword2 = String.valueOf(binding.registerPasswordInput2.getText());
                if (Objects.equals(registerInputPassword, registerInputPassword2) &!registerInputPassword.isEmpty() &!registerInputNickname.isEmpty() & !registerInputPassword2.isEmpty() &
                        !registerInputEmail.isEmpty() & registerInputEmail.contains("@") & registerInputEmail.contains(".") & registerInputPassword.length() >=6) {
                    mAuth.createUserWithEmailAndPassword(registerInputEmail, registerInputPassword2)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        loginUID = task.getResult().getUser().getUid();
                                        LoginActivity.setLoggedInUserUID(loginUID);
//                                        Push в базу данных вместе с ником
                                        newUser = new UserInfo(loginUID, registerInputNickname, registerInputEmail);
                                        userDB = FirebaseDatabase.getInstance().getReference(USER_KEY);
                                        userDB.child(loginUID).setValue(newUser);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, task.getResult().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                } else { // Введены пустые значения
                    if (registerInputPassword.isEmpty() || !(registerInputEmail.contains("@") & registerInputEmail.contains("."))) {
                        binding.registerPasswordInput.setBackgroundTintList(errorTintColor);
                    } else { binding.registerPasswordInput.setBackgroundTintList(defaultTintColor); }
                    if (registerInputPassword2.isEmpty()) {
                        binding.registerPasswordInput2.setBackgroundTintList(errorTintColor);
                    } else { binding.registerPasswordInput2.setBackgroundTintList(defaultTintColor); }
                    if (registerInputNickname.isEmpty()) {
                        binding.registerNicknameInput.setBackgroundTintList(errorTintColor);
                    } else { binding.registerNicknameInput.setBackgroundTintList(defaultTintColor); }
                    if (registerInputEmail.isEmpty()) {
                        binding.registerEmailInput.setBackgroundTintList(errorTintColor);
                    } else { binding.registerEmailInput.setBackgroundTintList(defaultTintColor); }
//                    Пароли не совпадают
                    if (!Objects.equals(registerInputPassword, registerInputPassword2)) {
                        Toast.makeText(RegisterActivity.this, "Введенные пароли не совпадают", Toast.LENGTH_SHORT).show();
                        binding.registerPasswordInput.setBackgroundTintList(errorTintColor);
                        binding.registerPasswordInput2.setBackgroundTintList(errorTintColor);
                    }else {
                        binding.registerPasswordInput.setBackgroundTintList(defaultTintColor);
                        binding.registerPasswordInput2.setBackgroundTintList(defaultTintColor);
                    }
                    if (registerInputPassword.length() < 6) {
                        Toast.makeText(RegisterActivity.this, "Пароль должен содержать больше 6 символов", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        Objects.requireNonNull(
                        getSupportActionBar())
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
}