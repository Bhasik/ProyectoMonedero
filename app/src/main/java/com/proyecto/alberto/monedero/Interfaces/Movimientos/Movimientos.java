package com.proyecto.alberto.monedero.Interfaces.Movimientos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

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
 * Fragment donde se muestra una lista de todos los movimientos
 */
public class Movimientos extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "MOVIMIENTOS";

    private View view;
    private Activity context;
    private Fragment fragment;

    private SwipeListView list;
    private ArrayList<Movimiento> movimientos_list;
    private Movimientos_Adapter movimientos_adapter;
    private PopupMenu popup;

    private Boolean ordenNombre = false;
    private Boolean ordenImporte = false;
    private Boolean ordenFecha = false;

    private EditText caja_busqueda;
    private ImageButton lupa;
    private ImageButton flecha_izq;
    private ImageButton papelera2;
    private ImageButton reloj;
    private ImageButton limpiar_edit;

    private ImageButton flechanomasc;
    private ImageButton flechanomdesc;
    private ImageButton flechaimpasc;
    private ImageButton flechaimpdesc;
    private ImageButton flechafechaasc;
    private ImageButton flechafechadesc;

    private FragmentTransaction ft;

    public Movimientos() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.movimientos_fragment, container, false);
        this.context = getActivity();
        this.fragment = this;

        //Inicialización
        list = (SwipeListView) view.findViewById(R.id.lista_movimientos);
        lupa = (ImageButton) view.findViewById(R.id.lupa);
        flecha_izq = (ImageButton) view.findViewById(R.id.flecha_izq);
        reloj = (ImageButton) view.findViewById(R.id.reloj);
        papelera2 = (ImageButton) view.findViewById(R.id.papelera2);
        caja_busqueda = (EditText) view.findViewById(R.id.caja_busqueda);
        limpiar_edit = (ImageButton) view.findViewById(R.id.borraredit_icono);

        flechanomasc = (ImageButton) view.findViewById(R.id.nomasc);
        flechanomdesc = (ImageButton) view.findViewById(R.id.nomdesc);
        flechaimpasc = (ImageButton) view.findViewById(R.id.impasc);
        flechaimpdesc = (ImageButton) view.findViewById(R.id.impdesc);
        flechafechaasc = (ImageButton) view.findViewById(R.id.fechaasc);
        flechafechadesc = (ImageButton) view.findViewById(R.id.fechadesc);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        popup = new PopupMenu(context, reloj);

        movimientos_list = new ArrayList();

        //Listener
        view.findViewById(R.id.insertar).setOnClickListener(this);
        view.findViewById(R.id.flecha_izq).setOnClickListener(this);
        view.findViewById(R.id.caja_busqueda).setOnClickListener(this);
        view.findViewById(R.id.lupa).setOnClickListener(this);
        view.findViewById(R.id.reloj).setOnClickListener(this);
        view.findViewById(R.id.papelera2).setOnClickListener(this);
        view.findViewById(R.id.borraredit_icono).setOnClickListener(this);
        view.findViewById(R.id.nombre).setOnClickListener(this);
        view.findViewById(R.id.importe).setOnClickListener(this);
        view.findViewById(R.id.fecha).setOnClickListener(this);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Listener de filtro en EditText
        caja_busqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

                try {

                    movimientos_adapter.getFilter().filter(arg0);

                } catch (NullPointerException e) {

                    e.printStackTrace();

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.menu_filtro_tiempo, popup.getMenu());

        new SubProcesosGestion(this, context, SubProcesosGestion.MOVIMIENTOS_BAJAR).execute();

    }

    public ArrayList getMovimientos_list() {

        return movimientos_list;

    }

    public void lanzarMovimientos_Detalle(Movimiento mov) {

        if (!Alertas.haveNetworkConnection(context)) {

            Alertas.errorInternet(context);

        } else if (mov != null) {

            Bundle arguments = new Bundle();

            arguments.putInt("id_mov", mov.getId());
            arguments.putInt("id_concepto", mov.getConcepto().getId());
            arguments.putString("concepto_mov", mov.getConcepto().getNombre());
            arguments.putDouble("movimiento_mov", mov.getMovimiento());
            arguments.putString("fecha_mov", mov.getFecha());
            arguments.putString("hora_mov", mov.getHora());
            arguments.putDouble("km_mov", mov.getKm());


            ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
            ft.replace(R.id.container, new Movimientos_Detalle().newInstance(arguments), Movimientos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(Movimientos.TAG_FRAGMENT);
            ft.commit();

        } else {

            ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
            ft.replace(R.id.container, new Movimientos_Detalle(), Movimientos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(Movimientos.TAG_FRAGMENT);
            ft.commit();

        }

    }

    public void configurarAdapter() {

        movimientos_adapter = new Movimientos_Adapter(context, this);

        list.setAdapter(movimientos_adapter);
        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
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
                lanzarMovimientos_Detalle(movimientos_adapter.getMovimiento(position));
            }

        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.insertar:

                lanzarMovimientos_Detalle(null);

                break;

            case R.id.lupa:

                Animaciones.lupa(context, this, view);

                break;

            case R.id.papelera2:

                break;


            case R.id.reloj:

                Animation giro = AnimationUtils.loadAnimation(context, R.anim.giro_derecha);
                reloj.startAnimation(giro);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {

                            case R.id.mensual:

                                movimientos_adapter.clear();

                                new SubProcesosGestion(fragment, context, SubProcesosGestion.FILTRAR_FECHA, 0).execute();

                                Toast.makeText(context, getString(R.string.filtrarMes), Toast.LENGTH_SHORT).show();

                                break;


                            case R.id.trimestral:

                                movimientos_adapter.clear();

                                new SubProcesosGestion(fragment, context, SubProcesosGestion.FILTRAR_FECHA, 1).execute();


                                Toast.makeText(context, getString(R.string.filtrarTrimestral), Toast.LENGTH_SHORT).show();

                                break;


                            case R.id.anual:

                                movimientos_adapter.clear();

                                new SubProcesosGestion(fragment, context, SubProcesosGestion.FILTRAR_FECHA, 2).execute();

                                Toast.makeText(context, getString(R.string.filtrarAnual), Toast.LENGTH_SHORT).show();

                                break;

                        }

                        return true;
                    }
                });

                popup.show();

                break;

            case R.id.flecha_izq:

                Animaciones.flecha_izq(context, this, view);

                break;

            case R.id.nombre:

                if (movimientos_adapter != null) {

                    if (!ordenNombre) {

                        movimientos_adapter.ordenarNombre();
                        flechanomdesc.setVisibility(View.GONE);
                        flechanomasc.setVisibility(View.VISIBLE);
                        ordenNombre = true;

                    } else {

                        movimientos_adapter.ordenarNombre();
                        flechanomdesc.setVisibility(View.VISIBLE);
                        flechanomasc.setVisibility(View.GONE);
                        ordenNombre = false;

                    }

                    flechaimpasc.setVisibility(View.GONE);
                    flechaimpdesc.setVisibility(View.GONE);

                    flechafechadesc.setVisibility(View.GONE);
                    flechafechaasc.setVisibility(View.GONE);

                }

                break;

            case R.id.importe:

                if (movimientos_adapter != null) {

                    if (!ordenImporte) {

                        movimientos_adapter.ordenarImporte();
                        flechaimpasc.setVisibility(View.VISIBLE);
                        flechaimpdesc.setVisibility(View.GONE);
                        ordenImporte = true;

                    } else {

                        movimientos_adapter.ordenarImporte();
                        flechaimpdesc.setVisibility(View.VISIBLE);
                        flechaimpasc.setVisibility(View.GONE);

                        ordenImporte = false;
                    }

                    flechanomasc.setVisibility(View.GONE);
                    flechanomdesc.setVisibility(View.GONE);

                    flechafechadesc.setVisibility(View.GONE);
                    flechafechaasc.setVisibility(View.GONE);
                }

                break;

            case R.id.fecha:

                if (movimientos_adapter != null) {

                    if (!ordenFecha) {

                        movimientos_adapter.ordenarFecha();
                        flechafechaasc.setVisibility(View.VISIBLE);
                        flechafechadesc.setVisibility(View.GONE);
                        ordenFecha = true;

                    } else {

                        movimientos_adapter.ordenarFecha();
                        flechafechadesc.setVisibility(View.VISIBLE);
                        flechafechaasc.setVisibility(View.GONE);
                        ordenFecha = false;
                    }

                    flechanomasc.setVisibility(View.GONE);
                    flechanomdesc.setVisibility(View.GONE);

                    flechaimpasc.setVisibility(View.GONE);
                    flechaimpdesc.setVisibility(View.GONE);

                }
                break;

            case R.id.borraredit_icono:

                limpiar_edit.setImageResource(R.drawable.boton_borrar_caja);
                caja_busqueda.setText("");

                break;
        }

    }


}
