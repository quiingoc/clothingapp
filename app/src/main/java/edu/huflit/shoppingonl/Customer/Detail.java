package edu.huflit.shoppingonl.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

import edu.huflit.shoppingonl.Adapter.ProductAdapter;
import edu.huflit.shoppingonl.Model.Cart;
import edu.huflit.shoppingonl.R;

public class Detail extends AppCompatActivity {

    ShapeableImageView shapeableImageView;
    TextView tvProName, tvProID, tvProDes, tvPrice, tvCate;
    ImageButton back, btnLike;

    Button btnCart;
    String proName, proID, proImage, proDes, proCate, formattedPrice, userName;
    int price, proQuan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //anh xa
        Mapping();

        //get parameter
        getData();


        //set information of product
        setInfo();



        //click button add to cart
        btnCart.setOnClickListener(view -> {
            //addProductToCart();
            Cart cart = new Cart(userName, proID, proName,  proDes, price, proQuan, proCate, proImage, 1);

            DatabaseReference newCartRef = FirebaseDatabase.getInstance().getReference("Cart")
                    .child(userName)
                    .child(proID);

            newCartRef.setValue(cart)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });

        //back
        back.setOnClickListener(view -> onBackPressed());
    }

    private void Mapping() {
        shapeableImageView = (ShapeableImageView) findViewById(R.id.sivDetail);
        back = (ImageButton) findViewById(R.id.back);
        tvProName = (TextView) findViewById(R.id.tvProName);
        tvProID = (TextView) findViewById(R.id.tvProID);
        tvProDes = (TextView) findViewById(R.id.tvProDes);
        tvPrice =  (TextView) findViewById(R.id.tvPrice);
        tvCate = (TextView) findViewById(R.id.tvCate);
        btnCart = (Button) findViewById(R.id.btnCart);
        btnLike = (ImageButton) findViewById(R.id.btnLike);
    }
    private void getData() {
        Intent intent = getIntent();
        proName = intent.getStringExtra("proName");
        proID = intent.getStringExtra("proID");
        proImage = intent.getStringExtra("proImage");
        price = intent.getIntExtra("price", 0);
        proDes = intent.getStringExtra("proDes");
        proQuan = intent.getIntExtra("proQuan",0);
        proCate = intent.getStringExtra("proCate");
        userName = intent.getStringExtra("userName");
    }
    private void setInfo() {
        // Create a DecimalFormat pattern to format the price
        DecimalFormat decimalFormat = new DecimalFormat("#,##0");
        formattedPrice = decimalFormat.format(price);

        //note: don't show quantity of product. Just announce Sold out when quantity = 0
        //set image
        Glide.with(this)
                .load(proImage)
                .apply(new RequestOptions()
                        .override(ViewGroup.LayoutParams.MATCH_PARENT, 300))
                .into(shapeableImageView);

        //set name, ID, price, description, category
        tvCate.setText("Category: " + proCate);
        tvPrice.setText(formattedPrice + " VND");
        tvProDes.setText(proDes);
        tvProID.setText("Product ID: " +proID);
        tvProName.setText(proName);
    }

//    private void addProductToCart() {
//        FirebaseDatabase db;
//        DatabaseReference reference;
//
//        db = FirebaseDatabase.getInstance();
//        reference = db.getReference("Cart");
//
//        //check xem giỏ hàng đã có sản phẩm đó chưa
//        Query query = reference.child(userName).orderByChild("proID").equalTo(proID);
//
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final boolean[] productExists = {false};
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                    String getProID = childSnapshot.getKey();
//                    DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart")
//                            .child(userName)
//                            .child(getProID);
//
//                    cartRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(@NonNull DataSnapshot snapshot) {
//                            if (snapshot.exists()) {
//                                // If the product item exists in the cart, update the quantity
//                                String getQuantity = snapshot.child("quantity").getValue(String.class); // Retrieve the value as a String
//                                int currentQuantity = Integer.parseInt(getQuantity); // Convert the String to an int
//
//                                cartRef.child("quantity").setValue(currentQuantity + 1)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(Detail.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Detail.this, "Cập nhật số lượng thất bại", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                                productExists[0] = true;
//                            }
//                            else {
//                                //userName, proID, proName,  proDes, price, proQuan, proCate, proImage
//                                // If the product item does not exist in the cart, add a new cart item
//                                edu.huflit.shoppingonl.Model.Cart cart = new Cart(userName, proID, proName,  proDes, price, proQuan, proCate, proImage, "1");
//
//                                DatabaseReference newCartRef = FirebaseDatabase.getInstance().getReference("Cart")
//                                        .child(userName)
//                                        .child(proID);
//
//                                newCartRef.setValue(cart)
//                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                            @Override
//                                            public void onComplete(@NonNull Task<Void> task) {
//                                                if (task.isSuccessful()) {
//                                                    Toast.makeText(Detail.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
//                                                } else {
//                                                    Toast.makeText(Detail.this, "Thêm vào giỏ hàng thất bại", Toast.LENGTH_SHORT).show();
//                                                }
//                                            }
//                                        });
//                            }
//                        }
//
//                        @Override
//                        public void onCancelled(@NonNull DatabaseError error) {
//
//                        }
//                    });
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                // Handle errors
//            }
//        });
//    }

}