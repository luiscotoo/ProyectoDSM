package sv.edu.udb.medone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class ProductosPrincipal extends AppCompatActivity {
    private FloatingActionButton addProductFAB;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private RecyclerView productRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ProductRvModal> productRVModalArrayList;
    private ProductRvAdapter productRVAdapter;
    private RelativeLayout productRL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productos_principal);
        productRV=findViewById(R.id.idRVProductos);
        productRL=findViewById(R.id.idRLProductos);
        loadingPB = findViewById(R.id.idPBLoading);
        addProductFAB=findViewById(R.id.idFABAddProductos);
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        productRVModalArrayList=new ArrayList<>();

        databaseReference = firebaseDatabase.getReference("Product");
        productRVAdapter=new ProductRvAdapter(productRVModalArrayList,this,this::onProductClick);
        productRV.setLayoutManager(new GridLayoutManager(this,2));
        productRV.setAdapter(productRVAdapter);
        getProduct();
    }
    private void getProduct(){
        productRVModalArrayList.clear();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                productRVModalArrayList.add(snapshot.getValue(ProductRvModal.class));
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                loadingPB.setVisibility(View.GONE);
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    
    public void onProductClick(int position){
        displayBottomSheet(productRVModalArrayList.get(position));
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // adding a click listener for option selected on below line.
        int id = item.getItemId();
        switch (id) {
            case R.id.idLogOut:
                // displaying a toast message on user logged out inside on click.
                Toast.makeText(getApplicationContext(), "User Logged Out", Toast.LENGTH_LONG).show();
                // on below line we are signing out our user.
                mAuth.signOut();
                // on below line we are opening our login activity.
                Intent i = new Intent(ProductosPrincipal.this, Login.class);
                startActivity(i);
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
    private void displayBottomSheet(ProductRvModal modal) {
        // on below line we are creating our bottom sheet dialog.
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        // on below line we are inflating our layout file for our bottom sheet.
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_service, productRL,false);
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
        serviceNameTV.setText(modal.getProductName());
        serviceDescTV.setText(modal.getProductDescription());
        serviceForTV.setText("Restricciones " + modal.getProductRestrictions());
        serviceTV.setText("$" + modal.getProductPrice());
        Picasso.get().load(modal.getProductImg()).into(serviceIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button editBtn = layout.findViewById(R.id.idBtnEditService);

        // adding on click listener for our edit button.
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // on below line we are opening our EditCourseActivity on below line.
                Intent i = new Intent(ProductosPrincipal.this, EditService.class);
                // on below line we are passing our course modal
                i.putExtra("product", modal);
                startActivity(i);
            }
        });
        // adding click listener for our view button on below line.
        viewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(modal.getProductLink()));
                startActivity(i);
            }
        });
    }
}