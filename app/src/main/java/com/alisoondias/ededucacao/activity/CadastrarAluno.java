package com.alisoondias.ededucacao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.model.Aluno;
import com.alisoondias.ededucacao.model.Escola;

public class CadastrarAluno extends AppCompatActivity {

    private EditText editTextNome;
    private EditText editTextFiliaxao;
    private EditText editTextResposaveis;
    private EditText editTextTelefone;
    private EditText editTextObeservacoes;

    private Button buttonCadastrarAluno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_aluno);

        inicializarComponentes();

        buttonCadastrarAluno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editTextNome.getText().toString();
                String filiacao = editTextFiliaxao.getText().toString();
                String resposaveis = editTextResposaveis.getText().toString();
                String telefone = editTextTelefone.getText().toString();
                String observacoes = editTextObeservacoes.getText().toString();

                if( !nome.isEmpty() ){
                    if( !filiacao.isEmpty() ){
                        if( !resposaveis.isEmpty() ){
                            if( !telefone.isEmpty() ){
                                if( !observacoes.isEmpty() ){

                                Aluno aluno = new Aluno();
                                aluno.setNome(nome);
                                aluno.setFiliacao(filiacao);
                                aluno.setResponsaveis(resposaveis);
                                aluno.setNumeroCelular(telefone);
                                aluno.setObservacoes(observacoes);

                                aluno.salvarAluno();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));

                                }else{


                                    Toast.makeText(CadastrarAluno.this,
                                            "Preencha as observações!",
                                            Toast.LENGTH_SHORT).show();


                                }

                            }else{


                                Toast.makeText(CadastrarAluno.this,
                                        "Preencha os numero de telefone!",
                                        Toast.LENGTH_SHORT).show();


                            }

                        }else{


                            Toast.makeText(CadastrarAluno.this,
                                    "Preencha os responsaveis autorizados a retirar a Criança!",
                                    Toast.LENGTH_SHORT).show();


                        }
                    }else{

                        Toast.makeText(CadastrarAluno.this,
                                "Preencha a Filiação!",
                                Toast.LENGTH_SHORT).show();


                    }
                }else{

                    Toast.makeText(CadastrarAluno.this,
                            "Preencha o nome do Aluno!",
                            Toast.LENGTH_SHORT).show();


                }
            }
        });

    }

     public void inicializarComponentes(){

        editTextNome = findViewById(R.id.editTextNome);
        editTextFiliaxao = findViewById(R.id.editTextFiliaxao);
        editTextResposaveis = findViewById(R.id.editTextResposaveis);
        editTextTelefone = findViewById(R.id.editTextTelefone);
        editTextObeservacoes = findViewById(R.id.editTextObeservacoes);
        buttonCadastrarAluno = findViewById(R.id.buttonCadastrarAluno);


    }
}