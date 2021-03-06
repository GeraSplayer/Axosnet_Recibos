package com.example.axosnet_recibos.AxFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.Feedback;
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
    Feedback feedback;

    View view;
    TextView mDate;
    TextInputLayout mProvider;
    TextInputLayout mAmount;
    TextInputLayout mComment;
    Spinner mCurrency;
    Button mBtnSubmit;
    TextView tvEditID;
    TextView lblId;

    public AxFragmentNew(Context context, int id) {
        onBackbuttonListener = (OnBackbuttonListener)context;
        mId = id;
        feedback = (Feedback) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ax_new, container, false);
        mDate = (TextView) view.findViewById(R.id.tvCurrentDate);
        mProvider = (TextInputLayout) view.findViewById(R.id.tilProvider);
        mProvider.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mProvider.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mAmount = (TextInputLayout) view.findViewById(R.id.tilAmount);
        mAmount.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAmount.setErrorEnabled(false);
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mComment = (TextInputLayout) view.findViewById(R.id.tilComment);
        mCurrency = (Spinner) view.findViewById(R.id.sCurrency);
        mBtnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        tvEditID = (TextView) view.findViewById(R.id.tvEditID);
        lblId = (TextView) view.findViewById(R.id.lblId);

        if(mId > 0) {
            lblId.setVisibility(View.VISIBLE);
            tvEditID.setText(String.valueOf(mId));
            mBtnSubmit.setText(R.string.EditR);
            getReceiptDetails();
        }else{
            lblId.setVisibility(View.GONE);
            String currentDate = new SimpleDateFormat("MM/dd/yyyy hh:mm a", Locale.getDefault()).format(new Date());
            mDate.setText(currentDate);
        }

        mBtnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmptyInputs())
                    submit();
            }
        });
        return view;
    }

    private void getReceiptDetails(){
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
                        if (recibo.getCurrencyCode().equals("MXN") || recibo.getCurrencyCode().equals("mxn") )
                            mCurrency.setSelection(0);
                        else if (recibo.getCurrencyCode().equals("USD") || recibo.getCurrencyCode().equals("usd") )
                            mCurrency.setSelection(1);
                        mComment.getEditText().setText(recibo.getComment());
                    }
                });
            }
        });
    }

    private boolean checkEmptyInputs(){
        boolean flag = true;
        String string1 = mProvider.getEditText().getText().toString();
        String string2 = mAmount.getEditText().getText().toString();

        if(string1.isEmpty() || string2.isEmpty() || string1.startsWith(" ") )
            flag = false;

        if(!flag){
            if(string1.isEmpty() || string1.startsWith(" "))
                mProvider.setError(getResources().getString(R.string.validationP));

            if(string2.isEmpty())
                mAmount.setError(getResources().getString(R.string.validationP));

        }

        return flag;
    }

    private void submit(){
        String submitType = mId > 0 ? "update":"insert";
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        String comment = "Sin comentario";
        if(mComment.getEditText().getText().toString().equals("-") || mComment.getEditText().getText().toString().isEmpty() || mComment.getEditText().getText().toString().startsWith(" ") )
            comment = "Sin comentario";
        else
            comment = mComment.getEditText().getText().toString();

        AxReciboContent recibo = new AxReciboContent(mId,
                mProvider.getEditText().getText().toString(),
                mAmount.getEditText().getText().toString(),
                comment,
                currentDate,
                mCurrency.getSelectedItem().toString()
        );

        new AxNetworking(view.getContext()).execute(submitType, recibo, new NetCallback() {
            @Override
            public void onWorkFinish(Object data) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if((Boolean)data) {
                            if(mId > 0)
                                feedback.showUpdate(getString(R.string.updateMsg));
                            else
                                feedback.showUpdate(getString(R.string.insertMsg));
                            onBackbuttonListener.backlistener();
                        }else
                            Toast.makeText(getContext(), "error", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}