package com.aaron.contentprovinmo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Display;
import android.view.Surface;


public class Actividad extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        if(display.getRotation()== Surface.ROTATION_0){
            setContentView(R.layout.activity_actividad);
            String s=getIntent().getExtras().getString("id");
            final Detalle fdetalle=(Detalle)getFragmentManager().findFragmentById(R.id.fDetalle);
            fdetalle.inicia(s);
        }else{
            finish();
        }
    }
}
