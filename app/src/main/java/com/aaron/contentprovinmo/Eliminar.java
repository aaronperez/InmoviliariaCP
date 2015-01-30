package com.aaron.contentprovinmo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


public class Eliminar extends Activity {

    private TextView tvLocalidad,tvDireccion,tvPrecio;
    private ImageView ivTipo;
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar);
        initComponent();
    }

    public void initComponent(){
        tvLocalidad=(TextView)findViewById(R.id.tvLocalidadE);
        tvDireccion=(TextView)findViewById(R.id.tvDireccionE);
        tvPrecio=(TextView)findViewById(R.id.tvPrecioE);
        ivTipo=(ImageView)findViewById(R.id.ivTipo);
        Bundle b = getIntent().getExtras();
        index=b.getInt("index");
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        String where = Contrato.TablaInmueble._ID +" = ? ";
        String args[] = new String[]{index+""};
        Cursor cursor=getContentResolver().query(uri,null, where,args, null);
        cursor.moveToFirst();
        tvLocalidad.setText(cursor.getString(2));
        tvDireccion.setText(cursor.getString(3));
        tvPrecio.setText(cursor.getString(4));
        ivTipo.setImageResource(Integer.parseInt(cursor.getString(1)));
    }


    public void aceptar(View v){
        Intent i = new Intent();
        Bundle bundle= new Bundle();
        bundle.putInt("index", index);
        i.putExtras(bundle);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }
}
