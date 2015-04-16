package com.proyecto.alberto.monedero.Interfaces;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;
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


public class Login extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "LOGIN";
    ProgressDialog mProgressDialog;
    //Variables
    private String correoSeleccionado;
    //Botones
    private EditText usuario;
    private EditText password;
    //Activity
    private View v;
    private Activity context;
    private ImageView fondo_logo;
    private ImageView icono_logo;
    private Button btn_login;
    private Button btn_registro;
    //Variables para controlar conexiones
    private boolean error = false;
    private boolean conexion = true;
    private int login = 0;
    private Animation anim_preparacion_fondo;

    private FragmentTransaction ft;

    public Login() {

    }

    /**
     * Metodo que crea la vista del Fragment
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        this.v = inflater.inflate(R.layout.login_fragment, container, false);
        this.context = getActivity();

        usuario = (EditText) v.findViewById(R.id.usuario);
        password = (EditText) v.findViewById(R.id.password);

        anim_preparacion_fondo = AnimationUtils.loadAnimation(context, R.anim.anim_inicio_app);

        fondo_logo = (ImageView) v.findViewById(R.id.fondo_logo);
        icono_logo = (ImageView) v.findViewById(R.id.icono_logo);

        //Listeners
        btn_login = (Button) v.findViewById(R.id.login);
        btn_registro = (Button) v.findViewById(R.id.registro);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btn_login.setOnClickListener(this);
        btn_registro.setOnClickListener(this);

        anim_preparacion_fondo.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                usuario.setText(((Main_Activity) context).getConfig().getString("usuario", ""));
                password.setText(((Main_Activity) context).getConfig().getString("password", ""));

                if (!usuario.getText().toString().equals("") && !password.getText().toString().equals("")) {

                    btn_login.setEnabled(false);
                    btn_registro.setEnabled(false);

                }
            }

            @Override
            public void onAnimationEnd(Animation animation) {

                fondo_logo.setVisibility(View.GONE);
                icono_logo.setVisibility(View.GONE);

                ((Main_Activity) context).setInicio(false);

                if (!usuario.getText().toString().equals("") && !password.getText().toString().equals("")) {

                    new comprobarLogin(context).execute();

                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        if (((Main_Activity) context).isInicio()) {

            fondo_logo.setVisibility(View.VISIBLE);
            icono_logo.setVisibility(View.VISIBLE);

            fondo_logo.startAnimation(anim_preparacion_fondo);
            icono_logo.startAnimation(anim_preparacion_fondo);

        }

    }

    /**
     * Metodo que retorna todas las cuentas que tiene el usuario en el movil y son de google (con las que se efectua la compra)
     *
     * @return
     */
    public ArrayList<String> getCuentasGoogle() {

        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        ArrayList<String> correos = new ArrayList<>();

        for (Account account : accounts) {

            correos.add(account.name);

        }

        return correos;

    }

    /**
     * Metodo que pasa a String un array y quita los []
     *
     * @param correos
     * @return
     */
    public String convertStringCorreos(ArrayList<String> correos) {

        String correo = correos.toString();
        correo = correo.replace('[', ' ');
        correo = correo.replace(']', ' ');

        return correo.trim();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.login:

                if (Alertas.haveNetworkConnection(context)) {

                    new comprobarLogin(context).execute();

                } else {

                    Alertas.errorInternet(context);

                }

                break;

            case R.id.registro:

                ft = getFragmentManager().beginTransaction();
                ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                ft.replace(R.id.container, new Registro(), Registro.TAG_FRAGMENT);
                ft.addToBackStack(Login.TAG_FRAGMENT);
                ft.commit();

                break;

        }

    }


    /**
     * Subproceso que comprueba si el usuario esta registrado en la base de datos y si ha comprado o no el producto.
     */
    class comprobarLogin extends AsyncTask<String, String, String> {

        private int tipo_conexion;
        private Activity context;

        comprobarLogin(Activity context) {

            this.context = context;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            btn_login.setEnabled(false);
            btn_registro.setEnabled(false);

            // this.context.showDialog(Main_Activity.DIALOG_DOWNLOAD_JSON_PROGRESS);

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Logeando...");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();

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

                consutlarBBDDLogin(usuario.getText().toString(), password.getText().toString());

            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            btn_login.setEnabled(true);
            btn_registro.setEnabled(true);

            mProgressDialog.dismiss();

            if (error) {

                Alertas.errorConexion(context);

            }

            //Aqui se tiene que cambiar y hacer para que sea un numerico, 0 fail login, 1 login sin compra, 2 login con compra (Tambien se tiene que modificar el php)
            if (tipo_conexion == 2) {

                Toast.makeText(context, getString(R.string.corretLogin), Toast.LENGTH_SHORT).show();
                ((Main_Activity) context).setComprado(true);
                ((Main_Activity) context).cargarBanner();
                lanzarMenuPrincipal();

            } else if (tipo_conexion == 1) {

                Toast.makeText(context, getString(R.string.corretLogin), Toast.LENGTH_SHORT).show();
                ((Main_Activity) this.context).setComprado(false);
                lanzarMenuPrincipal();

            } else {

                Toast.makeText(context, getString(R.string.failLogin), Toast.LENGTH_SHORT).show();

            }

        }

        public void lanzarMenuPrincipal() {

            ((Main_Activity) context).setUsuario(new Usuario(usuario.getText().toString(), password.getText().toString()));

            SharedPreferences.Editor editver = ((Main_Activity) context).getConfig().edit();
            editver.putString("usuario", usuario.getText().toString());
            editver.putString("password", password.getText().toString());
            editver.commit();

            ft = getFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
            ft.replace(R.id.container, new MenuPrincipal(), MenuPrincipal.TAG_FRAGMENT);
            ft.addToBackStack(Login.TAG_FRAGMENT);
            ft.commit();

        }

        private void consutlarBBDDLogin(String usuario, String password) {

            String request;
            String address = "http://89.248.107.8:3310/consultas/comprobarLogin.php";
            List<NameValuePair> nameValuePairs;

            HttpClient httpClient;
            HttpPost httpPost;
            httpClient = new DefaultHttpClient();
            httpPost = new HttpPost(address);

            nameValuePairs = new ArrayList<>(3);
            nameValuePairs.add(new BasicNameValuePair("usuario", usuario));
            nameValuePairs.add(new BasicNameValuePair("password", password));
            nameValuePairs.add(new BasicNameValuePair("correo", convertStringCorreos(getCuentasGoogle())));

            try {

                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                request = httpClient.execute(httpPost, responseHandler);

                if (request.equals("2")) {

                    tipo_conexion = 2;

                } else if (request.equals("1")) {

                    tipo_conexion = 1;

                } else {

                    tipo_conexion = 0;

                }

            } catch (UnsupportedEncodingException e) {

                e.printStackTrace();

            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                error = false;

            }

        }

    }

}


