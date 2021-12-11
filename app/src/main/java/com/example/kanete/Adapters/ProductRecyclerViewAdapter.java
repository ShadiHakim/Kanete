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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ProductRecyclerViewAdapter extends RecyclerView.Adapter<ProductRecyclerViewAdapter.ViewHolder> {

    private List<Product> products;
    private LayoutInflater inflater;
    private ProductClickListener productClickListener;

    public ProductRecyclerViewAdapter(Context context, List<Product> products) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_product_row, parent, false);
        return new ProductRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        Picasso.get().load(product.getImages().get(0)).into(holder.imageViewProductitem);
        holder.textViewNameProductitem.setText(product.getName());
        holder.textViewPriceProductitem.setText(product.getPrice() + " ₪");
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageViewProductitem;
        TextView textViewNameProductitem;
        TextView textViewPriceProductitem;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewProductitem = itemView.findViewById(R.id.imageViewProductitem);
            textViewNameProductitem = itemView.findViewById(R.id.textViewNameProductitem);
            textViewPriceProductitem = itemView.findViewById(R.id.textViewPriceProductitem);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (productClickListener != null) productClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    Product getItem(int id) {
        return products.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ProductClickListener itemClickListener) {
        this.productClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ProductClickListener {
        void onItemClick(View view, int position);
    }
}
