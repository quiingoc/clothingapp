package edu.huflit.shoppingonl.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Timer;
import java.util.TimerTask;

import edu.huflit.shoppingonl.Adapter.ProductAdapter;
import edu.huflit.shoppingonl.Adapter.ViewPagerAdapter;
import edu.huflit.shoppingonl.Model.Product;
import edu.huflit.shoppingonl.R;

public class Home extends AppCompatActivity {

    ViewPager viewPager;
    int currentPage = 0;
    ImageButton cart;

    ViewPagerAdapter viewPagerAdapter;
    RecyclerView rvNewPro;
    ProductAdapter productAdapter;
    TextView test;

    String userName, passWord, fullName, phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // anh xa
        Mapping();

        //lấy thông tin đăng nhập
        getData();

        //set recycleview
        SetRV();

        //click vào giỏ hàng
        cart.setOnClickListener(view -> {
            Intent i = new Intent(getApplicationContext(), Cart.class);
            i.putExtra("userName", userName);
            startActivity(i);
        });

    }

    private void Mapping() {
        viewPager = findViewById(R.id.viewPager);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPager.setAdapter(viewPagerAdapter);
        setAutoScrollViewScroll();
        viewPager.setClipToOutline(true);

        rvNewPro = (RecyclerView) findViewById(R.id.rvNewPro);
        cart = (ImageButton) findViewById(R.id.ibCart);
    }

    private void getData() {
        //lấy thông tin tài khoản => người dùng bấm nút đăng nhập => correct => đẩy dữ liệu
        Bundle b = getIntent().getExtras();
        userName = b.getString("userName");
        phoneNumber = b.getString("phoneNumber");
        fullName = b.getString("fullName");
        passWord = b.getString("passWord");
    }

    private void SetRV() {
//
//        Bundle b = getIntent().getExtras();
//        userName = b.getString("userName");

        rvNewPro.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Product"), Product.class)
                        .build();


        productAdapter = new ProductAdapter(options, Home.this, userName);
        rvNewPro.setAdapter(productAdapter);
    }

    private void setAutoScrollViewScroll() {
        Timer timer;
        final long DELAY_MS = 500;//delay in milliseconds before task is to be executed
        final long PERIOD_MS = 2000; // time in milliseconds between successive task executions.
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 4) {
                    currentPage = 0;
                }
                viewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer(); // This will create a new Thread
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        },DELAY_MS, PERIOD_MS);
    }

    public void onStart() {
        super.onStart();
        productAdapter.startListening();
//        if (foodAdapter_user != null) {
//            foodAdapter_user.startListening();
//        }
    }
}