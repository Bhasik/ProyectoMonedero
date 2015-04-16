package com.proyecto.alberto.monedero;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
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


public class alarmChecker extends Service {

    public static final int APP_ID_NOTIFICATION = 1;
    private static final String TAG = "MyService";
    public static boolean mIsServiceRunning = false;
    private NotificationManager mManager;
    private int movimientos = 0;
    private int gastosfijos = 0;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

                case 1:
                    Notificar();
                    break;
            }
        }
    };

    @Override
    public void onCreate() {

        Toast.makeText(this, "MyService Created", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onCreate");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        MyThread myThread = new MyThread();
        myThread.start();

        Log.d(TAG, "onStartCommand");
        mIsServiceRunning = true; // set service running status = true

        return START_STICKY;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "MyService Stopped", Toast.LENGTH_LONG).show();
        Log.d(TAG, "onDestroy");

        mIsServiceRunning = false; // make it false, as the service is already destroyed.

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void Notificar() {

        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intentNot = new Intent(this, Main_Activity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this.getBaseContext(), 0, intentNot, PendingIntent.FLAG_CANCEL_CURRENT);

        String[] notificaciones = new String[2];
        int numMessages = 0;

        if (movimientos != 0) {

            notificaciones[0] = "Tiene " + movimientos + " entradas diarias \n";

        }

        if (gastosfijos != 0) {

            notificaciones[1] = "Tiene " + gastosfijos + " entradas periodicas";

        }

        Notification.Builder notification = new Notification.Builder(this);

        notification
                .setSmallIcon(R.drawable.logo48)
                .setContentIntent(contentIntent)
                .setContentInfo("Taxi")
                .setNumber(++numMessages)
                .build();

        Notification.InboxStyle n = new Notification.InboxStyle(notification)
                .setBigContentTitle("Cita");

        for (int i = 0; i < notificaciones.length; i++) {

            n.addLine(notificaciones[i]);

        }

        n.build();

        mManager.notify(APP_ID_NOTIFICATION, notification.getNotification());

    }

    class MyThread extends Thread {

        private static final String INNER_TAG = "MyThread";
        int dia = ((60 * 1000) * 60) * 24;

        @Override
        public void run() {


            if (mIsServiceRunning) {

                String address = "http://89.248.107.8:3310/consultas/comprobacionAgenda.php";
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

                nameValuePairs = new ArrayList<>(2);

                nameValuePairs.add(new BasicNameValuePair("usuario", getSharedPreferences("app_taxi", MODE_PRIVATE).getString("usuario", null)));
                nameValuePairs.add(new BasicNameValuePair("password", getSharedPreferences("app_taxi", MODE_PRIVATE).getString("password", null)));

                try {

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                    response = httpClient.execute(httpPost);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();
                    resultado = convertStreamToString(is);


                    Log.e("Resultado", resultado + "\n");

                    if (!resultado.equals("")) {
                        JSONObject json;

                        try {

                            json = new JSONObject(resultado);
                            JSONArray jsonArray = json.optJSONArray("movimientosNotificacion");

                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject jsonArrayChild = jsonArray.getJSONObject(i);

                                int tipo = jsonArrayChild.optInt("tipo");

                                if (tipo == 0) {

                                    movimientos = jsonArrayChild.optInt("cantidad");

                                } else {

                                    gastosfijos = jsonArrayChild.optInt("cantidad");
                                }


                                if (!resultado.equals("")) {

                                    handler.sendEmptyMessage(1);

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

                }

            }


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
    }

}
