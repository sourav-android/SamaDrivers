package com.sama.samadrivers.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.sama.samadrivers.Common.Constant.SHARED_PREF;
import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;

public class LoginActivity extends AppCompatActivity {
    private EditText email_et, pass;
    private Button button1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email_et = findViewById(R.id.email_et);
        pass = findViewById(R.id.pass);
        button1 = findViewById(R.id.button1);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    pass.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    pass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pass.setTransformationMethod(new HideReturnsTransformationMethod());
                }
            }
        });


    }

    public void login(View view) {
        String get_email = "", get_pswrd = "";
        get_email = email_et.getText().toString().trim();
        get_pswrd = pass.getText().toString().trim();

        if (get_email.length() == 0) {
            email_et.setError("please enter valid email address");
            email_et.requestFocus();
            return;
        }
        if (!MethodClass.emailValidator(get_email)) {
            email_et.setError("please enter valid email address");
            email_et.requestFocus();
            return;
        }
        if (get_pswrd.length() == 0) {
            pass.setError(getString(R.string.enterpas));
            pass.requestFocus();
            return;
        }

        if (!isNetworkConnected(LoginActivity.this)) {
            MethodClass.network_error_alert(LoginActivity.this);
            return;
        }


        MethodClass.showProgressDialog(LoginActivity.this);
        String android_id = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPreferences pref = getSharedPreferences(SHARED_PREF, 0);
        String firebase_reg_no = pref.getString("regId", null);
//        String server_url = getString(R.string.SERVER_URL) + "login-driver";

        String server_url = getString(R.string.SERVER_BASE_URL) + "login-driver";

        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("device_id", android_id);
        params.put("device_type", "A");
        params.put("firebase_reg_no", firebase_reg_no);
        params.put("email", get_email);
        params.put("password", get_pswrd);
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("parames", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(LoginActivity.this);
                Log.e("respLogin", response.toString());
                try {
                    JSONObject resultResponce = MethodClass.get_result_from_webservice(LoginActivity.this, response);
                    if (resultResponce != null) {


                        String status = resultResponce.getString("status");
                        if (status.equals("A")) {
                            JSONObject userdata = resultResponce.getJSONObject("userdata");
                            String id = userdata.getString("id");
                            String fname = userdata.getString("fname");
                            String lname = userdata.getString("lname");
                            String email = userdata.getString("email");
                            String token = resultResponce.getString("token");

                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("token", token).commit();
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("fname", fname).commit();
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("lname", lname).commit();
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("user_id", id).commit();
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putString("email", email).commit();
                            PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit().putBoolean("is_logged_in", true).commit();

                            Intent intent = new Intent(LoginActivity.this, NewOrderActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                        } else if (status.equals("U")) {
                            LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_WARNING)
                                    .setTitle(resultResponce.getString("message"))
                                    .setDescription(resultResponce.getString("meaning"))
                                    .setPositiveText(getString(R.string.ok))
                                    .setPositiveListener(new ClickListener() {
                                        @Override
                                        public void onClick(LottieAlertDialog lottieAlertDialog) {
                                            lottieAlertDialog.dismiss();
                                            //resendOtp();
                                        }
                                    })
                                    .build();
                            alertDialog.show();
                        } else if (status.equals("I")) {
                            LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(LoginActivity.this, DialogTypes.TYPE_ERROR)
                                    .setTitle(resultResponce.getString("message"))
                                    .setDescription(resultResponce.getString("meaning"))
                                    .setPositiveText(getString(R.string.ok))
                                    .setPositiveListener(new ClickListener() {
                                        @Override
                                        public void onClick(LottieAlertDialog lottieAlertDialog) {
                                            lottieAlertDialog.dismiss();
                                        }
                                    })
                                    .build();
                            alertDialog.show();
                        }

                    }
                } catch (JSONException e) {
                    MethodClass.error_alert(LoginActivity.this);
                    e.printStackTrace();
                    Log.e("login_parce", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MethodClass.hideProgressDialog(LoginActivity.this);
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(LoginActivity.this);
                } else {
                    MethodClass.error_alert(LoginActivity.this);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void forgotPassword(View view) {
        MethodClass.go_to_next_activity(this,ForgotPasswordActivity.class);
    }
}