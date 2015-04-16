package com.proyecto.alberto.monedero.Gestiones;

import android.widget.ArrayAdapter;

import com.proyecto.alberto.monedero.Tablas.Concepto;
import com.proyecto.alberto.monedero.Tablas.Movimiento;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Metodos static para ordenar
 */
public class Ordenar {

    public static boolean ordenarMovimientoPorNombre(ArrayList<Movimiento> movimientos, boolean order_nombre_desc, ArrayAdapter adapter) {

        if (order_nombre_desc) {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {
                    return m1.getConcepto().getNombre().toLowerCase().compareTo(m2.getConcepto().getNombre().toLowerCase());
                }
            });

            adapter.notifyDataSetChanged();

            order_nombre_desc = false;

        } else {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {
                    return m2.getConcepto().getNombre().toLowerCase().compareTo(m1.getConcepto().getNombre().toLowerCase());
                }
            });

            adapter.notifyDataSetChanged();

            order_nombre_desc = true;
        }

        return order_nombre_desc;

    }

    public static boolean ordenarConceptoPorNombre(ArrayList<Concepto> conceptos, boolean order_nombre_desc, ArrayAdapter adapter) {

        if (order_nombre_desc) {

            Collections.sort(conceptos, new Comparator<Concepto>() {
                @Override
                public int compare(Concepto c1, Concepto c2) {
                    return c1.getNombre().toLowerCase().compareTo(c2.getNombre().toLowerCase());
                }
            });

            adapter.notifyDataSetChanged();

            order_nombre_desc = false;

        } else {

            Collections.sort(conceptos, new Comparator<Concepto>() {
                @Override
                public int compare(Concepto c1, Concepto c2) {
                    return c2.getNombre().toLowerCase().compareTo(c1.getNombre().toLowerCase());
                }
            });

            adapter.notifyDataSetChanged();

            order_nombre_desc = true;
        }

        return order_nombre_desc;

    }

    public static boolean ordenarPorImporte(ArrayList<Movimiento> movimientos, boolean order_import_desc, ArrayAdapter adapter) {

        if (order_import_desc) {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {
                    return Double.compare(m1.getCoste(), m2.getCoste());
                }
            });

            adapter.notifyDataSetChanged();

            order_import_desc = false;

        } else {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {
                    return Double.compare(m2.getCoste(), m1.getCoste());
                }
            });

            adapter.notifyDataSetChanged();

            order_import_desc = true;

        }

        return order_import_desc;

    }

    public static boolean ordenarPorDias(ArrayList<Movimiento> movimientos, boolean order_dias_desc, ArrayAdapter adapter) {

        if (order_dias_desc) {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    return Double.compare(m1.getConcepto().getDias(), m2.getConcepto().getDias());

                }
            });

            adapter.notifyDataSetChanged();

            order_dias_desc = false;

        } else {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    return Double.compare(m2.getConcepto().getDias(), m1.getConcepto().getDias());

                }
            });

            adapter.notifyDataSetChanged();

            order_dias_desc = true;

        }

        return order_dias_desc;

    }

    public static boolean ordenarPorImporteDia(ArrayList<Movimiento> movimientos, boolean order_importdiario_desc, ArrayAdapter adapter) {

        if (order_importdiario_desc) {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    return Double.compare((m1.getCoste() / m1.getConcepto().getDias()), (m2.getCoste() / m2.getConcepto().getDias()));

                }
            });

            adapter.notifyDataSetChanged();

            order_importdiario_desc = false;

        } else {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    return Double.compare((m2.getCoste() / m2.getConcepto().getDias()), (m1.getCoste() / m1.getConcepto().getDias()));

                }
            });

            adapter.notifyDataSetChanged();

            order_importdiario_desc = true;

        }

        return order_importdiario_desc;

    }

    public static boolean ordenarPorFecha(ArrayList<Movimiento> movimientos, boolean order_fecha_desc, ArrayAdapter adapter) {

        final SimpleDateFormat formato = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        if (order_fecha_desc) {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    try {

                        String f1 = m1.getFecha() + " " + m1.getHora();
                        Date d1 = formato.parse(f1);

                        String f2 = m2.getFecha() + " " + m2.getHora();
                        Date d2 = formato.parse(f2);

                        return d1.compareTo(d2);

                    } catch (ParseException e) {

                        e.printStackTrace();

                        return 0;
                    }

                }
            });

            adapter.notifyDataSetChanged();

            order_fecha_desc = false;

        } else {

            Collections.sort(movimientos, new Comparator<Movimiento>() {
                @Override
                public int compare(Movimiento m1, Movimiento m2) {

                    try {

                        String f1 = m1.getFecha() + " " + m1.getHora();
                        Date d1 = formato.parse(f1);

                        String f2 = m2.getFecha() + " " + m2.getHora();
                        Date d2 = formato.parse(f2);

                        return d2.compareTo(d1);

                    } catch (ParseException e) {

                        e.printStackTrace();

                        return 0;
                    }

                }
            });

            adapter.notifyDataSetChanged();

            order_fecha_desc = true;

        }

        return order_fecha_desc;

    }

    public static boolean ordenarPorTipo(ArrayList<Concepto> conceptos, boolean order_tipo_desc, ArrayAdapter adapter) {

        if (order_tipo_desc) {

            Collections.sort(conceptos, new Comparator<Concepto>() {
                @Override
                public int compare(Concepto c1, Concepto c2) {

                    String t1;
                    String t2;

                    if (c1.isPeriodico()) {
                        t1 = "P";
                    } else {
                        t1 = "D";
                    }

                    if (c2.isPeriodico()) {
                        t2 = "P";
                    } else {
                        t2 = "D";
                    }

                    return t1.compareTo(t2);
                }
            });

            adapter.notifyDataSetChanged();

            order_tipo_desc = false;

        } else {

            Collections.sort(conceptos, new Comparator<Concepto>() {
                @Override
                public int compare(Concepto c1, Concepto c2) {
                    String t1;
                    String t2;

                    if (c1.isPeriodico()) {
                        t1 = "P";
                    } else {
                        t1 = "D";
                    }

                    if (c2.isPeriodico()) {
                        t2 = "P";
                    } else {
                        t2 = "D";
                    }

                    return t2.compareTo(t1);
                }
            });

            adapter.notifyDataSetChanged();

            order_tipo_desc = true;
        }

        return order_tipo_desc;

    }

}
