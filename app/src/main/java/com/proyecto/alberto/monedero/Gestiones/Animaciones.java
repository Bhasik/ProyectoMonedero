package com.proyecto.alberto.monedero.Gestiones;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageButton;

import com.proyecto.alberto.monedero.R;

public class Animaciones {

    public static void lupa(FragmentActivity context, Fragment fragment, View view) {

        Animation giro;
        Animation desvanecer;
        Animation aparecer;

        giro = AnimationUtils.loadAnimation(context, R.anim.giro_derecha_desvanecer);
        aparecer = AnimationUtils.loadAnimation(context, R.anim.anim_aparecer);
        desvanecer = AnimationUtils.loadAnimation(context, R.anim.desvancer);

        ImageButton lupa = (ImageButton) view.findViewById(R.id.lupa);
        ImageButton flecha_izq = (ImageButton) view.findViewById(R.id.flecha_izq);
        EditText caja_busqueda = (EditText) view.findViewById(R.id.caja_busqueda);
        ImageButton borrar_caja = (ImageButton) view.findViewById(R.id.borraredit_icono);

        switch (fragment.getClass().getSimpleName()) {

            case "Conceptos":
            case "GastosFijos":


                lupa.startAnimation(giro);
                flecha_izq.startAnimation(aparecer);
                caja_busqueda.startAnimation(aparecer);
                borrar_caja.startAnimation(aparecer);

                view.findViewById(R.id.lupa).setVisibility(View.GONE);
                caja_busqueda.setVisibility(View.VISIBLE);
                flecha_izq.setVisibility(View.VISIBLE);
                borrar_caja.setVisibility(View.VISIBLE);

                break;


            case "Movimientos":

                ImageButton reloj = (ImageButton) view.findViewById(R.id.reloj);

                reloj.startAnimation(desvanecer);
                lupa.startAnimation(giro);
                flecha_izq.startAnimation(aparecer);
                caja_busqueda.startAnimation(aparecer);
                borrar_caja.startAnimation(aparecer);

                reloj.setVisibility(View.GONE);
                lupa.setVisibility(View.GONE);
                caja_busqueda.setVisibility(View.VISIBLE);
                flecha_izq.setVisibility(View.VISIBLE);
                borrar_caja.setVisibility(View.VISIBLE);

                break;

        }

    }

    public static void flecha_izq(FragmentActivity context, Fragment fragment, View view) {

        Animation giro;
        Animation desvanecer;
        Animation aparecer;

        ImageButton lupa = (ImageButton) view.findViewById(R.id.lupa);
        ImageButton flecha_izq = (ImageButton) view.findViewById(R.id.flecha_izq);
        EditText caja_busqueda = (EditText) view.findViewById(R.id.caja_busqueda);
        ImageButton borrar_caja = (ImageButton) view.findViewById(R.id.borraredit_icono);

        giro = AnimationUtils.loadAnimation(context, R.anim.giro_izquierda_desvanecer);
        aparecer = AnimationUtils.loadAnimation(context, R.anim.anim_aparecer);
        desvanecer = AnimationUtils.loadAnimation(context, R.anim.desvancer);

        switch (fragment.getClass().getSimpleName()) {

            case "Conceptos":
            case "GastosFijos":


                lupa.startAnimation(aparecer);
                flecha_izq.startAnimation(giro);
                caja_busqueda.startAnimation(desvanecer);
                borrar_caja.startAnimation(desvanecer);

                lupa.startAnimation(aparecer);
                flecha_izq.startAnimation(giro);
                caja_busqueda.startAnimation(desvanecer);
                borrar_caja.startAnimation(desvanecer);

                lupa.setVisibility(View.VISIBLE);
                caja_busqueda.setVisibility(View.GONE);
                flecha_izq.setVisibility(View.GONE);
                borrar_caja.setVisibility(View.GONE);

                break;


            case "Movimientos":

                ImageButton reloj = (ImageButton) view.findViewById(R.id.reloj);

                reloj.startAnimation(aparecer);
                lupa.startAnimation(aparecer);
                flecha_izq.startAnimation(giro);
                caja_busqueda.startAnimation(desvanecer);
                borrar_caja.startAnimation(desvanecer);

                reloj.setVisibility(View.VISIBLE);
                lupa.setVisibility(View.VISIBLE);
                caja_busqueda.setVisibility(View.GONE);
                flecha_izq.setVisibility(View.GONE);
                borrar_caja.setVisibility(View.GONE);

                break;

        }

    }
}
