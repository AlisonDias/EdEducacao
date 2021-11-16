package com.alisoondias.ededucacao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.model.Aluno;

public class AlunoSelecionado extends AppCompatActivity {

    private TextView textViewNomeAluno,textViewFiliacao, textViewResponsaveis, textViewTelefone, textViewObservacoes;

    private Aluno alunoSelecionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aluno_selecionado);

        inicializarComponentes();

        if(getIntent().getExtras() != null){

            alunoSelecionado = (Aluno) getIntent().getExtras().getSerializable("alunoSelecionado");
            textViewNomeAluno.setText(alunoSelecionado.getNome());
            textViewFiliacao.setText(alunoSelecionado.getFiliacao());
            textViewResponsaveis.setText(alunoSelecionado.getResponsaveis());
            textViewTelefone.setText(alunoSelecionado.getNumeroCelular());
            textViewObservacoes.setText(alunoSelecionado.getObservacoes());

        }
    }

    public void inicializarComponentes(){

        textViewNomeAluno = findViewById(R.id.textViewNomeAluno);
        textViewFiliacao = findViewById(R.id.textViewFiliacao);
        textViewResponsaveis = findViewById(R.id.textViewResponsaveis);
        textViewTelefone = findViewById(R.id.textViewTelefone);
        textViewObservacoes = findViewById(R.id.textViewObservacoes);
    }
}