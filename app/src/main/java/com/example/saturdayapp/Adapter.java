package com.example.saturdayapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.saturdayapp.databinding.ListItemBinding;

import java.util.List;



public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final List<ListEntity> data; // данные для вывода в список
    private final LayoutInflater localInflater; // "раздуватель" с контекстом
    private RecyclerViewItemClickListener clickListener; // слушатель нажатия

    public void setOnItemClickListener(RecyclerViewItemClickListener listener) {
        clickListener = listener;
    }


    public Adapter(Context context, List<ListEntity> data) {
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
        ListItemBinding binding = ListItemBinding.inflate(localInflater, parent, false);
        return new ViewHolder(binding);
    }

    // Выставляет значения из списка данных во вьюхи по номеру элемента списка
    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {
        ListEntity item = data.get(position);
        holder.header.setText(item.getHeader());
//        Укорачивание описания инструкции
        String shortDesc = item.getDescription().replace('\n', ' ');
        int limit = 100;
        String subStr = shortDesc.length() > limit ? shortDesc.substring(0, limit) + "..." : shortDesc;
        holder.description.setText(subStr);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clickListener != null) {
                    clickListener.onItemClick(view, holder.getAdapterPosition());
                }
            }
        });
    }

    // Возвращает размер списка данных, нужно для внутренней работы ресайклера
    @Override
    public int getItemCount() {
        return data.size();
    }

    // Хранит переменные вьюх в разметке элементов списка
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView header;
        TextView description;

        public ViewHolder(@NonNull ListItemBinding binding) {
            super(binding.getRoot());
            header = binding.header;
            description = binding.description;
        }
    }
}