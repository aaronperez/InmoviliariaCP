package com.aaron.contentprovinmo;

/**
 * Created by Aaron on 27/01/2015.
 */
import android.net.Uri;
import android.provider.BaseColumns;

public class Contrato {

    private Contrato (){
    }

    public static abstract class TablaInmueble implements BaseColumns{
        public static final String TABLA = "inmueble";
        public static final String TIPO ="tipo";
        public static final String LOCALIDAD = "localidad";
        public static final String DIRECCION = "direccion";
        public static final String PRECIO="precio";
        public static final String USUARIO="usuario";
        public static final String SUBIDO="subido";
        public static final String CONTENT_TYPE_INMUEBLES = "vdn.android.cursor.dir/vnd.izv.inmuebles";
        public static final String CONTENT_TYPE_INMUEBLE = "vdn.android.cursor.item/vnd.izv.inmueble";
        public static final Uri CONTENT_URI = Uri.parse("content://"+ProveedorInmueble.AUTORIDAD+"/" +TABLA);
    }
}
