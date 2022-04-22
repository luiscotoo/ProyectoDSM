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

import java.util.HashMap;
import java.util.Map;

public class EditService extends AppCompatActivity {
    private TextInputEditText serviceNameEdt, serviceDescEdt, servicePriceEdt, serviceRestrictionsEdt, serviceImgEdt, serviceLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ServiceRVModal serviceRVModal;
    private ProgressBar loadingPB;
    // creating a string for our course id.
    private String serviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_service);
        // initializing all our variables on below line.
        Button addServiceBtn = findViewById(R.id.idBtnAddService);
        serviceNameEdt = findViewById(R.id.idEdtServiceName);
        serviceDescEdt = findViewById(R.id.idEdtServiceDescription);
        servicePriceEdt = findViewById(R.id.idEdtServicePrice);
        serviceRestrictionsEdt = findViewById(R.id.idEdtSuitedFor);
        serviceImgEdt = findViewById(R.id.idEdtServiceImageLink);
        serviceLinkEdt = findViewById(R.id.idEdtServiceLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line we are getting our modal class on which we have passed.
        serviceRVModal = getIntent().getParcelableExtra("service");
        Button deleteServiceBtn = findViewById(R.id.idBtnDeleteService);

        if (serviceRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            serviceNameEdt.setText(serviceRVModal.getServiceName());
            servicePriceEdt.setText(serviceRVModal.getServicePrice());
            serviceRestrictionsEdt.setText(serviceRVModal.getServiceRestrictions());
            serviceImgEdt.setText(serviceRVModal.getServiceImg());
            serviceLinkEdt.setText(serviceRVModal.getServiceLink());
            serviceDescEdt.setText(serviceRVModal.getServiceDescription());
            serviceID = serviceRVModal.getServiceId();
        }

        // on below line we are initialing our database reference and we are adding a child as our course id.
        databaseReference = firebaseDatabase.getReference("Service").child(serviceID);

        addServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are making our progress bar as visible.
                loadingPB.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String serviceName = serviceNameEdt.getText().toString();
                String serviceDesc = serviceDescEdt.getText().toString();
                String servicePrice = servicePriceEdt.getText().toString();
                String serviceRestrictions = serviceRestrictionsEdt.getText().toString();
                String serviceImg = serviceImgEdt.getText().toString();
                String serviceLink = serviceLinkEdt.getText().toString();
                // on below line we are creating a map for
                // passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("serviceName", serviceName);
                map.put("serviceDescription", serviceDesc);
                map.put("servicePrice", servicePrice);
                map.put("serviceRestrictions" , serviceRestrictions);
                map.put("serviceImg", serviceImg);
                map.put("serviceLink", serviceLink);
                map.put("serviceId", serviceID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        // adding a map to our database.
                        databaseReference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditService.this, "Se ha actualizado el servicio", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditService.this, ServiciosPrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditService.this, "Error al actualizar el servicio", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        deleteServiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteService();
            }
        });
    }

    private void deleteService() {
        // on below line calling a method to delete the course.
        databaseReference.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Se ha borrado el servicio", Toast.LENGTH_SHORT).show();
        // opening a main activity on below line.
        startActivity(new Intent(EditService.this, ServiciosPrincipal.class));
    }
}