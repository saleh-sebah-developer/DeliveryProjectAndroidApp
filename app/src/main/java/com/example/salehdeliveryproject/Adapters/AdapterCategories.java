package com.example.salehdeliveryproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.CustomCategoriesItemBinding;
import com.example.salehdeliveryproject.databinding.CustomEntityBinding;
import com.example.salehdeliveryproject.modles.Categories;
import com.example.salehdeliveryproject.modles.Entity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;

public class AdapterCategories extends RecyclerView.Adapter<AdapterCategories.CategoryHolder> {

    private ArrayList<Categories> categories;
    private OnRecyclerViewItemClickListener listener;

    public AdapterCategories(ArrayList<Categories> categories, OnRecyclerViewItemClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomCategoriesItemBinding binding = CustomCategoriesItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CategoryHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
        Categories categ = categories.get(position);

        holder.binding.customCategoriesItemTvTitle.setText(categ.getName());
        if (categ.getImage() != null) {
            Picasso.get()
                    .load(categ.getImage())
                    .resize(70, 70)
                    .centerCrop()
                    .into(holder.binding.customCategoriesItemIv);
        } else {
            Picasso.get()
                    .load(String.valueOf(R.drawable.item_photo))
                    .resize(70, 70)
                    .centerCrop()
                    .into(holder.binding.customCategoriesItemIv);
        }
        holder.cat = categ;

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


    class CategoryHolder extends RecyclerView.ViewHolder {
        CustomCategoriesItemBinding binding;
        Categories cat;

        public CategoryHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomCategoriesItemBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(cat);
                }
            });

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(Categories categories);
    }

}

