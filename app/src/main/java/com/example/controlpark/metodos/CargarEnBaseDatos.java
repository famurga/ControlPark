package com.example.controlpark.metodos;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.controlpark.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class CargarEnBaseDatos extends AppCompatActivity {


    public static DatabaseReference mRootReference;

    TextView txtnom, txtape,txttel,txtdir;
    public String personEmail="no entra";
    String nombre2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nombre2 = getIntent().getStringExtra("nombre");

    }




    public void cargarDatosFirebase(String nombre,String direccion, String descripcion, double latitud, double longitud ,
                                    int espacios, double precio) {




        Log.e("Prueba1","El correo es:");

        Map<Object, Object> datosEstacionamientos = new HashMap<>();
        datosEstacionamientos.put("nombre", nombre);
        datosEstacionamientos.put("direccion", direccion);
        datosEstacionamientos.put("descripcion", descripcion);
        datosEstacionamientos.put("latitud", latitud);
        datosEstacionamientos.put("longitud", longitud);
        datosEstacionamientos.put("espacios", espacios);
        datosEstacionamientos.put("precio", precio);





        mRootReference = FirebaseDatabase.getInstance().getReference();

        mRootReference.child("Estacionamientos").child("lista").child("listado").push().setValue(datosEstacionamientos);
    }


/*
    public void verificarExiste(String dato){

        Query consulta = FirebaseDatabase.getInstance().getReference()
                .child("Usuario").orderByChild("nombre").equalTo(dato);
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {


                        Actividad acti = dataSnapshot1.getValue(Actividad.class);
                        String nombre = acti.getNombre();
                        String apellido = acti.getFecha();
                        int telefono = acti.getImagenId();



                        txtnom.setText(String.valueOf(nombre));
                        txtape.setText(String.valueOf(apellido));
                        txttel.setText(String.valueOf(telefono));


                    }

                }
                else{
                    Toast.makeText(CargarEnBaseDatos.this, "No se encuentra en la base de datos", Toast.LENGTH_SHORT).show();
                    Log.e("NombreUsuario:", "No se encuentra en la base de datos");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


}
