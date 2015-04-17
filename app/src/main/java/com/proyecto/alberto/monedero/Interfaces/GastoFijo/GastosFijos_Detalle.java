package com.proyecto.alberto.monedero.Interfaces.GastoFijo;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Concepto;
import com.proyecto.alberto.monedero.Tablas.Movimiento;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Fragment donde se muestra en detalle un gasto fijo
 */
public class GastosFijos_Detalle extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "GASTOSFIJOS_DETALLE";

    private View view;
    private FragmentActivity context;

    private int posicion = 0;

    private Concepto concepto;
    private ArrayList<Concepto> conceptos_list;
    private ArrayList<String> nombresconcepto_list;

    private EditText movimiento;
    private TextView dias;
    private Button guardar;
    private Spinner conceptos;

    private DatePickerDialog datePickerdialog;
    private DateFormat dateFormatter;

    private TextView fecha;

    public GastosFijos_Detalle() {

    }

    public static GastosFijos_Detalle newInstance(Bundle arguments) {

        GastosFijos_Detalle gfd = new GastosFijos_Detalle();

        if (arguments != null) {

            gfd.setArguments(arguments);

        }

        return gfd;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.gastfijo_detalle_fragment, container, false);
        this.context = getActivity();

        movimiento = (EditText) view.findViewById(R.id.importe_edittext);
        dias = (TextView) view.findViewById(R.id.numdias_textView);
        conceptos = (Spinner) view.findViewById(R.id.concepto_edittext);
        guardar = (Button) view.findViewById(R.id.save);
        fecha = (TextView) view.findViewById(R.id.fechainicial_edittext);

        view.findViewById(R.id.save).setOnClickListener(this);

        conceptos_list = new ArrayList();
        nombresconcepto_list = new ArrayList<>();

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {

            movimiento.setText(String.valueOf(this.getArguments().getDouble("movimiento_gastofijo")));
            dias.setText(String.valueOf(this.getArguments().getInt("dias")));
            fecha.setText(String.valueOf(this.getArguments().getString("fecha")));

            guardar.setText(getString(R.string.actualizar));

        } else {

            guardar.setText(getString(R.string.añadir));

        }

        setDateTimeField();

        new SubProcesosGestion(this, context, SubProcesosGestion.CONCEPTOS_BAJAR).execute();

    }

    public void configurar_adapter() {

        if (conceptos_list.size() == 0) {

            Alertas.ningunConcepto(context, this);

        }

        //Si se le pasa argumentos busca el de los argumentos, sino rellena y coge la primera posición
        if (getArguments() != null) {

            for (int i = 0; i < conceptos_list.size(); i++) {

                nombresconcepto_list.add(conceptos_list.get(i).getNombre());

                if (conceptos_list.get(i).getId() == this.getArguments().getInt("id_concepto")) {

                    posicion = i;

                }
            }

        } else {


            for (int i = 0; i < conceptos_list.size(); i++) {

                nombresconcepto_list.add(conceptos_list.get(i).getNombre());

            }

            posicion = 0;

        }

        ArrayAdapter spin_adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, nombresconcepto_list);
        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        conceptos.setAdapter(spin_adapter);

        conceptos.setSelection(posicion);

        conceptos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                seleccionarConcepto(position);
                dias.setText(String.valueOf(conceptos_list.get(position).getDias()));

                if (getArguments() == null) {

                    fechaPorDefecto();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

                seleccionarConcepto(posicion);

            }

        });


    }

    public ArrayList<Concepto> getConceptos_list() {
        return conceptos_list;
    }

    private void setDateTimeField() {

        view.findViewById(R.id.fechainicial_edittext).setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();

        datePickerdialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);

                dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                fecha.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save:

                if (movimiento.getText().toString().equals("") || dias.getText().toString().equals("")) {

                    Alertas.faltaRellenar(context);

                } else {

                    if (getArguments() != null) {

                        int id = getArguments().getInt("id");

                        double mov = Double.parseDouble(movimiento.getText().toString());

                        String f = fecha.getText().toString();

                        Movimiento movimiento = new Movimiento(id, concepto, (mov * (-1)), f, "00:00:00", 0.0);

                        new SubProcesosGestion(context, this, movimiento, SubProcesosGestion.MOVIMIENTOS_ACTUALIZAR).execute();

                    } else {

                        double mov = Double.parseDouble(movimiento.getText().toString());
                        String f = fecha.getText().toString();

                        Movimiento movimiento = new Movimiento(concepto, (mov * (-1)), f, "00:00:00", 0.0);

                        new SubProcesosGestion(context, this, movimiento, SubProcesosGestion.MOVIMIENTOS_INSERTAR).execute();

                    }

                }

                break;

            case R.id.fechainicial_edittext:

                datePickerdialog.show();

                break;

        }

    }

    public void resetearValores() {

        movimiento.setText("");
        conceptos.setSelection(0);
        fechaPorDefecto();

    }

    public void seleccionarConcepto(int posicion) {

        this.concepto = conceptos_list.get(posicion);

    }

    public void fechaPorDefecto() {

        Calendar newDate = Calendar.getInstance();
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        fecha.setText(dateFormatter.format(newDate.getTime()));

    }


}
