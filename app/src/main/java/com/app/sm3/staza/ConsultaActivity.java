package com.app.sm3.staza;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ConsultaActivity extends AppCompatActivity {
    private Contato contatoConsultado;
    private EditText campoNome;
    private EditText campoTelefone;
    private EditText campoPlaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consulta);
        contatoConsultado = (Contato) getIntent().getSerializableExtra("consultaContato");
        String mostraNomeContato = contatoConsultado.getNome();
        String mostraTelefoneContato = contatoConsultado.getTelefone();
        String mostraPlacaContato = contatoConsultado.getPlaca();
        campoNome = (EditText) findViewById(R.id.mostra_nome_contato);
        campoTelefone = (EditText) findViewById(R.id.mostra_telefone_contato);
        campoPlaca = (EditText) findViewById(R.id.mostra_placa_contato);
        campoNome.setText(mostraNomeContato);
        campoTelefone.setText(mostraTelefoneContato);
        campoPlaca.setText(mostraPlacaContato);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_consulta,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.menu_consulta_excluir){
            AlertDialog confirmaExcluir = new AlertDialog.Builder(this)
                   .setTitle("Tem certeza que deseja excluir o contato?")
                   .setMessage("Este contato será apagado.")
                   .setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           ContatoDB contatoDB = new ContatoDB(ConsultaActivity.this);
                           contatoDB.excluir(contatoConsultado);
                           finish();
                       }
                   })
                   .setNegativeButton("Cancelar", null)
                   .create();
            confirmaExcluir.show();
        }
        else if(item.getItemId()==R.id.menu_consulta_editar) {
            campoNome.setEnabled(true);
            campoTelefone.setEnabled(true);
            campoPlaca.setEnabled(true);
            Button salvar = (Button) findViewById(R.id.consulta_salvar_editar);
            salvar.setVisibility(View.VISIBLE);
            salvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Contato contatoEditado = new Contato();
                    contatoEditado.setId(contatoConsultado.getId());
                    contatoEditado.setNome(campoNome.getText().toString());
                    contatoEditado.setTelefone(campoTelefone.getText().toString());
                    contatoEditado.setPlaca(campoPlaca.getText().toString());
                    ContatoDB db = new ContatoDB(ConsultaActivity.this);
                    db.editar(contatoEditado);
                    finish();
                }
            });
        }
            return super.onOptionsItemSelected(item);
    }
}

