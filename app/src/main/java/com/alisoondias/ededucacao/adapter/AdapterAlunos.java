package com.alisoondias.ededucacao.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.model.Aluno;

import java.util.List;

public class AdapterAlunos extends RecyclerView.Adapter<AdapterAlunos.MyViewHolder> {

    private List<Aluno> alunosLista;
    private Context context;

    public AdapterAlunos(List<Aluno> listaAlunos, Context c) {

        this.alunosLista = listaAlunos;
        this.context = c;

        Log.i("0000000000", listaAlunos.toString());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_alunos, parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Aluno aluno2 = alunosLista.get(position);
        holder.aluno.setText(aluno2.getNome());

        Log.i("0000000000", aluno2.toString());



    }

    @Override
    public int getItemCount() {
        return alunosLista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView aluno;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            aluno = itemView.findViewById(R.id.textViewAluno);


        }
    }
}
