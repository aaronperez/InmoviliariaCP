package com.aaron.contentprovinmo;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Aaron on 27/01/2015.
 */
public class Adaptador extends CursorAdapter {

    public class ViewHolder{
        public TextView tvLocalidad,tvDireccion,tvPrecio;
        public ImageView ivTipo;
    }

    public Adaptador(Context context, Cursor c) {
        super(context, c, true);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater i = LayoutInflater.from(parent.getContext());
        View v = i.inflate(R.layout.elementoinmobiliaria, parent, false);
        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vh = new ViewHolder();

        vh.tvLocalidad=(TextView)view.findViewById(R.id.tvLocalidadE);
        vh.tvDireccion=(TextView)view.findViewById(R.id.tvCalle);
        vh.tvPrecio=(TextView)view.findViewById(R.id.tvPrecioE);
        vh.ivTipo=(ImageView)view.findViewById(R.id.ivIcono);

        String tipo = cursor.getString(1);
        String localidad = cursor.getString(2);
        String direccion = cursor.getString(3);
        String precio = cursor.getDouble(4)+"";

        vh.tvLocalidad.setText(localidad);
        vh.tvDireccion.setText(direccion);
        vh.tvPrecio.setText(precio);

        Context n = context.getApplicationContext();
        Resources resources = n.getResources();
        int resourceId = resources.getIdentifier(tipo, "drawable",n.getPackageName());
        vh.ivTipo.setImageResource(resourceId);
    }
}
