package itson.mx.administraciondeusuarios;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class Navegador extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView txt_titulo, txt_estado, txt_modificar;
    int id;
    String descripcion, estado;
    Switch switch1;
    Button b_eliminar, boton_modificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navegador);

        Bundle b = getIntent().getExtras();

        if(b!=null){

            id = b.getInt("Id");
            descripcion = b.getString("Descripcion");
            estado = b.getString("Estado");
        }

        txt_titulo = (TextView) findViewById(R.id.txt_titulo);
        txt_estado = (TextView) findViewById(R.id.txt_estado);
        txt_modificar = (TextView) findViewById(R.id.txt_modificar);

        switch1 = (Switch) findViewById(R.id.switch1);

        b_eliminar = (Button) findViewById(R.id.b_eliminar);
        boton_modificar = (Button) findViewById(R.id.boton_modificar);

        if (estado.equals("Hecha")){
            switch1.setChecked(true);
        }else{
            switch1.setChecked(false);
        }

        txt_titulo.setText(descripcion);
        txt_estado.setText(estado);

        b_eliminar.setVisibility(View.INVISIBLE);

        txt_modificar.setVisibility(View.INVISIBLE);
        boton_modificar.setVisibility(View.INVISIBLE);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (switch1.isChecked()){

                    estado = "Hecha";
                    modificarEstado(id,"Hecha");
                    txt_estado.setText(estado);

                }else{

                    estado = "Pendiente";
                    modificarEstado(id,"Pendiente");
                    txt_estado.setText(estado);
                }
            }
        });

        b_eliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(id);
                onBackPressed();
            }
        });

        boton_modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modificar(id,txt_modificar.getText().toString());

                txt_titulo.setText(txt_modificar.getText().toString());
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Administrador de Tareas", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navegador, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();

        if (id == R.id.nav_camera) {

            fragmentManager.beginTransaction().replace(R.id.contenedor,new EditarDescripcion()).commit();
            b_eliminar.setVisibility(View.INVISIBLE);
            txt_modificar.setVisibility(View.VISIBLE);
            boton_modificar.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_gallery) {

            fragmentManager.beginTransaction().replace(R.id.contenedor,new EliminarTarea()).commit();
            b_eliminar.setVisibility(View.VISIBLE);
            txt_modificar.setVisibility(View.INVISIBLE);
            boton_modificar.setVisibility(View.INVISIBLE);

        } else if (id == R.id.nav_share) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "La tarea "+descripcion+ "se encuentra " +"*"+estado+"*");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);


        } else if (id == R.id.nav_send) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "La tarea "+descripcion+ "se encuentra " +"*"+estado+"*");
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void modificarEstado(int id, String estado){

        BaseHelper helper = new BaseHelper(this,"Demo",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "update tareas set estado='" +estado + "' where Id="+id;
        Toast.makeText(this,"La tarea ha sido establecida como "+estado,Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
        db.close();
    }

    private void eliminar(int id){

        BaseHelper helper = new BaseHelper(this,"Demo",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "delete from Tareas where Id="+id;
        Toast.makeText(this,"La tarea ha sido eliminada",Toast.LENGTH_SHORT).show();
        db.execSQL(sql);
        db.close();
    }

    private void modificar(int id, String descripcion){

        BaseHelper helper = new BaseHelper(this,"Demo",null,1);
        SQLiteDatabase db = helper.getWritableDatabase();

        String sql = "update tareas set Descripcion='" +descripcion + "' where Id="+id;
        Toast.makeText(this,"La tarea ha sido modificada",Toast.LENGTH_SHORT).show();

        db.execSQL(sql);
        db.close();
    }

}
