package com.alisoondias.ededucacao.fragment;


import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.adapter.AdapterFeed;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.helper.UsuarioFirebase;
import com.alisoondias.ededucacao.model.Feed;
import com.alisoondias.ededucacao.model.Postagem;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class FeedFragment extends Fragment {

    private RecyclerView recyclerFeed;
    private AdapterFeed adapterFeed;
    private List<Postagem> listaFeed = new ArrayList<>();
    private ValueEventListener valueEventListenerFeed;
    private DatabaseReference feedRef;
    private String idUsuarioLogado;
    private View view;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_feed, container, false);

        Log.d("teste", "teste00000000000000000000000000212121");

        //Configurações iniciais
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        feedRef = ConfiguracaoFirebase.getFirebase()
                .child("postagens")
                .child( idUsuarioLogado );

        //Inicializar componentes
        recyclerFeed = view.findViewById(R.id.recyclerFeed);

        //Configura recyclerview
        adapterFeed = new AdapterFeed(listaFeed, getActivity() );
        recyclerFeed.setHasFixedSize(true);
        recyclerFeed.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerFeed.setAdapter( adapterFeed );

        Log.i("teste", "teste");

        return view;
    }

    private void listarFeed(){


        valueEventListenerFeed = feedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for ( DataSnapshot ds: dataSnapshot.getChildren() ){
                    listaFeed.add( ds.getValue(Postagem.class) );


                }
                Collections.reverse( listaFeed );
                adapterFeed.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        listarFeed();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listarFeed();
    }

    @Override
    public void onStop() {
        super.onStop();
        feedRef.removeEventListener( valueEventListenerFeed );
    }
}
