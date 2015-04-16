package com.proyecto.alberto.monedero.Tablas;

public class Movimiento {

    private int id;
    private Concepto concepto;
    private double coste;
    private String fecha;
    private String hora;
    private double km;


    public Movimiento() {
    }

    public Movimiento(Concepto concepto, double coste) {

        this.concepto = concepto;
        this.coste = coste;

    }

    public Movimiento(Concepto concepto, double coste, String fecha, String hora, Double km) {

        this.concepto = concepto;
        this.coste = coste;
        this.fecha = fecha;
        this.hora = hora;
        this.km = km;

    }

    public Movimiento(int id, Concepto concepto, double coste, String fecha, String hora, Double km) {

        this.id = id;
        this.concepto = concepto;
        this.coste = coste;
        this.fecha = fecha;
        this.hora = hora;
        this.km = km;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Concepto getConcepto() {
        return concepto;
    }

    public void setConcepto(Concepto concepto) {
        this.concepto = concepto;
    }

    public double getMovimiento() {
        return coste;
    }

    public void setMovimiento(double coste) {
        this.coste = coste;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public double getCoste() {
        return coste;
    }

    public void setCoste(double coste) {
        this.coste = coste;
    }

    public double getKm() {
        return km;
    }

    public void setKm(double km) {
        this.km = km;
    }


}
