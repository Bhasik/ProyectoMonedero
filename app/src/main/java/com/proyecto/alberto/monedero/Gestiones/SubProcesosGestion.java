package com.proyecto.alberto.monedero.Gestiones;


import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.itextpdf.text.DocumentException;
import com.proyecto.alberto.monedero.Interfaces.Conceptos.Conceptos;
import com.proyecto.alberto.monedero.Interfaces.GastoFijo.GastosFijos;
import com.proyecto.alberto.monedero.Interfaces.GastoFijo.GastosFijos_Detalle;
import com.proyecto.alberto.monedero.Interfaces.MenuPrincipal;
import com.proyecto.alberto.monedero.Interfaces.Movimientos.Movimientos;
import com.proyecto.alberto.monedero.Interfaces.Movimientos.Movimientos_Detalle;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.Tablas.Concepto;
import com.proyecto.alberto.monedero.Tablas.Movimiento;
import com.proyecto.alberto.monedero.Tablas.Movimiento_PDF;
import com.proyecto.alberto.monedero.Tablas.Usuario;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class SubProcesosGestion extends AsyncTask<Integer, Void, Void> {

    //CONCEPTOS
    public static int CONCEPTOS_ACTUALIZAR = 1001;
    public static int CONCEPTOS_BAJAR = 1002;
    public static int CONCEPTOS_BORRAR = 1003;
    public static int CONCEPTOS_INSERTAR = 1004;

    //MOVIMIENTOS
    public static int MOVIMIENTOS_ACTUALIZAR = 2001;
    public static int MOVIMIENTOS_BAJAR = 2002;
    public static int MOVIMIENTOS_BORRAR = 2003;
    public static int MOVIMIENTOS_INSERTAR = 2004;
    public static int FILTRAR_FECHA = 2005;
    public static int NOTIFICACION_MOVIMIENTO = 2006;

    //BACKUP
    public static int BACKUP = 3001;

    //REPORTE
    public static int VISTA_DETALLADA = 4001;
    public static int COMPRA = 5001;

    //CLASES
    private FragmentActivity context;
    private Fragment fragment;
    //private Usuario usuario;
    private Concepto concepto;
    private Movimiento movimiento;

    //VARIABLES COMUNES
    private int subproceso;
    private int posicion;
    private String fecha_inicio;
    private String fecha_fin;
    private String kms_iniciales;
    private String kms_finales;
    private String correo;
    private ArrayList<Movimiento_PDF> datos;
    private double por_km;

    private boolean limite = false;

    //Variables para controlar conexiones
    private boolean error = false;
    private boolean conexion = true;
    private boolean correcto;
    Usuario usuario = new Usuario("a","1234");

    /**
     * Contructor para procesos que requieran un Fragment y un Acitivity
     *
     * @param fragment
     * @param context
     * @param subproceso
     */
    public SubProcesosGestion(Fragment fragment, FragmentActivity context, int subproceso) {

        this.fragment = fragment;
        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;


    }

    /**
     * Contructor para procesos que requieran un Fragment, Activity , posicion y el correo
     *
     * @param fragment
     * @param context
     * @param subproceso
     * @param correo
     */
    public SubProcesosGestion(Fragment fragment, FragmentActivity context, int subproceso, String correo) {

        this.fragment = fragment;
        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;
        this.correo = correo;

    }

    /**
     * Contructor para procesos que requieran un Fragment, Activity y una posición
     *
     * @param fragment
     * @param context
     * @param subproceso
     * @param posicion
     */
    public SubProcesosGestion(Fragment fragment, FragmentActivity context, int subproceso, int posicion) {

        this.fragment = fragment;
        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;
        this.posicion = posicion;

    }

    /**
     * Contructor para procesos que trabajen con un concepto
     *
     * @param context
     * @param concepto
     * @param subproceso
     */
    public SubProcesosGestion(FragmentActivity context, Concepto concepto, int subproceso) {

        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;
        this.concepto = concepto;

    }

    /**
     * Contructor para procesos que trabajen con un movimiento
     *
     * @param context
     * @param movimiento
     * @param subproceso
     */
    public SubProcesosGestion(FragmentActivity context, Movimiento movimiento, int subproceso) {

        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;
        this.movimiento = movimiento;

    }


    public SubProcesosGestion(FragmentActivity context, Fragment fragment, Movimiento movimiento, int subproceso) {

        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.fragment = fragment;
        this.subproceso = subproceso;
        this.movimiento = movimiento;

    }


    /**
     * Contructor para procesos que requieran un Fragment, Activity , posicion , fechas y kms
     *
     * @param fragment
     * @param context
     * @param subproceso
     * @param fecha_inicio
     * @param fecha_fin
     * @param kms_iniciales
     * @param kms_finales
     */
    public SubProcesosGestion(Fragment fragment, FragmentActivity context, int subproceso, String fecha_inicio, String fecha_fin, String kms_iniciales, String kms_finales) {

        this.fragment = fragment;
        this.context = context;
        this.usuario = ((Main_Activity) context).getUsuario();
        this.subproceso = subproceso;
        this.fecha_inicio = fecha_inicio;
        this.fecha_fin = fecha_fin;
        this.kms_iniciales = kms_iniciales;
        this.kms_finales = kms_finales;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if (!Alertas.haveNetworkConnection(context)) {

            Alertas.errorInternet(context);
            conexion = false;

        } else {

            conexion = true;

        }

    }

    @Override
    protected Void doInBackground(Integer... params) {

        if (conexion) {
            //Switch que elige que proceso se va a ejecutar, segun petición
            switch (subproceso) {

                case 1001:

                    actualizarConcepto();

                    break;

                case 1002:

                    bajarConcepto(comprobarClase(fragment));

                    break;

                case 1003:

                    borrarConcepto();

                    break;

                case 1004:

                    insertarConcepto();

                    break;

                case 2001:

                    actualizarMovimiento();

                    break;

                case 2002:

                    bajarMovimiento(comprobarClase(fragment));

                    break;

                case 2003:

                    borrarMovimiento();

                    break;

                case 2004:

                    insertarMovimiento();

                    break;

                case 2005:

                    filtrarMovimientos();

                    break;

                case 3001:

                    backup();

                    break;

                case 4001:

                    vistaDetallada();

                    break;

                case 5001:

                    efectuarCompra();

                    break;

            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        if (error) {

            Alertas.errorConexion(context);

        }

        if (fragment != null) {

            switch (fragment.getClass().getSimpleName()) {

                case "Movimientos_Detalle":


                    if (movimiento != null) {

                        if (subproceso == MOVIMIENTOS_ACTUALIZAR) {

                            Alertas.elementoModificado(context, "Movimiento");

                        } else if (limite) {

                            Alertas.limiteGratuito(context);

                        } else if (subproceso == MOVIMIENTOS_INSERTAR) {

                            Alertas.nuevoElemento(context, fragment, "Movimiento");

                        }

                    }

                    ((Movimientos_Detalle) fragment).configurar_adapter();


                    break;

                case "GastosFijos_Detalle":

                    if (movimiento != null) {

                        if (subproceso == MOVIMIENTOS_ACTUALIZAR) {

                            Alertas.elementoModificado(context, "Gasto Fijo");

                        } else if (limite) {

                            Alertas.limiteGratuito(context);

                        } else if (subproceso == MOVIMIENTOS_INSERTAR) {

                            Alertas.nuevoElemento(context, fragment, "Gasto Fijo");

                        }
                    }

                    ((GastosFijos_Detalle) fragment).configurar_adapter();


                    break;

                case "Conceptos":

                    ((Conceptos) fragment).configurar_adapter();

                    break;

                case "Movimientos":

                    ((Movimientos) fragment).configurarAdapter();

                    break;

                case "GastosFijos":

                    ((GastosFijos) fragment).configurarAdapter();

                    break;

                case "Reporte":

                    try {
                        new crearPdf(datos, fecha_inicio, fecha_fin, kms_iniciales, kms_finales, context).createPdf();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }
                    ((Main_Activity) context).setComprado(true);

                    break;

                case "MenuPrincipal":

                    ((Main_Activity) context).setComprado(true);

                    ((MenuPrincipal) fragment).compraEfectuada();

                    break;
            }

        }

    }

    private int comprobarClase(Fragment fragment) {

        if (fragment != null) {

            switch (fragment.getClass().getSimpleName()) {

                case "Movimientos":

                    return 0;

                case "Movimientos_Detalle":

                    return 0;

                case "GastosFijos":

                    return 1;

                case "GastosFijos_Detalle":

                    return 1;

                case "Conceptos":

                    return 2;

            }

        }

        return -1;

    }

    private String convertStreamToString(InputStream is) throws IOException {

        if (is != null) {

            StringBuilder sb = new StringBuilder();
            String line;

            try {

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is, "UTF8"));

                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }

            return sb.toString();

        } else {

            return "";

        }

    }

    private void actualizarConcepto() {

        String address = "http://89.248.107.8:3310/consultas/actualizarConcepto.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(6);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("nombre", concepto.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(concepto.getId())));

        int periodico;

        if (concepto.isPeriodico()) {

            periodico = 1;

        } else {

            periodico = 0;

        }

        nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(periodico)));
        nameValuePairs.add(new BasicNameValuePair("dias", String.valueOf(concepto.getDias())));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void bajarConcepto(int tipo) {

        String address = "http://89.248.107.8:3310/consultas/conceptos.php";
        List<NameValuePair> nameValuePairs;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);
        HttpResponse response;

        String resultado;

        nameValuePairs = new ArrayList<>(3);

        Usuario usuario = new Usuario("a","1234");

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(tipo)));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            resultado = convertStreamToString(is);

            if (!resultado.equals("")) {

                JSONObject json;

                try {

                    json = new JSONObject(resultado);
                    JSONArray jsonArray = json.optJSONArray("conceptos");


                    for (int i = 0; i < jsonArray.length(); i++) {
                        Concepto concepto = new Concepto();

                        JSONObject jsonArrJsonChild = jsonArray.getJSONObject(i);
                        concepto.setId(jsonArrJsonChild.optInt("id"));
                        concepto.setNombre(jsonArrJsonChild.optString("nombre"));
                        concepto.setDias(jsonArrJsonChild.optInt("dias"));

                        int periodico = jsonArrJsonChild.optInt("periodico");

                        if (periodico == 0) {
                            concepto.setPeriodico(false);
                        } else {
                            concepto.setPeriodico(true);
                        }

                        switch (fragment.getClass().getSimpleName()) {

                            case "Movimientos_Detalle":

                                ((Movimientos_Detalle) fragment).getConceptos_list().add(concepto);

                                break;

                            case "GastosFijos_Detalle":

                                ((GastosFijos_Detalle) fragment).getConceptos_list().add(concepto);

                                break;

                            case "Conceptos":

                                ((Conceptos) fragment).getConceptos_list().add(concepto);

                                break;

                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void borrarConcepto() {

        String address = "http://89.248.107.8:3310/consultas/borrarConcepto.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(3);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(concepto.getId())));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void insertarConcepto() {

        String address = "http://89.248.107.8:3310/consultas/insertarConcepto.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(5);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre().trim()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword().trim()));
        nameValuePairs.add(new BasicNameValuePair("nombre", concepto.getNombre().trim()));

        if (concepto.isPeriodico()) {

            nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(1).trim()));

        } else {

            nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(0).trim()));

        }

        nameValuePairs.add(new BasicNameValuePair("dias", String.valueOf(concepto.getDias()).trim()));


        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void actualizarMovimiento() {

        String address = "http://89.248.107.8:3310/consultas/actualizarMovimiento.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(7);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(movimiento.getId())));
        nameValuePairs.add(new BasicNameValuePair("concepto", String.valueOf(movimiento.getConcepto().getId())));
        nameValuePairs.add(new BasicNameValuePair("coste", String.valueOf(movimiento.getCoste())));
        nameValuePairs.add(new BasicNameValuePair("km", String.valueOf(movimiento.getKm())));

        String fecha = movimiento.getFecha() + " " + movimiento.getHora();

        nameValuePairs.add(new BasicNameValuePair("fecha", fecha));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);


        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void bajarMovimiento(int tipo) {

        String address = "http://89.248.107.8:3310/consultas/movimientos.php";
        List<NameValuePair> nameValuePairs;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);
        HttpResponse response;

        String resultado;

        nameValuePairs = new ArrayList<>(3);

        Usuario usuario = new Usuario("a","1234");

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(tipo)));


        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            resultado = convertStreamToString(is);

            if (!resultado.equals("")) {
                JSONObject json;

                try {

                    json = new JSONObject(resultado);
                    JSONArray jsonArray = json.optJSONArray("movimientos");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Concepto concepto = new Concepto();
                        Movimiento movimiento = new Movimiento();

                        JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                        concepto.setId(jsonArrayChild.optInt("id_concepto"));
                        concepto.setNombre(jsonArrayChild.optString("nombre"));
                        concepto.setDias(jsonArrayChild.optInt("dias"));

                        String fecha = jsonArrayChild.optString("fecha");
                        String[] fechayhora = fecha.split(" ");

                        movimiento.setConcepto(concepto);
                        movimiento.setId(jsonArrayChild.optInt("id"));
                        movimiento.setCoste(jsonArrayChild.optDouble("coste"));
                        movimiento.setKm(jsonArrayChild.optDouble("km"));
                        movimiento.setFecha(fechayhora[0]);
                        movimiento.setHora(fechayhora[1]);

                        switch (fragment.getClass().getSimpleName()) {

                            case "Movimientos":

                                ((Movimientos) fragment).getMovimientos_list().add(movimiento);

                                break;

                            case "GastosFijos":

                                ((GastosFijos) fragment).getGastosfijos_list().add(movimiento);

                                break;

                        }

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void borrarMovimiento() {

        String address = "http://89.248.107.8:3310/consultas/borrarMovimiento.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(3);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("id", String.valueOf(movimiento.getId())));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void insertarMovimiento() {

        String address = "http://89.248.107.8:3310/consultas/insertarMovimiento.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(7);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("coste", String.valueOf(movimiento.getCoste())));
        nameValuePairs.add(new BasicNameValuePair("km", String.valueOf(movimiento.getKm())));
        nameValuePairs.add(new BasicNameValuePair("fecha", String.valueOf(movimiento.getFecha())));
        nameValuePairs.add(new BasicNameValuePair("hora", String.valueOf(movimiento.getHora())));
        nameValuePairs.add(new BasicNameValuePair("id_concepto", String.valueOf(movimiento.getConcepto().getId())));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

            Log.d("Insertar", request);

            if (request.contains("limite")) {

                limite = true;
            } else {

                limite = false;
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void filtrarMovimientos() {

        String address = "http://89.248.107.8:3310/consultas/filtrarmes.php";
        List<NameValuePair> nameValuePairs;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);
        HttpResponse response;

        String resultado;

        nameValuePairs = new ArrayList<>(4);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("periodico", String.valueOf(0)));
        nameValuePairs.add(new BasicNameValuePair("posicion", String.valueOf(posicion)));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            resultado = convertStreamToString(is);

            if (!resultado.equals("")) {
                JSONObject json;

                try {

                    json = new JSONObject(resultado);
                    JSONArray jsonArray = json.optJSONArray("movimientos");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        Concepto concepto = new Concepto();
                        Movimiento movimiento = new Movimiento();

                        JSONObject jsonArrayChild = jsonArray.getJSONObject(i);
                        movimiento.setId(jsonArrayChild.optInt("id"));
                        concepto.setId(jsonArrayChild.optInt("id_concepto"));
                        concepto.setNombre(jsonArrayChild.optString("nombre"));
                        concepto.setDias(jsonArrayChild.optInt("dias"));
                        movimiento.setCoste(jsonArrayChild.optDouble("coste"));
                        movimiento.setKm(jsonArrayChild.optDouble("km"));

                        movimiento.setConcepto(concepto);

                        String fecha = jsonArrayChild.optString("fecha");
                        String[] fechayhora = fecha.split(" ");

                        movimiento.setFecha(fechayhora[0]);
                        movimiento.setHora(fechayhora[1]);

                        //Anyadimos el movimiento descargado al array del Fragment "Movimientos"
                        ((Movimientos) fragment).getMovimientos_list().add(movimiento);


                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    private void backup() {

        String address = "http://89.248.107.8:3310/Consultas/backUp.php";
        List<NameValuePair> nameValuePairs;
        String request;

        HttpClient httpClient;
        HttpPost httpPost;

        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(2);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

            ((Main_Activity) context).backup(request);

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    public void vistaDetallada() {

        String address = "http://89.248.107.8:3310/consultas/vistadetallada.php";
        List<NameValuePair> nameValuePairs;

        HttpClient httpClient;
        HttpPost httpPost;

        HttpParams httpParameters = new BasicHttpParams();

        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);

        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        httpClient = new DefaultHttpClient(httpParameters);
        httpPost = new HttpPost(address);
        HttpResponse response;

        String resultado;

        nameValuePairs = new ArrayList<>(6);

        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("fecha_inicio", fecha_inicio));
        nameValuePairs.add(new BasicNameValuePair("fecha_fin", fecha_fin));
        nameValuePairs.add(new BasicNameValuePair("km_inicio", kms_iniciales));
        nameValuePairs.add(new BasicNameValuePair("km_fin", kms_finales));

        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            resultado = convertStreamToString(is);
            Log.d("b", resultado);

            if (!resultado.equals("")) {
                JSONObject json;

                try {

                    json = new JSONObject(resultado);
                    JSONArray jsonArray = json.optJSONArray("detallada");

                    datos = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {

                        int id_concepto;
                        int tipo;
                        String nombre;
                        int dias;
                        double importe_periodo;
                        int num_operaciones;
                        double importe_dia;
                        double importe_fijo;

                        JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                        id_concepto = jsonArrayChild.optInt("id_concepto");
                        tipo = jsonArrayChild.optInt("tipo");
                        nombre = jsonArrayChild.optString("concepto");
                        dias = jsonArrayChild.optInt("dias");
                        importe_periodo = jsonArrayChild.optDouble("importe_periodo");
                        num_operaciones = jsonArrayChild.optInt("num_operaciones");
                        importe_dia = jsonArrayChild.optDouble("importe_dia");
                        importe_fijo = jsonArrayChild.optDouble("importe_fijo");
                        por_km = jsonArrayChild.optDouble("por_km");

                        datos.add(new Movimiento_PDF(id_concepto, tipo, nombre, dias, importe_periodo, num_operaciones, importe_dia, importe_fijo, por_km));

                    }

                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = true;

        }

    }

    public boolean efectuarCompra() {

        String request;
        String address = "http://89.248.107.8:3310/consultas/comprarAplicacion.php";
        List<NameValuePair> nameValuePairs;

        HttpClient httpClient;
        HttpPost httpPost;

        httpClient = new DefaultHttpClient();
        httpPost = new HttpPost(address);

        nameValuePairs = new ArrayList<>(3);
        nameValuePairs.add(new BasicNameValuePair("usuario", usuario.getNombre()));
        nameValuePairs.add(new BasicNameValuePair("password", usuario.getPassword()));
        nameValuePairs.add(new BasicNameValuePair("correo", correo));


        try {

            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            request = httpClient.execute(httpPost, responseHandler);

            Log.v("Request", request);

            if (request.equals("true")) {

                return true;

            } else {

                return false;

            }

        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();

        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            error = false;

        }

        return false;

    }


}


