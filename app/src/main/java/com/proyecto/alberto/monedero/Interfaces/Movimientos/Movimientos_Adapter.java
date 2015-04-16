package com.proyecto.alberto.monedero.Interfaces.Movimientos;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.Ordenar;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Movimiento;

import java.util.ArrayList;

/**
 * Fragment donde se muestra en detalle un movimiento
 */
public class Movimientos_Adapter extends ArrayAdapter implements Filterable {

    private View view;
    private Activity context;
    private LayoutInflater inflater;

    //ListView
    private ArrayList<Movimiento> movimientos_list_filter;
    private ArrayList<Movimiento> movimientos_list_items;

    //UI
    private LinearLayout linear;

    private boolean order_nombre_desc = false;
    private boolean order_import_desc = false;
    private boolean order_fecha_desc = false;

    private Movimientos movimientos;

    private Filter filter;

    public Movimientos_Adapter(Activity context, Movimientos movimientos) {
        super(context, R.layout.movimientos_listview, movimientos.getMovimientos_list());

        this.context = context;
        this.movimientos_list_items = new ArrayList<>();
        this.movimientos_list_items.addAll(movimientos.getMovimientos_list());
        this.movimientos_list_filter = new ArrayList<>();
        this.movimientos_list_filter.addAll(this.movimientos_list_items);
        this.movimientos = movimientos;
        this.inflater = context.getLayoutInflater();
        getFilter();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        this.view = null;

        Movimiento m = movimientos_list_filter.get(position);

        this.view = inflater.inflate(R.layout.movimientos_listview, null);

        TextView concepto = (TextView) view.findViewById(R.id.concepto);
        TextView movimiento = (TextView) view.findViewById(R.id.importe);
        TextView fecha = (TextView) view.findViewById(R.id.fecha);
        TextView hora = (TextView) view.findViewById(R.id.hora);
        ImageButton papelera = (ImageButton) view.findViewById(R.id.boton_borrar);
        linear = (LinearLayout) view.findViewById(R.id.swipelist_frontview);

        concepto.setText(m.getConcepto().getNombre());
        movimiento.setText(m.getMovimiento() + " €");
        fecha.setText(m.getFecha());
        hora.setText(m.getHora());

        papelera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Alertas.haveNetworkConnection(context)) {

                    Alertas.errorInternet(context);

                } else {

                    quitarItem(position);

                }
            }
        });

        if (movimientos_list_filter.get(position).getMovimiento() >= 0) {

            linear.setBackground(context.getResources().getDrawable(R.color.DarkSeaGreen));

        } else {

            linear.setBackground(context.getResources().getDrawable(R.color.IndianRed));

        }


        return view;

    }

    public void quitarItem(int position) {

        new SubProcesosGestion(context, movimientos_list_filter.get(position), SubProcesosGestion.MOVIMIENTOS_BORRAR).execute();

        for (Movimiento movimiento : movimientos_list_items) {

            if (movimiento.getId() == movimientos_list_filter.get(position).getId()) {

                movimientos_list_items.remove(movimiento);
                break;

            }
        }

        movimientos_list_filter.remove(position);

        notifyDataSetChanged();
        clear();

        for (Movimiento movimiento : movimientos_list_filter) {

            add(movimiento);

        }

        notifyDataSetInvalidated();
        notifyDataSetChanged();

    }

    public Movimiento getMovimiento(int id) {
        return movimientos_list_filter.get(id);
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new Filtro();
        }
        return filter;
    }

    public void ordenarNombre() {

        order_nombre_desc = Ordenar.ordenarMovimientoPorNombre(movimientos_list_filter, order_nombre_desc, this);

    }

    public void ordenarImporte() {

        order_import_desc = Ordenar.ordenarPorImporte(movimientos_list_filter, order_import_desc, this);

    }

    public void ordenarFecha() {

        order_fecha_desc = Ordenar.ordenarPorFecha(movimientos_list_filter, order_fecha_desc, this);

    }

    /**
     * Filtro para movimientos
     */
    private class Filtro extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Movimiento> filt = new ArrayList<Movimiento>();
                ArrayList<Movimiento> lItems = new ArrayList<Movimiento>();
                synchronized (this) {
                    lItems.addAll(movimientos_list_items);
                }
                for (Movimiento movimiento : lItems) {

                    if (movimiento.getConcepto().getNombre().toLowerCase().contains(constraint)) {

                        filt.add(movimiento);

                    }

                }

                result.count = filt.size();
                result.values = filt;

            } else {

                synchronized (this) {

                    result.values = movimientos_list_items;
                    result.count = movimientos_list_items.size();

                }

            }

            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            movimientos_list_filter = (ArrayList<Movimiento>) results.values;
            notifyDataSetChanged();
            clear();

            for (Movimiento movimiento : movimientos_list_filter) {

                add(movimiento);

            }

            notifyDataSetInvalidated();

        }
    }

}
