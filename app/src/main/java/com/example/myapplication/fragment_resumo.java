package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * ResumoFragment.java
 * Exibe o texto de resumo do conteúdo na aba "Resumo".
 *
 * Como usar na DetalhesActivity:
 *     ResumoFragment fragment = ResumoFragment.newInstance("Texto do resumo aqui...");
 */
public class fragment_resumo extends Fragment {

    private static final String ARG_RESUMO = "resumo";

    // Cria o fragment já com o texto do resumo
    public static fragment_resumo newInstance(String resumo) {
        fragment_resumo fragment = new fragment_resumo();
        Bundle args = new Bundle();
        args.putString(ARG_RESUMO, resumo);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_fragment_resumo, container, false);

        TextView txtResumo = view.findViewById(R.id.txtResumo);

        // Pega o texto passado pelo newInstance e preenche na tela
        if (getArguments() != null) {
            txtResumo.setText(getArguments().getString(ARG_RESUMO));
        }
        return view;
    }
}