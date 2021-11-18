package com.alisoondias.ededucacao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.model.Aluno;
import com.alisoondias.ededucacao.model.Avaliacao;
import com.alisoondias.ededucacao.model.Escola;
import com.alisoondias.ededucacao.model.Turma;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastrarAvaliacao extends AppCompatActivity {

    private EditText editTextData;
    private TextInputEditText texteInputEditTexteAvaliacao;
    private Spinner spinnerAluno;
    private Button buttonCadastrar;
    private List<Aluno> alunosOBJ = new ArrayList<>();
    private List<String> alunosString = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_avaliacao);

        inicializarCompontentes();

        ConfiguracaoFirebase.getFirebase().child("alunos").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot alunoSnapshot: snapshot.getChildren()) {

                    Aluno aluno = alunoSnapshot.getValue(Aluno.class);

                    String areaName = alunoSnapshot.child("nome").getValue(String.class);

                    //Log.i("teste", escola.getId());
                    alunosOBJ.add(aluno);
                    alunosString.add(areaName);

                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CadastrarAvaliacao.this, android.R.layout.simple_spinner_item, alunosString);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerAluno.setAdapter(areasAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        buttonCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nomeData = editTextData.getText().toString();
                String textoAvaliação = texteInputEditTexteAvaliacao.getText().toString();

                if( !nomeData.isEmpty() ){

                    int posicao = spinnerAluno.getSelectedItemPosition();
                    String itemSelecionado = alunosString.get(posicao);



                    Aluno alunoSelecionado = new Aluno();
                    alunoSelecionado.setNome(itemSelecionado);
                    alunoSelecionado.setId(alunosOBJ.get(posicao).getId());
                    Avaliacao avaliacao = new Avaliacao();
                    avaliacao.setData(nomeData);
                    avaliacao.setAluno(alunoSelecionado);
                    avaliacao.setAvaliacao(textoAvaliação);
                    avaliacao.salvarAvaliacao();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }else{

                    Toast.makeText(CadastrarAvaliacao.this,
                            "Preencha os dados da Avaliação",
                            Toast.LENGTH_SHORT).show();


                }

            }
        });


    }

    public void inicializarCompontentes(){

        editTextData = findViewById(R.id.editTextTextData);
        texteInputEditTexteAvaliacao = findViewById(R.id.texteInputEditTextAvaliacao);
        spinnerAluno = findViewById(R.id.spinner_Aluno);
        buttonCadastrar = findViewById(R.id.buttonCadastrarAvaliacao);


    }
}