package sv.edu.udb.medone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditHomeActivity extends AppCompatActivity {

    // creating variables for our edit text, firebase database,
    // database reference, course rv modal,progress bar.
    private TextInputEditText homeNameEdt, homeDescEdt, homePriceEdt, homeRestrictionsEdt, homeImgEdt, homeLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    HomeRVModal homeRVModal;
    private ProgressBar loadingPB;
    // creating a string for our course id.
    private String homeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_home);
        // initializing all our variables on below line.
        Button addHomeBtn = findViewById(R.id.idBtnAddCourse);
        homeNameEdt = findViewById(R.id.idEdtCourseName);
        homeDescEdt = findViewById(R.id.idEdtCourseDescription);
        homePriceEdt = findViewById(R.id.idEdtCoursePrice);
        homeRestrictionsEdt = findViewById(R.id.idEdtSuitedFor);
        homeImgEdt = findViewById(R.id.idEdtCourseImageLink);
        homeLinkEdt = findViewById(R.id.idEdtCourseLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line we are getting our modal class on which we have passed.
        homeRVModal = getIntent().getParcelableExtra("home");
        Button deleteHomeBtn = findViewById(R.id.idBtnDeleteCourse);

        if (homeRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            homeNameEdt.setText(homeRVModal.getHomeName());
            homePriceEdt.setText(homeRVModal.getHomePrice());
            homeRestrictionsEdt.setText(homeRVModal.getHomeRestrictions());
            homeImgEdt.setText(homeRVModal.getHomeImg());
            homeLinkEdt.setText(homeRVModal.getHomeLink());
            homeDescEdt.setText(homeRVModal.getHomeDescription());
            homeID = homeRVModal.getHomeId();
        }

        // on below line we are initialing our database reference and we are adding a child as our course id.
        databaseReference = firebaseDatabase.getReference("Home").child(homeID);
        // on below line we are adding click listener for our add course button.
        addHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // on below line we are making our progress bar as visible.
                loadingPB.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String homeName = homeNameEdt.getText().toString();
                String homeDesc = homeDescEdt.getText().toString();
                String homePrice = homePriceEdt.getText().toString();
                String homeRestrictions = homeRestrictionsEdt.getText().toString();
                String homeImg = homeImgEdt.getText().toString();
                String homeLink = homeLinkEdt.getText().toString();
                // on below line we are creating a map for
                // passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("homeName", homeName);
                map.put("homeDescription", homeDesc);
                map.put("homePrice", homePrice);
                map.put("homeRestrictions" , homeRestrictions);
                map.put("homeImg", homeImg);
                map.put("homeLink", homeLink);
                map.put("homeId", homeID);

                // on below line we are calling a database reference on
                // add value event listener and on data change method
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        // adding a map to our database.
                        databaseReference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditHomeActivity.this, "Se ha actualizado el evento", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditHomeActivity.this, HomePrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on toast.
                        Toast.makeText(EditHomeActivity.this, "Error al actualizar evento", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // adding a click listener for our delete course button.
        deleteHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // calling a method to delete a course.
                deleteHome();
            }
        });

    }

    private void deleteHome() {
        // on below line calling a method to delete the course.
        databaseReference.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Se ha borrado el evento", Toast.LENGTH_SHORT).show();
        // opening a main activity on below line.
        startActivity(new Intent(EditHomeActivity.this, HomePrincipal.class));
    }
}
