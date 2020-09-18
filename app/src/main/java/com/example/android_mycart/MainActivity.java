package com.example.android_mycart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.android_mycart.activity.CartActivity;
import com.example.android_mycart.adapter.ProductAdapter;
import com.example.android_mycart.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerViewlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        recyclerViewlist = findViewById(R.id.recyclerView_list);

        loadData();
    }

    private void loadData() {
        List<Product> list = new ArrayList<>();
        list.add(new Product(1,"Pizza",R.drawable.pizza,59.0F));
        list.add(new Product(2,"Pasta",R.drawable.pasta,101.0F));
        list.add(new Product(3,"Chowmine",R.drawable.buba,56.2F));
        list.add(new Product(4,"Ginger Burger",R.drawable.pizza,26.0F));
        list.add(new Product(4,"Cheesy Burger",R.drawable.pasta,88.0F));

        setAdapter(list);

    }

    private void setAdapter(List<Product> list) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerViewlist.setLayoutManager(linearLayoutManager);
        ProductAdapter productAdapter = new ProductAdapter(MainActivity.this,list);
        recyclerViewlist.setAdapter(productAdapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.cart_option:
                startActivity(new Intent(MainActivity.this, CartActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}