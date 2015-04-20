package com.proyecto.alberto.monedero.Interfaces.Conceptos;

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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.Animaciones;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.Interfaces.insertarConceptosClass;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Swipe.BaseSwipeListViewListener;
import com.proyecto.alberto.monedero.Swipe.SwipeListView;
import com.proyecto.alberto.monedero.Tablas.Concepto;

import java.util.ArrayList;

/**
 * Fragment donde se muestra una lista de todos los conceptos
 */
public class Conceptos extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "CONCEPTOS";

    private View view;
    private FragmentActivity context;
    private FrameLayout conceptosfrag;


    private SwipeListView list;
    private ArrayList<Concepto> conceptos_list;
    private Conceptos_Adapter conceptos_adapter;

    private EditText caja_busqueda;
    private ImageButton lupa;
    private ImageButton flecha_izq;
    private ImageButton limpiar_edit;

    private boolean ordenarNombre = false;
    private boolean ordenarImporte = false;

    private ImageButton nomdesc;
    private ImageButton nomasc;
    private ImageButton importedesc;
    private ImageButton importeasc;

    private FragmentTransaction ft;

    public Conceptos() {

    }

    public ArrayList getConceptos_list() {
        return conceptos_list;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.conceptos_fragment, container, false);
        this.context = getActivity();

        lupa = (ImageButton) view.findViewById(R.id.lupa);
        flecha_izq = (ImageButton) view.findViewById(R.id.flecha_izq);
        caja_busqueda = (EditText) view.findViewById(R.id.caja_busqueda);
        limpiar_edit = (ImageButton) view.findViewById(R.id.borraredit_icono);

        nomasc = (ImageButton) view.findViewById(R.id.nomasc);
        nomdesc = (ImageButton) view.findViewById(R.id.nomdesc);
        importeasc = (ImageButton) view.findViewById(R.id.tipoasc);
        importedesc = (ImageButton) view.findViewById(R.id.tipodesc);

        view.findViewById(R.id.insertar).setOnClickListener(this);
        view.findViewById(R.id.concepto_textview_boton).setOnClickListener(this);
        view.findViewById(R.id.tipo_textview_boton).setOnClickListener(this);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        conceptos_list = new ArrayList();

        flecha_izq.setOnClickListener(this);
        caja_busqueda.setOnClickListener(this);
        lupa.setOnClickListener(this);
        limpiar_edit.setOnClickListener(this);



        caja_busqueda.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub

                conceptos_adapter.getFilter().filter(arg0);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        new SubProcesosGestion(this, context, SubProcesosGestion.CONCEPTOS_BAJAR).execute();

    }

    public void configurar_adapter() {

        list = (SwipeListView) view.findViewById(R.id.lista_conceptos);

        conceptos_adapter = new Conceptos_Adapter(context, this);

        list.setAdapter(conceptos_adapter);

        list.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

        list.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {

            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {
                mode.setTitle("Selected (" + list.getCountSelected() + ")");
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                //  MenuInflater inflater = mode.getMenuInflater();
                //  inflater.inflate(R.menu_fragment.menu_choice_items, menu_fragment);
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
                lanzarConceptos_Detalle(conceptos_adapter.getConcepto(position));
            }

        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.insertar:

                ((Main_Activity)context).getConceptosanyadir().setVisibility(View.VISIBLE);

                list.setEnabled(false);
                list.setClickable(false);

                ((Main_Activity)context).getFondoNegro().setVisibility(View.VISIBLE);


                ft = context.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.conceptos_fragment, new insertarConceptosClass(), insertarConceptosClass.TAG_FRAGMENT);
                ft.addToBackStack(Conceptos.TAG_FRAGMENT);
                ft.commit();


                break;

            case R.id.lupa:

                Animaciones.lupa(context, this, view);

                break;

            case R.id.flecha_izq:

                Animaciones.flecha_izq(context, this, view);

                break;

            case R.id.concepto_textview_boton:

                if (conceptos_adapter != null) {

                    if (!ordenarNombre) {

                        conceptos_adapter.ordenarNombre();
                        nomasc.setVisibility(View.VISIBLE);
                        nomdesc.setVisibility(View.GONE);
                        ordenarNombre = true;

                    } else {

                        conceptos_adapter.ordenarNombre();
                        nomdesc.setVisibility(View.VISIBLE);
                        nomasc.setVisibility(View.GONE);
                        ordenarNombre = false;
                    }

                    importeasc.setVisibility(View.GONE);
                    importedesc.setVisibility(View.GONE);
                }
                break;

            case R.id.tipo_textview_boton:

                if (conceptos_adapter != null) {

                    if (!ordenarImporte) {

                        conceptos_adapter.ordenarTipo();
                        importeasc.setVisibility(View.VISIBLE);
                        importedesc.setVisibility(View.GONE);
                        ordenarImporte = true;

                    } else {

                        conceptos_adapter.ordenarTipo();
                        importedesc.setVisibility(View.VISIBLE);
                        importeasc.setVisibility(View.GONE);
                        ordenarImporte = false;
                    }

                    nomdesc.setVisibility(View.GONE);
                    nomasc.setVisibility(View.GONE);

                }

                break;

            case R.id.borraredit_icono:

                caja_busqueda.setText("");
                limpiar_edit.setImageResource(R.drawable.boton_borrar_caja);

                break;

        }
    }

    public void lanzarConceptos_Detalle(Concepto conp) {

        if (!Alertas.haveNetworkConnection(context)) {

            Alertas.errorInternet(context);

        } else if (conp != null) {

            Bundle arguments = new Bundle();

            arguments.putInt("id_con", conp.getId());
            arguments.putString("concepto_nom", conp.getNombre());
            arguments.putBoolean("periodico", conp.isPeriodico());
            arguments.putInt("dias", conp.getDias());

            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new Conceptos_Detalle().newInstance(arguments), Conceptos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(Conceptos.TAG_FRAGMENT);
            ft.commit();

        } else {

            ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.container, new Conceptos_Detalle(), Conceptos_Detalle.TAG_FRAGMENT);
            ft.addToBackStack(Conceptos.TAG_FRAGMENT);
            ft.commit();

        }

    }

}