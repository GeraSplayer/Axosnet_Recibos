package com.example.axosnet_recibos.AxViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.Interfaces.RecyclerViewClickListener;
import com.example.axosnet_recibos.R;

/**
 * Created by GerardoPC on 12/03/2021.
*/
public class AxRecibosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public final View mView;
    public final TextView mIdView;
    public final TextView mProviderView;
    public final TextView mAmountView;
    public final TextView mCommentView;
    public final TextView mEmissionDateView;
    public final TextView mCurrencyCodeView;
    public AxReciboContent mItem;
    RecyclerViewClickListener listener;
    public final Button mDeleteBtn;

    public AxRecibosViewHolder(View view, RecyclerViewClickListener rcvListener) {
        super(view);
        mView = view;
        mIdView = (TextView) view.findViewById(R.id.tvId);
        mProviderView = (TextView) view.findViewById(R.id.tvProvider);
        mAmountView = (TextView) view.findViewById(R.id.tvAmount);
        mCommentView = (TextView) view.findViewById(R.id.tvComment);
        mEmissionDateView = (TextView) view.findViewById(R.id.tvEmissionDate);
        mCurrencyCodeView = (TextView) view.findViewById(R.id.tvCurrencyCode);
        listener = rcvListener;
        mView.setOnClickListener(this);
        mDeleteBtn = (Button)view.findViewById(R.id.ibtnDelete);
        mDeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.deleteButtonClicked( Integer.parseInt(mIdView.getText().toString()) );
            }
        });
    }

    @Override
    public void onClick(View view) {
        listener.recyclerViewListClicked( view, Integer.parseInt(mIdView.getText().toString() ) );
    }
}
