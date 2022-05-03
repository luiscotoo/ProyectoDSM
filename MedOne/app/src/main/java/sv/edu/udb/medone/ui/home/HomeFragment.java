package sv.edu.udb.medone.ui.home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sv.edu.udb.medone.AddHomeActivity;
import sv.edu.udb.medone.EditHomeActivity;
import sv.edu.udb.medone.HomePrincipal;
import sv.edu.udb.medone.HomeRVAdapter;
import sv.edu.udb.medone.HomeRVModal;
import sv.edu.udb.medone.R;
import sv.edu.udb.medone.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView homeRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<HomeRVModal> homeRVModalArrayList;
    private HomeRVAdapter homeRVAdapter;
    private RelativeLayout homeRL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeRV = binding.idRVCourses;
        homeRL = binding.idRLHome;
        loadingPB = binding.idPBLoading;
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        homeRVModalArrayList = new ArrayList<>();
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Home");

        // on below line initializing our adapter class.
        homeRVAdapter = new HomeRVAdapter(homeRVModalArrayList, getContext(), this::onHomeClick);
        // setting layout malinger to recycler view on below line.
        homeRV.setLayoutManager(new LinearLayoutManager(getContext()));
        // setting adapter to recycler view on below line.
        homeRV.setAdapter(homeRVAdapter);
        // on below line calling a method to fetch courses from database.
        getHome();


        return root;
    }

    private void getHome() {
        // on below line clearing our list.
        homeRVModalArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                homeRVModalArrayList.add(snapshot.getValue(HomeRVModal.class));
                // notifying our adapter that data has changed.
                homeRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                loadingPB.setVisibility(View.GONE);
                homeRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                homeRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                homeRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void onHomeClick(int position) {
        // calling a method to display a bottom sheet on below line.
        displayBottomSheet(homeRVModalArrayList.get(position));
    }

    private void displayBottomSheet(HomeRVModal modal) {
        // on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        // on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_service_home, homeRL,false);
        // setting content view for bottom sheet on below line.
        bottomSheetTeachersDialog.setContentView(layout);
        // on below line we are setting a cancelable
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        // calling a method to display our bottom sheet.
        bottomSheetTeachersDialog.show();
        // on below line we are creating variables for
        // our text view and image view inside bottom sheet
        // and initialing them with their ids.
        TextView homeNameTV = layout.findViewById(R.id.idTVServiceName);
        TextView homeDescTV = layout.findViewById(R.id.idTVServiceDesc);
        TextView homeForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView homeTV = layout.findViewById(R.id.idTVServicePrice);
        ImageView homeIV = layout.findViewById(R.id.idIVService);
        // on below line we are setting data to different views on below line.
        homeNameTV.setText(modal.getHomeName());
        homeDescTV.setText(modal.getHomeDescription());
        homeForTV.setText(getResources().getString(R.string.RestriccionesHomeP)+": "+ modal.getHomeRestrictions());
        homeTV.setText("$" + modal.getHomePrice());
        Picasso.get().load(modal.getHomeImg()).into(homeIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);

        // adding click listener for our view button on below line.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are navigating to browser
                // for displaying course details from its url
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getHomeLink()));
                startActivity(i);
            }
        });
    }

    public HomeFragment(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}