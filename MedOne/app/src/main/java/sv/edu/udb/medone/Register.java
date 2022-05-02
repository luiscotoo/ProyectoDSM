package sv.edu.udb.medone;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Register extends AppCompatActivity {

    private EditText etEmailR, etPasswordR;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    private String userIsUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        mAuth = FirebaseAuth.getInstance();

        etEmailR = findViewById(R.id.etEmailR);
        etPasswordR = findViewById(R.id.etPasswordR);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerNewUser();
            }
        });
    }

    private void registerNewUser() {

        String email, password;
        email = etEmailR.getText().toString();
        password = etPasswordR.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.WarningEmail), Toast.LENGTH_LONG).show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.WarningPassword), Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            String userEmail, userPassword, userID;
                            userID = user.getUid();
                            userEmail = etEmailR.getText().toString();
                            userPassword = etPasswordR.getText().toString();
                            userIsUser = "1";
                            UserRVModal userRVModal= new UserRVModal(userID, userEmail, userPassword, userIsUser);

                            databaseReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    // on below line we are setting data in our firebase database.
                                    databaseReference.child(userID).setValue(userRVModal);
                                    // displaying a toast message.
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.ExitosoRegister), Toast.LENGTH_LONG).show();
                                    // starting a main activity.
                                    Intent intent = new Intent(Register.this, Login.class);
                                    startActivity(intent);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    // displaying a failure message on below line.
                                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.FallidoRegister), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                        else {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.FallidoRegister), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void Volver(View v){
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }

}