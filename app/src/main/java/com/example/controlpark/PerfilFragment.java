package com.example.controlpark;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PerfilFragment extends Fragment {
    View v;
    String CorreoUsuario,id, nombreUsuario;
    public TextView txtNombre,txtEmail,txtID;
    private ImageView imageUsuario;
    private Button btnCerrarS;
    private TextView textPuntaje;
    GoogleSignInClient mGoogleSignInClient;
    String puntaje,personName;
    String calificacionFB;
    DatabaseReference mDataBase;
    public static DatabaseReference mRootReference;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v=inflater.inflate(R.layout.fragment_perfil, container, false);



        mDataBase= FirebaseDatabase.getInstance().getReference();
        //getDatosFromFirebase();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);


        txtNombre =v.findViewById(R.id.txtNom);
        txtEmail =v.findViewById(R.id.txtEm);
        txtID =v.findViewById(R.id.txtid);
        imageUsuario =v.findViewById(R.id.Imagefoto1);

        btnCerrarS = v.findViewById(R.id.btnCerrarSesion);
        btnCerrarS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    // ...
                    case R.id.btnCerrarSesion:
                        signOut();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    // ...
                }

            }
        });










        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            String personEmail = acct.getEmail();
            String personId = acct.getId();
            Uri personPhoto = acct.getPhotoUrl();

            txtNombre.setText(personName);
            txtEmail.setText(personEmail);
            txtID.setText(personId);
            Glide.with(getContext()).load(String.valueOf(personPhoto)).into(imageUsuario);

            Toast.makeText(getContext(), "Este es su informacion de usuario", Toast.LENGTH_LONG).show();

            nombreUsuario = acct.getDisplayName();
            CorreoUsuario = acct.getEmail();
            id= acct.getId();
            mRootReference = FirebaseDatabase.getInstance().getReference();
            // FirebaseDatabase database = FirebaseDatabase.getInstance();
            // mDataBase = database.getReference();
            mRootReference.child("Usuarios").child(nombreUsuario).child("Correo").setValue(CorreoUsuario);
            //mRootReference.child("nombre").setValue("Frank");

        }


        return v;
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getContext(), "Cerró sesion Exitosamente", Toast.LENGTH_SHORT).show();

                    }
                });
    }

    public  void getDatosFromFirebase(){

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {


            personName = acct.getDisplayName();


            Log.e("Correo de usuario en HF", "Este es su correo en HF:" + personName    );

        }



        mDataBase.child("Usuarios").child(personName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    calificacionFB=dataSnapshot.child("Puntaje").getValue().toString();
                    textPuntaje.setText(calificacionFB);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}