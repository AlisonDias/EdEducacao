package com.alisoondias.ededucacao.model;

import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Avaliacao implements Serializable {

    String id, data, avaliacao;
    Escola escola;
    Turma turma;
    Aluno aluno;

    public Avaliacao(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference avaliacaoRef = firebaseRef.child("avaliacao");
        String idAvaliacao = avaliacaoRef.push().getKey();
        setId( idAvaliacao );
        setData("null");
        setAvaliacao("null");
        setEscola(null);
        setTurma(null);
        setAluno(null);

    }

    public void salvarAvaliacao(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference avaliacaoRef = firebaseRef.child("avaliacao")
                .child( getId() );
        avaliacaoRef.setValue(this);
    }

    public void atualizar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference avaliacaoRef = firebaseRef.child("avaliacao").child( getId() );

        Map<String, Object> valoresAvaliacao = converterParaMap();

        avaliacaoRef.updateChildren(valoresAvaliacao);
    }

    @Exclude
    public Map <String, Object> converterParaMap(){

        HashMap<String, Object> dragaMap = new HashMap<>();
        dragaMap.put("data", getData());
        dragaMap.put("avaliacao", getAvaliacao());
        dragaMap.put("escola", getEscola());
        dragaMap.put("turma", getTurma());
        dragaMap.put("aluno", getAluno());
        return dragaMap;

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getAvaliacao() {
        return avaliacao;
    }

    public void setAvaliacao(String avaliacao) {
        this.avaliacao = avaliacao;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }
}
