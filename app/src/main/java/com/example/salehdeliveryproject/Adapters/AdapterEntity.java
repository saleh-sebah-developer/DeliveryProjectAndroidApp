package com.example.salehdeliveryproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.CustomEntityBinding;
import com.example.salehdeliveryproject.databinding.CustomViewOrdersItemBinding;
import com.example.salehdeliveryproject.modles.Entity;
import com.example.salehdeliveryproject.modles.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdapterEntity extends RecyclerView.Adapter<AdapterEntity.EntityHolder> {

    private ArrayList<Entity> entities;
    private OnRecyclerViewItemClickListener listener;

    public AdapterEntity(ArrayList<Entity> entities, OnRecyclerViewItemClickListener listener) {
        this.entities = entities;
        this.listener = listener;
    }

    @NonNull
    @Override
    public EntityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomEntityBinding binding = CustomEntityBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new EntityHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull EntityHolder holder, int position) {
        Entity entity = entities.get(position);

        holder.binding.customEntityTvTitle.setText(entity.getTitle());
        holder.binding.customEntityTvAddress.setText(entity.getAddress());
        if (entity.getUrl_logo() != null) {
            Picasso.get()
                    .load(entity.getUrl_logo())
                    .resize(130, 100)
                    .centerCrop()
                    .into(holder.binding.customEntityIv);
            //  holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(item.getImage()));
        } else {
            Picasso.get()
                    .load(String.valueOf(R.drawable.your_logo_photo))
                    .resize(130, 100)
                    .centerCrop()
                    .into(holder.binding.customEntityIv);
            // holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(String.valueOf(R.drawable.ic_add_photo_item)));
        }
        holder.ent = entity;

    }

    @Override
    public int getItemCount() {
        return entities.size();
    }


    class EntityHolder extends RecyclerView.ViewHolder {
        CustomEntityBinding binding;
        Entity ent;

        public EntityHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomEntityBinding.bind(itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(ent);
                }
            });

        }
    }

    public interface OnRecyclerViewItemClickListener {
        void onItemClick(Entity entity);
    }

}

