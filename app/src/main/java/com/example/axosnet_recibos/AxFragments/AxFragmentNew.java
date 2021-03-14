package com.example.axosnet_recibos.AxFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.example.axosnet_recibos.Interfaces.OnBackbuttonListener;
import com.example.axosnet_recibos.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AxFragmentNew extends Fragment {

    int mId;
    OnBackbuttonListener onBackbuttonListener;

    TextView mDate;
    TextInputLayout mProvider;
    TextInputLayout mAmount;
    TextInputLayout mComment;
    Spinner mCurrency;
    Button mBtnSubmit;
    TextView lblId;

    public AxFragmentNew(OnBackbuttonListener context, int id) {
        onBackbuttonListener = context;
        mId = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ax_new, container, false);
        mDate = (TextView) view.findViewById(R.id.tvCurrentDate);
        mProvider = (TextInputLayout) view.findViewById(R.id.tilProvider);
        mAmount = (TextInputLayout) view.findViewById(R.id.tilAmount);
        mComment = (TextInputLayout) view.findViewById(R.id.tilComment);
        mCurrency = (Spinner) view.findViewById(R.id.sCurrency);
        mBtnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        lblId = (TextView) view.findViewById(R.id.lblId);

        if(mId > 0) {
            lblId.setText(String.valueOf(mId));
            mBtnSubmit.setText(R.string.EditR);
            new AxNetworking(view.getContext()).execute("getById", mId, new NetCallback() {
                @Override
                public void onWorkFinish(Object data) {
                    AxReciboContent recibo = (AxReciboContent) data;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mDate.setText( recibo.getEmissionDate() );
                            mProvider.getEditText().setText(recibo.getProvider());
                            mAmount.getEditText().setText(recibo.getAmount());
                            mComment.getEditText().setText(recibo.getComment());
                        }
                    });
                }
            });
        }else{
            String currentDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(new Date());
            mDate.setText(currentDate);
        }

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                AxReciboContent recibo = new AxReciboContent(0,
                        mProvider.getEditText().getText().toString(),
                        mAmount.getEditText().getText().toString(),
                        mComment.getEditText().getText().toString(),
                        currentDate,
                        mCurrency.getSelectedItem().toString()
                );

                new AxNetworking(view.getContext()).execute("insert", recibo, new NetCallback() {
                    @Override
                    public void onWorkFinish(Object data) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if((Boolean)data)
                                    onBackbuttonListener.backlistener();
                                else
                                    Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }
        });

        return view;
    }
}