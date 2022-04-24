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

public class EditProductos extends AppCompatActivity {
    private TextInputEditText productNameEdt, productDescEdt, productPriceEdt, productRestrictionsEdt, productImgEdt, productLinkEdt;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    ProductRvModal productRVModal;
    private ProgressBar loadingPB;
    // creating a string for our course id.
    private String productID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_productos);
        Button addProductBtn = findViewById(R.id.idBtnAEditProduct);
        productNameEdt = findViewById(R.id.idEdtProductName);
        productDescEdt = findViewById(R.id.idEdtProductDescription);
        productPriceEdt = findViewById(R.id.idEdtProductPrice);
        productRestrictionsEdt = findViewById(R.id.idEdtSuitedFor);
        productImgEdt = findViewById(R.id.idEdtProductImageLink);
        productLinkEdt = findViewById(R.id.idEdtProductLink);
        loadingPB = findViewById(R.id.idPBLoading);
        firebaseDatabase = FirebaseDatabase.getInstance();
        // on below line we are getting our modal class on which we have passed.
        productRVModal = getIntent().getParcelableExtra("product");
        Button deleteProductBtn = findViewById(R.id.idBtnDeleteProduct);

        if (productRVModal != null) {
            // on below line we are setting data to our edit text from our modal class.
            productNameEdt.setText(productRVModal.getProductName());
            productPriceEdt.setText(productRVModal.getProductPrice());
            productRestrictionsEdt.setText(productRVModal.getProductRestrictions());
            productImgEdt.setText(productRVModal.getProductImg());
            productLinkEdt.setText(productRVModal.getProductLink());
            productDescEdt.setText(productRVModal.getProductDescription());
            productID = productRVModal.getProductId();
        }

        // on below line we are initialing our database reference and we are adding a child as our course id.
        databaseReference = firebaseDatabase.getReference("Product").child(productID);

        addProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are making our progress bar as visible.
                        loadingPB.setVisibility(View.VISIBLE);
                // on below line we are getting data from our edit text.
                String productName = productNameEdt.getText().toString();
                String productDesc = productDescEdt.getText().toString();
                String productPrice = productPriceEdt.getText().toString();
                String productRestrictions = productRestrictionsEdt.getText().toString();
                String productImg = productImgEdt.getText().toString();
                String productLink = productLinkEdt.getText().toString();
                // on below line we are creating a map for
                // passing a data using key and value pair.
                Map<String, Object> map = new HashMap<>();
                map.put("productName", productName);
                map.put("productDescription", productDesc);
                map.put("productPrice", productPrice);
                map.put("productRestrictions" , productRestrictions);
                map.put("productImg", productImg);
                map.put("productLink", productLink);
                map.put("productId", productID);

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // making progress bar visibility as gone.
                        loadingPB.setVisibility(View.GONE);
                        // adding a map to our database.
                        databaseReference.updateChildren(map);
                        // on below line we are displaying a toast message.
                        Toast.makeText(EditProductos.this, "Se ha actualizado el producto", Toast.LENGTH_SHORT).show();
                        // opening a new activity after updating our coarse.
                        startActivity(new Intent(EditProductos.this, ProductosPrincipal.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(EditProductos.this, "Error al actualizar el producto", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        deleteProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteProduct();
            }
        });
    }
    private void deleteProduct() {
        // on below line calling a method to delete the course.
        databaseReference.removeValue();
        // displaying a toast message on below line.
        Toast.makeText(this, "Se ha borrado el producto", Toast.LENGTH_SHORT).show();
        // opening a main activity on below line.
        startActivity(new Intent(EditProductos.this, ProductosPrincipal.class));
    }
}