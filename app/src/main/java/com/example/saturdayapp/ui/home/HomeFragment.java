package com.example.saturdayapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saturdayapp.Adapter;
import com.example.saturdayapp.ArticleActivity;
import com.example.saturdayapp.CreateActivity;
import com.example.saturdayapp.ListEntity;
import com.example.saturdayapp.LoginActivity;
import com.example.saturdayapp.RecyclerViewItemClickListener;
import com.example.saturdayapp.databinding.FragmentHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseReference articleDB;
    private final String LIST_KEY = "allArticles";
    private List<ListEntity> articles = new ArrayList<>();
    private boolean numberStart = false;
    private void adapterCall() {
        Adapter adapter = new Adapter(getActivity(), articles);
        adapter.setOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ArticleActivity.class);
                intent.putExtra("uniqueID", articles.get(position).getArticleID());
                startActivity(intent);
            }
        });
        binding.recyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.recyclerView.setAdapter(adapter);
        numberStart = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (LoginActivity.getLoggedInUserUID().isEmpty()) {
            binding.addFab.hide();
        }

//        Переход в активность создания
        binding.addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateActivity.class);
                startActivity(intent);
            }
        });

        articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY);
        articleDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    for(DataSnapshot ds2 : ds.getChildren()) {
                        ListEntity animal = ds2.getValue(ListEntity.class);
                        animal.setArticleID(ds.getKey());
                        articles.add(animal);
                    }
                }
                if (!numberStart) adapterCall();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Toast.makeText(getActivity(), "Ошибка чтения БД", Toast.LENGTH_SHORT).show();
            }
        });


        if (numberStart) adapterCall();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}