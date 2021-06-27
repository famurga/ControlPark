package com.example.controlpark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class MainActivity extends AppCompatActivity {
    final  private  int REQUEST_CODE_PERMISION=111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        solicitarpermisos();
    }

    public void solicitarpermisos(){

        int permisoUbicacion= ActivityCompat.checkSelfPermission(MainActivity.this, ACCESS_FINE_LOCATION);
        int permisoUbicacion2= ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION);

        if(permisoUbicacion != PackageManager.PERMISSION_GRANTED || permisoUbicacion2 != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION},REQUEST_CODE_PERMISION);



            }
        }



    }
}