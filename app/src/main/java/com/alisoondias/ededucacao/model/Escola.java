package com.alisoondias.ededucacao.model;

import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Escola implements Serializable {

    private String id;
    private String nome;

    public Escola() {
    }

    public boolean salvar(){

        DatabaseReference comentariosRef = ConfiguracaoFirebase.getFirebase()
                .child("escola");
        String chaveescola = comentariosRef.push().getKey();
        setId( chaveescola );
        comentariosRef.child( getId()).setValue( this );

        return true;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
