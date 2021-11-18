package com.alisoondias.ededucacao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.model.Aluno;
import com.alisoondias.ededucacao.model.Avaliacao;

public class AvaliacaoSelecionado extends AppCompatActivity {

    private TextView textViewNomeAluno,textViewData, textViewAvaliacaoTexto;

    private Avaliacao avaliacaoSelecionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avaliacao_selecionado);

        inicializarComponentes();

        if(getIntent().getExtras() != null){

            avaliacaoSelecionada = (Avaliacao) getIntent().getExtras().getSerializable("avaliacaoSelecionada");
            textViewNomeAluno.setText(avaliacaoSelecionada.getAluno().getNome());
            textViewData.setText(avaliacaoSelecionada.getData());
            textViewAvaliacaoTexto.setText(avaliacaoSelecionada.getAvaliacao());


        }
    }

    public void inicializarComponentes(){



        textViewNomeAluno = findViewById(R.id.textViewNomeAvaliacao);
        textViewData = findViewById(R.id.textViewData);
        textViewAvaliacaoTexto = findViewById(R.id.textViewAvaliacaoTexto);

    }

}