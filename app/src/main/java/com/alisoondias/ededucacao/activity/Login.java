package com.alisoondias.ededucacao.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.model.Usuario;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

import java.util.Arrays;

public class Login extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextSenha;
    private Button buttonEntrar;
    private Usuario usuario;
    private ProgressBar progressBarLogin;
    private FirebaseAuth autenticacao;
    private DatabaseReference usuarioLogadoRef;
    private AlertDialog alerta;
    private TextView textViewCadastrar;
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        verificarUsuarioLogado();
        inicializarComponentes();

        progressBarLogin.setVisibility( View.GONE );
        buttonEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String textoEmail = editTextEmail.getText().toString();
                String textosenha = editTextSenha.getText().toString();

                if( !textoEmail.isEmpty() ){
                    if( !textosenha.isEmpty() ){

                        usuario = new Usuario();
                        usuario.setEmail( textoEmail );
                        usuario.setSenha( textosenha );
                        validarLogin( usuario );

                    }else{
                        Toast.makeText(Login.this,
                                "Preencha a senha!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Login.this,
                            "Preencha o e-mail!",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });

        textViewCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), CadastrarUsuario.class));

            }
        });
    }

    public void verificarUsuarioLogado(){


        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();



        if( autenticacao.getCurrentUser() != null ){

            //criaAlerta();

            verificaSeAdm();
            //startActivity(new Intent(getApplicationContext(), MainActivity.class));
            //finish();
        }
    }

    public void validarLogin(Usuario usuario ){

        progressBarLogin.setVisibility( View.VISIBLE );
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),
                usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if ( task.isSuccessful() ){
                    progressBarLogin.setVisibility( View.GONE );
                    verificaSeAdm();
                    //startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    //finish();
                }else {
                    Toast.makeText(Login.this,
                            "Erro ao fazer login",
                            Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility( View.GONE );
                }

            }
        });

    }


    public void inicializarComponentes(){

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextSenha = findViewById(R.id.editTextSenha);
        progressBarLogin = findViewById(R.id.progressBarLogin);
        buttonEntrar = findViewById(R.id.buttonEntrar);
        textViewCadastrar = findViewById(R.id.textViewCadastrar);

        editTextEmail.requestFocus();

    }

    public void verificaSeAdm(){

        criaAlerta();

        TredVerificaAdm tredVerificaAdm = new TredVerificaAdm();
        tredVerificaAdm.run();



    }

    public void criaAlerta(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Carregando");
        builder.setMessage("Realizando Verificação");
        builder.setCancelable(false);
        alerta = builder.create();
        alerta.show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = autenticacao.getCurrentUser();

    }

    class TredVerificaAdm extends Thread{

        @Override
        public void run() {
            super.run();

            usuarioLogadoRef = ConfiguracaoFirebase.getFirebase().child("usuarios").child(autenticacao.getUid());

            usuarioLogadoRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(dataSnapshot.exists()){

                        Usuario usuarioLogado= dataSnapshot.getValue(Usuario.class);


                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            alerta.dismiss();
                            finish();


                        }

                }


                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

}
