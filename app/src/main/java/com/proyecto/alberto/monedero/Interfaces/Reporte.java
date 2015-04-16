package com.proyecto.alberto.monedero.Interfaces;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Reporte extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "INFORME";

    private View view;
    private Activity context;

    private DatePickerDialog datePickerdialogFechaInicio;
    private DatePickerDialog datePickerdialogFechaFin;
    private DateFormat dateFormatter;
    private TextView dfecha;
    private TextView hfecha;
    private EditText kmsIniciales;
    private EditText kmsFinales;
    private EditText kmsRecorridos;
    private EditText numeroDias;

    private Button cInforme;
    private Button envInforme;
    private Button visuInforme;

    private String fecha_inicio;
    private String fecha_fin;
    private String kms_iniciales;
    private String kms_finales;
    private String kms_recorridos;
    private String numero_dias;


    private Date fechaI;
    private Date fechaF;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.informe_fragment, container, false);
        this.context = getActivity();

        dfecha = (TextView) view.findViewById(R.id.dfecha_edittext);
        hfecha = (TextView) view.findViewById(R.id.hfecha_edittext);
        kmsIniciales = (EditText) view.findViewById(R.id.kmsiniciales_edittext);
        kmsFinales = (EditText) view.findViewById(R.id.kmsfinales_edittext);

        cInforme = (Button) view.findViewById(R.id.cinforme);
        visuInforme = (Button) view.findViewById(R.id.visuInforme);
        envInforme = (Button) view.findViewById(R.id.envInforme);

        ((Main_Activity)context).getDrawerLayout().setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cInforme.setOnClickListener(this);
        visuInforme.setOnClickListener(this);
        envInforme.setOnClickListener(this);

        fechaPorDefecto();

        setDateTimeField();
    }


    /**
     *
     *
     *
     *
     */

    private void setDateTimeField() {

        view.findViewById(R.id.dfecha_edittext).setOnClickListener(this);
        view.findViewById(R.id.hfecha_edittext).setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerdialogFechaInicio = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                dfecha.setText(dateFormatter.format(newDate.getTime()));
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                fecha_inicio = dateFormatter.format(newDate.getTime());


            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datePickerdialogFechaFin = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);


                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                hfecha.setText(dateFormatter.format(newDate.getTime()));
                dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                fecha_fin = dateFormatter.format(newDate.getTime());


            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

    }


    /**
     *
     * Recogemos el valor del boton pulsado
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.dfecha_edittext:

                datePickerdialogFechaInicio.show();

                break;

            case R.id.hfecha_edittext:

                datePickerdialogFechaFin.show();

                break;

            case R.id.cinforme:

                SimpleDateFormat formatoDelTexto = new SimpleDateFormat("dd-MM-yyyy");

                try {

                    fechaI = formatoDelTexto.parse(dfecha.getText().toString());
                    fechaF = formatoDelTexto.parse(hfecha.getText().toString());
                    Log.v("Hora", fecha_inicio);


                } catch (ParseException e) {

                    e.printStackTrace();

                }

                kms_iniciales = kmsIniciales.getText().toString();
                kms_finales = kmsFinales.getText().toString();

                if (fecha_inicio == null || fecha_fin == null || fechaI.after(fechaF)) {

                    Alertas.errorFechasInforme(context);

                } else {

                    Toast.makeText(context, getString(R.string.informe), Toast.LENGTH_LONG).show();
                    new SubProcesosGestion(this, context, SubProcesosGestion.VISTA_DETALLADA, fecha_inicio, fecha_fin, kms_iniciales, kms_finales).execute();

                }

                break;

            case R.id.visuInforme:

                Alertas.abrirPdf(context);

                break;

            case R.id.envInforme:

                if (((Main_Activity) context).isComprado()) {

                    Alertas.enviarEmail(context, "informe");

                } else {

                    Alertas.necesitaComprar(context);

                }

                break;

        }
    }


    /**
     *
     * Coge la fecha que tengamos ahora en el sistema
     *
     */

    public void fechaPorDefecto() {

        Calendar newDate = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        hfecha.setText(dateFormatter.format(newDate.getTime()));

    }

}
