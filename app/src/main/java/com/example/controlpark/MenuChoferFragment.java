package com.example.controlpark;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.controlpark.Adaptadores.AdapterActivity;
import com.example.controlpark.Model.Estacionamiento;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MenuChoferFragment extends Fragment {
    View v;
    Button perfil;
    TextView txtNombre;

    AdapterActivity adapterActivity;
    RecyclerView recyclerViewEstacionamientos;
    ArrayList<Estacionamiento> listaEstacionamientos;
    EditText edtFecha, edtNombre;
    DialogFragment dialogFragment;
    String nombre,fecha, personName;
    int imagen;
    DatabaseReference mDataBase;

    Activity activity;
    //    iComunicaFragments interfazComunicaFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v= inflater.inflate(R.layout.fragment_menu_chofer, container, false);

        perfil = v.findViewById(R.id.btnPerfil);
        txtNombre = v.findViewById(R.id.textUsuario);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();

            txtNombre.setText(personName);



        }


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_choferFragment_to_perfilFragment);

            }
        });


        recyclerViewEstacionamientos = v.findViewById(R.id.recyclerView);

        recyclerViewEstacionamientos.setLayoutManager(new LinearLayoutManager(getContext()));
        mDataBase = FirebaseDatabase.getInstance().getReference();


        listaEstacionamientos = new ArrayList<>();

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acc != null) {

            getDatosFromFirebase();
        }
        else{
            Toast.makeText(getContext(), "No hay cuenta Asociada", Toast.LENGTH_SHORT).show();
        }


        return v;
    }


    public  void getDatosFromFirebase(){

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {


            personName = acct.getDisplayName();


            Log.e("Correo de usuario en HF", "Este es su correo en HF:" + personName);

        }

        mDataBase.child("Estacionamientos").child("lista").child("listado").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){



                    for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                        Estacionamiento estacionamiento = dataSnapshot1.getValue(Estacionamiento.class);
                        String nombre = estacionamiento.getNombre();
                        String direccion = estacionamiento.getDirección();
                        String descripcion = estacionamiento.getDescripcion();
                        double latitud = estacionamiento.getLatitud();
                        double longitud = estacionamiento.getLongitud();
                        int espacios = estacionamiento.getEspaciosDisponibles();


                        // Toast.makeText(getContext(), "Llega la descripcion"+Descripcion, Toast.LENGTH_SHORT).show();

                        listaEstacionamientos.add(new Estacionamiento(nombre,direccion,descripcion,latitud,longitud,espacios));

                    }



                    if(getActivity()!=null){
                        adapterActivity= new AdapterActivity(getContext(),listaEstacionamientos);
                        recyclerViewEstacionamientos.setAdapter(adapterActivity);

                        adapterActivity.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String nombre = listaEstacionamientos.get(recyclerViewEstacionamientos.getChildAdapterPosition(v)).getNombre();
                                Toast.makeText(getContext(), "Seleccionó: "+ nombre, Toast.LENGTH_SHORT).show();
                                //interfazComunicaFragments.enviarTarea(listaActividad.get(recyclerViewActividades.getChildAdapterPosition(v)));
                            }
                        });

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }
}