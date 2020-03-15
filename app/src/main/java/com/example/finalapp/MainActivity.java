package com.example.finalapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {
    TextView email,phone,fullName,textHomepage,verifyMsg,resendCode;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        phone = findViewById(R.id.profilePhone);
        email = findViewById(R.id.profileEmail);
        fullName = findViewById(R.id.profileName);
        textHomepage = findViewById(R.id.textHomepage);
        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        userId =fAuth.getCurrentUser().getUid();
        final FirebaseUser user = fAuth.getCurrentUser();


        textHomepage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Homepage.class);
                startActivity(intent);

            }
        });

            if(!user.isEmailVerified()){
                resendCode.setVisibility(View.VISIBLE);
                verifyMsg.setVisibility(View.VISIBLE);

                resendCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // Verification link

                        user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(v.getContext(),"A Verification Link Has Been Sent To Your Email ",Toast.LENGTH_SHORT).show();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("tag","Error: Email Not Sent "+ e.getMessage());

                            }
                        });

                    }
                });

            }

        DocumentReference documentReference = fStore.collection("Users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                phone.setText(documentSnapshot.getString("Phone Number"));
                fullName.setText(documentSnapshot.getString("Full Name"));
                email.setText(documentSnapshot.getString("email"));
            }


        });


    }

    public void logout (View view){

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),Login.class));
        finish();
    }






}
