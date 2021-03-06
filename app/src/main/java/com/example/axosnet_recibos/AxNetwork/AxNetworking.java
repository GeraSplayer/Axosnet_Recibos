package com.example.axosnet_recibos.AxNetwork;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.axosnet_recibos.AxClases.AxReciboContent;
import com.example.axosnet_recibos.Interfaces.NetCallback;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by GerardoPC on 12/03/2021.
 */
public class AxNetworking extends AsyncTask<Object, Integer, Object> {
    static final String SERVER_PATH = "https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/";
    /*
    GET getbyid
    https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/getbyid?blalalala=2
    GET GetAll
    https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/getall
    POST Insert
    https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/insert?provider=Axosnet&amount=128.10&comment=Sin comentario&emission_date=2019-05-17&currency_code=MXN
    POST Update
    https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/update?id=2&provider=Axosnet&amount=128.10&comment=Sin comentario&emission_date=2019-05-17&currency_code=MXN
    POST Delete
    https://devapi.axosnet.com/am/v2/api_receipts_beta/api/receipt/delete?id=5
    */
    static final int TIMEOUT = 3000;

    Context m_context;

    public AxNetworking(Context m_context) {
        this.m_context = m_context;
    }

    @Override
    protected Object doInBackground(Object... params) {
        String action = (String) params[0];
        if (action.equals("getById")) {
            int id = (int)params[1];
            AxReciboContent recibo = getById(id);
            NetCallback netCallback = (NetCallback) params[2];
            netCallback.onWorkFinish(recibo);
        } else if (action.equals("getAll")) {
            List<AxReciboContent> lista = getAll();
            NetCallback netCallback = (NetCallback) params[1];
            netCallback.onWorkFinish(lista);
        } else if (action.equals("insert")) {
            boolean insertado = Insert((AxReciboContent) params[1]);
            NetCallback netCallback = (NetCallback) params[2];
            netCallback.onWorkFinish(insertado);
        } else if (action.equals("update")) {
            boolean updateado = Update((AxReciboContent) params[1]);
            NetCallback netCallback = (NetCallback) params[2];
            netCallback.onWorkFinish(updateado);
        } else if (action.equals("delete")) {
            int id = (int)params[1];
            boolean insertado = Delete(id);
            NetCallback netCallback = (NetCallback) params[2];
            netCallback.onWorkFinish(insertado);
        }
        return null;
    }

    private List<AxReciboContent> getAll(){

        List<AxReciboContent> lista = new ArrayList<AxReciboContent>();
        //String postParams = "getall";
        String response = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH+"getall");
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            //conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            //OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            //out.write(postParams.getBytes());
            //out.flush();
            //out.close();

            int responseCode = conn.getResponseCode();
            Log.w("RESPONSE CODE", "" + responseCode);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String jsonResponse = inputStreamToString(in);
            try {
                /*
                "[{\"id\":19291,\"provider\":\"Axosnet\",\"amount\":128.10,\"emission_date\":\"5/17/2019 12:00:00 AM\",\"comment\":\"Sin comentario\",\"currency_code\":\"MXN\"},
                {\"id\":19294,\"provider\":\"test\",\"amount\":986.00,\"emission_date\":\"3/10/2021 12:00:00 AM\",\"comment\":null,\"currency_code\":\"USD\"},
                {\"id\":19295,\"provider\":\"test 2\",\"amount\":98.07,\"emission_date\":\"3/2/2021 12:00:00 AM\",\"comment\":\"ygictycu\",\"currency_code\":\"USD\"},
                {\"id\":19296,\"provider\":\"test 3\",\"amount\":989.00,\"emission_date\":\"3/7/2021 12:00:00 AM\",\"comment\":\"kiyoyguig8\",\"currency_code\":\"USD\"}]"
                */
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
                jsonResponse = gson.fromJson(jsonResponse, String.class);
                JSONArray jsonArray = new JSONArray(jsonResponse);

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    lista.add(new AxReciboContent(
                            object.optInt("id",0),
                            object.optString("provider","Axs"),
                            object.optString("amount","0.0"),
                            object.optString("comment","-"),
                            object.optString("emission_date","01/01/2000 12:00:00 AM"),
                            object.optString("currency_code","MXN") )
                    );
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lista;
    }
    private AxReciboContent getById(int id){

        AxReciboContent recibo = null;
        String response = "";
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH+"getbyid?id="+id);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            int responseCode = conn.getResponseCode();
            Log.w("RESPONSE CODE", "" + responseCode);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String jsonResponse = inputStreamToString(in);
            try {
            /*
                "[{\"id\":19291,\"provider\":\"Axosnet\",\"amount\":128.10,\"emission_date\":\"5/17/2019 12:00:00 AM\",\"comment\":\"Sin comentario\",\"currency_code\":\"MXN\"}]"
            */
                Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().serializeNulls().create();
                jsonResponse = gson.fromJson(jsonResponse, String.class);
                JSONArray jsonArray = new JSONArray(jsonResponse);

                JSONObject jsonObject = jsonArray.getJSONObject(0);
                recibo = new AxReciboContent(
                            jsonObject.optInt("id",0),
                            jsonObject.optString("provider","Axs"),
                            jsonObject.optString("amount","0.0"),
                            jsonObject.optString("comment","-"),
                            jsonObject.optString("emission_date","01/01/2000 12:00:00 AM"),
                            jsonObject.optString("currency_code","MXN") );

            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return recibo;
    }
    private boolean Insert(AxReciboContent recibo){

        String postParams = "insert?" + recibo.toString();
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH + postParams);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            Log.w("RESPONSE CODE", "" + responseCode);

            if(responseCode != 200)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean Update(AxReciboContent recibo){

        String postParams = "update?" + recibo.toStringUpdt();
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH + postParams);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            Log.w("RESPONSE CODE", "" + responseCode);

            if(responseCode != 200)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
    private boolean Delete(int id){

        String postParams = "delete?id=" + String.valueOf(id);
        HttpURLConnection conn = null;
        URL url = null;
        try {
            url = new URL(SERVER_PATH + postParams);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(TIMEOUT);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setFixedLengthStreamingMode(postParams.getBytes().length);

            OutputStream out = new BufferedOutputStream(conn.getOutputStream());
            out.write(postParams.getBytes());
            out.flush();
            out.close();

            int responseCode = conn.getResponseCode();
            Log.w("RESPONSE CODE", "" + responseCode);

            if(responseCode != 200)
                return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    private String inputStreamToString(InputStream is) {
        String rLine = "";
        StringBuilder response = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while((rLine = rd.readLine()) != null)
            {
                response.append(rLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }
}
