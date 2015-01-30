package com.aaron.contentprovinmo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class Edicion extends Activity {

    private Spinner spinner;
    private int index;
    private EditText etLocal,etCalle,etPrecio;
    private String opcion, identificador;
    private TextView titulo;
    private Button bOK;
    private SharedPreferences prefs;
    private String usuario;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edicion);
        initComponent();
    }

    public void initComponent(){
        prefs =getSharedPreferences("MisPreferencias",Context.MODE_PRIVATE);
        etLocal=(EditText)findViewById(R.id.etLocalidad);
        etCalle=(EditText)findViewById(R.id.etDireccion);
        etPrecio=(EditText)findViewById(R.id.etPrecio);
        titulo=(TextView)findViewById(R.id.tvTitulo);
        bOK=(Button)findViewById(R.id.bOK);
        iniciarSpinner();
        Bundle b = getIntent().getExtras();
        opcion=b.getString("opcion");
        index=b.getInt("index");
        if(b !=null){
            if(opcion.contains("edit")){
                Uri uri = Contrato.TablaInmueble.CONTENT_URI;
                String where = Contrato.TablaInmueble._ID +" = ? ";
                String args[] = new String[]{index+""};
                Cursor cursor=getContentResolver().query(uri,null, where,args, null);
                cursor.moveToFirst();
                titulo.setText(getText(R.string.editari));
                bOK.setText(getText(R.string.editar));
                etLocal.setText(cursor.getString(2));
                etCalle.setText(cursor.getString(3));
                etPrecio.setText(cursor.getString(4));
                int tipo=Integer.parseInt(cursor.getString(1));
                for (int i = 0; i < 4; i++) {
                    if (spinner.getItemAtPosition(i).toString().substring(1, 3).equals(this.getResources().getResourceEntryName(tipo).substring(1, 3))) {
                        spinner.setSelection(i);
                        break;
                    }
                }
            }
            else{
                titulo.setText(getText(R.string.agregari));
                bOK.setText(getText(R.string.agregar));
                etLocal.setText("");
                etCalle.setText("");
                etPrecio.setText("");
            }
        }
    }

    public void aceptar(View v){
        Uri uri= Contrato.TablaInmueble.CONTENT_URI;
        ContentValues values = new ContentValues();
        String where = Contrato.TablaInmueble._ID +" = ? ";
        usuario=prefs.getString("usario", "Aaron");
        values.put(Contrato.TablaInmueble.TIPO,spinnerDraw()+"");
        values.put(Contrato.TablaInmueble.LOCALIDAD,etLocal.getText().toString());
        values.put(Contrato.TablaInmueble.DIRECCION,etCalle.getText().toString());
        values.put(Contrato.TablaInmueble.PRECIO, etPrecio.getText().toString());
        values.put(Contrato.TablaInmueble.USUARIO, usuario);
        values.put(Contrato.TablaInmueble.SUBIDO, 0);
        if(opcion.contains("edit")){
            String args[] = new String[]{index+""};
            getContentResolver().update(uri, values, where, args);
        }else{
            getContentResolver().insert(uri,values);
        }
        finish();
    }

    public void cancelar(View v){
        setResult(Activity.RESULT_CANCELED);
        finish();
    }

    /*  Método para iniciar Spinner y escucharlo  */
    private void iniciarSpinner(){
        ArrayAdapter<CharSequence> stringArrayAdapter=ArrayAdapter.createFromResource(this,
                R.array.Tipo,android.R.layout.simple_spinner_dropdown_item);
        spinner =(Spinner)findViewById(R.id.spinnerM);
        spinner.setAdapter(stringArrayAdapter);
    }


    /* Método que convierte la posición en
    * el spinner en la imagen que le corresponde*/
    private int spinnerDraw(){
        Context n=Edicion.this.getApplicationContext();
        Resources resources = n.getResources();
        String nombre=spinner.getSelectedItem().toString().toLowerCase();
        final int resourceId = resources.getIdentifier(nombre, "drawable",n.getPackageName());
        return resourceId;
    }


}