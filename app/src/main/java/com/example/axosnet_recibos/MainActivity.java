package com.example.axosnet_recibos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.AxFragments.AxFragmentLista;
import com.example.axosnet_recibos.AxFragments.AxFragmentNew;
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.Feedback;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.example.axosnet_recibos.Interfaces.OnBackbuttonListener;
import com.example.axosnet_recibos.Interfaces.OnFragmentClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFragmentClickListener, OnBackbuttonListener, Feedback {

    FloatingActionButton fabAgregarRecibo;
    TextView tvCurrentFrag;
    ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fabAgregarRecibo = (FloatingActionButton) findViewById(R.id.fabAgregarRecibo);

        if (!isNetworkAvailable()) {
            ((TextView)findViewById(R.id.tvNoCon)).setVisibility(View.VISIBLE);
            ((ImageView)findViewById(R.id.ivNoCon)).setVisibility(View.VISIBLE);
            fabAgregarRecibo.setVisibility(View.GONE);

            return;
        }

        mLoading = (ProgressBar) findViewById(R.id.progressBar);
        mLoading.setVisibility(View.VISIBLE);
        tvCurrentFrag = (TextView) findViewById(R.id.tvCurrentFrag);

        changeFragment(new AxFragmentLista(this), "Lista");

        fabAgregarRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCurrentFrag.setText(R.string.New);
                changeFragment(new AxFragmentNew(MainActivity.this, 0), "New");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tvCurrentFrag.setText(R.string.Main);
        fabAgregarRecibo.show();
    }

    FragmentManager fm;
    private void changeFragment(Fragment newFragment, String tag) {
        fm = getSupportFragmentManager();

        Fragment currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()) {
            Toast.makeText(this, "Ya se esta mostrando este fragmento", Toast.LENGTH_SHORT).show();
            return;
        }

        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.containerFrameMain, newFragment, tag);
        if(!tag.equals("Lista")) {
            fabAgregarRecibo.hide();
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    @Override
    public void onListClick(int id) {
        tvCurrentFrag.setText(R.string.Edit);
        changeFragment(new AxFragmentNew(this, id), "Edit");
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void backlistener() {
        super.onBackPressed();
        fabAgregarRecibo.show();
    }

    @Override
    public void hideLoading() {
        mLoading.setVisibility(View.GONE);
    }

    @Override
    public void showUpdate(String message) {

    }
}