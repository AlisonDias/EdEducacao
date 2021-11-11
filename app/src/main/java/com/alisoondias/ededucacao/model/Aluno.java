package com.alisoondias.ededucacao.model;

import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Aluno implements Serializable {

    String id, nome, responsaveis, filiacao, numeroCelular, observacoes;
    Escola escola;
    Turma turma;

    public Aluno() {


        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference alunoRef = firebaseRef.child("alunos");
        String idAluno = alunoRef.push().getKey();
        setId( idAluno );
        setNome("null");
        setResponsaveis("null");
        setFiliacao("null");
        setNumeroCelular("null");
        setObservacoes("null");
        setEscola(null);
        setTurma(null);

    }

    public void salvarAluno(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference alunoRef = firebaseRef.child("alunos")
                .child( getId() );
        alunoRef.setValue(this);
    }

    public void atualizar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference alunoRef = firebaseRef.child("alunos").child( getId() );

        Map<String, Object> valoresAluno = converterParaMap();

        alunoRef.updateChildren(valoresAluno);
    }

    @Exclude
    public Map <String, Object> converterParaMap(){

        HashMap<String, Object> dragaMap = new HashMap<>();
        dragaMap.put("nome", getNome());
        dragaMap.put("responsaveis",getResponsaveis());
        dragaMap.put("filiacao", getFiliacao());
        dragaMap.put("numeroCelular", getNumeroCelular());
        dragaMap.put("observacoes", getObservacoes());
        dragaMap.put("escola", getEscola());
        dragaMap.put("turma", getTurma());
        return dragaMap;

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

    public String getResponsaveis() {
        return responsaveis;
    }

    public void setResponsaveis(String responsaveis) {
        this.responsaveis = responsaveis;
    }

    public String getFiliacao() {
        return filiacao;
    }

    public void setFiliacao(String filiacao) {
        this.filiacao = filiacao;
    }

    public String getNumeroCelular() {
        return numeroCelular;
    }

    public void setNumeroCelular(String numeroCelular) {
        this.numeroCelular = numeroCelular;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
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
}
