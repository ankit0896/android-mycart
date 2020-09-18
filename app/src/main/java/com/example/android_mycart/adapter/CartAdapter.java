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

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyCartViewHolder> {

    List<Product> productList = new ArrayList<>();
    Context context;
    SqliteHelper sqliteHelper;
    OnItemClickListner onItemClickListner;

    public CartAdapter(List<Product> productList, Context context, OnItemClickListner onItemClickListner) {
        this.productList = productList;
        this.context = context;
        sqliteHelper = new SqliteHelper(context);
        this.onItemClickListner = onItemClickListner;
    }

    @NonNull
    @Override
    public MyCartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart_layout, parent, false);
        return new CartAdapter.MyCartViewHolder(view, onItemClickListner);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyCartViewHolder holder, final int position) {
        final Product product = productList.get(position);
        holder.name.setText(product.getName());
        holder.quantity.setText("" + product.getQuantity());
        int quantity = product.getQuantity();
        holder.price_final.setText("Rs " + (quantity * product.getPrice()) + " /-");

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quantity = new Integer((String) holder.quantity.getText());
                quantity++;
                holder.quantity.setText("" + quantity);
                holder.price_final.setText("Rs " + (quantity * productList.get(position).getPrice()) + " /-");
                Product product1 = new Product();
                product1.setId(product.getId());
                product1.setName(product.getName());
                product1.setPrice(product.getPrice());
                product1.setQuantity(Integer.valueOf((String) holder.quantity.getText()));
                sqliteHelper.cartInsertProduct(product1,context);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int quanity = new Integer((String) holder.quantity.getText());
                if (quanity == 1) {
                    onItemClickListner.onDeleteItemClick(position);
                } else {
                    quanity--;
                    holder.quantity.setText("" + quanity);
                    holder.price_final.setText("Rs " + (quanity * productList.get(position).getPrice()) + " /-");
                    Product product1 = new Product();
                    product1.setId(product.getId());
                    product1.setName(product.getName());
                    product1.setPrice(product.getPrice());
                    product1.setQuantity(Integer.valueOf((String) holder.quantity.getText()));
                    sqliteHelper.cartInsertProduct(product1,context);
                }
            }
        });


    }

    private void setDecrement(MyCartViewHolder holder, int position, int quanity) {
        quanity--;
        holder.quantity.setText("" + quanity);
        holder.price_final.setText("Rs " + (quanity * productList.get(position).getPrice()) + " /-");
        onItemClickListner.onMinusClick(position,quanity);

    }

    private void setIncreament(MyCartViewHolder holder, int position, int quantity) {
        quantity++;
        holder.quantity.setText("" + quantity);
        holder.price_final.setText("Rs " + (quantity * productList.get(position).getPrice()) + " /-");
        onItemClickListner.onPlusClick(position,quantity);

    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class MyCartViewHolder extends RecyclerView.ViewHolder {
        ImageView plus, minus;
        TextView name, price_final, quantity;
        ImageView delete;


        public MyCartViewHolder(@NonNull View itemView, final OnItemClickListner onItemClickListner) {
            super(itemView);
            plus = itemView.findViewById(R.id.iv_add_cart);
            minus = itemView.findViewById(R.id.iv_remove_cart);
            name = itemView.findViewById(R.id.tv_cart_product_name);
            price_final = itemView.findViewById(R.id.tv_final_price);
            quantity = itemView.findViewById(R.id.tv_quantity_product_cart);
            delete = itemView.findViewById(R.id.delete_item);


            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListner.onDeleteItemClick(getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListner {
        void onDeleteItemClick(int postion);
        void onPlusClick(int postion, int quantity);
        void onMinusClick(int position, int quantity);

    }
}
