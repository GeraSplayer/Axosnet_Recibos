package com.example.axosnet_recibos.AxAdapters;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.AxViewHolder.AxRecibosViewHolder;
import com.example.axosnet_recibos.Interfaces.RecyclerViewClickListener;
import com.example.axosnet_recibos.R;

import java.util.List;

public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<AxRecibosViewHolder> {

    private final List<AxReciboContent> mValues;
    RecyclerViewClickListener recyclerListener;

    public MyItemRecyclerViewAdapter(List<AxReciboContent> items, RecyclerViewClickListener rvcListener) {
        mValues = items;
        recyclerListener = rvcListener;
    }

    @Override
    public AxRecibosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recibos, parent, false);
        return new AxRecibosViewHolder(view, recyclerListener);
    }

    @Override
    public void onBindViewHolder(final AxRecibosViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
        holder.mProviderView.setText(mValues.get(position).getProvider());
        holder.mAmountView.setText(mValues.get(position).getAmount());
        holder.mCommentView.setText(mValues.get(position).getComment());
        holder.mEmissionDateView.setText(mValues.get(position).getEmissionDate());
        holder.mCurrencyCodeView.setText(mValues.get(position).getCurrencyCode());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }


}