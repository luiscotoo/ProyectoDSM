package sv.edu.udb.medone;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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

public class HomePrincipal extends AppCompatActivity implements HomeRVAdapter.HomeClickInterface {

    // creating variables for fab, firebase database,
    // progress bar, list, adapter,firebase auth,
    // recycler view and relative layout.
    private FloatingActionButton addHomeFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView homeRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<HomeRVModal> homeRVModalArrayList;
    private HomeRVAdapter homeRVAdapter;
    private RelativeLayout homeRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_principal);
        // initializing all our variables.
        homeRV = findViewById(R.id.idRVCourses);
        homeRL = findViewById(R.id.idRLBSheet);
        loadingPB = findViewById(R.id.idPBLoading);
        addHomeFAB = findViewById(R.id.idFABAddCourse);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        homeRVModalArrayList = new ArrayList<>();
        // on below line we are getting database reference.
        databaseReference = firebaseDatabase.getReference("Home");
        // on below line adding a click listener for our floating action button.
        addHomeFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // opening a new activity for adding a course.
                Intent i = new Intent(HomePrincipal.this, AddHomeActivity.class);
                startActivity(i);
            }
        });
        // on below line initializing our adapter class.
        homeRVAdapter = new HomeRVAdapter(homeRVModalArrayList, this, this::onHomeClick);
        // setting layout malinger to recycler view on below line.
        homeRV.setLayoutManager(new LinearLayoutManager(this));
        // setting adapter to recycler view on below line.
        homeRV.setAdapter(homeRVAdapter);
        // on below line calling a method to fetch courses from database.
        getHome();
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

    @Override
    public void onHomeClick(int position) {
        // calling a method to display a bottom sheet on below line.
        displayBottomSheet(homeRVModalArrayList.get(position));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a click listener for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.LogOutHomeP), Toast.LENGTH_LONG).show();
                // on below line we are signing out our user.
                mAuth.signOut();
                // on below line we are opening our login activity.
                Intent i = new Intent(HomePrincipal.this, Login.class);
                startActivity(i);
                this.finish();
                return true;
            case R.id.idMenuOptions:
                // displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.RegresandoMenu), Toast.LENGTH_LONG).show();
                // on below line we are signing out our user.
                // on below line we are opening our login activity.
                Intent llamar = new Intent(HomePrincipal.this, HomeActivity.class);
                startActivity(llamar);
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // on below line we are inflating our menu
        // file for displaying our menu options.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void displayBottomSheet(HomeRVModal modal) {
        // on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        // on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_layout, homeRL);
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
        TextView homeNameTV = layout.findViewById(R.id.idTVCourseName);
        TextView homeDescTV = layout.findViewById(R.id.idTVCourseDesc);
        TextView homeForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView homeTV = layout.findViewById(R.id.idTVCoursePrice);
        ImageView homeIV = layout.findViewById(R.id.idIVCourse);
        // on below line we are setting data to different views on below line.
        homeNameTV.setText(modal.getHomeName());
        homeDescTV.setText(modal.getHomeDescription());
        homeForTV.setText(getResources().getString(R.string.RestriccionesHomeP)+": "+ modal.getHomeRestrictions());
        homeTV.setText("$" + modal.getHomePrice());
        Picasso.get().load(modal.getHomeImg()).into(homeIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditCourse);

        // adding on click listener for our edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are opening our EditCourseActivity on below line.
                Intent i = new Intent(HomePrincipal.this, EditHomeActivity.class);
                // on below line we are passing our course modal
                i.putExtra("home", modal);
                startActivity(i);
            }
        });
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
}