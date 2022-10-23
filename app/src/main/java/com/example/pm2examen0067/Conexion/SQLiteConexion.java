package com.example.pm2examen0067.Conexion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.pm2examen0067.Clases.Transacciones;

public class SQLiteConexion extends SQLiteOpenHelper {


    public SQLiteConexion(Context context,
                          String dbname,
                          SQLiteDatabase.CursorFactory factory,
                          int version){

        super(context,dbname,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Transacciones.CreateTablePaises);
        db.execSQL(Transacciones.CreateTableContactos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

        db.execSQL(Transacciones.DropTablePaises);
        db.execSQL(Transacciones.DropTableContactos);
        onCreate(db);
    }


}
