package com.example.android_mycart.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.android_mycart.model.Product;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "MyCartDB";
    public static String TABLE_NAME = "MyOrders";

    public SqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + TABLE_NAME + "(id integer primary key, product_id integer,product_name text,product_quantity integer,product_price float)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


    public boolean cartInsertProduct(Product product, Context context) {
        if (updateCartProduct(product)) {

        } else {

        }
        return true;
    }

    private boolean updateCartProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_quantity", product.getQuantity());
        db.update(TABLE_NAME, contentValues, "product_id = ? ", new String[]{Integer.toString(product.getId())});
        db.close();
        return true;
    }


    public boolean insertProduct(Product product, Context context) {
        if (getData(product.getId()) != 0) {
            if (updateContact(product, getData(product.getId()))) {
                Toast.makeText(context, "Item Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put("product_id", product.getId());
            contentValues.put("product_name", product.getName());
            contentValues.put("product_price", product.getPrice());
            contentValues.put("product_quantity", product.getQuantity());
            db.insert(TABLE_NAME, null, contentValues);
            Log.d("ProductId", product.toString());
            Toast.makeText(context, product.getQuantity() + " " + product.getName() + " Added to Cart !!", Toast.LENGTH_LONG).show();
            db.close();
        }
        return true;
    }

    public boolean updateContact(Product product, int data) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("product_quantity", (data + product.getQuantity()));
        db.update(TABLE_NAME, contentValues, "product_id = ? ", new String[]{Integer.toString(product.getId())});
        db.close();
        Log.d("Called", "" + product.getQuantity());
        return true;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, TABLE_NAME);
        db.close();
        return numRows;
    }

    public List<Product> getAllProducts() {
        List<Product> array_list = new ArrayList<>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME, null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            Product product = new Product();
            product.setId(res.getInt(res.getColumnIndex("product_id")));
            product.setName(res.getString(res.getColumnIndex("product_name")));
            product.setPrice(res.getFloat(res.getColumnIndex("product_price")));
            product.setQuantity(res.getInt(res.getColumnIndex("product_quantity")));
            array_list.add(product);

            Log.d("ProductId", product.toString());
            res.moveToNext();
        }
        db.close();
        return array_list;
    }

    public int getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where product_id=" + id, null);
        if (res.moveToFirst()) {
            return res.getInt(res.getColumnIndex("product_quantity"));
        } else {
            db.close();
            return 0;
        }


    }

    public Integer deleteProduct(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "product_id = ? ",
                new String[]{Integer.toString(id)});


    }
}
