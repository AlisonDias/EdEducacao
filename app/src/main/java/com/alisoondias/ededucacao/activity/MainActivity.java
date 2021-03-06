package com.alisoondias.ededucacao.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;;
import androidx.fragment.app.Fragment;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.fragment.AlunoFragment;
import com.alisoondias.ededucacao.fragment.HomeFragment;
import com.alisoondias.ededucacao.fragment.PostagemFragment;
import com.alisoondias.ededucacao.fragment.TrabalhosFragment;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference usuarioLogadoRef;
    private BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializarComponentes();
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new AlunoFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectFragment = null;

                switch (item.getItemId()) {

                    case R.id.ic_aluno:
                        selectFragment = new AlunoFragment();
                        break;
                    case R.id.ic_trabalhos:
                        selectFragment = new TrabalhosFragment();
                        break;
                    case R.id.ic_feed:
                        selectFragment = new FeedFragment();
                        break;
                    case R.id.ic_postagem:
                        selectFragment = new PostagemFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectFragment).commit();

                return true;
            }
        });

    }


    public void inicializarComponentes(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        bottomNavigationView = findViewById(R.id.bnv);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menu_sair :
                deslogarUsuario();
                startActivity(new Intent(getApplicationContext(), Login.class));
                break;

            case R.id.menu_perfil:
                startActivity(new Intent(getApplicationContext(), EditarPerfilActivity.class));
                break;

            case R.id.menu_add_escola:
                startActivity(new Intent(getApplicationContext(), CadastrarEscola.class));
                break;
            case R.id.menu_add_Turma:
                startActivity(new Intent(getApplicationContext(), CadastrarTurma.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario(){
        try{
            auth.signOut();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}