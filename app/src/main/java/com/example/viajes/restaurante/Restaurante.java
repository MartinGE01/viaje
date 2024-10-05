package com.example.viajes.restaurante;

public class Restaurante {
    private int imagen;
    private String nombre;
    private String ciudad;
    private double latitud;
    private double longitud;

    public Restaurante(int imagen, String nombre, String ciudad, double latitud, double longitud) {
        this.imagen = imagen;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getImagen() {
        return imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public double getLatitud() {
        return latitud;
    }

    public double getLongitud() {
        return longitud;
    }
}
