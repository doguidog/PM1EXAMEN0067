package com.example.pm2examen0067;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pm2examen0067.Clases.Transacciones;
import com.example.pm2examen0067.Conexion.SQLiteConexion;

public class ActivityPaises extends AppCompatActivity {

    EditText txtpais,txtcodigonumerico;
    Button btnmenu1,btnguardarpais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paises);

        casteo();

        btnmenu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intencion);
            }
        });

        btnguardarpais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingresarPais();
            }
        });

    }

    private void casteo(){
        txtpais = (EditText) findViewById(R.id.txtpais);
        txtcodigonumerico = (EditText) findViewById(R.id.txtcodigonumerico);
        btnmenu1 = (Button) findViewById(R.id.btnmenu1);
        btnguardarpais = (Button) findViewById(R.id.btnguardarpais);
    }

    //Metodo que ingresa un pais en el spinner
    private void ingresarPais() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);
        SQLiteDatabase db = conexion.getWritableDatabase();

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.codigo,txtcodigonumerico.getText().toString());
        valores.put(Transacciones.nombre_pais,txtpais.getText().toString());

        Long resultado = db.insert(Transacciones.TbPaises,Transacciones.codigo,valores);

        Toast.makeText(getApplicationContext(),"Registro #"+resultado.toString()+" Ingresado",Toast.LENGTH_LONG).show();
        db.close();

        limpiarPantalla();

    }

    private void limpiarPantalla() {
        txtpais.setText("");
        txtcodigonumerico.setText("");
    }
}