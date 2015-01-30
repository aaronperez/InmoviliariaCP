package com.aaron.contentprovinmo;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.aaron.contentprovinmo.Ayudante;

/**
 * Created by Aaron on 27/01/2015.
 */
public class ProveedorInmueble extends ContentProvider {

    public static String AUTORIDAD = "com.aaron.contentprovinmo.proveedorinmueble";
    private Ayudante adb;
    private static final UriMatcher convierteUri2Int;
    private static final int INMUEBLES = 1;
    private static final int INMUEBLE_ID = 2;

    static{
        convierteUri2Int = new UriMatcher(UriMatcher.NO_MATCH);
        convierteUri2Int.addURI(AUTORIDAD,Contrato.TablaInmueble.TABLA, INMUEBLES);
    }

    public ProveedorInmueble() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = adb.getWritableDatabase();
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                break;
            case INMUEBLE_ID:
                selection = selection + "_id = " + uri.getLastPathSegment();
                selectionArgs = new String[]{uri.getLastPathSegment()};
                break;
            default: throw new IllegalArgumentException("URI " + uri);
        }
        int cuenta = db.delete(Contrato.TablaInmueble.TABLA, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cuenta;
    }

    @Override
    public String getType(Uri uri) {
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                return Contrato.TablaInmueble.CONTENT_TYPE_INMUEBLES;
            case INMUEBLE_ID:
                return Contrato.TablaInmueble.CONTENT_TYPE_INMUEBLE;
            default:
                return null;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (convierteUri2Int.match(uri) != INMUEBLES) {
            throw new IllegalArgumentException("URI " + uri);
        }
        SQLiteDatabase bd = adb.getWritableDatabase();
        long id = bd.insert(Contrato.TablaInmueble.TABLA, null, values);
        if (id > 0) {
            Uri uriElemento = ContentUris.withAppendedId(Contrato.TablaInmueble.CONTENT_URI, id);
            getContext().getContentResolver().notifyChange(uriElemento, null);
            return uriElemento;
        }
        return null;
    }

    @Override
    public boolean onCreate() {
        adb=new Ayudante(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,String[] selectionArgs, String sortOrder) {
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLES:
                break;
            case INMUEBLE_ID:
                selection = Contrato.TablaInmueble._ID + " = ? ";
                selectionArgs=new String[] {uri.getLastPathSegment()};
                break;
            default:
                throw new IllegalArgumentException("URI " + uri);
        }
        SQLiteDatabase bd = adb.getReadableDatabase();
        Cursor cursor = bd.query(Contrato.TablaInmueble.TABLA,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues valores, String condicion, String[] parametros) {
        SQLiteDatabase db = adb.getWritableDatabase();
        int cuenta;
        switch (convierteUri2Int.match(uri)) {
            case INMUEBLE_ID:
                condicion= Contrato.TablaInmueble.TABLA+ " = ? ";
                parametros = new String[]{uri.getLastPathSegment()};
                break;
            case INMUEBLES:
                break;
            default: throw new IllegalArgumentException("URI " + uri);
        }
        cuenta = db.update(Contrato.TablaInmueble.TABLA, valores, condicion,parametros);
        getContext().getContentResolver().notifyChange(uri, null);
        return cuenta;
    }
}
