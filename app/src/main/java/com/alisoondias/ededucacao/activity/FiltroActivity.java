package com.alisoondias.ededucacao.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.adapter.AdapterMiniaturas;
import com.alisoondias.ededucacao.helper.ConfiguracaoFirebase;
import com.alisoondias.ededucacao.helper.RecyclerItemClickListener;
import com.alisoondias.ededucacao.helper.UsuarioFirebase;
import com.alisoondias.ededucacao.model.Aluno;
import com.alisoondias.ededucacao.model.Postagem;
import com.alisoondias.ededucacao.model.Usuario;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class FiltroActivity extends AppCompatActivity {

    static
    {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView imageFotoEscolhida;
    private Bitmap imagem;
    private Bitmap imagemFiltro;
    private TextInputEditText textDescricaoFiltro;
    private List<ThumbnailItem> listaFiltros;
    private String idUsuarioLogado;
    private Usuario usuarioLogado;
    private AlertDialog dialog;
    private Spinner spinner;

    private RecyclerView recyclerFiltros;
    private AdapterMiniaturas adapterMiniaturas;

    private DatabaseReference usuariosRef;
    private DatabaseReference usuarioLogadoRef;
    private DatabaseReference firebaseRef;
    private DataSnapshot seguidoresSnapshot;

    private List<Aluno> alunosOBJ = new ArrayList<>();
    private List<String> alunosString = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtro);

        //Configura????es iniciais
        listaFiltros = new ArrayList<>();
        firebaseRef = ConfiguracaoFirebase.getFirebase();
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario();
        usuariosRef = ConfiguracaoFirebase.getFirebase().child("usuarios");


        //Inicializar componentes
        imageFotoEscolhida = findViewById(R.id.imageFotoEscolhida);
        recyclerFiltros = findViewById(R.id.recyclerFiltros);
        textDescricaoFiltro = findViewById(R.id.textDescricaoFiltro);
        spinner = findViewById(R.id.spinner_Aluno_Postagem);

        //Recuperar dados para uma nova postagem
        recuperarDadosPostagem();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);


        //Recupera a imagem escolhida pelo usu??rio
        Bundle bundle = getIntent().getExtras();

        if( bundle != null ){
            byte[] dadosImagem = bundle.getByteArray("fotoEscolhida");
            imagem = BitmapFactory.decodeByteArray(dadosImagem, 0, dadosImagem.length );
            imageFotoEscolhida.setImageBitmap( imagem );
            imagemFiltro = imagem.copy(imagem.getConfig(), true );

            //Configura recyclerview de filtros
            adapterMiniaturas = new AdapterMiniaturas(listaFiltros, getApplicationContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false );
            recyclerFiltros.setLayoutManager( layoutManager );
            recyclerFiltros.setAdapter( adapterMiniaturas );

            //Adiciona evento de clique no recyclerview
            recyclerFiltros.addOnItemTouchListener(
                    new RecyclerItemClickListener(
                            getApplicationContext(),
                            recyclerFiltros,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    ThumbnailItem item = listaFiltros.get(position);

                                    imagemFiltro = imagem.copy(imagem.getConfig(), true );
                                    Filter filtro = item.filter;
                                    imageFotoEscolhida.setImageBitmap( filtro.processFilter(imagemFiltro) );

                                }

                                @Override
                                public void onLongItemClick(View view, int position) {

                                }

                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                }
                            }
                    )
            );

            //Recupera filtros
            recuperarFiltros();



        }

        ConfiguracaoFirebase.getFirebase().child("alunos").orderByChild("nome").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                for (DataSnapshot alunoSnapshot: snapshot.getChildren()) {

                    Aluno aluno = alunoSnapshot.getValue(Aluno.class);

                    String areaName = alunoSnapshot.child("nome").getValue(String.class);

                    //Log.i("teste", escola.getId());
                    alunosOBJ.add(aluno);
                    alunosString.add(areaName);

                }

                ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(FiltroActivity.this, android.R.layout.simple_spinner_item, alunosString);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(areasAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void abrirDialogCarregamento(String titulo){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle( titulo );
        alert.setCancelable(false);
        alert.setView(R.layout.carregamento);

        dialog = alert.create();
        //dialog.show();

    }

    private void recuperarDadosPostagem(){

        //abrirDialogCarregamento("Carregando dados, aguarde!");
        usuarioLogadoRef = usuariosRef.child( idUsuarioLogado );
        usuarioLogadoRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        //Recupera dados de usu??rio logado
                        usuarioLogado = dataSnapshot.getValue( Usuario.class );


                        /*
                        * Recuperar seguidores */
                        DatabaseReference seguidoresRef = firebaseRef
                                .child("seguidores")
                                .child( idUsuarioLogado );
                        seguidoresRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                seguidoresSnapshot = dataSnapshot;
                                //dialog.cancel();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

    }

    private void recuperarFiltros(){

        //Limpar itens
        ThumbnailsManager.clearThumbs();
        listaFiltros.clear();

        //Configurar filtro normal
        ThumbnailItem item = new ThumbnailItem();
        item.image = imagem;
        item.filterName = "Normal";
        ThumbnailsManager.addThumb( item );

        //Lista todos os filtros
        List<Filter> filtros = FilterPack.getFilterPack(getApplicationContext());
        for (Filter filtro: filtros ){

            ThumbnailItem itemFiltro = new ThumbnailItem();
            itemFiltro.image = imagem;
            itemFiltro.filter = filtro;
            itemFiltro.filterName = filtro.getName();

            ThumbnailsManager.addThumb( itemFiltro );

        }

        listaFiltros.addAll( ThumbnailsManager.processThumbs(getApplicationContext()) );
        adapterMiniaturas.notifyDataSetChanged();

    }

    private void publicarPostagem(){

        abrirDialogCarregamento("Salvando postagem");
        final Postagem postagem = new Postagem();
        postagem.setIdUsuario( idUsuarioLogado );
        postagem.setDescricao( textDescricaoFiltro.getText().toString() );

        int posicao = spinner.getSelectedItemPosition();
        postagem.setAluno(alunosOBJ.get(posicao));
        postagem.setUsuario(usuarioLogado);

        //Recuperar dados da imagem para o firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemFiltro.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] dadosImagem = baos.toByteArray();

        //Salvar imagem no firebase storage
        StorageReference storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        StorageReference imagemRef = storageRef
                .child("imagens")
                .child("postagens")
                .child( postagem.getId() + ".jpeg");

        UploadTask uploadTask = imagemRef.putBytes( dadosImagem );
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(FiltroActivity.this,
                        "Erro ao salvar a imagem, tente novamente!",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                //Recuperar local da foto

                Task<Uri> task = taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        postagem.setCaminhoFoto(uri.toString());
                        Log.d("foto", uri.toString());

                        //Atualizar qtde de postagens
                        int qtdPostagem = usuarioLogado.getPostagens() + 1;
                        usuarioLogado.setPostagens( qtdPostagem );
                        usuarioLogado.atualizarQtdPostagem();

                        //Salvar postagem
                         if( postagem.salvar( seguidoresSnapshot ) ){

                            Toast.makeText(FiltroActivity.this,
                                    "Sucesso ao salvar postagem!",
                                    Toast.LENGTH_SHORT).show();
                            dialog.cancel();
                            finish();
                        }

                        //postagem.salvarPostagem();

                    }
                });




            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_filtro, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.ic_salvar_postagem :
                publicarPostagem();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
