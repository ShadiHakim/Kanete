package com.example.kanete.ProductManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kanete.Adapters.ImageSliderAdapter;
import com.example.kanete.Models.Product;
import com.example.kanete.R;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.util.List;

public class ProductViewActivity extends AppCompatActivity implements ImageSliderAdapter.ImageClickListener {

    ProductViewModel productViewModel;

    ImageSliderAdapter imageSliderAdapter;
    SliderView imageSlider;
    TextView textViewProductViewName;
    TextView textViewProductViewPrice;
    TextView textViewProductViewDescription;
    TextView textViewMinus;
    TextView textViewAmount;
    TextView textViewPlus;
    Button buttonAddCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        init();
    }

    private void init() {
        productViewModel = new ProductViewModel(this);
        Product this_product = productViewModel.getProduct();
        imageSlider = findViewById(R.id.imageSlider);
        textViewProductViewName = findViewById(R.id.textViewProductViewName);
        textViewProductViewPrice = findViewById(R.id.textViewProductViewPrice);
        textViewProductViewDescription = findViewById(R.id.textViewProductViewDescription);
        textViewMinus = findViewById(R.id.textViewMinus);
        textViewAmount = findViewById(R.id.textViewAmount);
        textViewPlus = findViewById(R.id.textViewPlus);
        buttonAddCart = findViewById(R.id.buttonAddCart);


        setup_amountChooser(this_product.getQuantity());
        setup_if_customer();

        setup_imageSlider();
        textViewProductViewName.setText(this_product.getName());
        textViewProductViewPrice.setText(this_product.getPrice() + " ₪");
        textViewProductViewDescription.setText(this_product.getDescription());

        buttonAddCart.setOnClickListener(listener_add_cart);
    }

    View.OnClickListener listener_add_cart = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int quantity = Integer.parseInt(textViewAmount.getText().toString());
            productViewModel.addToCart(quantity).observeForever(new Observer<Boolean>() {
                @Override
                public void onChanged(Boolean flag) {
                    if (flag) {
                        Toast.makeText(getApplicationContext(), "Added to Cart!", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                }
            });
        }
    };

    public void setup_imageSlider(){
        productViewModel.getImages().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> images) {
                imageSliderAdapter = new ImageSliderAdapter(getApplicationContext(), images);
                imageSliderAdapter.setClickListener(ProductViewActivity.this);
                imageSlider.setSliderAdapter(imageSliderAdapter);

//                imageSlider.setIndicatorAnimation(IndicatorAnimationType.WORM); //set indicator animation by using IndicatorAnimationType. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
//                imageSlider.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
//                imageSlider.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);
                imageSlider.setIndicatorSelectedColor(Color.WHITE);
                imageSlider.setIndicatorUnselectedColor(Color.GRAY);
//                imageSlider.setScrollTimeInSec(4); //set scroll delay in seconds :
//                imageSlider.startAutoCycle();
                imageSlider.setAutoCycle(false);
            }
        });
    }

    private void setup_if_customer() {
        if (!productViewModel.isCustomer()){
            textViewMinus.setVisibility(View.GONE);
            textViewAmount.setVisibility(View.GONE);
            textViewPlus.setVisibility(View.GONE);
            buttonAddCart.setVisibility(View.GONE);
        }
    }

    public void setup_amountChooser(int quantity) {
        textViewMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(textViewAmount.getText().toString());
                if (curr > 1)
                    textViewAmount.setText(String.valueOf(curr - 1));
                else
                    Toast.makeText(getApplicationContext(), "Can not go less than 1!", Toast.LENGTH_SHORT).show();
            }
        });
        textViewPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int curr = Integer.parseInt(textViewAmount.getText().toString());
                if (curr < quantity)
                    textViewAmount.setText(String.valueOf(curr + 1));
                else
                    Toast.makeText(getApplicationContext(), "Maximum Quantity!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onImageClick(View view, int position) {
        Toast.makeText(this, imageSliderAdapter.getProduct(position), Toast.LENGTH_SHORT).show();
        // TODO extra open in full screen
    }
}