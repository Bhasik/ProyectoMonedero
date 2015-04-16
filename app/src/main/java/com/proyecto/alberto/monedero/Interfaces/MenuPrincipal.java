package com.proyecto.alberto.monedero.Interfaces;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.Interfaces.Conceptos.Conceptos;
import com.proyecto.alberto.monedero.Interfaces.GastoFijo.GastosFijos;
import com.proyecto.alberto.monedero.Interfaces.Movimientos.Movimientos;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;

import java.util.ArrayList;

public class MenuPrincipal extends Fragment implements View.OnClickListener, DialogInterface.OnClickListener {

    public static String TAG_FRAGMENT = "MENU PRINCIPAL";

    private View view;
    private Activity context;

    private Button resultados;
    private Button backup;
    private Button comprar;
    private String correo;
    private AlertDialog actions;

    private FragmentTransaction ft;

    public MenuPrincipal() {

    }

    public static MenuPrincipal newInstance() {

        MenuPrincipal mp = new MenuPrincipal();

        return mp;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.menu_fragment, container, false);
        this.context = getActivity();

        resultados = (Button) view.findViewById(R.id.resultados);
        backup = (Button) view.findViewById(R.id.backup);
        comprar = (Button) view.findViewById(R.id.comprar);

        view.findViewById(R.id.movimientos).setOnClickListener(this);
        view.findViewById(R.id.resultados).setOnClickListener(this);
        view.findViewById(R.id.gastos).setOnClickListener(this);
        view.findViewById(R.id.conceptos).setOnClickListener(this);
        view.findViewById(R.id.backup).setOnClickListener(this);
        view.findViewById(R.id.comprar).setOnClickListener(this);
        view.findViewById(R.id.noti).setOnClickListener(this);
        view.findViewById(R.id.menulateral).setOnClickListener(this);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        compraEfectuada();

    }

    public void compraEfectuada() {

        ((Main_Activity) context).cargarBanner();

        if (((Main_Activity) context).isComprado()) {

            comprar.setEnabled(false);
            comprar.setVisibility(View.GONE);

        } else {

            comprar.setEnabled(true);
            comprar.setVisibility(View.VISIBLE);

        }

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.movimientos:

                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                ft.replace(R.id.container, new Movimientos(), Movimientos.TAG_FRAGMENT);
                ft.addToBackStack(MenuPrincipal.TAG_FRAGMENT);
                ft.commit();

                break;

            case R.id.resultados:

                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                ft.replace(R.id.container, new Reporte(), Reporte.TAG_FRAGMENT);
                ft.addToBackStack(MenuPrincipal.TAG_FRAGMENT);
                ft.commit();


                break;

            case R.id.gastos:

                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                ft.replace(R.id.container, new GastosFijos(), GastosFijos.TAG_FRAGMENT);
                ft.addToBackStack(MenuPrincipal.TAG_FRAGMENT);
                ft.commit();

                break;

            case R.id.conceptos:

                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                ft.replace(R.id.container, new Conceptos(), Conceptos.TAG_FRAGMENT);
                ft.addToBackStack(MenuPrincipal.TAG_FRAGMENT);
                ft.commit();

                break;

            case R.id.backup:

                if (Alertas.haveNetworkConnection(context)) {

                    new SubProcesosGestion(null, context, SubProcesosGestion.BACKUP).execute();
                    Alertas.enviarEmail(context, "backup");


                } else {

                    Alertas.errorInternet(context);
                    context.getFragmentManager().popBackStack();

                }

                break;

            case R.id.comprar:

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Elija el correo con el que desea registrar su compra");
                String[] options = getCuentasGoogle();

                builder.setItems(options, this);

                builder.setNegativeButton("Cancel", null);
                actions = builder.create();
                actions.setCancelable(false);

                actions.show();

                break;


            case R.id.menulateral:

                ((Main_Activity) context).getDrawerLayout().openDrawer(Gravity.LEFT);

                break;

        }

    }


    public String[] getCuentasGoogle() {

        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        ArrayList<String> correos = new ArrayList<>();

        String[] correosArray;

        for (Account account : accounts) {

            correos.add(account.name);

        }

        correosArray = new String[correos.size()];

        for (int i = 0; i < correosArray.length; i++) {

            correosArray[i] = correos.get(i);

        }


        return correosArray;

    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

        correo = correoSeleccionado(i);

    }

    public String correoSeleccionado(int which) {

        String[] correos = getCuentasGoogle();


        for (int i = 0; i < correos.length; i++) {

            if (i == which) {

                correo = correos[which];

                new SubProcesosGestion(this, context, SubProcesosGestion.COMPRA, correo).execute();


            }

        }


        return correo;
    }


}



