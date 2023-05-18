package com.example.saturdayapp.ui.account;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.saturdayapp.Adapter;
import com.example.saturdayapp.ArticleActivity;
import com.example.saturdayapp.ListEntity;
import com.example.saturdayapp.LoginActivity;
import com.example.saturdayapp.RecyclerViewItemClickListener;
import com.example.saturdayapp.databinding.FragmentAccountBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding binding;
    private String articleAuthorID;
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
        binding.accountRecyclerView
                .setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.accountRecyclerView.setAdapter(adapter);
        numberStart = true;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccountViewModel accountViewModel =
                new ViewModelProvider(this).get(AccountViewModel.class);

        binding = FragmentAccountBinding.inflate(inflater, container, false);
        View root = binding.getRoot();




        if (!LoginActivity.isUserLoggedIn()) {
            binding.myArticlesHeader.setVisibility(View.GONE);
            binding.accountRecyclerView.setVisibility(View.GONE);
//            Обработка нажатия на плашку неавторизации
            binding.unauthorizedUserText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            });
        } else {
            //Скрыть плашку "вы не авторизованы" если пользователь вошёл
            binding.unauthorizedUserText.setVisibility(View.GONE);
//            Вывод статей пользователя
            articleAuthorID = LoginActivity.getLoggedInUserUID();
            articleDB = FirebaseDatabase.getInstance().getReference(LIST_KEY);
            articleDB.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        for (DataSnapshot ds2 : ds.getChildren()) {
                            ListEntity articleToAdd = ds2.getValue(ListEntity.class);
                            if (articleToAdd.getAuthorID().equals(articleAuthorID)) {
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
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}