package com.aaron.contentprovinmo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.io.IOException;


public class Main extends ActionBarActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    private ListView lv;
    private Adaptador ad;
    private int index;
    private final int ACTIVIDAD_ELIMINAR = 1;
    public View row;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        try {
            initComponent();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.agregar) {
            edicion(index, "add");
            return true;
        }
        if (id == R.id.usuario) {
            AlertDialog.Builder alert= new AlertDialog.Builder(this);
            alert.setTitle(R.string.usuarioCambiar);
            LayoutInflater inflater= LayoutInflater.from(this);
            final View vista = inflater.inflate(R.layout.usuario, null);
            alert.setView(vista);
            alert.setPositiveButton(R.string.editar, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    EditText et = (EditText) vista.findViewById(R.id.etUsuario);
                    if(et.getText().toString().isEmpty()){
                        tostada(R.string.mensajeCancelar);
                    }else{
                        editor = prefs.edit();
                        editor.putString("usuario", et.getText().toString());
                        editor.commit();
                        tostada(R.string.usuarioCambiado);
                    }
                }
            });
            alert.setNegativeButton(R.string.cancelar,new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    tostada(R.string.mensajeCancelar);
                    dialog.dismiss();
                }
            });
            alert.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* Desplegar menú contextual*/
    @Override
    public void onCreateContextMenu(ContextMenu main, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(main, v, menuInfo);
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.contextual, main);
    }

    /* Al seleccionar elemento del menú contextual */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id=item.getItemId();
        AdapterView.AdapterContextMenuInfo info= (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int index= info.position;
        Cursor cursor = (Cursor)lv.getItemAtPosition(index);
        int idInmueble= cursor.getInt(0);
        if (id == R.id.action_editar) {
            edicion(idInmueble, "edit");
            return true;
        }else if (id == R.id.action_eliminar) {
            edicion(idInmueble, "delete");
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void initComponent()throws IOException {
        // Iniciamos las preferencias compartidas
        prefs=this.getSharedPreferences("usuario", 0);
        ad = new Adaptador(this, null);
        lv = (ListView) findViewById(R.id.lvLista);
        lv.setAdapter(ad);
        registerForContextMenu(lv);
        getLoaderManager().initLoader(0, null, this);
        final Detalle fdetalle=(Detalle)getFragmentManager().findFragmentById(R.id.fDetalle);
        final boolean horizontal=fdetalle!=null && fdetalle.isInLayout();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicion, long id) {
                if(horizontal){
                    if (row != null) {
                        row.setBackgroundResource(R.color.blanco);
                    }
                    row = view;
                    view.setBackgroundResource(R.color.secundario);
                    fdetalle.inicia(id+"");
                }
                else{
                    Intent intent=new Intent(Main.this, Actividad.class);
                    intent.putExtra("id", id+"");
                    startActivity(intent);
                }
            }
        });

    }

    /***********Activities************/
    @Override
    public void onActivityResult(int requestCode,int resultCode, Intent data){
        if (resultCode== Activity.RESULT_OK) {
            switch (requestCode){
                case ACTIVIDAD_ELIMINAR:
                    index= data.getIntExtra("index",0);
                    Uri uri = Contrato.TablaInmueble.CONTENT_URI;
                    String where = Contrato.TablaInmueble._ID +" = ? ";
                    String args[] = new String[]{index+""};
                    getContentResolver().delete(uri,where,args);
                    break;
            }
        }
        else{
            tostada(R.string.mensajeCancelar);
        }
    }

    /* Mostramos un mensaje flotante a partir de un recurso string*/
    public void tostada(int s){
        Toast.makeText(this, getText(s), Toast.LENGTH_SHORT).show();
    }

    /*        Menús          */
    /*************************/
    public void edicion(int index,String opcion) {
        Intent i;
        Bundle b = new Bundle();
        b.putString("opcion", opcion);
        b.putInt("index", index);
        if (opcion.equals("delete")) {
            i = new Intent(this, Eliminar.class);
            i.putExtras(b);
            startActivityForResult(i, ACTIVIDAD_ELIMINAR);
        } else {
            i = new Intent(this, Edicion.class);
            i.putExtras(b);
            startActivity(i, b);
        }
    }


    /*  Clases para el Callback del CP */
    /* ******************************* */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //cargar el cursor del content provider
        Uri uri = Contrato.TablaInmueble.CONTENT_URI;
        return new CursorLoader( this, uri, null, null, null, Contrato.TablaInmueble.LOCALIDAD +" collate localized asc");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Necesitamos un adaptador
        ad.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        ad.swapCursor(null);
    }

}
