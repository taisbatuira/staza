package com.app.sm3.staza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //traz td do layout
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_novo_contato) {
            Intent irParaCadastro = new Intent(this, FormularioActivity.class);
            startActivity(irParaCadastro);
        }
        return super.onOptionsItemSelected(item);
    }

// mostrar lista na tela - chamar metodo listaContatos


    @Override
    protected void onResume() {
        super.onResume();
        ContatoDB db = new ContatoDB(this); //crio o objeto vazio
        List<Contato> lista = db.listaContatos();
        ListView listaTela = (ListView) findViewById(R.id.lista_contatos);
        ArrayAdapter<Contato> adapter = new ArrayAdapter<Contato>(this,android.R.layout.simple_list_item_1,lista);
        listaTela.setAdapter(adapter);
        listaTela.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
                Contato consultaContato = (Contato) adapter.getItemAtPosition(position);
                Intent consultaActivity = new Intent(MainActivity.this, ConsultaActivity.class);
                consultaActivity.putExtra("consultaContato",consultaContato);
                startActivity(consultaActivity);

            }
        });

    }

}
