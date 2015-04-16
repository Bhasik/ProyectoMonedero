package com.proyecto.alberto.monedero.Tablas;

public class Movimiento_PDF {

    int id_concepto;
    int tipo;
    String nombre;
    int dias;
    double importe_periodo;
    int num_operaciones;
    double importe_dia;
    double importe_fijo;
    double por_km;

    public Movimiento_PDF(int id_concepto, int tipo, String nombre, int dias, double importe_periodo, int num_operaciones, double importe_dia, double importe_fijo, double por_km) {

        this.id_concepto = id_concepto;
        this.tipo = tipo;
        this.nombre = nombre;
        this.dias = dias;
        this.importe_periodo = importe_periodo;
        this.num_operaciones = num_operaciones;
        this.importe_dia = importe_dia;
        this.importe_fijo = importe_fijo;
        this.por_km = por_km;

    }

    public int getId_concepto() {
        return id_concepto;
    }

    public void setId_concepto(int id_concepto) {
        this.id_concepto = id_concepto;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }

    public double getImporte_periodo() {
        return importe_periodo;
    }

    public void setImporte_periodo(double importe_periodo) {
        this.importe_periodo = importe_periodo;
    }

    public int getNum_operaciones() {
        return num_operaciones;
    }

    public void setNum_operaciones(int num_operaciones) {
        this.num_operaciones = num_operaciones;
    }

    public double getImporte_dia() {
        return importe_dia;
    }

    public void setImporte_dia(double importe_dia) {
        this.importe_dia = importe_dia;
    }

    public double getImporte_fijo() {
        return importe_fijo;
    }

    public void setImporte_fijo(double importe_fijo) {
        this.importe_fijo = importe_fijo;
    }

    public double getPor_km() {
        return por_km;
    }

    public void setPor_km(double por_km) {
        this.por_km = por_km;
    }

    @Override
    public String toString() {
        return "Movimiento_PDF{" +
                "id_concepto=" + id_concepto +
                ", tipo=" + tipo +
                ", nombre='" + nombre + '\'' +
                ", dias=" + dias +
                ", importe_periodo=" + importe_periodo +
                ", num_operaciones=" + num_operaciones +
                ", importe_dia=" + importe_dia +
                '}';
    }
}
