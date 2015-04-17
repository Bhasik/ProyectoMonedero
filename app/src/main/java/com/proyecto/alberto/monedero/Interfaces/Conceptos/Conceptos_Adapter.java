package com.proyecto.alberto.monedero.Interfaces.Conceptos;

import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.Ordenar;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Concepto;
import com.proyecto.alberto.monedero.Tablas.Usuario;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Configura el adapter de la listview creada en Conceptos
 */
public class Conceptos_Adapter extends ArrayAdapter {

    private FragmentActivity context;
    private View view;
    private LayoutInflater inflater;

    private ArrayList<Concepto> conceptos_list;
    private ArrayList<Concepto> conceptos_filter;

    private ImageButton papelera;

    private Filter filter;
    private Conceptos conceptos;

    private boolean order_nombre_desc = false;
    private boolean order_tipo_desc = false;

    public Conceptos_Adapter(FragmentActivity context, Conceptos conceptos) {
        super(context, R.layout.concepto_listview, conceptos.getConceptos_list());

        this.context = context;
        this.conceptos_list = new ArrayList<>();
        this.conceptos_list.addAll(conceptos.getConceptos_list());
        this.conceptos_filter = new ArrayList<>();
        this.conceptos_filter.addAll(this.conceptos_list);
        this.conceptos = conceptos;
        this.inflater = context.getLayoutInflater();
        getFilter();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        this.view = inflater.inflate(R.layout.concepto_listview, null);

        TextView concepto = (TextView) view.findViewById(R.id.concepto_textview_list);
        TextView tipo = (TextView) view.findViewById(R.id.tipo_textview_list);
        papelera = (ImageButton) view.findViewById(R.id.boton_borrar);

        concepto.setText(conceptos_filter.get(position).getNombre());
        String periodico;

        if (conceptos_filter.get(position).isPeriodico()) {

            periodico = "Periodico";

        } else {

            periodico = "Diario";

        }

        tipo.setText(periodico);
        papelera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!Alertas.haveNetworkConnection(context)) {

                    Alertas.errorInternet(context);

                } else {

                    new ComprobarMovimientos(context, getConcepto(position), position).execute();

                }

            }
        });

        return view;
    }

    public Concepto getConcepto(int id) {

        return conceptos_filter.get(id);

    }

    @Override
    public Filter getFilter() {

        if (filter == null) {
            filter = new Filtro();
        }
        return filter;
    }

    public void ordenarNombre() {

        order_nombre_desc = Ordenar.ordenarConceptoPorNombre(conceptos_filter, order_nombre_desc, this);

    }

    public void ordenarTipo() {

        order_tipo_desc = Ordenar.ordenarPorTipo(conceptos_filter, order_tipo_desc, this);
    }

    /**
     * Filtro para conceptos
     */
    private class Filtro extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            constraint = constraint.toString().toLowerCase();
            FilterResults result = new FilterResults();
            if (constraint != null && constraint.toString().length() > 0) {
                ArrayList<Concepto> filt = new ArrayList();
                ArrayList<Concepto> lItems = new ArrayList();
                synchronized (this) {
                    lItems.addAll(conceptos_list);
                }
                for (Concepto concepto : lItems) {

                    if (concepto.getNombre().toLowerCase().contains(constraint)) {
                        filt.add(concepto);
                    }
                }
                result.count = filt.size();
                result.values = filt;
            } else {
                synchronized (this) {
                    result.values = conceptos_list;
                    result.count = conceptos_list.size();
                }
            }
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            conceptos_filter = (ArrayList<Concepto>) results.values;
            notifyDataSetChanged();
            clear();

            for (Concepto concepto : conceptos_filter) {
                add(concepto);
            }

            notifyDataSetInvalidated();

        }
    }

    /**
     * Subproceso para comprobar si un concepto tiene algun movimiento asociado
     */
    public class ComprobarMovimientos extends AsyncTask<Integer, Void, Integer> {

        private FragmentActivity context;
        private Concepto concepto;
        private Usuario usuario;
        private int position;

        public ComprobarMovimientos(FragmentActivity context, Concepto concepto, int position) {

            this.context = context;
            this.concepto = concepto;
            this.position = position;
            this.usuario = ((Main_Activity) context).getUsuario();

        }

        @Override
        protected Integer doInBackground(Integer... params) {

            return comprobarDatos();

        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

            if (integer == 0) {

                quitarItem(concepto.getId());

            } else {

                Alertas.alertaConcepto(this, context, concepto.getId(), integer);

            }

        }

        public void quitarItem(int id) {

            new SubProcesosGestion(context, concepto, SubProcesosGestion.CONCEPTOS_BORRAR).execute();

            for (Concepto con : conceptos_list) {

                if (con.getId() == id) {

                    conceptos_list.remove(con);

                    break;

                }
            }

            conceptos_filter.remove(position);

            notifyDataSetChanged();
            clear();

            for (Concepto concepto : conceptos_filter) {

                add(concepto);

            }

            notifyDataSetInvalidated();
            notifyDataSetChanged();

        }

        private int comprobarDatos() {

            String address = "http://89.248.107.8:3310/consultas/comprobarConceptosMovimientos.php";
            List<NameValuePair> nameValuePairs;
            String request;

            HttpClient httpClient;
            HttpPost httpPost;

            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

            nameValuePairs = new ArrayList<>(3);

            nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
            nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
            nameValuePairs.add(new BasicNameValuePair("idC", String.valueOf(concepto.getId())));

            try {

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                request = httpClient.execute(httpPost, responseHandler);

                return Integer.parseInt(request);

            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            }

            return 0;

        }

    }

}
