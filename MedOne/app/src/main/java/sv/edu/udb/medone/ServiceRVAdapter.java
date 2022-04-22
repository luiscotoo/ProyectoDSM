package sv.edu.udb.medone;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ServiceRVAdapter extends RecyclerView.Adapter<ServiceRVAdapter.ViewHolder>{
    private ArrayList<ServiceRVModal> serviceRVModalsArrayList;
    private Context context;
    private ServiceClickInterface serviceClickInterface;
    int lastPos=-1;

    public ServiceRVAdapter(ArrayList<ServiceRVModal> serviceRVModalsArrayList, Context context, ServiceClickInterface serviceClickInterface){
        this.serviceRVModalsArrayList=serviceRVModalsArrayList;
        this.context=context;
        this.serviceClickInterface=serviceClickInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.course_rv_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,@SuppressLint("RecyclerView") int position) {
        ServiceRVModal serviceRVModal = serviceRVModalsArrayList.get(position);
        holder.serviceTV.setText(serviceRVModal.getServiceName());
        holder.servicePriceTV.setText("$"+serviceRVModal.getServicePrice());
        Picasso.get().load(serviceRVModal.getServiceImg()).into(holder.serviceIV);

        setAnimation(holder.itemView,position);
        holder.serviceIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                serviceClickInterface.onServiceClick(position);
            }
        });
    }

    private void setAnimation(View itemView, int position) {
        if (position > lastPos) {
            // on below line we are setting animation.
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            itemView.setAnimation(animation);
            lastPos = position;
        }
    }

    @Override
    public int getItemCount() {
        return serviceRVModalsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private ImageView serviceIV;
        private TextView serviceTV, servicePriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            serviceIV = itemView.findViewById(R.id.idIVCourse);
            serviceTV = itemView.findViewById(R.id.idTVCOurseName);
            servicePriceTV = itemView.findViewById(R.id.idTVCousePrice);
        }
    }

    // creating a interface for on click
    public interface ServiceClickInterface {
        void onServiceClick(int position);
    }

}
