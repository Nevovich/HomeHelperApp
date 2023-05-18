package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityAddCommentBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class AddCommentActivity extends AppCompatActivity {

    ActivityAddCommentBinding binding;
    private String articleID, mainCommentText, authorNickName;
    private Integer commentRate;
    private Long addCommentTime = System.currentTimeMillis()/1000;
    DatabaseReference commentsDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityAddCommentBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
//      Получаем id статьи, к которой оставляем коммент
        Bundle arguments = getIntent().getExtras();
        articleID = arguments.getString("uniqueID");

        binding.addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCommentText = binding.mainComment.getText().toString();
                commentRate = Integer.parseInt(binding.addTaskRate.getText().toString());
                if (!mainCommentText.isEmpty() & commentRate >= 1 & commentRate <= 5) {
                    Intent intent = new Intent(AddCommentActivity.this, ArticleActivity.class);
                    intent.putExtra("uniqueID", articleID);
//                    Получаем ник автора
                    DatabaseReference usersDB = FirebaseDatabase.getInstance().getReference("allUsers")
                            .child(LoginActivity.getLoggedInUserUID());

                    usersDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            authorNickName = snapshot.child("username").getValue(String.class);
                            commentsDB = FirebaseDatabase.getInstance().getReference("allComments");
//                Загрузка на сервер данных
                            CommentsEntity map = new CommentsEntity(mainCommentText, authorNickName, addCommentTime, commentRate, articleID);
//                    Toast.makeText(AddCommentActivity.this, authorNickName + map.getArticleRate(), Toast.LENGTH_SHORT).show();
                            commentsDB.push().setValue(map);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {}
                    });
                    startActivity(intent);
                    finish();
                }
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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