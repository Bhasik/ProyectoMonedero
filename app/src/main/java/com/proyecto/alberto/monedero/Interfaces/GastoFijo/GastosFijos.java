package com.proyecto.alberto.monedero.Interfaces.GastoFijo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.Animaciones;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Swipe.BaseSwipeListViewListener;
import com.proyecto.alberto.monedero.Swipe.SwipeListView;
import com.proyecto.alberto.monedero.Tablas.Movimiento;

import java.util.ArrayList;

/**
 * Fragment donde se muestra una lista de todos los gastos fijos
 */
public class GastosFijos extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "GASTOS FIJOS";

    private View view;
    private FragmentActivity context;

    private SwipeListView list;
    private ArrayList<Movimiento> gastosfijos_list;
    private GastosFijos_Adapter gastosfijos_adapter;


    private EditText caja_busqueda;
    private ImageButton lupa;
    private ImageButton flecha_izq;
    private ImageButton papelera2;
    private ImageButton reloj;
    private ImageButton limpiar_edit;

    private Boolean nombre = false;
    private Boolean importe = false;
    private Boolean dias = false;
    private Boolean impdias = false;

    private ImageButton flecha_arriba_nombre;
    private ImageButton flecha_abajo_nombre;
    private ImageButton flecha_arriba_import;
    private ImageButton flecha_abajo_import;
    private ImageButton flecha_arriba_dias;
    private ImageButton flecha_abajo_dias;
    private ImageButton flecha_arriba_importdia;
    private ImageButton flecha_abajo_importdia;

    private FragmentTransaction ft;

    public GastosFijos() {

    }

    public ArrayList getGastosfijos_list() {
        return gastosfijos_list;
    }

    public SwipeListView getList() {
        return list;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.gastosfijos_fragment, container, false);
        this.context = getActivity();

        list = (SwipeListView) view.findViewById(R.id.lista_gastofijo);

        caja_busqueda = (EditText) view.findViewById(R.id.caja_busqueda);

        flecha_izq = (ImageButton) view.findViewById(R.id.flecha_izq);
        limpiar_edit = (ImageButton) view.findViewById(R.id.borraredit_icono);
        lupa = (ImageButton) view.findViewById(R.id.lupa);
        flecha_abajo_nombre = (ImageButton) view.findViewById(R.id.conceptodesc);
        flecha_arriba_nombre = (ImageButton) view.findViewById(R.id.conceptoasc);
        flecha_abajo_import = (ImageButton) view.findViewById(R.id.importedesc);
        flecha_arriba_import = (ImageButton) view.findViewById(R.id.importeasc);
        flecha_abajo_dias = (ImageButton) view.findViewById(R.id.diasdesc);
        flecha_arriba_dias = (ImageButton) view.findViewById(R.id.diasasc);
        flecha_abajo_importdia = (ImageButton) view.findViewById(R.id.impdiadesc);
        flecha_arriba_importdia = (ImageButton) view.findViewById(R.id.impdiaasc);

        view.findViewById(R.id.insertar).setOnClickListener(this);
        view.findViewById(R.id.nombre_textview).setOnClickListener(this);
        view.findViewById(R.id.importe_textview).setOnClickListener(this);
        view.findViewById(R.id.dias_textview).setOnClickListener(this);
        view.findViewById(R.id.importedia_textview).setOnClickListener(this);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        flecha_izq.setOnClickListener(this);
        caja_busqueda.setOnClickListener(this);
        lupa.setOnClickListener(this);
        limpiar_edit.setOnClickListener(this);

        gastosfijos_list = new ArrayList();

        caja_busqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

                gastosfijos_adapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

            }
        });

        new SubProcesosGestion(this, context, SubProcesosGestion.MOVIMIENTOS_BAJAR).execute();

    }

    public void configurarAdapter() {

        gastosfijos_adapter = new GastosFijos_Adapter(context, this);

        list.setAdapter(gastosfijos_adapter);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return true;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                list.unselectedChoiceStates();
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }
        });


        list.setSwipeListViewListener(new BaseSwipeListViewListener() {

            @Override
            public void onClickFrontView(int position) {
                lanzarGastosFijos_Detalle(gastosfijos_adapter.getMovimiento(position));
            }


        });

    }

    public void lanzarGastosFijos_Detalle(Movimiento mov) {

        if (!Alertas.haveNetworkConnection(context)) {

            Alertas.errorInternet(context);

        } else if (mov != null) {

            Bundle arguments = new Bundle();

            arguments.putInt("id", mov.getId());
            arguments.putInt("dias", mov.getConcepto().getDias());
            arguments.putInt("id_concepto", mov.getConcepto().getId());
            arguments.putString("concepto_gastofijo", mov.getConcepto().getNombre());
            arguments.putDouble("movimiento_gastofijo", mov.getMovimiento());
            arguments.putString("fecha", mov.getFecha());


            ft = getFragmentManager().beginTransaction();

            ft.replace(R.id.container, new GastosFijos_Detalle().newInstance(arguments), GastosFijos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(GastosFijos.TAG_FRAGMENT);
            ft.commit();

        } else {

            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new GastosFijos_Detalle(), GastosFijos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(GastosFijos.TAG_FRAGMENT);
            ft.commit();

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.insertar:

                lanzarGastosFijos_Detalle(null);

                break;


            case R.id.lupa:

                Animaciones.lupa(context, this, view);

                break;

            case R.id.flecha_izq:

                Animaciones.flecha_izq(context, this, view);

                break;

            case R.id.nombre_textview:

                if (gastosfijos_adapter != null) {

                    if (!nombre) {

                        gastosfijos_adapter.ordenarNombre();
                        flecha_abajo_nombre.setVisibility(View.VISIBLE);
                        flecha_arriba_nombre.setVisibility(View.GONE);

                        nombre = true;

                    } else {

                        gastosfijos_adapter.ordenarNombre();
                        flecha_arriba_nombre.setVisibility(View.VISIBLE);
                        flecha_abajo_nombre.setVisibility(View.GONE);

                        nombre = false;


                    }

                    flecha_abajo_import.setVisibility(View.GONE);
                    flecha_arriba_import.setVisibility(View.GONE);

                    flecha_arriba_dias.setVisibility(View.GONE);
                    flecha_abajo_dias.setVisibility(View.GONE);

                    flecha_abajo_importdia.setVisibility(View.GONE);
                    flecha_arriba_importdia.setVisibility(View.GONE);
                }
                break;

            case R.id.importe_textview:

                if (gastosfijos_adapter != null) {

                    if (!importe) {

                        gastosfijos_adapter.ordenarImporte();
                        flecha_abajo_import.setVisibility(View.VISIBLE);
                        flecha_arriba_import.setVisibility(View.GONE);

                        importe = true;

                    } else {

                        gastosfijos_adapter.ordenarImporte();
                        flecha_arriba_import.setVisibility(View.VISIBLE);
                        flecha_abajo_import.setVisibility(View.GONE);

                        importe = false;
                    }

                    flecha_arriba_dias.setVisibility(View.GONE);
                    flecha_abajo_dias.setVisibility(View.GONE);

                    flecha_abajo_importdia.setVisibility(View.GONE);
                    flecha_arriba_importdia.setVisibility(View.GONE);

                    flecha_abajo_nombre.setVisibility(View.GONE);
                    flecha_arriba_nombre.setVisibility(View.GONE);
                }

                break;

            case R.id.dias_textview:

                if (gastosfijos_adapter != null) {

                    if (!dias) {

                        gastosfijos_adapter.ordenarDias();
                        flecha_abajo_dias.setVisibility(View.VISIBLE);
                        flecha_arriba_dias.setVisibility(View.GONE);

                        dias = true;

                    } else {

                        gastosfijos_adapter.ordenarDias();
                        flecha_arriba_dias.setVisibility(View.VISIBLE);
                        flecha_abajo_dias.setVisibility(View.GONE);

                        dias = false;
                    }

                    flecha_abajo_import.setVisibility(View.GONE);
                    flecha_arriba_import.setVisibility(View.GONE);

                    flecha_abajo_nombre.setVisibility(View.GONE);
                    flecha_arriba_nombre.setVisibility(View.GONE);

                    flecha_abajo_importdia.setVisibility(View.GONE);
                    flecha_arriba_importdia.setVisibility(View.GONE);
                }
                break;

            case R.id.importedia_textview:

                if (gastosfijos_adapter != null) {

                    if (!impdias) {

                        gastosfijos_adapter.ordenarImporteDia();
                        flecha_abajo_importdia.setVisibility(View.VISIBLE);
                        flecha_arriba_importdia.setVisibility(View.GONE);

                        impdias = true;

                    } else {

                        gastosfijos_adapter.ordenarImporteDia();
                        flecha_arriba_importdia.setVisibility(View.VISIBLE);
                        flecha_abajo_importdia.setVisibility(View.GONE);

                        impdias = false;

                    }

                    flecha_abajo_nombre.setVisibility(View.GONE);
                    flecha_arriba_nombre.setVisibility(View.GONE);

                    flecha_abajo_import.setVisibility(View.GONE);
                    flecha_arriba_import.setVisibility(View.GONE);

                    flecha_arriba_dias.setVisibility(View.GONE);
                    flecha_abajo_dias.setVisibility(View.GONE);
                }

                break;

            case R.id.borraredit_icono:

                limpiar_edit.setImageResource(R.drawable.boton_borrar_caja);
                caja_busqueda.setText("");

                break;
        }
    }

}
