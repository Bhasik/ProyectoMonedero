package com.proyecto.alberto.monedero.Interfaces;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.R;

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
 * Created by user on 01/04/2015.
 */
public class Registro extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "REGISTRO";

    private EditText usuario;
    private EditText password;
    private Spinner correos;

    private String correoSeleccionado;

    private View v;
    private FragmentActivity context;

    //Variables para controlar conexiones
    private boolean error = false;
    private boolean conexion = true;

    private ArrayAdapter spin_adapter;

    public Registro() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.v = inflater.inflate(R.layout.registrarse, container, false);
        this.context = getActivity();

        usuario = (EditText) v.findViewById(R.id.user_edittext);
        password = (EditText) v.findViewById(R.id.pass_edittext);
        correos = (Spinner) v.findViewById(R.id.correos_spinner);

        v.findViewById(R.id.registro_btn).setOnClickListener(this);

        spin_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, getCuentasGoogle());

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        correos.setAdapter(spin_adapter);

        correos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                correoSeleccionado = parent.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public ArrayList<String> getCuentasGoogle() {

        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        ArrayList<String> correos = new ArrayList<>();

        for (Account account : accounts) {

            correos.add(account.name);

        }

        return correos;

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.registro_btn:

                if (usuario.getText().toString().equals("") || password.getText().toString().equals("")) {

                    Alertas.faltaRellenar(context);

                } else {

                    new registrarUsuario(context).execute();

                }

                break;
        }

    }

    /**
     * Metodo de registro para ingresar usuario en la base de datos
     */
    class registrarUsuario extends AsyncTask<String, String, String> {

        private FragmentActivity context;
        private boolean correcto;

        registrarUsuario(FragmentActivity context) {

            this.context = context;

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
        protected String doInBackground(String... params) {

            if (conexion) {

                correcto = efectuarRegistro(usuario.getText().toString(), password.getText().toString());
            }

            return null;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (correcto) {

                Toast.makeText(context, getString(R.string.registrado), Toast.LENGTH_SHORT).show();

            } else {

                Toast.makeText(context, getString(R.string.noRegistrado), Toast.LENGTH_SHORT).show();

            }

        }

        private boolean efectuarRegistro(String usuario, String password) {

            String request;
            String address = "http://89.248.107.8:3310/consultas/registrarseAplicacion.php";
            List<NameValuePair> nameValuePairs;

            HttpClient httpClient;
            HttpPost httpPost;
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

            nameValuePairs = new ArrayList<>(2);
            nameValuePairs.add(new BasicNameValuePair("usuario", usuario));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("correo", correoSeleccionado));

            try {

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                request = httpClient.execute(httpPost, responseHandler);

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

}
