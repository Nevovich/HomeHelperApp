package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityArticleBinding;
import com.example.saturdayapp.ui.home.HomeFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;
    private String articleAuthorID, articleVideoLink, articleID;
    WebView youtubeWebView;
    private Integer articleTaskTime;
    public static boolean numberStart = false;
    private DatabaseReference commentsDB;
    private List<CommentsEntity> comments = new ArrayList<CommentsEntity>();

    private void commentsAdapterCall() {
        CommentsAdapter adapter = new CommentsAdapter(ArticleActivity.this, comments);
        binding.commentsRecycler
                .setLayoutManager(new LinearLayoutManager(ArticleActivity.this));
        binding.commentsRecycler.setAdapter(adapter);
        numberStart = true;
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArticleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();
        articleID = arguments.getString("uniqueID");

//        Youtube embeddind
        youtubeWebView = binding.youtubeWebView;
        youtubeWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        WebSettings webSettings = youtubeWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setUseWideViewPort(true);

        FirebaseDatabase ref = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference1 = ref.getReference("allArticles");
        databaseReference1.child(articleID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                        binding.articleHeader.setText(ds.child("header").getValue().toString());
                        binding.articleText.setText(ds.child("description").getValue().toString());
                        articleVideoLink = ds.child("videoLink").getValue().toString();
                        youtubeWebView.loadUrl("https://www.youtube.com/embed/" + articleVideoLink);
                        articleVideoLink = binding.videolinkAnnotation.getText() + articleVideoLink;
                        binding.videolinkAnnotation.setText(articleVideoLink);
                        articleAuthorID = ds.child("authorID").getValue(String.class);
                        articleTaskTime = ds.child("taskTime").getValue(Integer.class);
                        binding.taskTime.setText(articleTaskTime + " мин.");
//                        Вставляем никнейм автора
                    ref.getReference("allUsers")
                            .child(articleAuthorID)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    binding.articleAuthorLabel.setText(dataSnapshot.child("username").getValue(String.class));
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {}
                            });
//                        binding.articleAuthorLabel.setText(LoginActivity.getUserNicknameByUID(articleAuthorID));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

//        Скрываем кнопку если пользователь не авторизован
        if (!LoginActivity.isUserLoggedIn()) {
            binding.commentAddBtn.setVisibility(View.GONE);
        }
//      Переход в создание комментария
        binding.commentAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArticleActivity.this, AddCommentActivity.class);
                intent.putExtra("uniqueID", articleID);
                startActivity(intent);
                finish();
            }
        });

//        Comments section
        commentsDB = FirebaseDatabase.getInstance().getReference("allComments");
        commentsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments.clear();
                    for(DataSnapshot ds2 : dataSnapshot.getChildren()) {
                        CommentsEntity animal = ds2.getValue(CommentsEntity.class);
                        if (Objects.equals(animal.getParentArticleID(), articleID)) {
                            comments.add(animal);
                        }
                    }
                commentsAdapterCall();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(ArticleActivity.this, "Ошибка чтения БД", Toast.LENGTH_SHORT).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (Objects.equals(LoginActivity.getLoggedInUserUID(), articleAuthorID)) {
            getMenuInflater().inflate(R.menu.article, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_bar_edit_article:
                this.finish();
                Intent intent = new Intent(ArticleActivity.this, EditArticleActivity.class);
                intent.putExtra("uniqueID", articleID);
                startActivity(intent);
                this.finish();
                return true;
            case R.id.action_bar_delete_article:
                FirebaseDatabase.getInstance()
                        .getReference("allArticles")
                        .child(articleID).removeValue();
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}