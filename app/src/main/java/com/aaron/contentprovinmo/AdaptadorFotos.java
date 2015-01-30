package com.aaron.contentprovinmo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.ArrayList;

/**
 * Created by Aaron on 04/12/2014.
 */
public class AdaptadorFotos extends ArrayAdapter<File> {
    private Context contexto;
    private ArrayList<File> fotos;
    private int recurso;
    private LayoutInflater i;

    public AdaptadorFotos(Context context, int resource, ArrayList<File> objects) {
        super(context, resource, objects);
        this.contexto=context;
        this.fotos=objects;
        this.recurso=resource;
        this.i= (LayoutInflater)contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class ViewHolder{
        public TextView tvIdFoto;
        public ImageView ivFoto;
        public int posicion;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        //El if entra cuando se crea el ViewHolder por primera vez
        if(convertView == null){
            convertView= i.inflate(recurso, null);
            vh = new ViewHolder();
            vh.tvIdFoto = (TextView)convertView.findViewById(R.id.tvFoto);
            vh.ivFoto = (ImageView)convertView.findViewById(R.id.ivFotos);
            convertView.setTag(vh);
        }
        else{
            vh=(ViewHolder)convertView.getTag();
        }
        if(fotos.size()==0){
            vh.tvIdFoto.setText(R.string.nofoto);
            Picasso.with(this.contexto).load(R.drawable.nofoto).resize(200,200).centerCrop().into(vh.ivFoto);
        }
        else {
            vh.tvIdFoto.setText(fotos.get(position).getName());
            Picasso.with(this.contexto).load(fotos.get(position)).resize(200,200).centerCrop().into(vh.ivFoto);
        }
        vh.posicion = position;
        return convertView;
    }

}

