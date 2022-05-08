package sv.edu.udb.medone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

import sv.edu.udb.medone.ui.productos.CartModal;

public class ShoppingCart extends AppCompatActivity {
    FirebaseDatabase firebaseDatabase;
    DatabaseReference cartReference;
    private FirebaseAuth mAuth;
    private RecyclerView productRV;
    private ArrayList<ProductRvModal> productRVModalArrayList;
    private ProductRvAdapter productRVAdapter;
    private ConstraintLayout productRL;
    private TextView totalPrecio;
    private Button comprar;
    private double cantidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_cart);
        productRV = findViewById(R.id.idRVShoping);
        firebaseDatabase = FirebaseDatabase.getInstance();
        productRVModalArrayList = new ArrayList<>();
        productRL = findViewById(R.id.SCRl);
        totalPrecio= findViewById(R.id.totalPrecio);
        comprar= findViewById(R.id.comprar);
        cartReference=firebaseDatabase.getReference("Cart");
        mAuth = FirebaseAuth.getInstance();
        productRVAdapter=new ProductRvAdapter(productRVModalArrayList,this,this::onProductClick);
        productRV.setLayoutManager(new LinearLayoutManager(this));
        productRV.setAdapter(productRVAdapter);

        comprar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),PagoActivity.class);
                intent.putExtra("cantidad",String.valueOf(cantidad));
                startActivity(intent);
            }
        });
        getProduct();
        String iduser = mAuth.getUid();
        cartReference.child(iduser).child("total").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    cantidad = snapshot.getValue(Double.class);
                    totalPrecio.setText(String.format(Locale.US,"Total: $%.2f",cantidad));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void getProduct(){
        productRVModalArrayList.clear();
        String iduser = mAuth.getUid();
        cartReference.child(iduser).child("productRvModalArrayList").addChildEventListener(new ChildEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productRVModalArrayList.add(snapshot.getValue(ProductRvModal.class));
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                productRVAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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

   public void AsignarCantidad(ProductRvModal p){
        cantidad = cantidad + Float.parseFloat(p.getProductPrice());

       for (int i=0; i<productRVModalArrayList.size();i++){
           cantidad = cantidad + Float.parseFloat(productRVModalArrayList.get(i).getProductPrice());
       }
       totalPrecio.setText("Total: $"+ cantidad);
   }



    private void displayBottomSheet(ProductRvModal modal){
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_products, productRL,false);
        bottomSheetTeachersDialog.setContentView(layout);
        bottomSheetTeachersDialog.setCancelable(false);
        bottomSheetTeachersDialog.setCanceledOnTouchOutside(true);
        bottomSheetTeachersDialog.show();
        TextView productNameTV = layout.findViewById(R.id.idTVProductName);
        TextView productDescTV = layout.findViewById(R.id.idTVProductDesc);
        TextView productForTV = layout.findViewById(R.id.idTVSuitedFor);
        TextView productTV = layout.findViewById(R.id.idTVProductPrice);
        ImageView productIV = layout.findViewById(R.id.idIVProduct);
        productNameTV.setText(modal.getProductName());
        productDescTV.setText(modal.getProductDescription());
        productForTV.setText("Restrictions"+": " + modal.getProductRestrictions());
        productTV.setText("$" + modal.getProductPrice());
        Picasso.get().load(modal.getProductImg()).into(productIV);
        Button viewBtn = layout.findViewById(R.id.idBtnVIewDetails);
        Button AddtoCarrito = layout.findViewById(R.id.idBtnEditProduct);
        AddtoCarrito.setText("Agregar al carrito");
        String userid= mAuth.getUid();
    }
}