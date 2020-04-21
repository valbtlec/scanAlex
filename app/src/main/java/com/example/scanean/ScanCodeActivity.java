package com.example.scanean;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.zxing.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanCodeActivity extends Activity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;

    private RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_scan_code);
        Button Retour = (Button)findViewById(R.id.btnRetour);


        hideSoftKeyboard();
        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        //  ScannerView = new ZXingScannerView(this);

        ScannerView = (ZXingScannerView) findViewById(R.id.zxscan);
        ScannerView.setResultHandler(this);


        Retour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentProducts = new Intent(ScanCodeActivity.this, MainActivity.class);
                startActivity(intentProducts);
                finish();
            }
        });
    }

    @Override
    public void handleResult(final Result result) {


        ConnectivityManager cm =
                (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
      //  System.out.println(result);

        final String EAN = result.getText();

        System.out.println("Scan ean : "+EAN);

        CheckEAN(EAN);




            }












    @Override
    public void onPause() {


super.onPause();
ScannerView.stopCamera();




    }

    @Override
    public void onResume() {

super.onResume();
ScannerView.setResultHandler(this);

ScannerView.startCamera();




    }

    public void hideSoftKeyboard() {
        if(getCurrentFocus()!=null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    @Override
    public void onBackPressed() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if (!isFinishing()) {
                    new AlertDialog.Builder(ScanCodeActivity.this)
                            .setTitle("Information")
                            .setMessage("Voulez-vous vraiment quitter l'application")
                            .setCancelable(false)
                            .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                    finishAffinity();
                                    System.exit(0);
                                }
                            })
                            .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).show();
                }
            }
        });
    }

    public void showKeyboard(){
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    private void  CheckEAN(final String EAN) {

        if(queue == null){
            queue = Volley.newRequestQueue(this);
        }

        final String url;

            url = "https://www.extralecbtlec.fr/myapi/retireveeantest.php";
//        String url = "";
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.

                            System.out.println("Reponse : "+response);

                            String Designation = "";
                            try {
                                JSONObject jObj = new JSONObject(response);
                                boolean error = jObj.getBoolean("error");



                                if (!error) {




                                        Designation = jObj.getString("Designation");



                                        final Intent intentProducts = new Intent(ScanCodeActivity.this, MainActivity.class);





                                        intentProducts.putExtra("EAN", EAN);
                                        intentProducts.putExtra("Designation", Designation);



                                        startActivity(intentProducts);
                                        finish();










                                } else {


                                    if (!isFinishing()) {
                                        new AlertDialog.Builder(ScanCodeActivity.this)
                                                .setTitle("EAN non existant")
                                                .setMessage("")
                                                .setCancelable(false)
                                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        final Intent intentProducts = new Intent(ScanCodeActivity.this, MainActivity.class);

                                                        startActivity(intentProducts);
                                                        finish();

                                                    }
                                                })

                                                .show();
                                    }

                                }


                            } catch (JSONException e) {


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    //    IconePresencePDF.setVisibility(View.GONE);
                    //    DetailsPDF.setText("L'accès à la fiche produit n'est disponible qu'avec une connexion à Internet");

                }


            }) {

                @Override
                protected Map<String, String> getParams() {
                    // Posting parameters to login url

                    Map<String, String> params = new HashMap<String, String>();


                    params.put("EAN", EAN);


System.out.println("Envoi param : "+params);


                    return params;

                }

            };

// Add the request to the RequestQueue.
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);




    }

}
