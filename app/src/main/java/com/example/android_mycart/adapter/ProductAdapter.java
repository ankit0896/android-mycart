package com.example.android_mycart.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mycart.R;
import com.example.android_mycart.database.SqliteHelper;
import com.example.android_mycart.model.Product;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context context;
    List<Product> productList = new ArrayList<>();
    SqliteHelper sqliteHelper;



    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
        sqliteHelper = new SqliteHelper(context);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, final int position) {
        final Product product = productList.get(position);

        holder.name.setText(product.getName());
        holder.price.setText("Rs "+product.getPrice()+"/-");
        holder.pic.setImageResource(product.getImage());


        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity= new Integer((String) holder.quantity.getText());
                setIncreament(holder,quantity);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quanity= new Integer((String) holder.quantity.getText());
                if(quanity==0){
                    Toast.makeText(context, "Quantity Cannot be Neagative !!", Toast.LENGTH_SHORT).show();
                }else{
                    setDecrement(holder,quanity);
                }
            }
        });

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int quanity= new Integer((String) holder.quantity.getText());
                if(quanity==0){
                    Toast.makeText(context, "Add Some Quantity !!", Toast.LENGTH_SHORT).show();
                }else{
                    Product product1 = new Product();
                    product1.setId(product.getId());
                    product1.setName(product.getName());
                    product1.setPrice(product.getPrice());
                    product1.setQuantity(Integer.valueOf((String) holder.quantity.getText()));
                    sqliteHelper.insertProduct(product1,context);
                    holder.quantity.setText(""+0);
                }
            }
        });
    }

    private void setDecrement(ProductViewHolder holder, int quanity) {
        quanity--;
        holder.quantity.setText(""+quanity);
    }

    private void setIncreament(ProductViewHolder holder, int quantity) {
        quantity++;
        holder.quantity.setText(""+quantity);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView plus,minus;
        TextView name,price,quantity;
        CircleImageView pic;
        Button button;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            plus = itemView.findViewById(R.id.iv_add);
            minus = itemView.findViewById(R.id.iv_remove);
            name = itemView.findViewById(R.id.tv_product_name);
            price = itemView.findViewById(R.id.tv_product_price);
            pic = itemView.findViewById(R.id.iv_product);
            button = itemView.findViewById(R.id.btn_Add_to_cart);
            quantity = itemView.findViewById(R.id.tv_quantity_product);

        }
    }
}
