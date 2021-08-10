package com.example.controlpark;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.controlpark.Adaptadores.AdapterActivity;
import com.example.controlpark.Model.Estacionamiento;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;


public class MenuChoferFragment extends Fragment implements OnMapReadyCallback,GoogleMap.OnInfoWindowClickListener{
    TextView txtNombre;
    ImageView imguser;


    GoogleMap mgoogleMap;
    MapView mapView;
    View mview;

    Boolean actualPosition = true;



    AdapterActivity adapterActivity;
    RecyclerView recyclerViewEstacionamientos;
    ArrayList<Estacionamiento> listaEstacionamientos;
    EditText edtFecha, edtNombre;
    DialogFragment dialogFragment;
    String nombre,fecha, personName;
    int imagen;
    DatabaseReference mDataBase;

    Double longitudOrigen=5.5, latitudOrigen=5.6;

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
        mview= inflater.inflate(R.layout.fragment_menu_chofer, container, false);

        imguser = mview.findViewById(R.id.imgUsuario);
        imguser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Navigation.findNavController(mview).navigate(R.id.action_choferFragment_to_perfilFragment);
            }
        });

        //mapa

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





        //Traer datos de FIREBASE Y PONERLOS AL RECYCLERVIEW

        txtNombre = mview.findViewById(R.id.textUsuario);

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(getContext());
        if (acct != null) {
            String personName = acct.getDisplayName();

            txtNombre.setText(personName);



        }
        recyclerViewEstacionamientos = mview.findViewById(R.id.recyclerView);

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


        mgoogleMap = googleMap;

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }




            return;
        }


        mgoogleMap.setMyLocationEnabled(true);

        mgoogleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

                //2.942043!4d-75.2522789


                if (actualPosition){
                    latitudOrigen = location.getLatitude();
                    longitudOrigen = location.getLongitude();
                    actualPosition=false;

                    LatLng miPosicion = new LatLng(latitudOrigen,longitudOrigen);



                    googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


                    addMarker(googleMap);
                    CameraPosition Liberty = CameraPosition.builder().target(new LatLng(latitudOrigen,longitudOrigen)).zoom(14).bearing(0).tilt(45).build();
                    googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));


                }
            }
        });

        googleMap.setOnInfoWindowClickListener(this);




    }


    public void addMarker(GoogleMap googleMap){

        mDataBase.child("Estacionamientos").child("lista").child("listado").
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    for( DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        Estacionamiento estacionamiento = dataSnapshot1.getValue(Estacionamiento.class);
                        String nombre = estacionamiento.getNombre();
                        String descripcion = estacionamiento.getDescripcion();
                        double latitud = estacionamiento.getLatitud();
                        double longitud = estacionamiento.getLongitud();
                        int espacios = estacionamiento.getEspacios();
                        double precio = estacionamiento.getPrecio();

                        // Toast.makeText(getContext(), "Llega la descripcion"+Descripcion, Toast.LENGTH_SHORT).show();
                        mgoogleMap = googleMap;
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitud,longitud))
                                .title(nombre).snippet("Espacios:"+espacios +"-Precio:"+precio));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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
                        int espacios = estacionamiento.getEspacios();
                        double precio = estacionamiento.getPrecio();

                        Log.e("Direccion", "onDataChange: "+direccion );


                        // Toast.makeText(getContext(), "Llega la descripcion"+Descripcion, Toast.LENGTH_SHORT).show();

                        listaEstacionamientos.add(new Estacionamiento(nombre,direccion,descripcion,latitud,longitud,espacios,precio));

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

    @Override
    public void onInfoWindowClick(Marker marker) {
        Toast.makeText(getContext(),   marker.getTitle(),
                Toast.LENGTH_SHORT).show();

        Bundle bundle = new Bundle();
        bundle.putString("name",marker.getTitle());
        getParentFragmentManager().setFragmentResult("key",bundle);

        Navigation.findNavController(mview).navigate(R.id.action_choferFragment_to_ruta_v2_Fragment);



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
                        String nombre = estacionamiento.getNombre();
                        String direccion = estacionamiento.getDirección();
                        String descripcion = estacionamiento.getDescripcion();
                        double latitud = estacionamiento.getLatitud();
                        double longitud = estacionamiento.getLongitud();
                        int espacios = estacionamiento.getEspacios();

                    }

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

    /*
    public void miLocalizacion(){

        LocationManager locationManager = (LocationManager) getActivity().
                getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

               // txtLatitud.setText(String.valueOf(location.getLatitude()));
                //txtLongitud.setText(String.valueOf(location.getLongitude()));


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
*/

}