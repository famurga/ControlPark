package com.example.controlpark;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlpark.Model.Estacionamiento;
import com.example.controlpark.metodos.CargarEnBaseDatos;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class edit_localFragment extends Fragment implements OnMapReadyCallback {
    EditText name,dir,des,lat,lon,espacio,precios;
    Button btneditar, btneliminar;
    String nomb,lati,longi,espa, desc,dire,preci;
    View mview;
    Button gps;
    DatabaseReference mDataBase;
    double dlati,dlong,dprecio;
    int iespacios;
    GoogleMap mgoogleMap;

    MapView mapView;
    String nombre,direccion,descripcion;
    double latitud,longitud,precio;
    int espacios;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview = inflater.inflate(R.layout.fragment_edit_local, container, false);

        mDataBase = FirebaseDatabase.getInstance().getReference();
        verificarExistencia("Frank ");
        int permisoUbicacion = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION);
        int permisoUbicacion2 = ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION);

        if (permisoUbicacion != PackageManager.PERMISSION_GRANTED || permisoUbicacion2
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {



            } else {
                ActivityCompat.requestPermissions(getActivity(),new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION},1);

            }
        }
        gps = mview.findViewById(R.id.btngps);
        //Formulario
        name=mview.findViewById(R.id.edtNombre);
        dir=mview.findViewById(R.id.edtDir);
        des=mview.findViewById(R.id.edtDesc);
        lat=mview.findViewById(R.id.edtLat);
        lon=mview.findViewById(R.id.edtLong);
        espacio=mview.findViewById(R.id.edtEspacios);
        precios=mview.findViewById(R.id.edtprecioso);
        btneditar=mview.findViewById(R.id.btnactualizar);
        btneliminar=mview.findViewById(R.id.btndelete);


        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miLocalizacion();
            }
        });

        btneditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nomb = name.getText().toString();
                dire = dir.getText().toString();
                desc = des.getText().toString();
                lati = lat.getText().toString();
                longi = lon.getText().toString();
                espa = espacio.getText().toString();
                preci= precios.getText().toString();

                if(!lati.isEmpty() || !longi.isEmpty()|| !preci.isEmpty()){
                    dlati = Double.parseDouble(lati);
                    dlong = Double.parseDouble(longi);
                    iespacios =Integer.parseInt(espa);
                    dprecio =Double.parseDouble(preci);

                }
                else{
                    Toast.makeText(getContext(),"Debe obtener la ubicación", Toast.LENGTH_SHORT).show();
                }


                if( nomb.isEmpty() || dire.isEmpty() || desc.isEmpty()  || lati.isEmpty()  || longi.isEmpty()  || espa.isEmpty()  ){
                    Toast.makeText(getContext(), "Tiene que ingresar todos los campos", Toast.LENGTH_SHORT).show();
                }
                else{

                    Map<String,Object> estacio= new HashMap<>();
                    estacio.put("nombre",nomb);
                    estacio.put("direccion",dire);
                    estacio.put("descripcion",desc);
                    estacio.put("latitud",dlati);
                    estacio.put("longitud",dlong);
                    estacio.put("espacios",iespacios);
                    estacio.put("precio",dprecio);



                    mDataBase.child("Estacionamientos").child("lista").child("listado").push().updateChildren(estacio).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(), "Los datos se han actualizado correctamente", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull  Exception e) {
                            Toast.makeText(getContext(), "Hubo un error al actualizar", Toast.LENGTH_SHORT).show();
                        }
                    });



                    Toast.makeText(getContext(), "Estacionamiento Editado", Toast.LENGTH_SHORT).show();

                }


            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return mview;


    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = (MapView) mview.findViewById(R.id.map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
    }

    public void miLocalizacion(){

        LocationManager locationManager = (LocationManager) getActivity().
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                lat.setText(String.valueOf(location.getLatitude()));
                lon.setText(String.valueOf(location.getLongitude()));


            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int permision = ContextCompat.checkSelfPermission(getActivity(), ACCESS_FINE_LOCATION);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0,
                locationListener);
    }

    public void verificarExistencia(String dato){

        Query consulta = FirebaseDatabase.getInstance().getReference()
                .child("Estacionamientos").child("lista").child("listado").orderByChild("nombre").equalTo(dato);
        consulta.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Estacionamiento estacionamiento = dataSnapshot1.getValue(Estacionamiento.class);
                        nombre = estacionamiento.getNombre();
                         direccion = estacionamiento.getDirección();
                         descripcion = estacionamiento.getDescripcion();
                        latitud = estacionamiento.getLatitud();
                         longitud = estacionamiento.getLongitud();
                         espacios = estacionamiento.getEspacios();
                       precio = estacionamiento.getPrecio();

                        Log.e("El precio es", "onDataChange: "+precio );
                    }

                            name.setText(nombre);
                            dir.setText(direccion);
                             des.setText(descripcion);
                            lat.setText(String.valueOf(latitud));
                            lon.setText(String.valueOf(longitud));
                            espacio.setText(String.valueOf(espacios));
                         precios.setText(String.valueOf(precio));


                }
                else{
                    Toast.makeText(getActivity(), "No se encuentra ese dato", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}