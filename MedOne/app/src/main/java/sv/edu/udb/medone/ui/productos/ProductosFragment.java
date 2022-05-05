package sv.edu.udb.medone.ui.productos;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import sv.edu.udb.medone.EditProductos;
import sv.edu.udb.medone.ProductRvAdapter;
import sv.edu.udb.medone.ProductRvModal;
import sv.edu.udb.medone.ProductosPrincipal;
import sv.edu.udb.medone.R;
import sv.edu.udb.medone.databinding.FragmentProductosBinding;

public class ProductosFragment extends Fragment {

    private ProductosViewModel productosViewModel;
    private FragmentProductosBinding binding;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference cartReference;
    private RecyclerView productRV;
    private FirebaseAuth mAuth;
    private ProgressBar loadingPB;
    private ArrayList<ProductRvModal> productRVModalArrayList;
    private ProductRvAdapter productRVAdapter;
    private RelativeLayout productRL;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        productosViewModel =
                new ViewModelProvider(this).get(ProductosViewModel.class);

        binding = FragmentProductosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        productRV= binding.idRVProductos;
        productRL= binding.idRLProductos;
        loadingPB= binding.idPBLoadingProduct;
        firebaseDatabase=FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        productRVModalArrayList=new ArrayList<>();
        databaseReference = firebaseDatabase.getReference("Product");
        cartReference=firebaseDatabase.getReference("Cart");
        productRVAdapter=new ProductRvAdapter(productRVModalArrayList,getContext(),this::onProductClick);
        productRV.setLayoutManager(new LinearLayoutManager(getContext()));
        productRV.setAdapter(productRVAdapter);
        getProduct();
        return root;
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
    private void displayBottomSheet(ProductRvModal modal) {
        final BottomSheetDialog bottomSheetTeachersDialog = new BottomSheetDialog(getContext(), R.style.BottomSheetDialogTheme);
        View layout = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_products, productRL,false);
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
        // adding on click listener for our edit button.
        AddtoCarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cartReference.child(userid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CartModal cartModal = snapshot.getValue(CartModal.class);
                        ArrayList<ProductRvModal> productRvModalArrayList= cartModal.getProductRvModalArrayList();
                        productRvModalArrayList.add(modal);
                        Float total= Float.parseFloat(modal.getProductPrice()+ cartModal.getTotal());
                        Integer cantidad= productRvModalArrayList.size()+1;
                        cartModal.setCantidad(cantidad);
                        cartModal.setProductRvModalArrayList(productRvModalArrayList);
                        cartModal.setTotal(total);
                        cartReference.child(userid).setValue(cartModal);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}