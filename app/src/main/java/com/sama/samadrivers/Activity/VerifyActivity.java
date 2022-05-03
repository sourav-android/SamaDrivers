package com.sama.samadrivers.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;

public class VerifyActivity extends AppCompatActivity {

    private Button verify;
    private TextView otp;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private String edtxt1, edtxt2, edtxt3, edtxt4, edtxt5, edtxt6;
    private TextView resend_tv;
    private String get_phone_from_intent="",code="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);
        setContentView(R.layout.activity_verify);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.verify));
        otp = findViewById(R.id.otp);
        verify = findViewById(R.id.verify);

        editText1 = (EditText) findViewById(R.id.editText1);
        editText2 = (EditText) findViewById(R.id.editText2);
        editText3 = (EditText) findViewById(R.id.editText3);
        editText4 = (EditText) findViewById(R.id.editText4);
        editText5 = (EditText) findViewById(R.id.editText5);
        editText6 = (EditText) findViewById(R.id.editText6);
        resend_tv = (TextView) findViewById(R.id.resend_tv);

        get_phone_from_intent = getIntent().getStringExtra("email");
        code = getIntent().getStringExtra("code");
        otp.setText("your otp=" + code);
        editText1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editText2.requestFocus();
                } else {
                }
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
        });
        editText2.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editText3.requestFocus();
                } else {
                }

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
        editText3.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editText4.requestFocus();
                } else {
                }

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
        editText4.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editText5.requestFocus();
                } else {
                }

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
        editText5.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    editText6.requestFocus();
                } else {
                }

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });
        editText6.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    if (!editText1.getText().toString().equals("") && !editText2.getText().toString().equals("") && !editText3.getText().toString().equals("") && !editText4.getText().toString().equals("")) {
                        Log.e("editText1", editText1.getText().toString());
                        Log.e("editText2", editText2.getText().toString());
                        Log.e("editText3", editText3.getText().toString());
                        Log.e("editText4", editText4.getText().toString());
                        Log.e("editText5", editText5.getText().toString());
                        Log.e("editText6", editText6.getText().toString());
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                verification();
                            }
                        },2000);
                    } else {
                        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.please_enter_all_the_four_digits_in_order_to_continue), Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                } else {

                    //editText5.setBackground(getDrawable(R.drawable.verify_grey_edit_back));
                }


            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verification();
            }
        });
        resend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });
        setTimer();
        setOnkeyLis();
        editText1.requestFocus();
    }

    private void verification() {
        if (editText1.getText().toString().trim().length() == 0 || editText2.getText().toString().trim().length() == 0 || editText3.getText().toString().trim().length() == 0 || editText4.getText().toString().trim().length() == 0) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), getResources().getString(R.string.entered_verification_code_format_is_invalid), Snackbar.LENGTH_SHORT);
            snackbar.show();
            return;
        }
        edtxt1 = editText1.getText().toString();
        edtxt2 = editText2.getText().toString();
        edtxt3 = editText3.getText().toString();
        edtxt4 = editText4.getText().toString();
        edtxt5 = editText5.getText().toString();
        edtxt6 = editText6.getText().toString();
        String vcode = (edtxt1 + "" + edtxt2 + "" + edtxt3 + "" + edtxt4 + "" + edtxt5 + "" + edtxt6);

        if (!isNetworkConnected(VerifyActivity.this)) {
            MethodClass.network_error_alert(VerifyActivity.this);
            return;
        }

        MethodClass.showProgressDialog(VerifyActivity.this);
//        String server_url = getString(R.string.SERVER_URL) + "driver-verify-otp";

        String server_url = getString(R.string.SERVER_BASE_URL) + "driver-verify-otp";

        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", get_phone_from_intent);
        params.put("vcode", vcode);
        Log.e("parames", MethodClass.Json_rpc_format(params).toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url, MethodClass.Json_rpc_format(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(VerifyActivity.this);
                Log.e("resp", response.toString());

                try {
                    if (response.has("error")) {
                        editText1.setText("");
                        editText2.setText("");
                        editText3.setText("");
                        editText4.setText("");
                        editText5.setText("");
                        editText6.setText("");
                        editText1.requestFocus();

                    }

                    JSONObject resultResponce = MethodClass.get_result_from_webservice(VerifyActivity.this, response);
                    if (resultResponce != null) {
                        Toast.makeText(VerifyActivity.this, getResources().getString(R.string.verification_successful), Toast.LENGTH_SHORT).show();
                        String token = resultResponce.getString("token");
                        JSONObject userdata = resultResponce.getJSONObject("userdata");
                        String id = userdata.getString("id");
                        String fname = userdata.getString("fname");
                        String lname = userdata.getString("lname");
                        String email = userdata.getString("email");

                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putString("token", token).commit();
                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putString("fname", fname).commit();
                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putString("lname", lname).commit();
                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putString("user_id", id).commit();
                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putString("email", email).commit();
                        PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).edit().putBoolean("is_logged_in", true).commit();

                        Intent intent = new Intent(VerifyActivity.this, NewOrderActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                } catch (Exception e) {
                    Log.e("Exception: ", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MethodClass.hideProgressDialog(VerifyActivity.this);
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(VerifyActivity.this).addToRequestQueue(request);
    }


    private void resendOtp() {
        if (!isNetworkConnected(VerifyActivity.this)) {
            MethodClass.network_error_alert(VerifyActivity.this);
            return;
        }
        MethodClass.showProgressDialog(VerifyActivity.this);
//        String server_url = getString(R.string.SERVER_URL) + "driver-forget-password";

        String server_url = getString(R.string.SERVER_BASE_URL) + "driver-forget-password";
        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", get_phone_from_intent);
        Log.e("params", params.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, server_url, MethodClass.Json_rpc_format(params), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(VerifyActivity.this);
                JSONObject jsonObject = MethodClass.get_result_from_webservice(VerifyActivity.this, response);
                try {
                    if (jsonObject != null) {
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(VerifyActivity.this, DialogTypes.TYPE_SUCCESS)
                                .setTitle(jsonObject.getString("message"))
                                .setDescription(jsonObject.getString("meaning"))
                                .setPositiveText(getString(R.string.ok))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                                        lottieAlertDialog.dismiss();
                                    }
                                })
                                .build();
                        alertDialog.show();
                        otp.setText(jsonObject.getString("vcode"));
                        setTimer();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.e("resp", response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MethodClass.hideProgressDialog(VerifyActivity.this);
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(VerifyActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(VerifyActivity.this).addToRequestQueue(request);
    }

    public void back(View view) {
        VerifyActivity.super.onBackPressed();
    }

    @Override
    public void onBackPressed() {
        String et1 = "", et2 = "", et3 = "", et4 = "", et5 = "", et6 = "";

        et1 = editText1.getText().toString().trim();
        et2 = editText2.getText().toString().trim();
        et3 = editText3.getText().toString().trim();
        et4 = editText4.getText().toString().trim();
        et5 = editText5.getText().toString().trim();
        et6 = editText6.getText().toString().trim();

        if (!et6.equals("")) {
            editText6.setText("");
            editText5.requestFocus();
            return;
        }
        if (!et5.equals("")) {
            editText5.setText("");
            editText4.requestFocus();
            return;
        }
        if (!et4.equals("")) {
            editText4.setText("");
            editText3.requestFocus();
            return;
        }
        if (!et3.equals("")) {
            editText3.setText("");
            editText2.requestFocus();
            return;
        }
        if (!et2.equals("")) {
            editText2.setText("");
            editText1.requestFocus();
            return;
        }
        if (!et1.equals("")) {
            editText1.setText("");
            editText1.requestFocus();
            return;
        }
        super.onBackPressed();
    }

    private void setTimer() {
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                resend_tv.setText(getString(R.string.please_wait) + " " + millisUntilFinished / 1000);
                resend_tv.setEnabled(false);
                resend_tv.setClickable(false);
            }

            public void onFinish() {
                resend_tv.setEnabled(true);
                resend_tv.setClickable(true);
                resend_tv.setText(getString(R.string.click_here));
            }

        }.start();
    }




    private void setOnkeyLis() {
        editText6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editText5.requestFocus();
                    editText5.setText("");
                }
                return false;
            }
        });
        editText5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editText4.requestFocus();
                    editText4.setText("");
                }
                return false;
            }
        });
        editText4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editText3.requestFocus();
                    editText3.setText("");
                }
                return false;
            }
        });
        editText3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editText2.requestFocus();
                    editText2.setText("");
                }
                return false;
            }
        });
        editText2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    editText1.requestFocus();
                    editText1.setText("");
                }
                return false;
            }
        });
    }
}