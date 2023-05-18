package com.example.saturdayapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saturdayapp.databinding.CommentItemBinding;
import com.example.saturdayapp.databinding.ListItemBinding;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;


public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {

    private final List<CommentsEntity> data; // данные для вывода в список
    private final LayoutInflater localInflater; // "раздуватель" с контекстом


    public CommentsAdapter(Context context, List<CommentsEntity> data) {
        this.data = data;
        this.localInflater = LayoutInflater.from(context);
    }


    // Создание вьюхолдера из разметки
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {
        CommentItemBinding binding = CommentItemBinding.inflate(localInflater, parent, false);
        return new ViewHolder(binding);
    }

    // Выставляет значения из списка данных во вьюхи по номеру элемента списка
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        CommentsEntity item = data.get(position);
        holder.authorNick.setText(item.getAuthorNickname());
        holder.commentText.setText(item.getMainText());
//      Convertation to date
        holder.commentTime.setText(new Date((item.getAddTime())*1000).toString());
    }

    // Возвращает размер списка данных, нужно для внутренней работы ресайклера
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Хранит переменные вьюх в разметке элементов списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView authorNick, commentText, commentTime;

        public ViewHolder(@NonNull CommentItemBinding binding) {
            super(binding.getRoot());
            authorNick = binding.authorNick;
            commentTime = binding.commentTime;
            commentText = binding.commentText;
        }
    }
}