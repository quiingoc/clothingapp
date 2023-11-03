package edu.huflit.shoppingonl.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.huflit.shoppingonl.Adapter.CartAdapter;
import edu.huflit.shoppingonl.Adapter.ProductAdapter;
import edu.huflit.shoppingonl.Model.Product;
import edu.huflit.shoppingonl.Model.PurchaseOrder;
import edu.huflit.shoppingonl.R;

public class Cart extends AppCompatActivity {

    ImageButton back;
    Button btnBook;
    TextView tvTotalPrice, test;
    RecyclerView rvCart;

    CartAdapter cartAdapter;
    String userName, phoneNumber, fullName, passWord;
    private double total = 0;
    private String formattedTotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        //ánh xạ
        Mapping();

        //lấy dữ liệu từ Home
        getData();

        //set rv
        setRV();


        //tính tổng tiền
        calcTotalPricce();

        //nhấn đặt hàng
        btnBook.setOnClickListener(view -> {
            createPurchaseOder();
        });

        //click quay lại
        back.setOnClickListener(view -> onBackPressed());
    }

    private void calcTotalPricce() {
        DatabaseReference cartRef = FirebaseDatabase.getInstance().getReference("Cart").child(userName);
        cartRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                total = 0;
                for (DataSnapshot foodSnapshot : snapshot.getChildren()) {
                    double price = foodSnapshot.child("price").getValue(Double.class);
                    int quantity = foodSnapshot.child("quantity").getValue(Integer.class);
                    double foodPrice = price * quantity;
                    total += foodPrice;
                    DecimalFormat formatter = new DecimalFormat("#,###");
                    formattedTotal = formatter.format(total);
                    if (quantity == 0) {
                        total = 0;
                    }
                }
                Log.d("Cart", "Total price: " + total);
                if (total == 0) {
                    tvTotalPrice.setText(0 + " VNĐ");
                } else {
                    tvTotalPrice.setText(formattedTotal + " VNĐ");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // handle errors
            }
        });
    }

    private void Mapping() {
        back = (ImageButton) findViewById(R.id.btnBack);
        btnBook = (Button) findViewById(R.id.btnBook);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        rvCart = (RecyclerView) findViewById(R.id.rvCart);
        test = (TextView) findViewById(R.id.test);
    }

    private void getData() {
        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");
    }

    private void setRV() {
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        FirebaseRecyclerOptions<edu.huflit.shoppingonl.Model.Cart> options =
                new FirebaseRecyclerOptions.Builder<edu.huflit.shoppingonl.Model.Cart>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Cart")
                                .child(userName).orderByChild("userName").equalTo(userName), edu.huflit.shoppingonl.Model.Cart.class)
                        .build();


        cartAdapter = new CartAdapter(options, Cart.this, userName);
        rvCart.setAdapter(cartAdapter);
    }

    private void createPurchaseOder() {
        //kiểm tra trong giỏ hàng có sản phẩm hay không
        if (total == 0) {
            Toast.makeText(Cart.this, "Không có sản phẩm nào trong giỏ hàng!", Toast.LENGTH_SHORT).show();
        } else {
            //build trang dialog xác nhận thông tin (dialog_confirm_xml)
            android.app.AlertDialog.Builder builder = new AlertDialog.Builder(Cart.this);
            LayoutInflater inflater = LayoutInflater.from(Cart.this);
            View dialogView = inflater.inflate(R.layout.dialog_confirm_info, null);
            builder.setView(dialogView);

            EditText edFullName = dialogView.findViewById(R.id.txtFullName);
            EditText edPhoneNumber = dialogView.findViewById(R.id.txtPhoneNumber);
            EditText edAddress = dialogView.findViewById(R.id.txtAddress);
            Button confirm = dialogView.findViewById(R.id.btnConfirm);

            //lay du lieu
            DatabaseReference usersReference = FirebaseDatabase.getInstance().getReference("User").child(userName);
            usersReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        phoneNumber = snapshot.child("phoneNumber").getValue(String.class);
                        passWord = snapshot.child("passWord").getValue(String.class);
                        fullName = snapshot.child("fullName").getValue(String.class);

                        edFullName.setText(fullName);
                        edPhoneNumber.setText(phoneNumber);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();

            //xác nhận đặt hàng
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String address = edAddress.getText().toString().trim();
                    String fullname = edFullName.getText().toString().trim();
                    String phone = edPhoneNumber.getText().toString().trim();

                    //kiểm tra điền đủ thông tin giao hàng
                    if (address.equals("")||fullname.equals("")||phone.equals("")) {
                        Toast.makeText(Cart.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("PurchaseOrder").child(userName);
                        //tạo bill
                        //lấy ngày giờ tạo bill
                        Date now = new Date();
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String datetime = format.format(now);

                        //tạo node billID
                        String orderID = reference.push().getKey();
                        String detailID = reference.push().getKey();
                        //String userName, billID, datetime, fullName, phoneNumber, address, detailID;
                        PurchaseOrder order = new PurchaseOrder(userName,  orderID, datetime, fullName, phoneNumber, address, detailID, total);

                        reference.child(orderID).setValue(order).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(Cart.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Cart")
                                        .child(userName);
                                ref.removeValue();
                                boolean isLoggedIn = getIntent().getBooleanExtra("login_status", false);
                                Intent intent = new Intent(Cart.this, BookingSuccess.class);
                                intent.putExtra("userName", userName);
                                startActivity(intent);
                            }
                        });
                    }
                }
            });

        }
    }


    public void onStart() {
        super.onStart();
        cartAdapter.startListening();
//        if (foodAdapter_user != null) {
//            foodAdapter_user.startListening();
//        }
    }
}