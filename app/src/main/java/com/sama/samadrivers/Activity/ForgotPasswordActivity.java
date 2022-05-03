package com.sama.samadrivers.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.setBottomFun2;

public class ForgotPasswordActivity extends AppCompatActivity  {
    private EditText email_et;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        email_et = findViewById(R.id.email_et);
    }


    public void submit(View view) {
        if (email_et.getText().toString().trim().length() == 0) {
            email_et.setError("please enter valid email address");
            email_et.requestFocus();
            return;
        }

        if (!isNetworkConnected(ForgotPasswordActivity.this)) {
            MethodClass.network_error_alert(ForgotPasswordActivity.this);
            return;
        }

        MethodClass.showProgressDialog(ForgotPasswordActivity.this);
//        String server_url = getString(R.string.SERVER_URL) + "driver-forget-password";

        String server_url = getString(R.string.SERVER_BASE_URL) + "driver-forget-password";

        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", email_et.getText().toString().trim());
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("parames", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(ForgotPasswordActivity.this);
                Log.e("respLogin", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(ForgotPasswordActivity.this, response);
                    if (resultResponse != null) {
                        String otp = resultResponse.getString("vcode");
                        Toast.makeText(ForgotPasswordActivity.this, getString(R.string.otp_has_been_sent_your_mail), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ForgotPasswordActivity.this, VerifyActivity.class);
                        intent.putExtra("email", email_et.getText().toString().trim());
                        intent.putExtra("code", otp);
                        startActivity(intent);

                    }

                } catch (JSONException e) {
                    MethodClass.error_alert(ForgotPasswordActivity.this);
                    e.printStackTrace();
                    Log.e("login_parce", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MethodClass.hideProgressDialog(ForgotPasswordActivity.this);
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(ForgotPasswordActivity.this);
                } else {
                    MethodClass.error_alert(ForgotPasswordActivity.this);
                }
            }
        }){
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(ForgotPasswordActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(ForgotPasswordActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(ForgotPasswordActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(ForgotPasswordActivity.this).addToRequestQueue(jsonObjectRequest);
    }


    public void back(View view) {
        onBackPressed();
    }

}