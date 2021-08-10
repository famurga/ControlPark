package com.example.controlpark;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.controlpark.metodos.CargarEnBaseDatos;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class CrearLocalFragment extends Fragment implements OnMapReadyCallback {

    GoogleMap mgoogleMap;
    MapView mapView;
    View mview;
    Button gps;

    //Formulario
    EditText name,dir,des,lat,lon,espacios,precio;
    Button btncrear;
    String nomb,lati,longi,espa, desc,dire,preci;
    double dlati,dlong,dprecio;
    int iespacios;

    DatabaseReference mDataBase;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mview= inflater.inflate(R.layout.fragment_crear_local, container, false);
        mDataBase = FirebaseDatabase.getInstance().getReference();

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
        espacios=mview.findViewById(R.id.edtEspacios);
        precio=mview.findViewById(R.id.edtPrecio);
        btncrear=mview.findViewById(R.id.btnCrear);





        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                miLocalizacion();
            }
        });

        btncrear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nomb = name.getText().toString();
                dire = dir.getText().toString();
                desc = des.getText().toString();
                lati = lat.getText().toString();
                longi = lon.getText().toString();
                espa = espacios.getText().toString();
                preci= precio.getText().toString();

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
                    CargarEnBaseDatos c = new CargarEnBaseDatos();
                    c.cargarDatosFirebase(nomb,dire,desc,dlati,dlong,iespacios,dprecio);
                    Toast.makeText(getContext(), "Estacionamiento Creado", Toast.LENGTH_SHORT).show();

                }


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


        MapsInitializer.initialize(getContext());

        mgoogleMap = googleMap;
        mgoogleMap.setMyLocationEnabled(true);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-16.3846,-71.5243695)).title("MI POSICION ALEATORIA").snippet("ewrwerwerw"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(-16.3846,-71.5243695)).zoom(18).bearing(0).tilt(45).build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));




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
}