package com.example.saturdayapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityCreateBinding;
import com.example.saturdayapp.databinding.ActivityMainBinding;
import com.example.saturdayapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    private ActivityCreateBinding binding;
    private DatabaseReference articleDB;
    private String LIST_KEY = "allArticles";

    String articleHeader, articleDescription, articleVideoLink;
    List<ListEntity> articleToAdd = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityCreateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                articleHeader = String.valueOf(binding.createHeader.getText());
                articleDescription = String.valueOf(binding.createDescription.getText());
                articleVideoLink = String.valueOf(binding.createLink.getText());
                articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY);
                articleToAdd.add(new ListEntity( articleDB.getKey(),
                        articleHeader,
                        articleDescription,
                        articleVideoLink
                    ));
                articleDB.push().setValue(articleToAdd);


                startActivity(intent);
                finish();
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