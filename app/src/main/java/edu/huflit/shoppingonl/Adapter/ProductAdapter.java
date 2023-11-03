package edu.huflit.shoppingonl.Adapter;

import android.content.Context;
import android.content.Intent;
import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;

import edu.huflit.shoppingonl.Customer.Detail;
import edu.huflit.shoppingonl.Model.Product;
import edu.huflit.shoppingonl.R;

public class ProductAdapter extends FirebaseRecyclerAdapter<Product,ProductAdapter.myViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */

    Context mContext;
    String mUsername;

    public ProductAdapter(@NonNull FirebaseRecyclerOptions<Product> options, Context context, String username) {
        super(options);
        mContext = context;
        mUsername = username;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Product model) {

        // load hình
        if (holder.shapeableImageView != null) {
            Glide.with(holder.shapeableImageView.getContext()).load(model.getProImage()).into(holder.shapeableImageView);
        } else {
            // set hình mặc định neu gia tri rỗng
            holder.shapeableImageView.setImageResource(R.drawable.ic_launcher_background);
        }

        // click hinh => xem chi tiet sp
        holder.itemView.setOnClickListener(view -> {
            Intent i = new Intent(mContext, Detail.class);
            i.putExtra("proImage", model.getProImage());
            i.putExtra("proID", model.getProID());
            i.putExtra("proName", model.getProName());
            i.putExtra("price", model.getPrice());
            i.putExtra("proQuan", model.getProQuan());
            i.putExtra("proDes", model.getProDes());
            i.putExtra("proCate", model.getProCate());
            i.putExtra("userName", mUsername);
            mContext.startActivity(i);
        });
    }


    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_new_product,parent,false);
        return new ProductAdapter.myViewHolder(v);
    }

    class myViewHolder extends RecyclerView.ViewHolder{

        ShapeableImageView shapeableImageView;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            shapeableImageView = itemView.findViewById(R.id.sivNewPro);
        }
    }

}
