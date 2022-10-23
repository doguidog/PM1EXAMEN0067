package com.example.pm2examen0067;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pm2examen0067.Clases.Contactos;
import com.example.pm2examen0067.Clases.Transacciones;
import com.example.pm2examen0067.Conexion.SQLiteConexion;

import java.util.ArrayList;

public class ActivityLista extends AppCompatActivity {

    int previousPosition = 1;
    int count=1;
    long previousMil=0;
    final Context context = this;

    Button btnmenu2,btncompartir,btneliminar,btnactualizar;
    EditText txtbuscar;
    Intent intent;
    ListView listacontactos;

    ArrayList<Contactos> listaContactos;
    ArrayList <String> arrayContactos;

    SQLiteConexion conexion;
    Contactos contact;

    static final int PERMISO_LLAMADA = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        casteo();
        intent = new Intent(getApplicationContext(),ActivityActualizar.class);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase,null,1);

        btnmenu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        obtenerlistaContactos();

        ArrayAdapter adp = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,arrayContactos);
        listacontactos.setAdapter(adp);

        txtbuscar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adp.getFilter().filter(charSequence);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        listacontactos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(previousPosition==i)
                {
                    count++;
                    if(count==2 && System.currentTimeMillis()-previousMil<=1000)
                    {
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                        alertDialogBuilder.setTitle("Acción");
                        alertDialogBuilder
                                .setMessage("¿Quiere llamar a "+contact.getNombre()+"?")
                                .setCancelable(false)
                                .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        try{
                                            permisoLlamada();
                                        }catch (Exception ex){
                                            ex.toString();
                                        }

                                        Toast.makeText(getApplicationContext(),"Llamada en proceso",
                                                Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        count=1;
                    }
                }
                else
                {
                    previousPosition=i;
                    count=1;
                    previousMil=System.currentTimeMillis();

                    contact = listaContactos.get(i);
                    setContactoSeleccionado();
                }
            }


        });

        btncompartir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enviarContacto();
            }
        });

        btneliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                alertDialogBuilder.setTitle("Eliminar Contacto");

                alertDialogBuilder
                        .setMessage("¿Está seguro de eliminar el contacto?")
                        .setCancelable(false)
                        .setPositiveButton("SI",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                deleteContact();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });


                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });

        btnactualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intencion = new Intent(getApplicationContext(),ActivityActualizar.class);
                startActivity(intent);
            }
        });
    }

    //Metodo de casteo
    private void casteo(){
        btnmenu2 = (Button) findViewById(R.id.btnmenu2);
        btncompartir = (Button) findViewById(R.id.btncompartir);
        btneliminar = (Button) findViewById(R.id.btneliminar);
        btnactualizar = (Button) findViewById(R.id.btnactualizar);
        txtbuscar = (EditText) findViewById(R.id.txtbuscar);
        listacontactos = (ListView) findViewById(R.id.listacontactos);
    }

    //Metodo seleccionado de informacion
    private void setContactoSeleccionado() {

        intent.putExtra("codigo", contact.getId()+"");
        intent.putExtra("nombre", contact.getNombre());
        intent.putExtra("telefono", contact.getTelefono()+"");
        intent.putExtra("codigopais", contact.getCodigoPais()+"");
        intent.putExtra("nota", contact.getNota());

    }

    //Metodo que da permiso a las llamadas
    private void permisoLlamada() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CALL_PHONE}, PERMISO_LLAMADA);
        }else{
            call();
        }
    }

    //Metodo que llama al contacto seleccionado
    private void call() {
        String numero = "+"+contact.getCodigoPais()+contact.getTelefono();
        Intent intent=new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+numero));
        startActivity(intent);
    }

    //Metodo que comparte los contactos
    private void enviarContacto(){
        String contactoEnviado = "El numero de "+contact.getNombre().toString()+
                " es +"+contact.getCodigoPais()+contact.getTelefono() ;
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, contactoEnviado);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }

    //Metodo que elimina un contacto de la base de datos
    private void deleteContact() {
        SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        SQLiteDatabase db = conexion.getWritableDatabase();
        int obtenerCodigo = contact.getId();

        db.delete(Transacciones.TbContactos,Transacciones.id +" = "+ obtenerCodigo, null);

        Toast.makeText(getApplicationContext(), "Registro #" + obtenerCodigo + " Eliminado."
                ,Toast.LENGTH_LONG).show();
        db.close();

        Intent intent = new Intent(this, ActivityLista.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    //Metodo obtener de la lista de contactos
    private void obtenerlistaContactos() {
        SQLiteDatabase db = conexion.getReadableDatabase();

        Contactos list_contact = null;

        listaContactos = new ArrayList<Contactos>();

        Cursor cursor = db.rawQuery("SELECT * FROM "+ Transacciones.TbContactos, null);

        while (cursor.moveToNext())
        {
            list_contact = new Contactos();
            list_contact.setId(cursor.getInt(0));
            list_contact.setNombre(cursor.getString(1));
            list_contact.setTelefono(cursor.getInt(2));
            list_contact.setNota(cursor.getString(3));
            list_contact.setCodigoPais(cursor.getString(5));
            listaContactos.add(list_contact);
        }
        cursor.close();

        llenarlista();

    }

    private void llenarlista()
    {
        arrayContactos = new ArrayList<String>();

        for (int i=0; i<listaContactos.size();i++)
        {
            arrayContactos.add(listaContactos.get(i).getNombre()+" | "+
                    listaContactos.get(i).getCodigoPais()+
                    listaContactos.get(i).getTelefono());

        }
    }


}