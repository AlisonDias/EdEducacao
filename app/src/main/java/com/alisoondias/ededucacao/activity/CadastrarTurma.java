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
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.model.Escola;
import com.alisoondias.ededucacao.model.Turma;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastrarTurma extends AppCompatActivity {

    private EditText editTextNomeTurma;
    private Button buttonCadastrarTurma;
    private Spinner spinnerEscolaTurma;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_turma);

        inicializarCompontentes();

        ConfiguracaoFirebase.getFirebase().child("escola").orderByChild("nome").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        final List<String> escolas = new ArrayList<String>();
                        for (DataSnapshot escolaSnapshot: snapshot.getChildren()) {
                            String areaName = escolaSnapshot.child("nome").getValue(String.class);
                            escolas.add(areaName);
                        }


                        Spinner areaSpinner = (Spinner) findViewById(R.id.spinnerEscola);
                        ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CadastrarTurma.this, android.R.layout.simple_spinner_item, escolas);
                        areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        areaSpinner.setAdapter(areasAdapter);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


        buttonCadastrarTurma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nomeTurma = editTextNomeTurma.getText().toString();


                if( !nomeTurma.isEmpty() ){

                    Turma turma = new Turma();
                    turma.setNome(nomeTurma);
                    turma.salvar();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));

                }else{

                    Toast.makeText(CadastrarTurma.this,
                            "Preencha o nome da Escola!",
                            Toast.LENGTH_SHORT).show();


                }
            }
        });
    }

    public void inicializarCompontentes(){

        editTextNomeTurma = findViewById(R.id.editTextNomeTurma);
        buttonCadastrarTurma = findViewById(R.id.buttonCadastrarTurma);
        spinnerEscolaTurma = (Spinner) findViewById(R.id.spinnerEscola);

    }
}