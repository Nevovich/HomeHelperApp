package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.saturdayapp.databinding.ActivityEditArticleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditArticleActivity extends AppCompatActivity {

    private ActivityEditArticleBinding binding;
    private String articleAuthorID, articleHeader, articleDescription, articleVideoLink, articleID;
    private DatabaseReference articleDB;
    private final String LIST_KEY = "allArticles";
    private Integer articleTime;

    List<ListEntity> articleToAdd = new ArrayList<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditArticleBinding.inflate(getLayoutInflater());
        ColorStateList defaultTintColor = binding.editArticleLink.getBackgroundTintList();
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        articleID = arguments.getString("uniqueID");

//        Иницализация бд
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("allArticles");
        databaseReference1.child(articleID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
//                    Вставка из бд в EditText
                    binding.editArticleHeader.setText(ds.child("header").getValue().toString());
                    binding.editArticleDescription.setText(ds.child("description").getValue().toString());
                    binding.editArticleLink.setText("youtu.be/" + ds.child("videoLink").getValue().toString());
                    articleAuthorID = ds.child("authorID").getValue().toString();
                    articleTime = ds.child("articleTaskTime").getValue(Integer.class);
                    binding.editArticleTime.setText(articleTime);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
//          Нажатие на кнопку подтверждения
        binding.confirmEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditArticleActivity.this, MainActivity.class);
                articleHeader = String.valueOf(binding.editArticleHeader.getText());
                articleDescription = String.valueOf(binding.editArticleDescription.getText());
                articleVideoLink = String.valueOf(binding.editArticleLink.getText());
                articleTime = Integer.parseInt(binding.editArticleTime.getText().toString());
                if (!articleDescription.isEmpty() &
                        !articleHeader.isEmpty() &
                        (articleVideoLink.contains("youtu.be/") |
                                articleVideoLink.contains("youtube.com/watch?v="))) {
                    articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY).child(articleID).child("0");
//                Загрузка на сервер данных
                    HashMap map = new HashMap();
                    map.put("description", articleDescription);
                    map.put("header", articleHeader);
                    map.put("articleTaskTime", articleTime);
                    map.put("videoLink", articleVideoLink.subSequence(articleVideoLink.length()-11, articleVideoLink.length()).toString());
                    articleDB.updateChildren(map);
//                    articleDB.push().setValue(articleToAdd);
                    startActivity(intent);
                    finish();
                } else {
                    ColorStateList errorTintColor = getBaseContext().getColorStateList(
                            com.google.android.material.R.color.design_default_color_error);
                    if (articleDescription.isEmpty()) {
                        binding.editArticleDescription.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.editArticleDescription.setBackgroundTintList(defaultTintColor);
                    }
                    if (articleHeader.isEmpty()) {
                        binding.editArticleHeader.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.editArticleHeader.setBackgroundTintList(defaultTintColor);
                    }
                    if (!(articleVideoLink.contains("youtu.be/") |
                            articleVideoLink.contains("youtube.com/watch?v="))) {
                        binding.editArticleLink.setBackgroundTintList(errorTintColor);
                    } else {
                        binding.editArticleLink.setBackgroundTintList(defaultTintColor);
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}