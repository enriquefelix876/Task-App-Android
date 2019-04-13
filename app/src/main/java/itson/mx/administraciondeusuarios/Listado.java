package itson.mx.administraciondeusuarios;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Listado extends AppCompatActivity {

    ListView l_listado;
    ArrayList<String> listado;

    @Override
    protected void onPostResume() {
        super.onPostResume();
        cargarListado();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        l_listado = (ListView) findViewById(R.id.l_listado);
        cargarListado();

        l_listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(Listado.this,listado.get(position),Toast.LENGTH_SHORT).show();
                int clave = Integer.parseInt(listado.get(position).split(" ")[0]);

                String[]partes = listado.get(position).split(" ");

                String descripcion = "";

                for (int p = 1; p < partes.length-1; p++){

                    descripcion = descripcion + partes[p] + " ";
                }

                String estado = listado.get(position).split(" ")[partes.length-1];

                Intent intent = new Intent(Listado.this,Navegador.class);

                intent.putExtra("Id",clave);
                intent.putExtra("Descripcion",descripcion);
                intent.putExtra("Estado",estado);
                startActivity(intent);
            }
        });

    }

    private void cargarListado(){

        listado = ListaTareas();
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, listado);
        l_listado.setAdapter(adapter);

    }

    private ArrayList<String>ListaTareas(){
        ArrayList<String>datos = new ArrayList<String>();
        BaseHelper helper = new BaseHelper(this,"Demo",null,1);
        SQLiteDatabase db = helper.getReadableDatabase();
        String sql = "SELECT ID,DESCRIPCION,ESTADO FROM TAREAS";
        Cursor c = db.rawQuery(sql,null);

        if (c.moveToFirst()){

            do {

                String linea = c.getInt(0) + " " +c.getString(1) + " " + c.getString(2);
                datos.add(linea);
            }while (c.moveToNext());
        }
        db.close();
        return datos;
    }

}
