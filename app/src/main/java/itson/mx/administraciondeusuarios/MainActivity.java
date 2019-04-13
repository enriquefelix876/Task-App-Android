package itson.mx.administraciondeusuarios;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText txt_descripcion;
    Button btn_guardar, btn_lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        txt_descripcion = (EditText) findViewById(R.id.txt_descripcion);

        btn_guardar = (Button) findViewById(R.id.btn_guardar);
        btn_lista = (Button) findViewById(R.id.btn_lista);

        btn_guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                guardar(txt_descripcion.getText().toString());
            }
        });

        btn_lista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this, Listado.class));
            }
        });

    }

    private void guardar(String descripcion){

        BaseHelper helper = new BaseHelper(this,"Demo",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        try{

            ContentValues c = new ContentValues();
            c.put("DESCRIPCION",descripcion);
            c.put("ESTADO","Pendiente");
            db.insert("Tareas",null,c);
            db.close();
            txt_descripcion.setText(null);
            txt_descripcion.requestFocus();
            Toast.makeText(this,"La tarea ha sido agregada",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(this,"Error: "+e,Toast.LENGTH_SHORT).show();
        }

    }
}
