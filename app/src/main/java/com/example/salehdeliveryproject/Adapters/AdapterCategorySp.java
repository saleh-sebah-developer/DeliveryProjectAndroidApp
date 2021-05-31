package com.example.salehdeliveryproject.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.salehdeliveryproject.R;
import com.example.salehdeliveryproject.modles.Categories;
import com.example.salehdeliveryproject.modles.Item;

import java.util.List;

public class AdapterCategorySp extends BaseAdapter {

    private List<Categories> categoriesList;

    public AdapterCategorySp(List<Categories> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @Override
    public int getCount() {
        return categoriesList.size();
    }

    @Override
    public Categories getItem(int i) {
        return categoriesList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v =view;

        if(v == null){
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_sp_category,viewGroup,false);
        }
        TextView tv_title = v.findViewById(R.id.custom_categories_tv_category);

        Categories cat = categoriesList.get(i);
        tv_title.setText(cat.getName());

        return v;
    }
}
