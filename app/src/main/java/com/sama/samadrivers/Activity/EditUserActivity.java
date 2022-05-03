package com.sama.samadrivers.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.SimpleMultiPartRequest;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.Helper.RequestManager;
import com.sama.samadrivers.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.setBottomFun2;


public class EditUserActivity extends AppCompatActivity {
    private EditText fname,lname, email;
    private Button submit;
    private EditText pass, confpass;
    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_edit_user);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.edit_profile));
        MethodClass.setBottomFun(this);
        fname = findViewById(R.id.fname);
        lname = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        submit = findViewById(R.id.submit);
        pass = findViewById(R.id.pass);
        confpass = findViewById(R.id.confpass);
        button1 = findViewById(R.id.button1);
        button2 = findViewById(R.id.button2);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_user();
            }
        });

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
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confpass.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                    confpass.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    confpass.setTransformationMethod(new PasswordTransformationMethod());
                } else {
                    confpass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    confpass.setTransformationMethod(new HideReturnsTransformationMethod());
                }
            }
        });
        getUser();

    }

    @Override
    protected void onResume() {
        super.onResume();
        MeowBottomNavigation bottomNavigation=findViewById(R.id.bottomNavigation);
        setBottomFun2(this,bottomNavigation);
    }

    public void update_user() {
        if (fname.getText().toString().length() == 0) {
            fname.setError(getString(R.string.enter_first_name));
            fname.requestFocus();
            return;
        }
        if (lname.getText().toString().length() == 0) {
            lname.setError(getString(R.string.enter_last_name));
            lname.requestFocus();
            return;
        }
        if (!MethodClass.emailValidator(email.getText().toString().trim())) {
            email.setError(getString(R.string.enteremail));
            email.requestFocus();
            return;
        }

        if (pass.getText().toString().trim().length()!=0 || confpass.getText().toString().trim().length()!=0) {
            if (pass.getText().toString().length() == 0) {
                pass.setError(getString(R.string.enterpas));
                pass.requestFocus();
                return;
            }
            if (!confpass.getText().toString().trim().equals(pass.getText().toString().trim())) {
                confpass.setError(getString(R.string.confpasswrr));
                confpass.requestFocus();
                return;
            }
        }

        MethodClass.showProgressDialog(this);
        if (!isNetworkConnected(this)) {
            MethodClass.network_error_alert(this);
            return;
        }

//        String server_url = getString(R.string.SERVER_URL) + "driver-update-profile";
        String server_url = getString(R.string.SERVER_BASE_URL) + "driver-update-profile";

        Log.e("server_url", server_url);
        SimpleMultiPartRequest simpleMultiPartRequest = new SimpleMultiPartRequest(Request.Method.POST, server_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("onResponse: ", response);
                MethodClass.hideProgressDialog(EditUserActivity.this);
                try {
                    JSONObject jsonObject = MethodClass.get_result_from_webservice(EditUserActivity.this, new JSONObject(response));
                    if (jsonObject != null) {
                        String message = jsonObject.getString("message");
                        String meaning = jsonObject.getString("meaning");
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(EditUserActivity.this, DialogTypes.TYPE_SUCCESS)
                                .setTitle(message)
                                .setDescription(meaning)
                                .setPositiveText(getString(R.string.ok))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                                        lottieAlertDialog.dismiss();
                                    }
                                })
                                .build();
                        alertDialog.show();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                getUser();
                            }
                        });
                    }
                } catch (JSONException e) {
                    MethodClass.error_alert(EditUserActivity.this);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("error2", error.getMessage());
                MethodClass.hideProgressDialog(EditUserActivity.this);
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(EditUserActivity.this);
                } else {
                    MethodClass.error_alert(EditUserActivity.this);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        RequestQueue requestQueue = RequestManager.getnstance(EditUserActivity.this);
        simpleMultiPartRequest.addMultipartParam("fname", "text", fname.getText().toString().trim());
        simpleMultiPartRequest.addMultipartParam("lname", "text", lname.getText().toString().trim());

        if (pass.getText().toString().trim().length()!=0 || confpass.getText().toString().trim().length()!=0) {
            simpleMultiPartRequest.addMultipartParam("new_password", "text", pass.getText().toString().trim());
            simpleMultiPartRequest.addMultipartParam("confirm_password", "text", confpass.getText().toString().trim());
        }
        simpleMultiPartRequest.setFixedStreamingMode(true);
        int socketTimeout = 100000;
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 3, 5);
        simpleMultiPartRequest.setRetryPolicy(policy);
        requestQueue.getCache().clear();
        requestQueue.add(simpleMultiPartRequest);
    }


    public void getUser() {
        MethodClass.showProgressDialog(this);
//        String server_url = getString(R.string.SERVER_URL) + "show-profile";

        String server_url = getString(R.string.SERVER_BASE_URL) + "show-profile";
        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url, MethodClass.Json_rpc_format(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("resp", response.toString());

                try {
                    MethodClass.hideProgressDialog(EditUserActivity.this);
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(EditUserActivity.this, response);
                    if (resultResponse != null) {
                        JSONObject user=resultResponse.getJSONObject("user");
                        String fname_str = MethodClass.checkNull(user.getString("fname"));
                        String lname_str = MethodClass.checkNull(user.getString("lname"));
                        String email_str = MethodClass.checkNull(user.getString("email"));
                        fname.setText(fname_str);
                        lname.setText(lname_str);
                        email.setText(email_str);

                        PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).edit().putString("fname", fname_str).commit();
                        PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).edit().putString("lname", lname_str).commit();
                        PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).edit().putString("email", email_str).commit();

                    }
                } catch (JSONException e) {
                    MethodClass.hideProgressDialog(EditUserActivity.this);
                    MethodClass.error_alert(EditUserActivity.this);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MethodClass.hideProgressDialog(EditUserActivity.this);
                MethodClass.error_alert(EditUserActivity.this);
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(EditUserActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(EditUserActivity.this).addToRequestQueue(request);
    }


    public void back(View view) { onBackPressed();}

}