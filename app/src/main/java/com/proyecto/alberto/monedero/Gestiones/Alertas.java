package com.proyecto.alberto.monedero.Gestiones;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;

import com.proyecto.alberto.monedero.Interfaces.Conceptos.Conceptos;
import com.proyecto.alberto.monedero.Interfaces.Conceptos.Conceptos_Adapter;
import com.proyecto.alberto.monedero.Interfaces.Conceptos.Conceptos_Detalle;
import com.proyecto.alberto.monedero.Interfaces.GastoFijo.GastosFijos_Detalle;
import com.proyecto.alberto.monedero.Interfaces.Movimientos.Movimientos_Detalle;
import com.proyecto.alberto.monedero.Main_Activity;
import com.proyecto.alberto.monedero.R;

import java.io.File;


public class Alertas {

    public static void errorConexion(Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.errorServidor))
                .setTitle(context.getString(R.string.tituloDialogError))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void elementoModificado(final Activity context, String nombre) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogModificar) + " " + nombre + ".")
                .setTitle(context.getString(R.string.actualizar))
                .setCancelable(false);

        builder.setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void faltaRellenar(Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.mensajeDialogErrorCampos))
                .setTitle(context.getString(R.string.tituloDialogError))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void ningunConcepto(final Activity context, final Fragment fragment) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.mensajeDialogSinConceptos))
                .setTitle(context.getString(R.string.tituloDialogAdvertencia))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.si), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                FragmentTransaction ft;

                switch (fragment.getClass().getSimpleName()) {
                    case "GastosFijos_Detalle":

                        ft = context.getFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                        ft.replace(R.id.container, new Conceptos(), Conceptos.TAG_FRAGMENT);
                        ft.addToBackStack(((GastosFijos_Detalle) fragment).TAG_FRAGMENT);
                        ft.commit();

                        break;

                    case "Movimientos_Detalle":

                        ft = context.getFragmentManager().beginTransaction();
                        ft.setCustomAnimations(R.animator.slide_go_in, R.animator.slide_go_out, R.animator.slide_back_in, R.animator.slide_back_out);
                        ft.replace(R.id.container, new Conceptos(), Conceptos.TAG_FRAGMENT);
                        ft.addToBackStack(((Movimientos_Detalle) fragment).TAG_FRAGMENT);
                        ft.commit();

                        break;


                }

            }
        });

        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void nuevoElemento(final Activity context, final Fragment fragment, String nombre) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogNuevoElemento) + " " + nombre + ".")
                .setTitle(context.getString(R.string.tituloDialogInsertar))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        builder.setNegativeButton(context.getString(R.string.añadirDialog), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                switch (fragment.getClass().getSimpleName()) {

                    case "Conceptos_Detalle":

                        ((Conceptos_Detalle) fragment).resetearValores();

                        break;

                    case "GastosFijos_Detalle":

                        ((GastosFijos_Detalle) fragment).resetearValores();

                        break;

                    case "Movimientos_Detalle":

                        ((Movimientos_Detalle) fragment).resetearValores();

                        break;

                }

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public static boolean haveNetworkConnection(Activity context) {

        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

    public static void errorInternet(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.errorInternet))
                .setTitle(context.getString(R.string.tituloDialogError))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void enviarEmail(final Activity context, final String tema) {

        final String extension;
        final String nombreU = ((Main_Activity) context).getUsuario().getNombre();

        if (tema.equals("backup")) {

            extension = "/Download/" + tema + "_" + nombreU + ".sql";

        } else {

            extension = "/Download/" + tema + ".pdf";

        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.mensaje_dialog_backUp))
                .setTitle(tema)
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));

                emailIntent.putExtra(Intent.EXTRA_EMAIL, ((Main_Activity) context).getUsuario().getCorreo());
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, tema);
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                emailIntent.putExtra(Intent.EXTRA_STREAM,
                        Uri.parse("file://" + Environment.getExternalStorageDirectory().getAbsolutePath() + extension));
                emailIntent.setType("message/rfc822");

                context.startActivityForResult(Intent.createChooser(emailIntent, context.getString(R.string.titulo_intent_email)), Main_Activity.ENVIADO);
            }
        });

        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                new SubProcesosGestion(null, context, SubProcesosGestion.BACKUP).execute();


            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void alertaConcepto(final Conceptos_Adapter.ComprobarMovimientos conceptos_adapter, final Activity context, final int id_usuario, int veces) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.borrar_movimientos_asociados) + " " + veces + " " + context.getString(R.string.borrar_movimientos_asociados_2))
                .setTitle(context.getString(R.string.tituloDialogAdvertencia));
        builder.setPositiveButton(context.getString(R.string.borrarDialgo), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                conceptos_adapter.quitarItem(id_usuario);

            }
        });

        builder.setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public static void abrirPdf(final Activity context) {

        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/informe.pdf");
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(Uri.fromFile(file), "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        context.startActivityForResult(Intent.createChooser(pdfIntent, context.getString(R.string.titulo_intent_email)), Main_Activity.ENVIADO);


    }

    public static void errorFechasInforme(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogErrorFechaInforme))
                .setTitle(context.getString(R.string.tituloDialogError))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public static void registroUsuario(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogRegistro))
                .setTitle(context.getString(R.string.tituloDialogRegistro))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog dialog = builder.create();
        dialog.show();


    }

    public static void salirAplicacion(final Activity context) {


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogSalirAplicacion))
                .setTitle(context.getString(R.string.tituloDialogAdvertencia))
                .setCancelable(false);
        builder.setPositiveButton(context.getString(R.string.aceptar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                context.getFragmentManager().popBackStack();

            }
        });

        builder.setNegativeButton(context.getString(R.string.cancelar), null);

        AlertDialog mostrar = builder.create();

        mostrar.show();

    }

    public static void necesitaComprar(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(context.getString(R.string.mensajeDialogNecesitaComprar))
                .setTitle(context.getString(R.string.tituloDialogAdvertencia))
                .setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog mostrar = builder.create();

        mostrar.show();

    }

    public static void limiteGratuito(final Activity context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage("Ya ha llegado al límite de operaciones gratuitas")
                .setTitle("Máximo de operaciones permitidas")
                .setPositiveButton(context.getString(R.string.aceptar), null);

        AlertDialog mostrar = builder.create();

        mostrar.show();

    }

}
