package com.example.salehdeliveryproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.salehdeliveryproject.Activitys.LoginActivity;
import com.example.salehdeliveryproject.Activitys.LoginAdminActivity;
import com.example.salehdeliveryproject.R;

public class AdapterSlide extends PagerAdapter {
    Context context;
    LayoutInflater inflate;
    TextView slide_tv_skip;
    public AdapterSlide(Context context) {
        this.context = context;
    }
    // list of image
    public int[] lst_image = {
            R.drawable.slide1,
            R.drawable.slide2,
            R.drawable.slide3
    };
    // list of title
    public String[] lst_title = {
            "Safe and easy delivery",
            "lower price (cheaper)",
            "Delivery of wider "
    };
    // list of desc
    public String[] lst_desc = {
            "Powered by a secure tracking and packaging system",
            "Best delivery rate",
            "We cover all areas of the city"
    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflate = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflate.inflate(R.layout.slide,container,false);
       // LayoutInflater layoutslider = view.findViewById(R.id.slide_linearLayout);
        ImageView imageSlide = view.findViewById(R.id.slide_iv);
        TextView titleSlide = view.findViewById(R.id.slide_tv_title);
        TextView desceSlide = view.findViewById(R.id.slide_tv_desc);
        imageSlide.setImageResource(lst_image[position]);
        titleSlide.setText(lst_title[position]);
        desceSlide.setText(lst_desc[position]);
        container.addView(view);
        slide_tv_skip = view.findViewById(R.id.slide_tv_skip);
        slide_tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                context.startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public int getCount() {
        return lst_title.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return ( view == (LinearLayout)object);
    }
}
