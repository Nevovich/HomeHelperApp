package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.saturdayapp.databinding.ActivityCreateBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CreateActivity extends AppCompatActivity {

    private ActivityCreateBinding binding;
    private DatabaseReference articleDB;
    private final String LIST_KEY = "allArticles";

    String articleHeader, articleDescription, articleVideoLink;

    List<ListEntity> articleToAdd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        ColorStateList defaultTintColor = binding.createDescription.getBackgroundTintList();
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                articleHeader = String.valueOf(binding.createHeader.getText());
                articleDescription = String.valueOf(binding.createDescription.getText());
                articleVideoLink = String.valueOf(binding.createLink.getText());
                if (!articleDescription.isEmpty() &
                        !articleHeader.isEmpty() &
                        (articleVideoLink.contains("youtu.be/") |
                                articleVideoLink.contains("youtube.com/watch?v="))) {
                    articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY);
                    articleToAdd.add(new ListEntity( articleDB.getKey(),
                            articleHeader,
                            articleDescription,
                            articleVideoLink.subSequence(articleVideoLink.length()-11, articleVideoLink.length()).toString()
                    ));
//                Загрузка на сервер данных
                    articleDB.push().setValue(articleToAdd);
                    startActivity(intent);
                    finish();
                } else {
                    ColorStateList errorTintColor = getBaseContext().getColorStateList(
                            com.google.android.material.R.color.design_default_color_error);
                    if (articleDescription.isEmpty()) {
                        binding.createDescription.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.createDescription.setBackgroundTintList(defaultTintColor);
                    }
                    if (articleHeader.isEmpty()) {
                        binding.createHeader.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.createHeader.setBackgroundTintList(defaultTintColor);
                    }
                    if (!(articleVideoLink.contains("youtu.be/") |
                            articleVideoLink.contains("youtube.com/watch?v="))) {
                        binding.createLink.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.createLink.setBackgroundTintList(defaultTintColor);
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