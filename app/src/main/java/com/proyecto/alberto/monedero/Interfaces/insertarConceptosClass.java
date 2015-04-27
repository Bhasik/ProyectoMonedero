package com.proyecto.alberto.monedero.Interfaces;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.R;

/**
 * Created by Alberto on 17/04/2015.
 */
public class insertarConceptosClass extends Fragment implements View.OnClickListener{

    public static String TAG_FRAGMENT = "CONCEPTOS AÑADIR";

    private View view;
    private FragmentActivity context;

    private EditText nombre;
    private EditText precio;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       super.onCreateView(inflater, container, savedInstanceState);


        this.view = inflater.inflate(R.layout.insertarconceptos, container, false);
        this.context = getActivity();

        this.nombre = (EditText) view.findViewById(R.id.nombre);
        this.precio = (EditText) view.findViewById(R.id.precio);

        view.findViewById(R.id.btn_anyadir).setOnClickListener(this);


        return view;

    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.btn_anyadir:

                new SubProcesosGestion(this, context, SubProcesosGestion.CONCEPTOS_BAJAR,nombre.getText().toString()).execute();

                break;



        }

    }
}
