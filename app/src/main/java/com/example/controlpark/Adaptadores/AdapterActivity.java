package com.example.controlpark.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.controlpark.Model.Estacionamiento;
import com.example.controlpark.R;

import java.util.ArrayList;

public class AdapterActivity extends RecyclerView.Adapter<AdapterActivity.ViewHolder> implements View.OnClickListener {
    LayoutInflater inflater;
    ArrayList<Estacionamiento> model;

    //Listener
    private  View.OnClickListener listener;


    public AdapterActivity(Context context, ArrayList<Estacionamiento> model){
        this.inflater = LayoutInflater.from(context);
        this.model = model;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lista_estacionamientos, parent, false);
        view.setOnClickListener(this);
        return new ViewHolder(view);
    }

    public void  setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        String nombre = model.get(position).getNombre();
        String direccion = model.get(position).getDirección();
        String descripcion = model.get(position).getDescripcion();
        double latitud = model.get(position).getLatitud();
        double longitud = model.get(position).getLongitud();
        int espacios = model.get(position).getEspacios();
        double precio = model.get(position).getPrecio();

        holder.nombre.setText(nombre);
        /*holder.direccion.setText(direccion);*/
        holder.descripcion.setText(descripcion);
        /*holder.latitud.setText(String.valueOf(latitud));
        holder.longitud.setText(String.valueOf(longitud));*/
        holder.espacios.setText(String.valueOf(espacios));
        holder.precio.setText(String.valueOf(precio));

    }

    @Override
    public int getItemCount() {
        return model.size();
    }

    @Override
    public void onClick(View v) {

        if(listener != null){
            listener.onClick(v);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView nombre,  direccion, descripcion, latitud, longitud,espacios,precio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.txtnombreEstacionamiento);
            /*direccion = itemView.findViewById(R.id.txtDireccion);*/
            descripcion = itemView.findViewById(R.id.txtDescripcion);
            /*latitud = itemView.findViewById(R.id.txtlatitud);
            longitud = itemView.findViewById(R.id.txtLongitud);*/
            espacios = itemView.findViewById(R.id.txtEspacios);
            precio = itemView.findViewById(R.id.txtPrecio);


        }
    }

}
