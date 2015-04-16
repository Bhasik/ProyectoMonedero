package com.proyecto.alberto.monedero.Interfaces.Conceptos;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.proyecto.alberto.monedero.Gestiones.Alertas;
import com.proyecto.alberto.monedero.Gestiones.SubProcesosGestion;
import com.proyecto.alberto.monedero.R;
import com.proyecto.alberto.monedero.Tablas.Concepto;


/**
 * Fragment donde se muestra en detalle un concepto
 */
public class Conceptos_Detalle extends Fragment implements View.OnClickListener {

    public static String TAG_FRAGMENT = "CONCEPTO_DETALLE";

    private View view;
    private Activity context;

    private EditText conceptoEdit;
    private RadioButton radioDiario;
    private RadioButton radioPeriodo;

    private TextView diasText;
    private EditText diasEdit;

    private TextView tipoText;
    private Button guardar;

    public Conceptos_Detalle() {

    }

    public static Conceptos_Detalle newInstance(Bundle arguments) {

        Conceptos_Detalle cd = new Conceptos_Detalle();

        if (arguments != null) {

            cd.setArguments(arguments);

        }

        return cd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.concepto_detalle_fragment, container, false);
        this.context = getActivity();

        //Inicialización objetos
        conceptoEdit = (EditText) view.findViewById(R.id.concepto_edittext);
        radioDiario = (RadioButton) view.findViewById(R.id.radioDiario);
        radioPeriodo = (RadioButton) view.findViewById(R.id.radioPeriodico);
        diasText = (TextView) view.findViewById(R.id.dias_textview);
        diasEdit = (EditText) view.findViewById(R.id.dias_edittext);
        tipoText = (TextView) view.findViewById(R.id.tipo_textview_boton);
        guardar = (Button) view.findViewById(R.id.save);

        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Inicialización listener
        radioDiario.setOnClickListener(this);
        radioPeriodo.setOnClickListener(this);
        guardar.setOnClickListener(this);

        //Si tenemos argumentos del fragment anterior
        if (getArguments() != null) {

            conceptoEdit.setText(String.valueOf(this.getArguments().getString("concepto_nom")));
            radioDiario.setEnabled(false);
            radioPeriodo.setEnabled(false);
            tipoText.setEnabled(false);


            if (getArguments().getBoolean("periodico")) {

                radioPeriodo.setChecked(true);
                diasText.setVisibility(View.VISIBLE);
                diasEdit.setVisibility(View.VISIBLE);
                diasEdit.setText(String.valueOf(this.getArguments().getInt("dias")));

            } else {

                radioDiario.setChecked(true);

            }
        }

        if (getArguments() != null) {

            guardar.setText(getString(R.string.actualizar));

        } else {

            guardar.setText(getString(R.string.añadir));

        }

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.save:

                //Si tenemos argumentos del fragment anterior

                if (conceptoEdit.getText().toString().equals("") || (radioPeriodo.isChecked() && diasEdit.getText().toString().equals(""))) {

                    Alertas.faltaRellenar(context);

                } else {

                    Concepto concepto;

                    if (getArguments() != null) {


                        if (radioDiario.isChecked()) {

                            concepto = new Concepto(this.getArguments().getInt("id_con"), conceptoEdit.getText().toString(), false, 1);


                        } else {

                            concepto = new Concepto(this.getArguments().getInt("id_con"), conceptoEdit.getText().toString(), true, Integer.parseInt(diasEdit.getText().toString()));

                        }

                        new SubProcesosGestion(context, concepto, SubProcesosGestion.CONCEPTOS_ACTUALIZAR).execute();

                        Alertas.elementoModificado(context, "Concepto");

                    } else {

                        if (radioDiario.isChecked()) {

                            concepto = new Concepto(conceptoEdit.getText().toString(), false, 1);

                        } else {

                            concepto = new Concepto(conceptoEdit.getText().toString(), true, Integer.parseInt(diasEdit.getText().toString()));

                        }

                        new SubProcesosGestion(context, concepto, SubProcesosGestion.CONCEPTOS_INSERTAR).execute();

                        Alertas.nuevoElemento(context, this, "Concepto");

                    }
                }

                break;

            case R.id.radioDiario:

                diasText.setVisibility(View.GONE);
                diasEdit.setVisibility(View.GONE);

                break;

            case R.id.radioPeriodico:

                diasText.setVisibility(View.VISIBLE);
                diasEdit.setVisibility(View.VISIBLE);

                break;
        }

    }

    public void resetearValores() {

        conceptoEdit.setText("");
        radioDiario.setChecked(true);
        diasEdit.setText("0");
        diasText.setVisibility(View.GONE);
        diasEdit.setVisibility(View.GONE);

    }

}
