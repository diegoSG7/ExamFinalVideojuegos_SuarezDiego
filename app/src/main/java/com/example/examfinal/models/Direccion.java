package com.example.examfinal.models;

public class Direccion {
    public int id;
    public int contacto_id;
    public String direccion;
    public float latitud;
    public float longitud;

    public Direccion() {
    }

    public Direccion(int id, int contacto_id, String direccion, float latitud, float longitud) {
        this.id = id;
        this.contacto_id = contacto_id;
        this.direccion = direccion;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContacto_id() {
        return contacto_id;
    }

    public void setContacto_id(int contacto_id) {
        this.contacto_id = contacto_id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public float getLatitud() {
        return latitud;
    }

    public void setLatitud(float latitud) {
        this.latitud = latitud;
    }

    public float getLongitud() {
        return longitud;
    }

    public void setLongitud(float longitud) {
        this.longitud = longitud;
    }
}
