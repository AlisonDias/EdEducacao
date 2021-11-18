package com.alisoondias.ededucacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.activity.AlunoSelecionado;
import com.alisoondias.ededucacao.activity.CadastrarAluno;
import com.alisoondias.ededucacao.adapter.AdapterAlunos;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.helper.RecyclerItemClickListener;
import com.alisoondias.ededucacao.model.Aluno;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AlunoFragment extends Fragment {

    private FloatingActionButton fab;
    private ValueEventListener valueEventListenerAlunos;
    private DatabaseReference alunosRef;
    private List<Aluno> listaAluno = new ArrayList<>();
    private AdapterAlunos adapter;
    private FirebaseAuth autenticacao;
    private RecyclerView recyclerView;

    private View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aluno, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAlunos) ;
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        alunosRef = ConfiguracaoFirebase.getFirebase().child("alunos");

        //configura adapter

        adapter = new AdapterAlunos(listaAluno, view.getContext());

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
                        Aluno alunoSelecionado = listaAluno.get(position);
                        Intent intent = new Intent(getActivity(), AlunoSelecionado.class);
                        intent.putExtra("alunoSelecionado", alunoSelecionado);
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                })
        );





        fab = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAluno);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CadastrarAluno.class));
            }
        });
        return view;
    }

    public void recuperarAlunos(){

        valueEventListenerAlunos = alunosRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //listaAluno.clear();

                for(DataSnapshot dados : dataSnapshot.getChildren()){

                    Log.i("Dados Recuperados", dados.toString());
                    Aluno aluno = dados.getValue(Aluno.class);
                    listaAluno.add(aluno);

                }

                Log.i("0000000000", listaAluno.toString());


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
        recuperarAlunos();

    }

    @Override
    public void onStop() {
        super.onStop();
        listaAluno.clear();
        alunosRef.removeEventListener(valueEventListenerAlunos);
    }

}


