package com.alisoondias.ededucacao.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference usuarioLogadoRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = ConfiguracaoFirebase.getFirebaseAutenticacao();
        auth.signOut();
    }
}