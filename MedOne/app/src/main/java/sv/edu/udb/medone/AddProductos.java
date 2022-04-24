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

public class AddProductos extends AppCompatActivity {

    private Button addProductBtn;
    private TextInputEditText productNameEdt, productDescEdt, productPriceEdt, productRestrictionsEdt, productImgEdt, productLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private ProgressBar loadingPB;
    private String productID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_productos);
        addProductBtn = findViewById(R.id.idBtnAEditProduct);
        productNameEdt = findViewById(R.id.idEdtProductName);
        productDescEdt = findViewById(R.id.idEdtProductDescription);
        productPriceEdt = findViewById(R.id.idEdtProductPrice);
        productRestrictionsEdt = findViewById(R.id.idEdtSuitedFor);
        productImgEdt = findViewById(R.id.idEdtProductImageLink);
        productLinkEdt = findViewById(R.id.idEdtProductLink);
        loadingPB = findViewById(R.id.idPBLoadingProduct);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line creating our database reference.
        databaseReference = firebaseDatabase.getReference("Product");
        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingPB.setVisibility(View.VISIBLE);
                // getting data from our edit text.
                String productName = productNameEdt.getText().toString();
                String productDesc = productDescEdt.getText().toString();
                String productPrice = productPriceEdt.getText().toString();
                String productRestrictions = productRestrictionsEdt.getText().toString();
                String productImg = productImgEdt.getText().toString();
                String productLink = productLinkEdt.getText().toString();
                productID = productName;
                // on below line we are passing all data to our modal class.
                ProductRvModal productRVModal = new ProductRvModal(productID, productName, productDesc, productPrice, productRestrictions, productImg, productLink);
                // on below line we are calling a add value event
                // to pass data to firebase database.
                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // on below line we are setting data in our firebase database.
                        databaseReference.child(productID).setValue(productRVModal);
                        // displaying a toast message.
                        Toast.makeText(AddProductos.this, "Se ha agregado el evento a productos", Toast.LENGTH_SHORT).show();
                        // starting a main activity.
                        startActivity(new Intent(AddProductos.this, HomePrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // displaying a failure message on below line.
                        Toast.makeText(AddProductos.this, "Error al agregar a Productos", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}