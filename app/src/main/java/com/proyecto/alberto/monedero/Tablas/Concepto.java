package com.proyecto.alberto.monedero.Tablas;

public class Concepto {

    private int id;
    private String nombre;
    private boolean periodico;
    private int dias;

    public Concepto() {

    }

    public Concepto(int id) {

        this.id = id;

    }

    public Concepto(int id, String nombre, boolean periodico, int dias) {

        this.id = id;
        this.nombre = nombre;
        this.periodico = periodico;
        this.dias = dias;

    }

    public Concepto(String nombre, boolean periodico, int dias) {

        this.nombre = nombre;
        this.periodico = periodico;
        this.dias = dias;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isPeriodico() {
        return periodico;
    }

    public void setPeriodico(boolean periodico) {
        this.periodico = periodico;
    }

    public int getDias() {
        return dias;
    }

    public void setDias(int dias) {
        this.dias = dias;
    }
}
