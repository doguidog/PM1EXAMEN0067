package com.example.pm2examen0067.Clases;

import java.sql.Blob;

public class Contactos {

    private int id;
    private String codigoPais;
    private String nombre;
    private int telefono;
    private String nota;
    private Blob foto;

    public Contactos() {

    }

    public Contactos(int id,String codigoPais, String nombre, int telefono, String nota, Blob foto) {
        this.id = id;
        this.codigoPais = codigoPais;
        this.nombre = nombre;
        this.telefono = telefono;
        this.nota = nota;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Blob getFoto() {
        return foto;
    }

    public void setFoto(Blob foto) {
        this.foto = foto;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
