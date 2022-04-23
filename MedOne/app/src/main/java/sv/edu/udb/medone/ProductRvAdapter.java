package sv.edu.udb.medone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRvAdapter extends RecyclerView.Adapter<ProductRvAdapter.ViewHolder> {

    private Context context;
    private ArrayList<ProductRvModal> productRvModalArrayList;
    private ProductClickInterface productClickInterface;
    int lastPosition=-1;
    public ProductRvAdapter(ArrayList<ProductRvModal> productRvModalArrayList,Context context, ProductClickInterface productClickInterface){
        this.productRvModalArrayList=productRvModalArrayList;
        this.productClickInterface=productClickInterface;
        this.context= context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.course_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductRvModal productRvModal= productRvModalArrayList.get(position);
        holder.productTV.setText(productRvModal.getProductName());
        holder.prductPriceTV.setText(productRvModal.getProductPrice());
        Picasso.get().load(productRvModal.getProductImg()).into(holder.productIV);
        setAnimation(holder.itemView,position);
        holder.productIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                productClickInterface.onProductClick(position);
            }
        });
    }
    private  void setAnimation(View view, int position){
        if(position>lastPosition){
            Animation animation= AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            view.setAnimation(animation);
            lastPosition=position;
        }
    }
    @Override
    public int getItemCount() {
        return productRvModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView productIV;
        private TextView productTV, prductPriceTV;
       public ViewHolder(@NonNull View itemView) {
           super(itemView);
           productIV= itemView.findViewById(R.id.idIVCourse);
           productTV=itemView.findViewById(R.id.idTVCOurseName);
           prductPriceTV=itemView.findViewById(R.id.idTVCoursePrice);
       }
   }
   public interface ProductClickInterface{void onProductClick(int position);}

}
