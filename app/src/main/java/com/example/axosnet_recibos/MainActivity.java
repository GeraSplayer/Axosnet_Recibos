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
import android.widget.TextView;
import android.widget.Toast;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.AxFragments.AxFragmentLista;
import com.example.axosnet_recibos.AxFragments.AxFragmentNew;
import com.example.axosnet_recibos.AxNetwork.AxNetworking;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.example.axosnet_recibos.Interfaces.OnFragmentClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnFragmentClickListener {

    FloatingActionButton fabAgregarRecibo;
    TextView tvCurrentFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!isNetworkAvailable()) {
            showToast("No tienes conectividad!!!");
            return;
        }

        tvCurrentFrag = (TextView) findViewById(R.id.tvCurrentFrag);

        new AxNetworking (MainActivity.this).execute("getAll", new NetCallback() {
            @Override
            public void onWorkFinish(Object data) {
                List<AxReciboContent> lista = (List<AxReciboContent>)data ;

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        changeFragment(new AxFragmentLista(MainActivity.this, lista), "Lista");
                    }
                });
            }
        });


        fabAgregarRecibo = (FloatingActionButton) findViewById(R.id.fabAgregarRecibo);
        fabAgregarRecibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tvCurrentFrag.setText("Nuevo Recibo");
                changeFragment(new AxFragmentNew(), "New");
            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        tvCurrentFrag.setText("Recibos");
    }

    private void changeFragment(Fragment newFragment, String tag) {
        //solo controla los fragmentos de una activity
        FragmentManager fm = getSupportFragmentManager();

        Fragment currentFragment = fm.findFragmentByTag(tag);
        if (currentFragment != null && currentFragment.isVisible()) {
            Toast.makeText(this, "Ya se esta mostrando este fragmento", Toast.LENGTH_SHORT).show();
            return;
        }

        //se encarga de los cambios
        FragmentTransaction ft = fm.beginTransaction();             //1 se inicia transacción
        ft.replace(R.id.containerFrameMain, newFragment, tag);      //2 el tipo de cambio
        ft.addToBackStack(null);
        ft.commit();                                                //3 ejecuta el cambio
    }

    @Override
    public void onListClick(int id) {
        tvCurrentFrag.setText("Editar Recibo");
        changeFragment(new AxFragmentNew(), "Edit");
        //Toast.makeText(this, "id", Toast.LENGTH_SHORT).show();
    }

    boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() && networkInfo.isAvailable();
    }
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}