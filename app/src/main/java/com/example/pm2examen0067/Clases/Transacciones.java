package com.example.pm2examen0067.Clases;

public class Transacciones {

    public static final String NameDatabase = "PM01DB";


    public static final String id = "id";
    public static final String foto = "foto";
    public static final String pais = "pais";
    public static final String nombre = "nombre";
    public static final String telefono = "telefono";
    public static final String nota = "nota";

    public static String TbPaises = "paises";
    public static final String codigo ="codigo";
    public static final String nombre_pais="pais";

    public static final String TbContactos = "contactos";

    public static final String CreateTablePaises = "CREATE TABLE " + TbPaises + "(codigo INTEGER PRIMARY KEY,"+"pais TEXT )";
    public static final String DropTablePaises = "DROP TABLE " + TbPaises;

    public static final String CreateTableContactos = "CREATE TABLE " + TbContactos +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "nombre TEXT, telefono INTEGER, nota TEXT, foto BLOB, pais TEXT)";

    public static final String DropTableContactos = "DROP TABLE IF EXIST" + TbContactos;
}
