package com.proyecto.alberto.monedero;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by user on 15/04/2015.
 */
public class Opciones extends Fragment implements  View.OnClickListener{

    public static String TAG_FRAGMENT = "OPCIONES";

    private View view;
    private Activity context;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.opciones_fragment, container, false);
        this.context = getActivity();

        view.findViewById(R.id.notificacion).setOnClickListener(this);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){

            case R.id.notificacion:

                ((Main_Activity) context).alertaExterna();

                break;

        }

    }
}
