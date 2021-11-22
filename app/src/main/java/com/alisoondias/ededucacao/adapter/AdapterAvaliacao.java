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
import com.alisoondias.ededucacao.model.Avaliacao;

import java.util.List;

public class AdapterAvaliacao extends RecyclerView.Adapter<AdapterAvaliacao.MyViewHolder> {

    private List<Avaliacao> avaliacaoLista;
    private Context context;

    public AdapterAvaliacao(List<Avaliacao> listaAvaliacao, Context c) {

        this.avaliacaoLista = listaAvaliacao;
        this.context = c;

        Log.i("0000000000", listaAvaliacao.toString());

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_adapter_avaliacao, parent,false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Avaliacao avaliacao2 = avaliacaoLista.get(position);
        holder.avaliacaoNome.setText(avaliacao2.getAluno().getNome());
        holder.avaliacaoData.setText(avaliacao2.getData());

        Log.i("0000000000", avaliacao2.toString());



    }

    @Override
    public int getItemCount() {
        return avaliacaoLista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView avaliacaoNome, avaliacaoData;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            avaliacaoNome = itemView.findViewById(R.id.textViewAvaliacao);
            avaliacaoData = itemView.findViewById(R.id.textViewDataAdapter);


        }
    }
}
