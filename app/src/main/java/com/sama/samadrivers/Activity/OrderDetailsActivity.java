package com.sama.samadrivers.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Adapter.OrderDetailsAdapter;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sama.samadrivers.Common.Constant.CART_DETAILS_ID;
import static com.sama.samadrivers.Common.Constant.CART_MASTER_ID;
import static com.sama.samadrivers.Common.Constant.CATEGORY;
import static com.sama.samadrivers.Common.Constant.CURR_CODE;
import static com.sama.samadrivers.Common.Constant.INTERNAL_STATUS;
import static com.sama.samadrivers.Common.Constant.INVOICE_NO;
import static com.sama.samadrivers.Common.Constant.LEFT_AXIS;
import static com.sama.samadrivers.Common.Constant.LEFT_B_C;
import static com.sama.samadrivers.Common.Constant.LEFT_CYL;
import static com.sama.samadrivers.Common.Constant.LEFT_EYE;
import static com.sama.samadrivers.Common.Constant.LEFT_SPH;
import static com.sama.samadrivers.Common.Constant.ORDER_ID;
import static com.sama.samadrivers.Common.Constant.ORIGINAL_PRICE;
import static com.sama.samadrivers.Common.Constant.POWER;
import static com.sama.samadrivers.Common.Constant.PRODUCT_DESC;
import static com.sama.samadrivers.Common.Constant.PRODUCT_ID;
import static com.sama.samadrivers.Common.Constant.PRODUCT_IMAGE;
import static com.sama.samadrivers.Common.Constant.PRODUCT_PRICE;
import static com.sama.samadrivers.Common.Constant.PRODUCT_QTY;
import static com.sama.samadrivers.Common.Constant.PRODUCT_TITLE;
import static com.sama.samadrivers.Common.Constant.RESCHEDULE_DATE;
import static com.sama.samadrivers.Common.Constant.RESCHEDULE_TIME;
import static com.sama.samadrivers.Common.Constant.RIGHT_AXIS;
import static com.sama.samadrivers.Common.Constant.RIGHT_B_C;
import static com.sama.samadrivers.Common.Constant.RIGHT_CYL;
import static com.sama.samadrivers.Common.Constant.RIGHT_EYE;
import static com.sama.samadrivers.Common.Constant.RIGHT_SPH;
import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.setBottomFun2;

public class OrderDetailsActivity extends AppCompatActivity {
    private RecyclerView recy_view_trend_prod;
    private ArrayList<HashMap<String, String>> arrayList;
    private NestedScrollView scrollView;
    private TextView order_no,invoice_no,order_date,reschedule_date,total_price,paybale_mode,paybale_mode2,item_count,status,discount_tv;
    private TextView name,address,phone;
    private TextView name_1,address_1,phone_1;
    private TextView wallet_tv,paybal_amount_tv;
    private TextView shipping_price_tv;
    private String driver_notes="";
    private Button add_note, return_store, reschedule;
    TextView choose_date_time;
    public String ch_date_time = "";
    private LinearLayout reSchdle;

//    int day, month, year, hour, minute;
//    int myday, myMonth, myYear, myHour, myMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_order_detais);
        TextView title = findViewById(R.id.title);
        title.setText(getString(R.string.order_details));
        MethodClass.setBottomFun(this);
        recy_view_trend_prod = findViewById(R.id.recy_view_trend_prod);
        order_no=findViewById(R.id.order_no);

        reSchdle = findViewById(R.id.reSchdle);
        invoice_no=findViewById(R.id.invoice_no);
        order_date=findViewById(R.id.order_date);
        reschedule_date =findViewById(R.id.rescdule_date);
        total_price=findViewById(R.id.total_price);
        discount_tv=findViewById(R.id.discount_tv);
        paybale_mode=findViewById(R.id.paybale_mode);
        paybale_mode2=findViewById(R.id.paybale_mode2);
        item_count=findViewById(R.id.item_count);
        status=findViewById(R.id.status);
        scrollView=findViewById(R.id.scrollView);
        scrollView.setVisibility(View.GONE);
        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        phone = findViewById(R.id.phone);
        name_1 = findViewById(R.id.name_1);
        address_1 = findViewById(R.id.address_1);
        phone_1 = findViewById(R.id.phone_1);
        wallet_tv = findViewById(R.id.wallet_tv);
        paybal_amount_tv = findViewById(R.id.paybal_amount_tv);
        shipping_price_tv = findViewById(R.id.shipping_price_tv);
        add_note = findViewById(R.id.add_note);
        return_store = findViewById(R.id.return_store);
        reschedule = findViewById(R.id.reschedule);

        getdata();
    }
    @Override
    protected void onResume() {
        super.onResume();
        MeowBottomNavigation bottomNavigation=findViewById(R.id.bottomNavigation);
        setBottomFun2(this,bottomNavigation);
    }

    /*public void back(View view) {
        if(isTaskRoot()){
            startActivity(new Intent(OrderDetailsActivity.this,NewOrderActivity.class));
            // using finish() is optional, use it if you do not want to keep currentActivity in stack
            finish();
        }else{
            super.onBackPressed();
        }
    }*/

    public void getdata() {
        if (!isNetworkConnected(this)) {
            MethodClass.network_error_alert(this);
            return;
        }
        MethodClass.showProgressDialog(this);

//        String server_url = getString(R.string.SERVER_URL) + "driver-view-order-details";

        String server_url = getString(R.string.SERVER_BASE_URL) + "driver-view-order-details";

        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id",getIntent().getStringExtra("order_id"));
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("jsonObject",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(OrderDetailsActivity.this);
                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(OrderDetailsActivity.this, response);
                    if (resultResponse != null) {
                        JSONObject orders=resultResponse.getJSONObject("orders");
                        String id=orders.getString("id");
                        String conv_factor = orders.getString("conv_factor" );
                        JSONObject currency = orders.getJSONObject("currency");
                        String currency_code = currency.getString("currency_code");
                        String order_no_str=orders.getString("order_no");
                        String invoice_no_str=orders.getString("invoice_id");
                        String driver_id=orders.getString("driver_id");
                        String shipping_status=orders.getString("status");

                        String order_total=orders.getString("order_total");
                        String payable_amount=orders.getString("payable_amount");
                        String adjustment = orders.getString("adjustment");

                        String total_discount=orders.getString("total_discount");
                        String created_at=orders.getString("created_at");
                        String reschedule_date1=orders.getString("rescdule_date");
                        String reschedule_time=orders.getString("reschedule_time");
                        String internal_status=orders.getString("internal_status");

                        String total_items=orders.getString("total_items");
                        String payment_method=orders.getString("payment_method");
                        String awb_number=orders.getString("awb_number");
                        String status_str=orders.getString("status");

                        if(Integer.parseInt(driver_id) ==0) {
                            add_note.setVisibility(View.GONE);
                            return_store.setVisibility(View.GONE);
                            reschedule.setVisibility(View.GONE);

                        }

                        if(shipping_status.equals("D")) {
                            add_note.setVisibility(View.GONE);
                            return_store.setVisibility(View.GONE);
                            reschedule.setVisibility(View.GONE);

                        }

                        driver_notes=MethodClass.checkNull(orders.getString("driver_notes"));
                        if (driver_notes.equals("")){
                            add_note.setText("Add Note");
                        }else {
                            add_note.setText("Update Note");
                        }

                        if (Double.parseDouble(orders.getString("shipping_price")) != 0){
                            shipping_price_tv.setText(getString(R.string.shippin_price_coln)+(orders.getString("shipping_price") +" "+currency_code));
                            shipping_price_tv.setVisibility(View.VISIBLE);
                        }else {
                            shipping_price_tv.setVisibility(View.GONE);
                        }

                        if (status_str.equals("N")){
                            status.setText(getString(R.string.new_str));
                            status.setTextColor(getResources().getColor(R.color.blue_clr));
                            //track.setVisibility(View.VISIBLE);
                        }else if (status_str.equals("C")){
                            status.setText(getString(R.string.cancelled));
                            status.setTextColor(getResources().getColor(R.color.red_clr));
                            //track.setVisibility(View.VISIBLE);
                        }else if (status_str.equals("R")){
                            status.setText(getString(R.string.ready_to_ship));
                            status.setTextColor(getResources().getColor(R.color.green_txt_clr));
                            //track.setVisibility(View.VISIBLE);
                        }else if (status_str.equals("S")){
                            status.setText(getString(R.string.shipped_status));
                            status.setTextColor(getResources().getColor(R.color.orange_clr));
                            //track.setVisibility(View.VISIBLE);
                        }else if (status_str.equals("D")){
                            status.setText(getString(R.string.delivered_status));
                            status.setTextColor(getResources().getColor(R.color.green_txt_clr));
                            //track.setVisibility(View.GONE);
                        }

                        if(awb_number.equals("null")){
                            //track.setVisibility(View.GONE);
                        }
                        order_no.setText(getString(R.string.order_no)+order_no_str);

                        if (MethodClass.checkNull(invoice_no_str).equals("")){
//                            invoice_no.setVisibility(View.GONE);
                            invoice_no.setText(getString(R.string.invoice_no)+"");
                        } else {
                            invoice_no.setText(getString(R.string.invoice_no)+invoice_no_str);
                        }

                        if (Double.parseDouble(total_discount)==0){
                            discount_tv.setVisibility(View.GONE);
                        }else {
                            discount_tv.setText(getString(R.string.discount_collon)+total_discount +" "+currency_code);
                            discount_tv.setVisibility(View.VISIBLE);
                        }
                        paybal_amount_tv.setText(getString(R.string.total_paybale_amount_collon) + payable_amount + " " + currency_code);

                        if (Double.parseDouble(payable_amount) == 0) {
                            paybal_amount_tv.setVisibility(View.GONE);
                        } else {
                            paybal_amount_tv.setVisibility(View.VISIBLE);
                        }

                        if (Double.parseDouble(adjustment)!=0){
                            wallet_tv.setText(getString(R.string.used_wallet_ball)+" : "+adjustment+" "+currency_code);
                            wallet_tv.setVisibility(View.VISIBLE);
                        }else {
                            wallet_tv.setVisibility(View.GONE);
                        }

                        if(payment_method.equals("O")){
                            total_price.setText(payable_amount +" "+currency_code);
                        } else if(payment_method.equals("C")) {
                            total_price.setText(payable_amount +" "+currency_code);
                        } else {
                            total_price.setText(adjustment +" "+currency_code);
                        }

                        item_count.setText("("+getString(R.string.item_colon)+total_items+")");

                        if (payment_method.equals("O")){
//                            paybale_mode.setText(getString(R.string.payment_mode)+" "+getString(R.string.only_online));
                            paybale_mode.setText(getString(R.string.payment_mode));
                            paybale_mode2.setText(getString(R.string.paid));
                            paybale_mode2.setTextColor(getResources().getColor(R.color.green_txt_clr));
                        }else if (payment_method.equals("C")){
                            paybale_mode.setText(getString(R.string.payment_mode));
                            paybale_mode2.setText(getString(R.string.cash_on_delivery));
                            paybale_mode2.setTextColor(getResources().getColor(R.color.red_clr));
                        }else if (payment_method.equals("W")){
//                            paybale_mode.setText(getString(R.string.payment_mode)+" "+getString(R.string.wallet));
                            paybale_mode.setText(getString(R.string.payment_mode));
                            paybale_mode2.setText(getString(R.string.paid));
                            paybale_mode2.setTextColor(getResources().getColor(R.color.green_txt_clr));
                        }
                        try{
                            DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            Date date=dateFormat.parse(created_at);
                            order_date.setText(getString(R.string.order_on)+android.text.format.DateFormat.format("dd, MMM yyyy",date));
                        }catch (Exception e){

                        }

                        if(internal_status.equals("RESCHEDULED")){

                            try{
                                String datestr2 = reschedule_date1 + " " + reschedule_time;
                                DateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                Date date2 = dateFormat.parse(datestr2);
                                reschedule_date.setText(getString(R.string.reschedule_on)+ "" + android.text.format.DateFormat.format("dd, MMM yyyy HH:mm:ss", date2));
                            }catch (Exception e){

                            }
                        }
                        else
                            reSchdle.setVisibility(View.GONE);


                        name.setText(orders.getString("shipping_full_name"));
                        phone.setText(orders.getString("shipping_phone"));
                        String full_address="";
                        full_address=orders.getString("shipping_address_line_1");
                        full_address=full_address+", "+MethodClass.checkNull(orders.getString("shipping_address_line_2"));
                        full_address=full_address+", "+MethodClass.checkNull(orders.getString("shipping_city"));
                        full_address=full_address+", "+MethodClass.checkNull(orders.getString("shipping_state"));
                        full_address=full_address+", "+MethodClass.checkNull(orders.getString("shipping_country"));
                        full_address=full_address+", "+MethodClass.checkNull(orders.getString("shipping_pincode"));
                        address.setText(full_address);

                        name_1.setText(orders.getString("billing_full_name"));
                        phone_1.setText(orders.getString("billing_phone"));
                        String full_address_1="";
                        full_address_1=orders.getString("billing_address_line_1");
                        full_address_1=full_address_1+", "+MethodClass.checkNull(orders.getString("billing_address_line_2"));
                        full_address_1=full_address_1+", "+MethodClass.checkNull(orders.getString("billing_city"));
                        full_address_1=full_address_1+", "+MethodClass.checkNull(orders.getString("billing_state"));
                        full_address_1=full_address_1+", "+MethodClass.checkNull(orders.getString("billing_country"));
                        full_address_1=full_address_1+", "+MethodClass.checkNull(orders.getString("billing_pincode"));
                        address_1.setText(full_address_1);

                        JSONArray order_master_details=orders.getJSONArray("order_master_details");
                        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
                        for (int i = 0; i < order_master_details.length(); i++) {
                            JSONObject cart_obj = order_master_details.getJSONObject(i);
                            String cart_details_id = cart_obj.getString("id");
                            String order_master_id = cart_obj.getString("order_master_id");
                            String original_price = cart_obj.getString("original_price");
                            String total = cart_obj.getString("total");
                            String quantity = cart_obj.getString("quantity");
                            //String power_type_no_power = cart_obj.getString("power_type_no_power");
                            //String power_type_with_power = cart_obj.getString("power_type_with_power");
                            String power = cart_obj.getString("power");
                            String left_eye = MethodClass.checkNull(cart_obj.getString("left_eye"));
                            String right_eye = MethodClass.checkNull(cart_obj.getString("right_eye"));
                            String left_sph = MethodClass.checkNull(cart_obj.getString("left_sph"));
                            String left_cyl = MethodClass.checkNull(cart_obj.getString("left_cyl"));
                            String left_axis = MethodClass.checkNull(cart_obj.getString("left_axis"));
                            String left_base_curve = MethodClass.checkNull(cart_obj.getString("left_base_curve"));
                            String right_sph = MethodClass.checkNull(cart_obj.getString("right_sph"));
                            String right_cyl = MethodClass.checkNull(cart_obj.getString("right_cyl"));
                            String right_axis = MethodClass.checkNull(cart_obj.getString("right_axis"));
                            String right_base_curve = MethodClass.checkNull(cart_obj.getString("right_base_curve"));


                            JSONObject product_by_language = cart_obj.getJSONObject("product_by_language");
                            String product_id = product_by_language.getString("product_id");
                            String title = product_by_language.getString("title");
                            String description = product_by_language.getString("description");

                            JSONObject default_image = cart_obj.getJSONObject("default_image");
                            String image = default_image.getString("image");

                           /* String category = "";
                            if(!cart_obj.getString("product_sub_category").equals("null")){
                                JSONObject product_sub_category = cart_obj.getJSONObject("product_sub_category");

                                category = product_sub_category.getJSONObject("category").getJSONObject("category_by_language").getString("title");

                            }else {
                                JSONObject product_sub_category = cart_obj.getJSONObject("product_parent_category");

                                category = product_sub_category.getJSONObject("category").getJSONObject("category_by_language").getString("title");

                            }*/
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put(CART_DETAILS_ID, cart_details_id);
                            hashMap.put(CART_MASTER_ID, order_master_id);
                            hashMap.put(ORIGINAL_PRICE, original_price);
                            hashMap.put(PRODUCT_PRICE, total);
                            hashMap.put(PRODUCT_QTY, quantity);
                            hashMap.put(PRODUCT_ID, product_id);
                            hashMap.put(PRODUCT_TITLE, title);
                            hashMap.put(PRODUCT_DESC, description);
                            hashMap.put(PRODUCT_IMAGE, image);
                            hashMap.put(POWER, power);
                            hashMap.put(LEFT_EYE, left_eye);
                            hashMap.put(RIGHT_EYE, right_eye);
                            hashMap.put(LEFT_SPH, left_sph);
                            hashMap.put(LEFT_CYL, left_cyl);
                            hashMap.put(LEFT_AXIS, left_axis);
                            hashMap.put(LEFT_B_C, left_base_curve);
                            hashMap.put(RIGHT_SPH, right_sph);
                            hashMap.put(RIGHT_CYL, right_cyl);
                            hashMap.put(RIGHT_AXIS, right_axis);
                            hashMap.put(RIGHT_B_C, right_base_curve);
                            //hashMap.put(CATEGORY, "");
                            hashMap.put(CURR_CODE, currency_code);
                            arrayList.add(hashMap);
                        }
                        OrderDetailsAdapter adapter = new OrderDetailsAdapter(OrderDetailsActivity.this, arrayList);
                        recy_view_trend_prod.setAdapter(adapter);

                        scrollView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    MethodClass.error_alert(OrderDetailsActivity.this);
                    scrollView.setVisibility(View.GONE);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.e("error2", error.getMessage());
                scrollView.setVisibility(View.GONE);
                MethodClass.hideProgressDialog(OrderDetailsActivity.this);
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(OrderDetailsActivity.this);
                } else {
                    MethodClass.error_alert(OrderDetailsActivity.this);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(OrderDetailsActivity.this).addToRequestQueue(jsonObjectRequest);
    }

    public void back(View view) {
        onBackPressed();
    }


    //driver_id = PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this).getString("driver_id", "");

    public void Add_Note(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_notes);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);
        EditText notes_et = (EditText) dialog.findViewById(R.id.notes_et);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notes_et.setText(Html.fromHtml(driver_notes, Html.FROM_HTML_MODE_LEGACY));
        } else {
            notes_et.setText(Html.fromHtml(driver_notes));
        }
        //notes_et.setText(driver_notes);

        Button submit = (Button) dialog.findViewById(R.id.submit);
        Button cancel = (Button) dialog.findViewById(R.id.cancel);
        ImageView cancel1 = (ImageView) dialog.findViewById(R.id.cancel1);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postNotes(OrderDetailsActivity.this,notes_et,dialog);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void postNotes(Activity activity,EditText editText,Dialog dialog) {
        if (editText.getText().toString().trim().length() == 0) {
            editText.setError("please enter yor notes");
            editText.requestFocus();
            return;
        }
        if (!isNetworkConnected(activity)) {
            MethodClass.network_error_alert(activity);
            return;
        }
        MethodClass.showProgressDialog(activity);
        String server_url = "";
//        server_url = activity.getString(R.string.SERVER_URL) + "update-note";
        server_url = activity.getString(R.string.SERVER_BASE_URL) + "update-note";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id",getIntent().getStringExtra("order_id"));
        params.put("driver_notes",editText.getText().toString().trim());
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("server_url", server_url);
        Log.e("jsonObject", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(activity);
                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(activity, response);
                    if (resultResponse != null) {
                        driver_notes=editText.getText().toString().trim();
                        dialog.dismiss();
                        String message = resultResponse.getString("message");
                        String meaning = resultResponse.getString("meaning");
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_SUCCESS)
                                .setTitle(message)
                                .setDescription(meaning)
                                .setPositiveText(activity.getString(R.string.ok))
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

                            }
                        });
                    }
                } catch (JSONException e) {
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

    public void Return_store(View view) {
        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(OrderDetailsActivity.this, DialogTypes.TYPE_QUESTION)
                .setTitle("Return to store")
                .setDescription("Do you want to Return to store?")
                .setPositiveText(OrderDetailsActivity.this.getString(R.string.ok))
                .setNegativeText(OrderDetailsActivity.this.getString(R.string.no))
                .setPositiveListener(new ClickListener() {
                    @Override
                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                        lottieAlertDialog.dismiss();
//                        grabOrder(map.get(ORDER_ID),position);
                        Store_return(OrderDetailsActivity.this,getIntent().getStringExtra("order_id"));
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

    public void Store_return(Activity activity,String id){
        if (!isNetworkConnected(activity)) {
            MethodClass.network_error_alert(activity);
            return;
        }
        MethodClass.showProgressDialog(activity);
        String serverUrl = "";
        serverUrl = activity.getString(R.string.SERVER_BASE_URL) + "return-to-store";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id",id);
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("server_url", serverUrl);
        Log.e("jsonObject", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, serverUrl, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(activity);
                Log.e("respUser2", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(activity, response);
                    if (resultResponse != null) {

                        String message = resultResponse.getString("message");
                        String meaning = resultResponse.getString("meaning");
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_SUCCESS)
                                .setTitle(message)
                                .setDescription(meaning)
                                .setPositiveText(activity.getString(R.string.ok))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                                        lottieAlertDialog.dismiss();
                                        Intent returnList = new Intent(OrderDetailsActivity.this,NewOrderActivity.class);
                                        returnList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        activity.startActivity(returnList);
                                        activity.finish();
                                    }
                                })
                                .build();
                        alertDialog.show();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                    }
                } catch (JSONException e) {
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

    public void Reschedule(View view) {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.select_reschedule);
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(true);

        choose_date_time = (TextView) dialog.findViewById(R.id.reschedule_date_time);
        Button re_save = (Button) dialog.findViewById(R.id.re_save);
        Button re_cancel = (Button) dialog.findViewById(R.id.re_cancel);
        ImageView re_cancel1 = (ImageView) dialog.findViewById(R.id.re_cancel1);

        re_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                save_date(OrderDetailsActivity.this,ch_date_time,dialog);
            }
        });


        choose_date_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateTimeDialog(choose_date_time);

            }
        });

        re_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        re_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void showDateTimeDialog(TextView choose_date_time) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        choose_date_time.setText(android.text.format.DateFormat.format("yyyy-MM-dd HH:mm:ss", calendar));
                        ch_date_time = choose_date_time.getText().toString();

                    }
                };

                new TimePickerDialog(OrderDetailsActivity.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(OrderDetailsActivity.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void save_date(Activity activity,String ch_date_time,Dialog dialog){

        if (!isNetworkConnected(activity)) {
            MethodClass.network_error_alert(activity);
            return;
        }
        MethodClass.showProgressDialog(activity);
        String server_url = "";
//        server_url = activity.getString(R.string.SERVER_URL) + "update-note";
        server_url = activity.getString(R.string.SERVER_BASE_URL) + "reschedule-order";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id",getIntent().getStringExtra("order_id"));
        params.put("rescdule_date_time",ch_date_time);

        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("server_url", server_url);
        Log.e("jsonObject", jsonObject.toString());

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(activity);
                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(activity, response);
                    if (resultResponse != null) {

                        dialog.dismiss();
                        String rescdule_date_time = resultResponse.getString("rescdule_date_time");
                        PreferenceManager.getDefaultSharedPreferences(OrderDetailsActivity.this).edit().putString("rescdule_date_time", rescdule_date_time).commit();
                        String message = resultResponse.getString("message");
                        String meaning = resultResponse.getString("meaning");
                        LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_SUCCESS)
                                .setTitle(message)
                                .setDescription(meaning)
                                .setPositiveText(activity.getString(R.string.ok))
                                .setPositiveListener(new ClickListener() {
                                    @Override
                                    public void onClick(LottieAlertDialog lottieAlertDialog) {
                                        lottieAlertDialog.dismiss();
                                        getdata();
                                        Intent returnList = new Intent(OrderDetailsActivity.this,GrabbedOrderActivity.class);
                                        returnList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        activity.startActivity(returnList);
                                        activity.finish();


//                                        Intent returnList = new Intent(OrderDetailsActivity.this,NewOrderActivity.class);
//                                        returnList.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                        activity.startActivity(returnList);
//                                        activity.finish();

                                    }
                                })
                                .build();
                        alertDialog.show();
                        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {

                            }
                        });
                    }
                } catch (JSONException e) {
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


}