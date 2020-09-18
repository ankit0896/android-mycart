package com.example.android_mycart.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.android_mycart.R;
import com.example.android_mycart.adapter.CartAdapter;
import com.example.android_mycart.database.SqliteHelper;
import com.example.android_mycart.model.Product;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnItemClickListner {

    SqliteHelper sqliteHelper;
    List<Product> productList = new ArrayList<>();
    RecyclerView recyclerView;
    CartAdapter carttAdapter;
    Button checkoutButton;
    ImageView null_cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Cart");
        sqliteHelper = new SqliteHelper(CartActivity.this);
        recyclerView = findViewById(R.id.cart_list);
        initViews();


    }

    private void initViews() {
        null_cart = findViewById(R.id.iv_null_cart);
        checkoutButton = findViewById(R.id.payment_button);
        productList = sqliteHelper.getAllProducts();
        if (productList.isEmpty()) {
            recyclerView.setVisibility(View.INVISIBLE);
            null_cart.setVisibility(View.VISIBLE);
            checkoutButton.setVisibility(View.GONE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            null_cart.setVisibility(View.GONE);
            checkoutButton.setVisibility(View.VISIBLE);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
            carttAdapter = new CartAdapter(productList, CartActivity.this, CartActivity.this);
            recyclerView.setAdapter(carttAdapter);

            clickEvents();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cleart_cart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.clear_cart_option:
                showDialogBox();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialogBox() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        builder.setTitle("Message !!")
                .setMessage("Are you sure want to clear Cart ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        clearCart();
                        dialog.dismiss();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private void clearCart() {
        for (int i=0;i<productList.size();i++){
            sqliteHelper.deleteProduct(productList.get(i).getId());
        }
        initViews();
    }

    private void clickEvents() {
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDialogBox();
            }
        });
    }

    private void setDialogBox() {
        int amount = 0;
        int quan = 0;
        productList = sqliteHelper.getAllProducts();
        for (Product p : productList) {
            amount = (int) (amount + (p.getQuantity() * p.getPrice()));
            quan = quan + p.getQuantity();
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        final int finalQuan = quan;
        final int finalAmount = amount;
        builder.setIcon(R.drawable.buba)
                .setTitle(R.string.confirm)
                .setMessage("Your Order " + quan + " Items and Total Amout is " + amount + ".")
                .setPositiveButton(R.string.go, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        clearCart();
                        startActivity(new Intent(CartActivity.this,ThankyouActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("quantity", finalQuan).putExtra("amount", finalAmount));
                    }
                }).setNegativeButton(R.string.back, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    @Override
    protected void onStop() {
        super.onStop();
        productList.clear();
    }

    @Override
    protected void onPause() {
        super.onPause();
        productList.clear();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        productList.clear();
    }

    @Override
    public void onDeleteItemClick(int postion) {
        sqliteHelper.deleteProduct(productList.get(postion).getId());
        initViews();
    }

    @Override
    public void onPlusClick(int postion, int quantity) {
        //initViews();
    }

    @Override
    public void onMinusClick(int position, int quantity) {
        //initViews();
    }
}