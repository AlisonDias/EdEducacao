package com.alisoondias.ededucacao.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.alisoondias.ededucacao.R;
import com.alisoondias.ededucacao.activity.CadastrarAluno;
import com.alisoondias.ededucacao.activity.CadastrarAvaliacao;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TrabalhosFragment extends Fragment {

    private FloatingActionButton fabTrabalhos;

    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_trabalhos, container, false);





        fabTrabalhos = (FloatingActionButton) view.findViewById(R.id.floatingActionButtonAvaliacao);
        fabTrabalhos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), CadastrarAvaliacao.class));
            }
        });

        return view;
    }
}
