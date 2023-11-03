package edu.huflit.shoppingonl.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import edu.huflit.shoppingonl.Customer.Detail;
import edu.huflit.shoppingonl.Model.Cart;
import edu.huflit.shoppingonl.Model.Product;
import edu.huflit.shoppingonl.R;

public class CartAdapter extends FirebaseRecyclerAdapter<Cart,CartAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context mContext;
    String mUsername;
    public CartAdapter(@NonNull FirebaseRecyclerOptions<Cart> options, Context context, String username) {
        super(options);
        mContext = context;
        mUsername = username;
    }


    @Override
    protected void onBindViewHolder(@NonNull CartAdapter.myViewHolder holder, int position, @NonNull Cart model) {
        // load hình
        if (holder.shapeableImageView != null) {
            Glide.with(holder.shapeableImageView.getContext()).load(model.getProImage()).into(holder.shapeableImageView);
        } else {
            // set hình mặc định neu gia tri rỗng
            holder.shapeableImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        holder.tvProName.setText(model.getProName());
        holder.tvPrice.setText(String.valueOf(model.getPrice()));
        holder.tvQuantity.setText(String.valueOf(model.getQuantity()));

        //tăng số lượng
        holder.ibPlus.setOnClickListener(view -> {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart")
                    .child(mUsername).child(model.getProID());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        // If the product item exists in the cart, update the quantity
                        int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                        reference.child("quantity").setValue(currentQuantity + 1);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            int sl = Integer.parseInt(holder.tvQuantity.getText().toString()) + 1;
            holder.tvQuantity.setText(String.valueOf(sl));
        });

        //giảm số lượng
        holder.ibMinus.setOnClickListener(view -> {
            int sl = Integer.parseInt(holder.tvQuantity.getText().toString()) - 1;
            if (sl >= 1) {

                // Check if the food item already exists in the cart
                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Cart")
                        .child(mUsername).child(model.getProID());
                reference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            // If the food item exists in the cart, update the quantity
                            int currentQuantity = snapshot.child("quantity").getValue(Integer.class);
                            reference.child("quantity").setValue(currentQuantity - 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
                holder.tvQuantity.setText(String.valueOf(sl));

            } else {
                holder.tvQuantity.setText("1");
            }
        });


        //xóa sản phẩm khỏi giỏ hàng
        holder.ibDelete.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(holder.tvProName.getContext());
            builder.setTitle("Xóa sản phẩm");
            builder.setMessage("Bạn có muốn xóa sản phẩm " + holder.tvProName.getText().toString()+ " không?");

            builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Cart")
                            .child(mUsername)
                            .child(model.getProID());
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(holder.tvProName.getContext(), "Đã xóa", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Toast.makeText(holder.tvProName.getContext(), "Đã hủy", Toast.LENGTH_SHORT).show();
                }
            });
            builder.show();
        });
    }

    @NonNull
    @Override
    public CartAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart,parent,false);
        return new CartAdapter.myViewHolder(v);
    }

    public class myViewHolder extends RecyclerView.ViewHolder{
        ShapeableImageView shapeableImageView;
        ImageButton ibPlus, ibMinus, ibDelete;
        TextView tvQuantity, tvPrice, tvProName;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            shapeableImageView = itemView.findViewById(R.id.sivProduct);
            ibPlus = (ImageButton) itemView.findViewById(R.id.ibPlus);
            ibMinus = (ImageButton) itemView.findViewById(R.id.ibMinus);
            ibDelete = (ImageButton) itemView.findViewById(R.id.ibDelete);
            tvPrice = (TextView) itemView.findViewById(R.id.tvPrice);
            tvQuantity = (TextView) itemView.findViewById(R.id.tvQuantity);
            tvProName = (TextView) itemView.findViewById(R.id.tvProName);
        }
    }
}
