package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityArticleBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ArticleActivity extends AppCompatActivity {

    private ActivityArticleBinding binding;
    private String articleHeader, articleDescription, articleVideoLink, articleID;
    WebView youtubeWebView;



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



        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("allArticles");
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
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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