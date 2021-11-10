package com.alisoondias.ededucacao.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;


import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.activity.FiltroActivity;
import com.alisoondias.ededucacao.activity.MainActivity;
import com.alisoondias.ededucacao.helper.Permissao;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class PostagemFragment extends Fragment {

    private Button buttonAbrirGaleria, buttonAbrirCamera;
    private static final int SELECAO_CAMERA  = 100;
    private static final int SELECAO_GALERIA = 200;

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };
    private String currentPhotoPath, texto = "alison";


    public PostagemFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_postagem, container, false);

        //Validar permissões
        Permissao.validarPermissoes(permissoesNecessarias, getActivity(), 1 );

        //Inicializar componentes
        buttonAbrirCamera = view.findViewById(R.id.buttonAbrirCamera);
        buttonAbrirGaleria = view.findViewById(R.id.buttonAbrirGaleria);

        //Adiciona evento de clique no botão da camera
        buttonAbrirCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Teste" , "entrou");

                /*Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if( i.resolveActivity( getActivity().getPackageManager() ) != null ){
                    startActivityForResult(i, SELECAO_CAMERA );
                }
                */
                dispatchTakePictureIntent();
            }
        });

        //Adiciona evento de clique no botão da galeria
        buttonAbrirGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent( Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI );
                if( i.resolveActivity( getActivity().getPackageManager() ) != null ){
                    startActivityForResult(i, SELECAO_GALERIA );
                }
            }
        });


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == getActivity().RESULT_OK ){

            Bitmap imagem = null;

            try {

                //Valida tipo de seleção da imagem
                switch ( requestCode ){
                    case SELECAO_CAMERA :
                       /// imagem = (Bitmap) data.getExtras().get("data");

                        if (resultCode == Activity.RESULT_OK) {
                            File f = new File(currentPhotoPath);
                            Log.d("tag", "ABsolute Url of Image is " + Uri.fromFile(f));

                            Uri contentUri = Uri.fromFile(f);
                            imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentUri);


                        }


                        break;
                    case SELECAO_GALERIA :
                        Uri localImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), localImagemSelecionada);
                        break;
                }

                //Valida imagem selecionada
                if( imagem != null ){

                    //Converte imagem em byte array
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                    byte[] dadosImagem = baos.toByteArray();

                    //Envia imagem escolhida para aplicação de filtro
                    Intent i = new Intent(getActivity(), FiltroActivity.class);
                    i.putExtra("fotoEscolhida", dadosImagem );
                    startActivity( i );

                }

            }catch (Exception e){
                Log.i("Teste" , "entrou");
                Log.i("Teste" , e.toString());
                e.printStackTrace();

            }

        }

    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getContext(),
                        "com.alisoondias.ededucacao.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, SELECAO_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+ timeStamp + "_";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

}
