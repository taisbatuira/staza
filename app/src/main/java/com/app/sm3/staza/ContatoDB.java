package com.app.sm3.staza;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by taisbatuira on 02/05/16.
 */
public class ContatoDB extends SQLiteOpenHelper{

    public ContatoDB(Context context) {
        super(context, "ContatoDB", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql  = "Create table TabelaContato(id integer primary key, camponome varchar, campotelefone char, campoplaca char)";
        db.execSQL(sql);
    }

    private ContentValues converteContatoParaContentValues (Contato contato){
        ContentValues values = new ContentValues();
        values.put("camponome",contato.getNome());
        values.put("campotelefone",contato.getTelefone());
        values.put("campoplaca",contato.getPlaca());
        return values;
    }

    public void insert(Contato contatoDigitado) {
        ContentValues values = converteContatoParaContentValues(contatoDigitado);
        getWritableDatabase().insertOrThrow("TabelaContato", null, values);
    }

    public List<Contato> listaContatos(){
        ArrayList<Contato> lista = new ArrayList<Contato>();
        String select = "select * from TabelaContato order by camponome asc";
        Cursor cursor = getReadableDatabase().rawQuery(select,null); //readable lê os dados e o raw monta a query a partir de uma string
        while (cursor.moveToNext()){
            Contato contato = new Contato();
            Long id = cursor.getLong(cursor.getColumnIndex("id"));
            contato.setId(id);
            String nomeContato = cursor.getString(cursor.getColumnIndex("camponome"));
            contato.setNome(nomeContato);
            String telefoneContato = cursor.getString(cursor.getColumnIndex("campotelefone"));
            contato.setTelefone(telefoneContato);
            String placaContato = cursor.getString(cursor.getColumnIndex("campoplaca"));
            contato.setPlaca(placaContato);
            lista.add(contato);
        }
        cursor.close();
        return lista;
    }

    public void excluir (Contato contatoSelecionado){
        Long idDoContato = contatoSelecionado.getId();
        getWritableDatabase().delete("TabelaContato","id=?",new String[]{idDoContato.toString()});
    }

    public void editar (Contato contatoParaAlterar){
        Long idDoContato = contatoParaAlterar.getId();
        ContentValues values = converteContatoParaContentValues(contatoParaAlterar);
        values.put("id",idDoContato);
        getWritableDatabase().update("TabelaContato",values,"id=?",new String[]{idDoContato.toString()});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
