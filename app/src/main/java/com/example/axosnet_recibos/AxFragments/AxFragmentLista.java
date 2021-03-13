package com.example.axosnet_recibos.AxFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.axosnet_recibos.AxAdapters.MyItemRecyclerViewAdapter;
import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.Interfaces.OnFragmentClickListener;
import com.example.axosnet_recibos.Interfaces.RecyclerViewClickListener;
import com.example.axosnet_recibos.R;

import java.util.ArrayList;
import java.util.List;


public class AxFragmentLista extends Fragment implements RecyclerViewClickListener {

    OnFragmentClickListener fragmentListener;
    List<AxReciboContent> mListaRecibos;

    public AxFragmentLista(Context context, List<AxReciboContent> lista) {
        fragmentListener = (OnFragmentClickListener) context;
        mListaRecibos = lista;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ax_lista, container, false);

        Context context = view.getContext();

        RecyclerView recyclerView = (RecyclerView) view;
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(mListaRecibos, this));

        return view;
    }

    @Override
    public void recyclerViewListClicked(View v, int id) {
        fragmentListener.onListClick(id);
    }
}