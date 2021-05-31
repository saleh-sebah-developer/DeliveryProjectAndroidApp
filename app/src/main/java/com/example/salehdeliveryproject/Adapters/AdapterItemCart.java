package com.example.salehdeliveryproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.databinding.CustomAddNewItemBinding;
import com.example.salehdeliveryproject.databinding.CustomShowItemCartBinding;
import com.example.salehdeliveryproject.modles.Item;
import com.example.salehdeliveryproject.modles.OrderItem;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AdapterItemCart extends RecyclerView.Adapter<AdapterItemCart.ItemCartHolder> {

    private List<OrderItem> orderItems;
    private OnButtonCancelItemClickListener listener;


    public AdapterItemCart(List<OrderItem> orderItems, OnButtonCancelItemClickListener listener) {
        this.orderItems = orderItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ItemCartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CustomShowItemCartBinding binding = CustomShowItemCartBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemCartHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemCartHolder holder, int position) {
        OrderItem orderItem = orderItems.get(position);
        holder.binding.customAddNewItemTvNameItem.setText(orderItem.getItem().getTitle());
        holder.binding.customAddNewItemTvPriceItem.setText(String.valueOf(orderItem.getItem().getPrice()) + " ₪");
        holder.binding.customAddNewItemTvDescriptionItem.setText(orderItem.getItem().getDescribe());
        holder.binding.customAddNewItemEtNumItem.setText(String.valueOf(orderItem.getNumber()));
        if (orderItem.getItem().getImage() != null) {
            Picasso.get()
                    .load(orderItem.getItem().getImage())
                    .resize(120, 120)
                    .centerCrop()
                    .into(holder.binding.customAddNewItemIvItem);
            //  holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(item.getImage()));
        } else {
            Picasso.get()
                    .load(String.valueOf(R.drawable.item_photo))
                    .resize(120, 120)
                    .centerCrop()
                    .into(holder.binding.customAddNewItemIvItem);
           // holder.binding.customAddNewItemIvItem.setImageURI(Uri.parse(String.valueOf(R.drawable.ic_add_photo_item)));
        }

        holder.orIt = orderItem;

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    class ItemCartHolder extends RecyclerView.ViewHolder {
        CustomShowItemCartBinding binding;
        OrderItem orIt;

        public ItemCartHolder(@NonNull View itemView) {
            super(itemView);
            binding = CustomShowItemCartBinding.bind(itemView);

            binding.customAddNewItemTvCancelItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onBtnItemClick(orIt);
                }
            });

        }
    }

        //------------??????????????????
    public interface OnButtonCancelItemClickListener {
        void onBtnItemClick(OrderItem orderItem );
    }
}
