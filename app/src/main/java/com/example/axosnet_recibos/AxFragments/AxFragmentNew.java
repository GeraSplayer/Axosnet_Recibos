package com.example.axosnet_recibos.AxFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.axosnet_recibos.R;


public class AxFragmentNew extends Fragment {

    public AxFragmentNew() {
        // Required empty public constructor
    }

    public static AxFragmentNew newInstance(String param1, String param2) {
        AxFragmentNew fragment = new AxFragmentNew();

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ax_new, container, false);
    }
}