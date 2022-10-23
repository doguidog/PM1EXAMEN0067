package com.example.pm2examen0067;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.pm2examen0067.Clases.Paises;
import com.example.pm2examen0067.Clases.Transacciones;
import com.example.pm2examen0067.Conexion.SQLiteConexion;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText txtnombre,txttelefono,txtnota;
    Spinner spinnerpaises;
    Button btntomarfotos,btnselectfotos,btncontactossalvados,btnsalvarcontactos;
    ImageView imageView;
    FloatingActionButton btnmaspaises;

    static final int PETICION_CAMARA = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Bitmap imagen;
    int codigoPaisSeleccionado;

    ArrayList<Paises> lista;
    ArrayList<String> lista_paises;
    SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        casteo();

        btntomarfotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tomarPermisos();
            }
        });

        btnselectfotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                galeria();
            }
        });

        btnmaspaises.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(), ActivityPaises.class);
                startActivity(intencion);
            }
        });

        listaPaises();

        ArrayAdapter<CharSequence> adp = new ArrayAdapter(this, android.R.layout.simple_spinner_item,lista_paises);
        spinnerpaises.setAdapter(adp);

        spinnerpaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String cadena = adapterView.getSelectedItem().toString();

                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btncontactossalvados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intencion = new Intent(getApplicationContext(), ActivityLista.class);
                startActivity(intencion);
            }
        });

        btnsalvarcontactos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    validarDatos();
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "Tomese una foto o seleccione una de galería.",Toast.LENGTH_LONG).show();
                }

            }
        });




    }

    //Metodo de casteo
    private void casteo(){
        txtnombre = (EditText) findViewById(R.id.txtnombre);
        txttelefono = (EditText) findViewById(R.id.txttelefono);
        txtnota = (EditText) findViewById(R.id.txtnota);
        spinnerpaises = (Spinner) findViewById(R.id.spinnerpaises);
        btntomarfotos = (Button) findViewById(R.id.btntomarfotos);
        btnselectfotos = (Button) findViewById(R.id.btnselecfotos);
        btncontactossalvados = (Button) findViewById(R.id.btncontactossalvados);
        btnsalvarcontactos = (Button) findViewById(R.id.btnsalvarcontactos);
        imageView = (ImageView) findViewById(R.id.imageView);
        btnmaspaises = (FloatingActionButton) findViewById(R.id.btnmaspaises);
    }

    //Metodo que valida el ingreso de datos
    private void validarDatos() {
        if (lista_paises.size() == 0){
            Toast.makeText(getApplicationContext(), "Debe de ingresar un Pais" ,Toast.LENGTH_LONG).show();
        }else  if (txtnombre.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if (txttelefono.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono" ,Toast.LENGTH_LONG).show();
        }else if (txtnota.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir una nota" ,Toast.LENGTH_LONG).show();
        }else{
            guardarContacto(imagen);
        }
    }

    //Metodo que pide permisos para tomar fotos
    private void tomarPermisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_CAMARA);
        }else{
            tomarFoto();
        }
    }

    //Metodo que toma fotos
    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    //Metodo que abre la galeria de mi telefono
    private void galeria() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Escoja la app"),10);

    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }

    @Override
    protected void onActivityResult(int requescode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requescode, resultCode, data);

        if(requescode == TAKE_PIC_REQUEST && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imagen);
        }else if (resultCode == RESULT_OK){
            Uri imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    //Metodo que guarda los contactos

    private void guardarContacto(Bitmap bitmap) {
        db = conexion.getWritableDatabase();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] ArrayFoto  = stream.toByteArray();

        ContentValues valores = new ContentValues();

        valores.put(String.valueOf(Transacciones.foto),ArrayFoto);
        valores.put(Transacciones.pais, codigoPaisSeleccionado);
        valores.put(Transacciones.nombre, txtnombre.getText().toString());
        valores.put(Transacciones.telefono, txttelefono.getText().toString());
        valores.put(Transacciones.nota, txtnota.getText().toString());


        Long resultado = db.insert(Transacciones.TbContactos, Transacciones.id, valores);

        Toast.makeText(getApplicationContext(), "Registro #" + resultado.toString() + "Ingresado"
                ,Toast.LENGTH_LONG).show();

        db.close();


        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();
    }

    private void listaPaises() {
        Paises pais = null;
        lista = new ArrayList<Paises>();
        db = conexion.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.TbPaises,null);

        while (cursor.moveToNext()) {
            pais = new Paises();
            pais.setCodigo(cursor.getString(0));
            pais.setNombrePais(cursor.getString(1));
            lista.add(pais);
        }
        cursor.close();
        fillCombo();
    }

    private void fillCombo() {
        lista_paises = new ArrayList<String>();
        for (int i=0; i < lista.size();i++) {
            lista_paises.add(lista.get(i).getNombrePais()+" ( "+lista.get(i).getCodigo()+" )");
        }
    }





}