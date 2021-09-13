package com.alisoondias.ededucacao.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.fragment.ChatFragment;
import com.alisoondias.ededucacao.fragment.FeedFragment;
import com.alisoondias.ededucacao.fragment.HomeFragment;
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
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new HomeFragment()).commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                Fragment selectFragment = null;

                switch (item.getItemId()) {
                    case R.id.ic_home:
                        selectFragment = new HomeFragment();
                        break;
                    case R.id.ic_chat:
                        selectFragment = new ChatFragment();
                        break;
                    case R.id.ic_trabalhos:
                        selectFragment = new TrabalhosFragment();
                        break;
                    case R.id.ic_feed:
                        selectFragment = new FeedFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, selectFragment).commit();

                return true;
            }
        });

        //auth.signOut();
    }

    public void inicializarComponentes(){
        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        bottomNavigationView = findViewById(R.id.bnv);
    }
}