package com.alisoondias.ededucacao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.model.Escola;
import com.alisoondias.ededucacao.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CadastrarUsuario extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private CheckBox checkBoxAdm;
    private Button buttonCadastrarUsuario;
    private ProgressBar progressBarCadastrarUsuario;
    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private FirebaseAuth verificaAutenticacao;

    private Spinner spinnerEscolaUsuario;
    private List<String> escolasString = new ArrayList<String>();
    private List<Escola> escolasOBJ = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        inicializarComponentes();

        progressBarCadastrarUsuario.setVisibility(View.GONE);

        ConfiguracaoFirebase.getFirebase().child("escola").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot escolaSnapshot: snapshot.getChildren()) {

                    Escola escola = escolaSnapshot.getValue(Escola.class);

                    String areaName = escolaSnapshot.child("nome").getValue(String.class);
                    escolasString.add(areaName);

                    //Log.i("teste", escola.getId());
                    escolasOBJ.add(escola);

                }
                Log.i("teste", escolasString.toString());



                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(CadastrarUsuario.this, android.R.layout.simple_spinner_item, escolasString);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerEscolaUsuario.setAdapter(areasAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        buttonCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBarCadastrarUsuario.setVisibility(View.VISIBLE);

                String textoEmail = editTextEmail.getText().toString();
                String textoSenha = editTextSenha.getText().toString();

                if(!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {

                        int posicao = spinnerEscolaUsuario.getSelectedItemPosition();
                        String itemSelecionado = escolasString.get(posicao);

                        Escola escola = new Escola();
                        escola.setNome(itemSelecionado);
                        escola.setId(escolasOBJ.get(posicao).getId());

                        usuario = new Usuario();
                        usuario.setEmail(textoEmail);
                        usuario.setSenha(textoSenha);
                        usuario.setEscola(escola);

                        cadastrarUsuario();

                    }else{
                        Toast.makeText(CadastrarUsuario.this, "Preencha a Senha!", Toast.LENGTH_SHORT).show();
                        progressBarCadastrarUsuario.setVisibility(View.GONE);
                    }
                }else{
                    Toast.makeText(CadastrarUsuario.this, "Preencha o Email!", Toast.LENGTH_SHORT).show();
                    progressBarCadastrarUsuario.setVisibility(View.GONE);
                }
            }
        });
    }

    public void cadastrarUsuario(){

        progressBarCadastrarUsuario.setVisibility(View.VISIBLE);

        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful()){


                    try {

                        progressBarCadastrarUsuario.setVisibility(View.GONE);

                        //Salvar Dados no Firebase

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);

                        usuario.salvar();

                        Toast.makeText(CadastrarUsuario.this, "Sucesso ao se cadastrar!", Toast.LENGTH_SHORT).show();
                        if(verificaAutenticacao == null){
                            startActivity( new Intent(getApplicationContext(), MainActivity.class));
                        }else {
                            startActivity( new Intent(getApplicationContext(), MainActivity.class));
                        }

                        finish();

                        //


                    }catch (Exception e){
                        e.printStackTrace();
                    }


                }else{

                    progressBarCadastrarUsuario.setVisibility(View.GONE);

                    String excecao = "";
                    try {
                        throw task.getException();
                    }catch ( FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch ( FirebaseAuthInvalidCredentialsException e){
                        excecao= "Por favor, digite um e-mail válido";
                    }catch ( FirebaseAuthUserCollisionException e){
                        excecao = "Este conta já foi cadastrada";
                    }catch (Exception e){
                        excecao = "Erro ao cadastrar usuário: "  + e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(CadastrarUsuario.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();

                }

            }
        });

    }

    public void inicializarComponentes(){

        editTextEmail = findViewById(R.id.editTextEmailCadastrar);
        editTextSenha = findViewById(R.id.editTextSenhaCadastrar);
        buttonCadastrarUsuario = findViewById(R.id.buttonCadastrarUsuario);
        progressBarCadastrarUsuario = findViewById(R.id.progressBarCadastrarUsuario);

        verificaAutenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        if(verificaAutenticacao.getCurrentUser() == null){
            //checkBoxAdm.setVisibility( View.GONE );
        }

        spinnerEscolaUsuario = (Spinner) findViewById(R.id.spinner_Escola_Usuario);
    }
}

