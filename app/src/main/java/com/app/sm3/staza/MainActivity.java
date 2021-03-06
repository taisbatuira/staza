package com.app.sm3.staza;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

    private ContatoDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); //traz td do layout
        this.db = new ContatoDB(this); //crio o objeto vazio
        verificaPermissaoChamada(Manifest.permission.READ_SMS);

        Intent intent = getIntent();
        if(intent != null){
            boolean chegouSMS = intent.getBooleanExtra("chegouSMS",false);
            final String telefoneDoSMS = intent.getStringExtra("telefoneDoSMS");
            if (chegouSMS){
                Contato contatoDoDB = this.db.buscaContatoComTelefone(telefoneDoSMS);
                if(contatoDoDB == null) {
                    //contato nao existe - mostra pop up se permite ou nao
                    AlertDialog popUpPermissao = new AlertDialog.Builder(this)
                            .setTitle("Permissão Solicitada")
                            .setMessage("O número "+telefoneDoSMS+" está solicitando sua localização. Você autoriza o rastreio?")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //cadastra usuario e dá permissao
                                    cadastraNumeroComPermissao(telefoneDoSMS,true);
                                    new GerenciadorSMS(MainActivity.this).iniciaGPS();
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //cadastra usuario e não dá permissao
                                    cadastraNumeroComPermissao(telefoneDoSMS,false);
                                }
                            })
                            .create();
                    popUpPermissao.show();
                }
            }
        }
    }

    private void cadastraNumeroComPermissao(String telefoneDoSMS, boolean permissao) {
        Intent irParaFormulario = new Intent(this,FormularioActivity.class);
        irParaFormulario.putExtra("telefone",telefoneDoSMS);
        irParaFormulario.putExtra("permissao", permissao);
        startActivity(irParaFormulario);
    }

    private void verificaPermissaoChamada(String permissao) {
        if (ContextCompat.checkSelfPermission(this, permissao)!= PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{permissao}, 12344);
        }
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
        List<Contato> lista = this.db.listaContatos();
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
