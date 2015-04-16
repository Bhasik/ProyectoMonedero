package com.proyecto.alberto.monedero.Tablas;

public class Usuario {

    private String nombre;
    private String password;
    private String correo;

    public Usuario() {

    }

    public Usuario(String nombre, String password) {

        this.nombre = nombre;
        this.password = password;
    }

    public Usuario(String nombre, String password, String correo) {

        this.nombre = nombre;
        this.password = password;
        this.correo = correo;

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }
}
