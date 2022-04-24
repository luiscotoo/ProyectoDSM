package sv.edu.udb.medone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddService extends AppCompatActivity {
    private Button addServiceBtn;
    private TextInputEditText serviceNameEdt, serviceDescEdt, servicePriceEdt, serviceRestrictionsEdt, serviceImgEdt, serviceLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_service);
        addServiceBtn=findViewById(R.id.idBtnUpdateService);
        serviceNameEdt=findViewById(R.id.idEdtServiceName);
        serviceDescEdt=findViewById(R.id.idEdtServiceDescription);
        servicePriceEdt=findViewById(R.id.idEdtServicePrice);
        serviceRestrictionsEdt=findViewById(R.id.idEdtSuitedFor);
        serviceImgEdt=findViewById(R.id.idEdtServiceImageLink);
        serviceLinkEdt=findViewById(R.id.idEdtServiceLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Service");

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String serviceName = serviceNameEdt.getText().toString();
                String serviceDesc = serviceDescEdt.getText().toString();
                String servicePrice = servicePriceEdt.getText().toString();
                String serviceRestrictions = serviceRestrictionsEdt.getText().toString();
                String serviceImg = serviceImgEdt.getText().toString();
                String serviceLink = serviceLinkEdt.getText().toString();
                serviceID = serviceName;

                ServiceRVModal serviceRVModal=new ServiceRVModal(serviceName,serviceDesc,serviceRestrictions,serviceImg,serviceID,servicePrice,serviceLink);


                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        databaseReference.child(serviceID).setValue(serviceRVModal);
                        // displaying a toast message.
                        Toast.makeText(AddService.this, "Se ha agregado el servicio", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(AddService.this, ServiciosPrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(AddService.this, "Error al agregar a servicios", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}