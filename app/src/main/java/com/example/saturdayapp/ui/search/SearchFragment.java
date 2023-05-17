package com.example.saturdayapp.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saturdayapp.Adapter;
import com.example.saturdayapp.ArticleActivity;
import com.example.saturdayapp.ListEntity;
import com.example.saturdayapp.RecyclerViewItemClickListener;
import com.example.saturdayapp.databinding.FragmentSearchBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;
    private String articleSearchQuery;
    private DatabaseReference articleDB;
    private String LIST_KEY = "allArticles";
    private List<ListEntity> articles = new ArrayList<>();
    private boolean numberStart = false;
    private void adapterCall() {
        Adapter adapter = new Adapter(getActivity(), articles);
        adapter.setOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ListEntity item = articles.get(position);
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
        SearchViewModel searchViewModel =
                new ViewModelProvider(this).get(SearchViewModel.class);

        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                articleSearchQuery = binding.searchView.getQuery().toString().toLowerCase(Locale.ROOT);
                    articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY);
                    articleDB.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                for (DataSnapshot ds2 : ds.getChildren()) {
                                    ListEntity articleToAdd = ds2.getValue(ListEntity.class);
                                    if (articleToAdd.getHeader().contains(articleSearchQuery) |
                                            articleToAdd.getDescription().toLowerCase(Locale.ROOT).contains(articleSearchQuery)) {
                                        articleToAdd.setArticleID(ds.getKey());
                                        articles.add(articleToAdd);
                                    }
                                }
                            }
                            if (!numberStart) adapterCall();
                        }
                        @Override
                        public void onCancelled(DatabaseError error) {
                            // Failed to read value
                            Toast.makeText(getActivity(), "Ошибка чтения БД", Toast.LENGTH_SHORT).show();
                        }
                    });
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}