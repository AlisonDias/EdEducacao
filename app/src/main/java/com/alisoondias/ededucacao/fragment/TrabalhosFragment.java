package com.alisoondias.ededucacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.activity.AlunoSelecionado;
import com.alisoondias.ededucacao.activity.AvaliacaoSelecionado;
import com.alisoondias.ededucacao.activity.CadastrarAvaliacao;
import com.alisoondias.ededucacao.adapter.AdapterAlunos;
import com.alisoondias.ededucacao.adapter.AdapterAvaliacao;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.helper.RecyclerItemClickListener;
import com.alisoondias.ededucacao.model.Aluno;
import com.alisoondias.ededucacao.model.Avaliacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TrabalhosFragment extends Fragment {

    private FloatingActionButton fabTrabalhos;
    private RecyclerView recyclerView;
    private FirebaseAuth autenticacao;
    private DatabaseReference avaliacaoRef;
    private AdapterAvaliacao adapter;
    private ValueEventListener valueEventListenerAvaliacoes;

    private List<Avaliacao> listaAvaliacao = new ArrayList<>();

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trabalhos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAvaliacao) ;
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        avaliacaoRef = ConfiguracaoFirebase.getFirebase().child("avaliacao");

        adapter = new AdapterAvaliacao(listaAvaliacao, view.getContext());

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(view.getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                    }

                    @Override
                    public void onItemClick(View view, int position) {
                        Avaliacao avaliacaoSelecionada = listaAvaliacao.get(position);
                        Intent intent = new Intent(getActivity(), AvaliacaoSelecionado.class);
                        intent.putExtra("avaliacaoSelecionada", avaliacaoSelecionada);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                })
        );


        fabTrabalhos = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAvaliacao);
        fabTrabalhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CadastrarAvaliacao.class));
            }
        });

        return view;
    }

    public void recuperarAvaliacaoes(){

        valueEventListenerAvaliacoes = avaliacaoRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //listaAluno.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){

                    Log.i("Dados Recuperados", dados.toString());
                    Avaliacao avaliacao= dados.getValue(Avaliacao.class);
                    listaAvaliacao.add(avaliacao);

                }

                Log.i("0000000000", listaAvaliacao.toString());


                adapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        recuperarAvaliacaoes();
    }

    @Override
    public void onStop() {
        super.onStop();
        listaAvaliacao.clear();
        avaliacaoRef.removeEventListener(valueEventListenerAvaliacoes);
    }
}
