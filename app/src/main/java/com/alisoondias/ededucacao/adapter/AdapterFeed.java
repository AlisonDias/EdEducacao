package com.alisoondias.ededucacao.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.helper.UsuarioFirebase;
import com.alisoondias.ededucacao.model.Feed;
import com.alisoondias.ededucacao.model.Postagem;
import com.alisoondias.ededucacao.model.PostagemCurtida;
import com.alisoondias.ededucacao.model.Usuario;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdapterFeed extends RecyclerView.Adapter<AdapterFeed.MyViewHolder> {

    private List<Postagem> listaFeed;
    private Context context;

    public AdapterFeed(List<Postagem> listaFeed, Context context) {
        this.listaFeed = listaFeed;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_feed, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final Postagem feed = listaFeed.get(position);
        final Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Carrega dados do feed

        Log.i("urlDENTRO", listaFeed.get(position).toString());

        Uri uriFotoUsuario = Uri.parse( feed.getUsuario().getCaminhoFoto() );
        Uri uriFotoPostagem = Uri.parse( feed.getCaminhoFoto() );

        Glide.with( context ).load( uriFotoUsuario ).into(holder.fotoPerfil);
        Glide.with( context ).load( uriFotoPostagem ).into(holder.fotoPostagem);

        //Log.i("uri", feed.getFotoPostagem().toString());

        holder.descricao.setText( feed.getDescricao() );
        holder.nome.setText( feed.getUsuario().getNome() );
        holder.textViewNomeAlunoPostagem.setText( feed.getAluno().getNome() );
        //holder.nome.setText( feed.getNomeUsuario() );




    }

    @Override
    public int getItemCount() {
        return listaFeed.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView fotoPerfil;
        TextView nome, descricao, textViewNomeAlunoPostagem;
        ImageView fotoPostagem;

        public MyViewHolder(View itemView) {
            super(itemView);

            fotoPerfil   = itemView.findViewById(R.id.imagePerfilPostagem);
            fotoPostagem = itemView.findViewById(R.id.imagePostagemSelecionada);
            nome         = itemView.findViewById(R.id.textPerfilPostagem);
            descricao    = itemView.findViewById(R.id.textDescricaoPostagem);
            textViewNomeAlunoPostagem    = itemView.findViewById(R.id.textViewNomeAlunoPostagem);


        }
    }

}
