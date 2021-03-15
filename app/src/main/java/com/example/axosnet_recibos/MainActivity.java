package com.example.axosnet_recibos;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet_recibos.AxFragments.AxFragmentLista;
import com.example.axosnet_recibos.AxFragments.AxFragmentNew;
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.Feedback;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.example.axosnet_recibos.Interfaces.OnBackbuttonListener;
import com.example.axosnet_recibos.Interfaces.OnFragmentClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements OnFragmentClickListener, OnBackbuttonListener, Feedback {

    ConstraintLayout mainlayout;
    FloatingActionButton fabAgregarRecibo;
    TextView tvCurrentFrag;
    ProgressBar mLoading;
    Button btnConfirmDelete;
    Button btnCancel;
    TextView tvDeleteId;
    CardView cvConfirmation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainlayout = (ConstraintLayout) findViewById(R.id.mainLayout);
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

        cvConfirmation = findViewById(R.id.cvConfirmation);
        tvDeleteId = findViewById(R.id.tvDeleteId);
        btnConfirmDelete = findViewById(R.id.btnConfirmDelete);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cvConfirmation.setVisibility(View.GONE);
                tvDeleteId.setText("");
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

    @Override
    public void deleteBtnClick(int id) {
        tvDeleteId.setText(String.valueOf(id));
        cvConfirmation.setVisibility(View.VISIBLE);
        btnConfirmDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteconfirmed(id);
                tvDeleteId.setText("");
            }
        });
    }

    @Override
    public void scrolledRV(boolean hide) {
        if(hide)
            fabAgregarRecibo.hide();
        else
            fabAgregarRecibo.show();
    }

    private void deleteconfirmed(int id){
        new AxNetworking(this).execute("delete", id, new NetCallback() {
            @Override
            public void onWorkFinish(Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cvConfirmation.setVisibility(View.GONE);
                        if((Boolean)data) {
                            showUpdate(String.valueOf(id)+" Correctamente eliminado");
                            ((AxFragmentLista) fm.findFragmentByTag("Lista")).initLista();
                        }else
                            showToast("Error");
                    }
                });
            }
        });
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
        Snackbar.make(mainlayout, message, Snackbar.LENGTH_SHORT).show();
    }
}