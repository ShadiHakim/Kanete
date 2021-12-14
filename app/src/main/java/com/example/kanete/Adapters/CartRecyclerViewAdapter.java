package com.example.kanete.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.kanete.Models.CartItem;
import com.example.kanete.Models.Product;
import com.example.kanete.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private List<Product> products;
    private List<CartItem> cartItems;
    private LayoutInflater inflater;
    private ProductClickListener productClickListener;

    public CartRecyclerViewAdapter(Context context, List<Product> products, List<CartItem> cartItems) {
        this.inflater = LayoutInflater.from(context);
        this.products = products;
        this.cartItems = cartItems;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public void setCartItems(List<CartItem> cartItems) {
        this.cartItems = cartItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_product_cart_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.get(position);
        Picasso.get().load(product.getImages().get(0)).into(holder.imageViewProductitem);
        holder.textViewNameProductitem.setText(product.getName());
        holder.textViewPriceProductitem.setText(product.getPrice() + " ₪");
        holder.textViewQuantityProductitem.setText("* " + cartItems.get(position).getQuantity());
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CheckBox checkBoxSelectProduct;
        ImageView imageViewProductitem;
        TextView textViewNameProductitem;
        TextView textViewPriceProductitem;
        TextView textViewQuantityProductitem;

        ViewHolder(View itemView) {
            super(itemView);
            checkBoxSelectProduct = itemView.findViewById(R.id.checkBoxSelectProduct);
            imageViewProductitem = itemView.findViewById(R.id.imageViewProductitem);
            textViewNameProductitem = itemView.findViewById(R.id.textViewNameProductitem);
            textViewPriceProductitem = itemView.findViewById(R.id.textViewPriceProductitem);
            textViewQuantityProductitem = itemView.findViewById(R.id.textViewQuantityProductitem);
            checkBoxSelectProduct.setOnClickListener(listener);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productClickListener != null) productClickListener.checkBoxClick(view, getAdapterPosition());
            }
        };

        @Override
        public void onClick(View view) {
            if (productClickListener != null) productClickListener.onProductClick(view, getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View view) {
            if (productClickListener != null) return productClickListener.onProductLongClick(view, getAdapterPosition());
            return false;
        }
    }

    public Product getProduct(int id) {
        return products.get(id);
    }

    // allows clicks events to be caught
    public void setClickListener(ProductClickListener itemClickListener) {
        this.productClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ProductClickListener {
        void onProductClick(View view, int position);
        boolean onProductLongClick(View view, int position);
        void checkBoxClick(View view, int position);
    }
}
