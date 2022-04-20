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

public class AddHomeActivity extends AppCompatActivity {

    // creating variables for our button, edit text,
    // firebase database, database reference, progress bar.
    private Button addHomeBtn;
    private TextInputEditText homeNameEdt, homeDescEdt, homePriceEdt, homeRestrictionsEdt, homeImgEdt, homeLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String homeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_home);
        // initializing all our variables.
        addHomeBtn = findViewById(R.id.idBtnAddCourse);
        homeNameEdt = findViewById(R.id.idEdtCourseName);
        homeDescEdt = findViewById(R.id.idEdtCourseDescription);
        homePriceEdt = findViewById(R.id.idEdtCoursePrice);
        homeRestrictionsEdt = findViewById(R.id.idEdtSuitedFor);
        homeImgEdt = findViewById(R.id.idEdtCourseImageLink);
        homeLinkEdt = findViewById(R.id.idEdtCourseLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Home");
        // adding click listener for our add course button.
        addHomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String homeName = homeNameEdt.getText().toString();
                String homeDesc = homeDescEdt.getText().toString();
                String homePrice = homePriceEdt.getText().toString();
                String homeRestrictions = homeRestrictionsEdt.getText().toString();
                String homeImg = homeImgEdt.getText().toString();
                String homeLink = homeLinkEdt.getText().toString();
                homeID = homeName;
                // on below line we are passing all data to our modal class.
                HomeRVModal homeRVModal = new HomeRVModal(homeID, homeName, homeDesc, homePrice, homeRestrictions, homeImg, homeLink);
                // on below line we are calling a add value event
                // to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        databaseReference.child(homeID).setValue(homeRVModal);
                        // displaying a toast message.
                        Toast.makeText(AddHomeActivity.this, "Se ha agregado el evento a home", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(AddHomeActivity.this, HomePrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
                        Toast.makeText(AddHomeActivity.this, "Error al agregar a Home", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
