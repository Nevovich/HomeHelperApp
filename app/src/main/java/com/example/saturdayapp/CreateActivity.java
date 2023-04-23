package com.example.saturdayapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.saturdayapp.databinding.ActivityCreateBinding;
import com.example.saturdayapp.databinding.ActivityMainBinding;
import com.example.saturdayapp.databinding.FragmentHomeBinding;

public class CreateActivity extends AppCompatActivity {

    private ActivityCreateBinding binding;

    String articleHeader, articleDescription;
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


                startActivity(intent);
                finish();
            }
        });

    }
}