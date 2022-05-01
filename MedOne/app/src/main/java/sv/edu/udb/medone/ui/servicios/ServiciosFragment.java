package sv.edu.udb.medone.ui.servicios;

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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sv.edu.udb.medone.EditService;
import sv.edu.udb.medone.R;
import sv.edu.udb.medone.ServiceRVAdapter;
import sv.edu.udb.medone.ServiceRVModal;
import sv.edu.udb.medone.ServiciosPrincipal;
import sv.edu.udb.medone.databinding.FragmentServiciosBinding;
import sv.edu.udb.medone.databinding.FragmentServiciosBinding;

public class ServiciosFragment extends Fragment {

    private ServiciosViewModel serviciosViewModel;
    private FragmentServiciosBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView serviceRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ServiceRVModal> serviceRVModalArrayList;
    private ServiceRVAdapter serviceRVAdapter;
    private RelativeLayout serviceRL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        serviciosViewModel =
                new ViewModelProvider(this).get(ServiciosViewModel.class);

        binding = FragmentServiciosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        serviceRV=binding.idRVServices;
        serviceRL=binding.idRLService;
        loadingPB = binding.idPBLoadingService;
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        serviceRVModalArrayList=new ArrayList<>();
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Service");

        serviceRVAdapter=new ServiceRVAdapter(serviceRVModalArrayList,getContext(),this::onServiceClick);
        // setting layout malinger to recycler view on below line.
        serviceRV.setLayoutManager(new LinearLayoutManager(getContext()));
        // setting adapter to recycler view on below line.
        serviceRV.setAdapter(serviceRVAdapter);
        // on below line calling a method to fetch courses from database.
        getService();


        return root;
    }

    private void getService() {
        // on below line clearing our list.
        serviceRVModalArrayList.clear();
        // on below line we are calling add child event listener method to read the data.
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // on below line we are hiding our progress bar.
                loadingPB.setVisibility(View.GONE);
                // adding snapshot to our array list on below line.
                serviceRVModalArrayList.add(snapshot.getValue(ServiceRVModal.class));
                // notifying our adapter that data has changed.
                serviceRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                loadingPB.setVisibility(View.GONE);
                serviceRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                // notifying our adapter when child is removed.
                serviceRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                // notifying our adapter when child is moved.
                serviceRVAdapter.notifyDataSetChanged();
                loadingPB.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void onServiceClick(int position) {
        // calling a method to display a bottom sheet on below line.
        displayBottomSheet(serviceRVModalArrayList.get(position));
    }

    private void displayBottomSheet(ServiceRVModal modal) {
        // on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        // on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_service_home, serviceRL,false);
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
        TextView serviceNameTV = layout.findViewById(R.id.idTVServiceName);
        TextView serviceDescTV = layout.findViewById(R.id.idTVServiceDesc);
        TextView serviceForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView serviceTV = layout.findViewById(R.id.idTVServicePrice);
        ImageView serviceIV = layout.findViewById(R.id.idIVService);
        // on below line we are setting data to different views on below line.
        serviceNameTV.setText(modal.getServiceName());
        serviceDescTV.setText(modal.getServiceDescription());
        serviceForTV.setText(getResources().getString(R.string.RestriccionesHomeP)+": " + modal.getServiceRestrictions());
        serviceTV.setText("$" + modal.getServicePrice());
        Picasso.get().load(modal.getServiceImg()).into(serviceIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);

        // adding click listener for our view button on below line.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getServiceLink()));
                startActivity(i);
            }
        });
    }

    public ServiciosFragment(){

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}