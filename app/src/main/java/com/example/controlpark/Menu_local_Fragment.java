package com.example.controlpark;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;


public class Menu_local_Fragment extends Fragment {

    public Button btnver,btncrear,btnexit;
    public TextView txtNombre;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_menu_local_, container, false);

        btnver=v.findViewById(R.id.btnver);
        btncrear=v.findViewById(R.id.btncrear);
        btnexit=v.findViewById(R.id.btnexit);
        txtNombre = v.findViewById(R.id.textUsuario);
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();
            int espacio=personName.indexOf(" ");
            String nombre= personName.substring(0,espacio);
            txtNombre.setText(nombre);



        }
        btnver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_menu_local_Fragment_to_edit_localFragment);

            }
        });

        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(v).navigate(R.id.action_menu_local_Fragment_to_crearLocalFragment);

            }
        });

        btnexit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Navigation.findNavController(v).navigate(R.id.action_menu_local_Fragment_to_rutaFragment);
                switch (view.getId()) {
                    // ...
                    case R.id.btnexit:
                       // signOut();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        break;
                    // ...
                }
            }
        });
        return v;
    }
}