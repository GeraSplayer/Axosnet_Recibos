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
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.Feedback;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.example.axosnet_recibos.Interfaces.OnFragmentClickListener;
import com.example.axosnet_recibos.Interfaces.RecyclerViewClickListener;
import com.example.axosnet_recibos.MainActivity;
import com.example.axosnet_recibos.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AxFragmentLista extends Fragment implements RecyclerViewClickListener {

    Context mContext;
    OnFragmentClickListener fragmentListener;
    Feedback feedback;
    List<AxReciboContent> mlista;
    MyItemRecyclerViewAdapter myItemRecyclerViewAdapter;

    public AxFragmentLista(Context context) {
        fragmentListener = (OnFragmentClickListener) context;
        feedback = (Feedback) context;
        mlista = new ArrayList<AxReciboContent>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ax_lista, container, false);

        mContext = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view;
        myItemRecyclerViewAdapter = new MyItemRecyclerViewAdapter(mlista, this);
        recyclerView.setAdapter(myItemRecyclerViewAdapter);
        myItemRecyclerViewAdapter.notifyDataSetChanged();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initLista();
    }

    public void initLista(){
        new AxNetworking(mContext).execute("getAll", new NetCallback() {
            @Override
            public void onWorkFinish(Object data) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            feedback.hideLoading();
                            mlista.clear();
                            mlista.addAll((Collection<? extends AxReciboContent>) data);
                            myItemRecyclerViewAdapter.notifyDataSetChanged();
                        }
                    });
            }
        });
    }

    @Override
    public void recyclerViewListClicked(View v, int id) {
        fragmentListener.onListClick(id);
    }

    @Override
    public void deleteButtonClicked(int id) {
        fragmentListener.deleteBtnClick(id);
    }
}