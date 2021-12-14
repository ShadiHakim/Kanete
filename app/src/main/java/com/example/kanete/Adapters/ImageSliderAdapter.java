package com.example.kanete.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Models.Product;
import com.example.kanete.R;
import com.smarteist.autoimageslider.SliderViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ImageSliderAdapter extends SliderViewAdapter<ImageSliderAdapter.ViewHolder> {

    private List<String> images;
    private LayoutInflater inflater;
    private ImageClickListener imageClickListener;

    public ImageSliderAdapter(Context context, List<String> images) {
        this.inflater = LayoutInflater.from(context);
        this.images = images;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        View view = inflater.inflate(R.layout.image_slider_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String image = images.get(position);
        holder.imageViewSlider.setTag(position);
        Picasso.get().load(image).into(holder.imageViewSlider);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    public class ViewHolder extends SliderViewAdapter.ViewHolder implements View.OnClickListener {
        ImageView imageViewSlider;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewSlider = itemView.findViewById(R.id.imageViewSlider);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (imageClickListener != null) imageClickListener.onImageClick(view, Integer.parseInt(imageViewSlider.getTag().toString()));
        }
    }

    public String getProduct(int id) {
        return images.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ImageClickListener itemClickListener) {
        this.imageClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ImageClickListener {
        void onImageClick(View view, int position);
    }
}
