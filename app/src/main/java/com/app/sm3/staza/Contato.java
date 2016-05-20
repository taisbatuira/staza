package com.app.sm3.staza;

import java.io.Serializable;

/**
 * Created by taisbatuira on 02/05/16.
 */
public class Contato implements Serializable {
    private long id;
    private String nome;
    private String telefone;
    private String placa;
    //protocol usa implements enquanto class usa extends


    public long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getPlaca() {
        return placa;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setTelefone(String telefone){
        this.telefone=telefone;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    @Override
    public String toString() {
        return nome;
    }
}
