package com.proyecto.alberto.monedero.Interfaces.GastoFijo;

import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.Ordenar;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Movimiento;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Fragment donde se muestra en detalle un gasto fijo
 */
public class GastosFijos_Adapter extends ArrayAdapter {

    private FragmentActivity context;
    private View item;
    private LayoutInflater inflater;

    private ArrayList<Movimiento> gastofijo_list;
    private ArrayList<Movimiento> gastofijo_filter;

    private boolean order_nombre_desc = false;
    private boolean order_import_desc = false;
    private boolean order_dias_desc = false;
    private boolean order_importdiario_desc = false;

    //UI
    private LinearLayout linear;

    private GastosFijos gastosFijos;

    private Filter filter;

    public GastosFijos_Adapter(FragmentActivity context, GastosFijos gastosFijos) {
        super(context, R.layout.gastosfijos_listview, gastosFijos.getGastosfijos_list());

        this.context = context;
        this.gastofijo_list = new ArrayList<>();
        this.gastofijo_list.addAll(gastosFijos.getGastosfijos_list());
        this.gastofijo_filter = new ArrayList<>();
        this.gastofijo_filter.addAll(this.gastofijo_list);
        this.gastosFijos = gastosFijos;
        this.inflater = context.getLayoutInflater();
        getFilter();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        inflater = LayoutInflater.from(context);
        this.item = inflater.inflate(R.layout.gastosfijos_listview, null);

        TextView concepto = (TextView) item.findViewById(R.id.concepto_listgf);
        TextView importe = (TextView) item.findViewById(R.id.importe_listgf);
        TextView dias = (TextView) item.findViewById(R.id.dias_listagf);
        TextView importeD = (TextView) item.findViewById(R.id.imported_listgf);
        ImageButton papelera = (ImageButton) item.findViewById(R.id.boton_borrar);
        concepto.setText(gastofijo_filter.get(position).getConcepto().getNombre());
        importe.setText(String.valueOf(gastofijo_filter.get(position).getCoste()));
        dias.setText(String.valueOf(gastofijo_filter.get(position).getConcepto().getDias()));
        linear = (LinearLayout) item.findViewById(R.id.swipelist_frontview);


        DecimalFormat df = new DecimalFormat("#.##");

        String importe_dia = df.format(gastofijo_filter.get(position).getCoste() / gastofijo_filter.get(position).getConcepto().getDias());

        importeD.setText(importe_dia);
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

        if (gastofijo_filter.get(position).getMovimiento() <= 0) {

            linear.setBackground(context.getResources().getDrawable(R.color.RedGasto));

        }


        return item;

    }

    public Movimiento getMovimiento(int id) {
        return gastofijo_filter.get(id);
    }

    @Override
    public Filter getFilter() {

        if (filter == null) {

            filter = new Filtro();

        }

        return filter;
    }

    public void quitarItem(int position) {

        new SubProcesosGestion(context, gastofijo_filter.get(position), SubProcesosGestion.MOVIMIENTOS_BORRAR).execute();

        for (Movimiento movimiento : gastofijo_list) {

            if (movimiento.getId() == gastofijo_filter.get(position).getId()) {

                gastofijo_list.remove(movimiento);
                break;

            }
        }

        gastofijo_filter.remove(position);

        notifyDataSetChanged();
        clear();

        for (Movimiento movimiento : gastofijo_filter) {

            add(movimiento);

        }

        notifyDataSetInvalidated();
        notifyDataSetChanged();

    }

    public void ordenarNombre() {

        order_nombre_desc = Ordenar.ordenarMovimientoPorNombre(gastofijo_filter, order_nombre_desc, this);

    }

    public void ordenarImporte() {

        order_import_desc = Ordenar.ordenarPorImporte(gastofijo_filter, order_import_desc, this);

    }

    public void ordenarDias() {

        order_dias_desc = Ordenar.ordenarPorDias(gastofijo_filter, order_dias_desc, this);

    }

    public void ordenarImporteDia() {

        order_importdiario_desc = Ordenar.ordenarPorImporteDia(gastofijo_filter, order_importdiario_desc, this);

    }

    /**
     * Metodo para filtrar los gastos fijos
     */
    private class Filtro extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Movimiento> filt = new ArrayList();
                ArrayList<Movimiento> lItems = new ArrayList();

                synchronized (this) {
                    lItems.addAll(gastofijo_list);
                }
                for (Movimiento m : lItems) {

                    if (m.getConcepto().getNombre().toLowerCase().contains(constraint)) {

                        filt.add(m);

                    }

                }

                result.count = filt.size();
                result.values = filt;

            } else {

                synchronized (this) {

                    result.values = gastofijo_list;
                    result.count = gastofijo_list.size();

                }

            }

            return result;

        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            gastofijo_filter = (ArrayList<Movimiento>) results.values;
            notifyDataSetChanged();
            clear();

            for (Movimiento movimiento : gastofijo_filter) {

                add(movimiento);

            }

            notifyDataSetInvalidated();

        }
    }


}
