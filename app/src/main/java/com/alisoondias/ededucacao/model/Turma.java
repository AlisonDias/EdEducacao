package com.alisoondias.ededucacao.model;

import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;

public class Turma implements Serializable {

    private String id;
    private String nome;
    private Escola escola;

    public Turma() {
    }

    public boolean salvar(){

        DatabaseReference turmaRef = ConfiguracaoFirebase.getFirebase()
                .child("escola").child(escola.getId()).child("turma");
        String chaveTurma = turmaRef.push().getKey();
        setId( chaveTurma );
        turmaRef.child( getId()).setValue( this );

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

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }
}
