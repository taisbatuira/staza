package com.app.sm3.staza;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class FormularioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_formulario);

        Button salvar = (Button) findViewById(R.id.formulario_salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Contato contato = new Contato();
                EditText campoNome = (EditText) findViewById(R.id.formulario_camponome); //identifico o campo fisico e atribuo para campoNome
                EditText campoTelefone = (EditText) findViewById(R.id.formulario_campotelefone);
                EditText campoPlaca = (EditText) findViewById(R.id.formulario_campoplaca);
                String nome = campoNome.getText().toString(); //recupero o texto digitado no campo e atribuo para nome
                String telefone = campoTelefone.getText().toString();
                String placa = campoPlaca.getText().toString();
                contato.setNome(nome); // uso o método setter para atribuir o valor da variável nome para o nome do objeto contato
                contato.setTelefone(telefone);
                contato.setPlaca(placa);
                ContatoDB contatoDB = new ContatoDB(FormularioActivity.this); // instancia a classe ContatoBD e cria a variável contatoDB
                contatoDB.insert(contato); //chamo o método insert através da variável contatoDB e armazeno os valores da variável contato no B.D.
                finish();
            }
        });
    }


}
