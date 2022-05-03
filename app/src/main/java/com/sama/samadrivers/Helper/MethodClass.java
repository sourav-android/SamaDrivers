package com.sama.samadrivers.Helper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Activity.DeliveredOrderActivity;
import com.sama.samadrivers.Activity.EditUserActivity;
import com.sama.samadrivers.Activity.GrabbedOrderActivity;
import com.sama.samadrivers.Activity.LoginActivity;
import com.sama.samadrivers.Activity.NewOrderActivity;
import com.sama.samadrivers.Adapter.CountryCodeRecyAdapter;
import com.sama.samadrivers.Adapter.CountryNameRecy2Adapter;
import com.sama.samadrivers.Adapter.CountryNameRecyAdapter;
import com.sama.samadrivers.Adapter.CountryNameRecyAdapter3;
import com.sama.samadrivers.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MethodClass {


    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static void setBottomFun(Activity activity) {
        MeowBottomNavigation bottomNavigation = activity.findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_new_order));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_grabbed));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_delivered));
        bottomNavigation.add(new MeowBottomNavigation.Model(4, R.drawable.ic_user_94));
        bottomNavigation.add(new MeowBottomNavigation.Model(5, R.drawable.ic_baseline_exit_to_app_24));

        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {
                // your codes
                if (item.getId() == 1) {
                    if (!activity.getLocalClassName().equals("Activity.NewOrderActivity")) {
                        Intent intent = new Intent(activity, NewOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 2) {
                    if (!activity.getLocalClassName().equals("Activity.GrabbedOrderActivity")) {
                        Intent intent = new Intent(activity, GrabbedOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 3) {
                    if (!activity.getLocalClassName().equals("Activity.DeliveredOrderActivity")) {
                        Intent intent = new Intent(activity, DeliveredOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 4) {
                    if (!activity.getLocalClassName().equals("Activity.EditUserActivity")) {
                        Intent intent = new Intent(activity, EditUserActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 5) {
                    LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
                            .setTitle("Logout")
                            .setDescription("Do you want to logout?")
                            .setPositiveText(activity.getString(R.string.yes))
                            .setNegativeText(activity.getString(R.string.no))
                            .setPositiveListener(new ClickListener() {
                                @Override
                                public void onClick(LottieAlertDialog lottieAlertDialog) {
                                    lottieAlertDialog.dismiss();
                                    Userlogout(activity);
                                }
                            }).setNegativeListener(new ClickListener() {
                                @Override
                                public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                    lottieAlertDialog.dismiss();
                                }
                            })
                            .build();
                    alertDialog.show();

                }
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                if (item.getId() == 1) {
                    if (!activity.getLocalClassName().equals("Activity.NewOrderActivity")) {
                        Intent intent = new Intent(activity, NewOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 2) {
                    if (!activity.getLocalClassName().equals("Activity.GrabbedOrderActivity")) {
                        Intent intent = new Intent(activity, GrabbedOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 3) {
                    if (!activity.getLocalClassName().equals("Activity.DeliveredOrderActivity")) {
                        Intent intent = new Intent(activity, DeliveredOrderActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 4) {
                    if (!activity.getLocalClassName().equals("Activity.EditUserActivity")) {
                        Intent intent = new Intent(activity, EditUserActivity.class);
                        activity.startActivity(intent);
                    }
                }
                if (item.getId() == 5) {
                    LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
                            .setTitle("Logout")
                            .setDescription("Do you want to logout?")
                            .setPositiveText(activity.getString(R.string.yes))
                            .setNegativeText(activity.getString(R.string.no))
                            .setPositiveListener(new ClickListener() {
                                @Override
                                public void onClick(LottieAlertDialog lottieAlertDialog) {
                                    lottieAlertDialog.dismiss();
                                    Userlogout(activity);
                                }
                            }).setNegativeListener(new ClickListener() {
                                @Override
                                public void onClick(@NotNull LottieAlertDialog lottieAlertDialog) {
                                    lottieAlertDialog.dismiss();
                                }
                            })
                            .build();
                    alertDialog.show();

                }
            }
        });

    }

    public static void setBottomFun2(Activity activity, MeowBottomNavigation bottomNavigation){
        if (activity.getLocalClassName().equals("Activity.NewOrderActivity")) {
            bottomNavigation.show(1, true);
        } else if (activity.getLocalClassName().equals("Activity.GrabbedOrderActivity")) {
            bottomNavigation.show(2, true);
        } else if (activity.getLocalClassName().equals("Activity.DeliveredOrderActivity")) {
            bottomNavigation.show(3, true);
        } else if (activity.getLocalClassName().equals("Activity.EditUserActivity")) {
            bottomNavigation.show(4, true);
        }
    }

    public static void hide_keyboard(Context context) {
        ((AppCompatActivity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public static void go_to_next_activity(Activity activity, Class next_activity) {
        activity.startActivity(new Intent(activity, next_activity));
    }


    static Dialog mDialog;

    public static void showProgressDialog(Activity activity) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
        mDialog = new Dialog(activity);
        mDialog.setCancelable(false);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setContentView(R.layout.custom_progress_dialog);
        mDialog.show();
    }

    public static void hideProgressDialog(Activity activity) {
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    public static boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static JSONObject Json_rpc_format(HashMap<String, String> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        return new JSONObject(main_param);
    }

    public static JSONObject Json_rpc_format_obj(HashMap<String, Object> params) {
        HashMap<String, Object> main_param = new HashMap<String, Object>();
        main_param.put("params", new JSONObject(params));
        main_param.put("jsonrpc", "2.0");
        return new JSONObject(main_param);
    }

    public static boolean isNetworkConnected(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    public static JSONObject get_result_from_webservice(Activity activity, JSONObject response) {
        JSONObject result = null;
        if (response.has("error")) {
            try {
                String error = response.getString("error");
                String error_msg = response.getString("message");

                JSONObject jsonObject = new JSONObject(error);
//                if (jsonObject.getString("code").equals("-33085")) {
                if (jsonObject.getString("code").equals("-33085") || jsonObject.getString("message").equals("token_invalid")) {
                    Toast.makeText(activity, jsonObject.getString("meaning"), Toast.LENGTH_SHORT).show();
                    Map<String, ?> prefs = PreferenceManager.getDefaultSharedPreferences(activity).getAll();
                    for (Map.Entry<String, ?> prefToReset : prefs.entrySet()) {
                        if (!prefToReset.getKey().equals("lang") && !prefToReset.getKey().equals("currency") && !prefToReset.getKey().equals("currency_code") && !prefToReset.getKey().equals("conv_factor") && !prefToReset.getKey().equals("country_id") && !prefToReset.getKey().equals("country_code") && !prefToReset.getKey().equals("is_first_time")) {
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().remove(prefToReset.getKey()).commit();
                        }
                    }

                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("token", "").commit();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("fname", "").commit();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("lname", "").commit();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("user_id", "").commit();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("email", "").commit();
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("is_logged_in", false).commit();

                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);


                } else {
                    if (jsonObject.has("message")) {

                        if (jsonObject.has("meaning")) {
                            LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                                    .setTitle(jsonObject.getString("message"))
                                    .setDescription(jsonObject.getString("meaning"))
                                    .setPositiveText(activity.getString(R.string.ok))
                                    .setPositiveListener(new ClickListener() {
                                        @Override
                                        public void onClick(LottieAlertDialog lottieAlertDialog) {
                                            lottieAlertDialog.dismiss();
                                        }
                                    })
                                    .build();
                            alertDialog.show();

                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("token", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("fname", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("lname", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("user_id", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("email", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("is_logged_in", false).commit();

                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);

                        } else {
                            LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                                    .setTitle(jsonObject.getString("message"))
                                    .setPositiveText(activity.getString(R.string.ok))
                                    .setPositiveListener(new ClickListener() {
                                        @Override
                                        public void onClick(LottieAlertDialog lottieAlertDialog) {
                                            lottieAlertDialog.dismiss();
                                        }
                                    })
                                    .build();
                            alertDialog.show();

                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("token", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("fname", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("lname", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("user_id", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("email", "").commit();
                            PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("is_logged_in", false).commit();

                            Intent intent = new Intent(activity, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            activity.startActivity(intent);
                        }

                    } else {
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                                .setTitle(jsonObject.getString("message"))
                                .setPositiveText(activity.getString(R.string.ok))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                                        lottieAlertDialog.dismiss();
                                    }
                                })
                                .build();
                        alertDialog.show();

                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("token", "").commit();
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("fname", "").commit();
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("lname", "").commit();
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("user_id", "").commit();
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putString("email", "").commit();
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("is_logged_in", false).commit();

                        Intent intent = new Intent(activity, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        activity.startActivity(intent);
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (response.has("result")) {
            try {
                result = response.getJSONObject("result");
            } catch (JSONException e) {
                e.printStackTrace();
                Log.e("methodeJSONException", e.toString());
            }

        } else {
            Log.e("methodeElse", "methode Else");
            error_alert(activity);
        }
        return result;
    }

    public static void error_alert(Activity activity) {
        try {
            if (!activity.isFinishing()) {
                LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                        .setTitle(activity.getString(R.string.oops))
                        .setDescription(activity.getString(R.string.something_went_wrong_please_try_again_later))
                        .setPositiveText(activity.getString(R.string.ok))
                        .setPositiveListener(new ClickListener() {
                            @Override
                            public void onClick(LottieAlertDialog lottieAlertDialog) {
                                lottieAlertDialog.dismiss();
                            }
                        })
                        .build();
                alertDialog.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void network_error_alert(final Activity activity) {
        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_ERROR)
                .setTitle(activity.getString(R.string.network_error))
                .setDescription(activity.getString(R.string.internet_connectivity_issue_occured_please_try_again))
                .setPositiveText(activity.getString(R.string.setting))
                .setNegativeText(activity.getString(R.string.ok))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                        activity.startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 1);
                        lottieAlertDialog.dismiss();
                    }
                }).setNegativeListener(new ClickListener() {
                    @Override
                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
                    }
                })
                .build();
        alertDialog.show();
    }

    public static void openWhatsAppConversation(Context context, String number, String message) {

        number = number.replace(" ", "").replace("+", "");

        Intent sendIntent = new Intent("android.intent.action.MAIN");

        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, message);
        sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
        sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");

        context.startActivity(sendIntent);
    }

    public static class StringWithTag {
        public String string;
        public Object tag;

        public StringWithTag(String stringPart, Object tagPart) {
            string = stringPart;
            tag = tagPart;
        }

        @Override
        public String toString() {
            return string;
        }
    }

    public static class CountryModelName {
        public String countryName;
        public String countrySortName;
        public String countryCode;
        public Object tag;

        public CountryModelName(String countryName, String countrySortName, String countryCode, Object tagPart) {
            this.countryName = countryName;
            this.countrySortName = countrySortName;
            this.countryCode = countryCode;
            this.tag = tagPart;
        }

        @Override
        public String toString() {
            return countryName;
        }
    }

    public static class CountryModelName3 {
        public String shippingAllowed;
        public String countryName;
        public String countrySortName;
        public String countryCode;
        public Object tag;

        public CountryModelName3(String shippingAllowed,String countryName, String countrySortName, String countryCode, Object tagPart) {
            this.shippingAllowed = shippingAllowed;
            this.countryName = countryName;
            this.countrySortName = countrySortName;
            this.countryCode = countryCode;
            this.tag = tagPart;
        }

        @Override
        public String toString() {
            return countryName;
        }
    }

    public static class CountryModelName2 {
        public String countryName;
        public String countrySortName;
        public String countryCode;
        public String currencyCode;
        public String currencyConvert;
        public Object tag;

        public CountryModelName2(String countryName, String countrySortName, String countryCode, String currencyCode, String currencyConvert, Object tagPart) {
            this.countryName = countryName;
            this.countrySortName = countrySortName;
            this.countryCode = countryCode;
            this.currencyCode = currencyCode;
            this.currencyConvert = currencyConvert;
            this.tag = tagPart;
        }

        @Override
        public String toString() {
            return countryName;
        }
    }

    public static class CountryModelCode implements Serializable {
        public String countryName;
        public String countrySortName;
        public String countryCode;
        public String tag;

        public CountryModelCode(String countryName, String countrySortName, String countryCode, String tagPart) {
            this.countryName = countryName;
            this.countrySortName = countrySortName;
            this.countryCode = countryCode;
            this.tag = tagPart;
        }

    }


    public static void Userlogout(final Activity activity) {
        if (!isNetworkConnected(activity)) {
            MethodClass.network_error_alert(activity);
            return;
        }
        MethodClass.showProgressDialog(activity);
//        String server_url = activity.getString(R.string.SERVER_URL) + "logout-driver";

        String server_url = activity.getString(R.string.SERVER_BASE_URL) + "logout-driver";

        HashMap<String, String> params = new HashMap<String, String>();
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(activity);
                Log.e("respLogout", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(activity, response);
                    PreferenceManager.getDefaultSharedPreferences(activity).edit().putBoolean("is_logged_in", false).commit();
                    Map<String, ?> prefs = PreferenceManager.getDefaultSharedPreferences(activity).getAll();
                    for (Map.Entry<String, ?> prefToReset : prefs.entrySet()) {
                        PreferenceManager.getDefaultSharedPreferences(activity).edit().remove(prefToReset.getKey()).commit();
                       /* if (!prefToReset.getKey().equals("lang") && !prefToReset.getKey().equals("currency") && !prefToReset.getKey().equals("currency_code") && !prefToReset.getKey().equals("conv_factor") && !prefToReset.getKey().equals("country_id") && !prefToReset.getKey().equals("country_code") && !prefToReset.getKey().equals("is_first_time")) {

                        }*/
                    }
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                } catch (Exception e) {
                    MethodClass.error_alert(activity);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("error2", error.getMessage());
                MethodClass.hideProgressDialog(activity);
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(activity);
                } else {
                    MethodClass.error_alert(activity);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(activity).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(activity).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(activity).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
    }

    public static String checkNull(Object data) {
        if (data.equals(null) || data.equals("null") || data.equals("") || data.equals("Select")) {
            return "";
        } else {
            return String.valueOf(data);
        }
    }

    public static String noFormat(double d) {
        d = noRound(d, 2);
        if (d == (long) d)
            return String.format(Locale.ENGLISH,"%d", (long) d);
        else
            return String.format(Locale.ENGLISH,"%s", d);
    }

    public static double noRound(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}
