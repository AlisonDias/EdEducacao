package com.alisoondias.ededucacao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.model.Escola;

public class CadastrarEscola extends AppCompatActivity {

    private EditText editTextNomeEscola;
    private Button buttonCadastrarEscola;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_escola);


            inicializarCompontentes();
            buttonCadastrarEscola.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String nomeEscola = editTextNomeEscola.getText().toString();

                    if( !nomeEscola.isEmpty() ){

                        Escola escola = new Escola();
                        escola.setNome(nomeEscola);
                        escola.salvar();
                        finish();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));

                    }else{

                        Toast.makeText(CadastrarEscola.this,
                                "Preencha o nome da Escola!",
                                Toast.LENGTH_SHORT).show();


                    }
                }
            });

        }

    public void inicializarCompontentes(){

        editTextNomeEscola = findViewById(R.id.editTextNomeEscola);
        buttonCadastrarEscola = findViewById(R.id.buttonCadastrarEscola);

    }
}