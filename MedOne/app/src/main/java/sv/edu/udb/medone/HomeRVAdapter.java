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

public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.ViewHolder> {
    // creating variables for our list, context, interface and position.
    private ArrayList<HomeRVModal> homeRVModalArrayList;
    private Context context;
    private HomeClickInterface homeClickInterface;
    int lastPos = -1;

    // creating a constructor.
    public HomeRVAdapter(ArrayList<HomeRVModal> homeRVModalArrayList, Context context, HomeClickInterface homeClickInterface) {
        this.homeRVModalArrayList = homeRVModalArrayList;
        this.context = context;
        this.homeClickInterface = homeClickInterface;
    }

    @NonNull
    @Override
    public HomeRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout file on below line.
        View view = LayoutInflater.from(context).inflate(R.layout.course_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRVAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        // setting data to our recycler view item on below line.
        HomeRVModal homeRVModal = homeRVModalArrayList.get(position);
        holder.homeTV.setText(homeRVModal.getHomeName());
        holder.homePriceTV.setText("$" + homeRVModal.getHomePrice());
        Picasso.get().load(homeRVModal.getHomeImg()).into(holder.homeIV);
        // adding animation to recycler view item on below line.
        setAnimation(holder.itemView, position);
        holder.homeIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                homeClickInterface.onHomeClick(position);
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
        return homeRVModalArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // creating variable for our image view and text view on below line.
        private ImageView homeIV;
        private TextView homeTV, homePriceTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // initializing all our variables on below line.
            homeIV = itemView.findViewById(R.id.idIVCourse);
            homeTV = itemView.findViewById(R.id.idTVCOurseName);
            homePriceTV = itemView.findViewById(R.id.idTVCousePrice);
        }
    }

    // creating a interface for on click
    public interface HomeClickInterface {
        void onHomeClick(int position);
    }
}

