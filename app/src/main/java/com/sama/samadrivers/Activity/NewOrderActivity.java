package com.sama.samadrivers.Activity;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.sama.samadrivers.Adapter.NewOrderAdapter;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sama.samadrivers.Common.Constant.ADJUSTMENT;
import static com.sama.samadrivers.Common.Constant.AWB_CODE;
import static com.sama.samadrivers.Common.Constant.CREATE_DATE;
import static com.sama.samadrivers.Common.Constant.CURR_CODE;
import static com.sama.samadrivers.Common.Constant.DRIVER_ID;
import static com.sama.samadrivers.Common.Constant.INTERNAL_STATUS;
import static com.sama.samadrivers.Common.Constant.INVOICE_NO;
import static com.sama.samadrivers.Common.Constant.ORDER_ID;
import static com.sama.samadrivers.Common.Constant.ORDER_NO;
import static com.sama.samadrivers.Common.Constant.PAYABLE_AMOUNT;
import static com.sama.samadrivers.Common.Constant.PAYMENT_MODE;
import static com.sama.samadrivers.Common.Constant.RESCHEDULE_DATE;
import static com.sama.samadrivers.Common.Constant.RESCHEDULE_TIME;
import static com.sama.samadrivers.Common.Constant.SHIPPING_COUNTRY_CODE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_PHONE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_PRICE;
import static com.sama.samadrivers.Common.Constant.STATUS;
import static com.sama.samadrivers.Common.Constant.TOTAL_ITEM;
import static com.sama.samadrivers.Common.Constant.TOTAL_PRICE;
import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.setBottomFun2;

public class NewOrderActivity extends AppCompatActivity {
    private RecyclerView recy_view_trend_prod;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;
    ArrayList<HashMap<String,String>> orderArrayList;
    NewOrderAdapter adapter;
    String page="1";
    int totalCount=0;
    ProgressBar progress_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_order);
        TextView title=findViewById(R.id.title);
        title.setText(getString(R.string.new_order));
        MethodClass.setBottomFun(this);
        recy_view_trend_prod = findViewById(R.id.recy_view_trend_prod);
        progress_circular = findViewById(R.id.progress_circular);
        orderArrayList=new ArrayList<>();
        list(page);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MeowBottomNavigation bottomNavigation=findViewById(R.id.bottomNavigation);
        setBottomFun2(this,bottomNavigation);
    }

    public void back(View view) {
        onBackPressed();
    }

    public void list(String page) {
        if (!isNetworkConnected(this)) {
            MethodClass.network_error_alert(this);
            return;
        }
        if (Objects.equals(page, "1")){
            MethodClass.showProgressDialog(this);
        }
//        String server_url = getString(R.string.SERVER_URL) + "order-list";
        String server_url = getString(R.string.SERVER_BASE_URL) + "order-list";
        Log.e("server_url", server_url);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_status","R");
        params.put("page",page);
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("jsonObject",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (Objects.equals(page, "1")){
                    MethodClass.hideProgressDialog(NewOrderActivity.this);
                }

                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(NewOrderActivity.this, response);
                    if (resultResponse != null) {
                        totalCount=resultResponse.getInt("orders_count");
                        Log.e("orders_count",""+totalCount);
                        JSONArray orders=resultResponse.getJSONArray("orders");
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject object=orders.getJSONObject(i);
                            String id=object.getString("id");
                            String order_no=object.getString("order_no");
                            String order_total=object.getString("order_total");
                            String created_at=object.getString("created_at");

                            String reschedule_date1=object.getString("rescdule_date");
                            String reschedule_time=object.getString("reschedule_time");
                            String internal_status=object.getString("internal_status");

                            String total_items=object.getString("total_items");
                            String payment_method=object.getString("payment_method");
                            String status=object.getString("status");

                            String invoice_no=object.getString("invoice_id");
                            //String driver_id=object.getString("driver_id");
                            String shipping_country_code=object.getString("shipping_country_code");
                            String shipping_phone=object.getString("shipping_phone");
                            String payable_amount = object.getString("payable_amount");
                            String adjustment = object.getString("adjustment");

                            JSONObject currency = object.getJSONObject("currency");
                            String currency_code = currency.getString("currency_code");
                            String shipping_price = object.getString("shipping_price");

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put(ORDER_ID,id);
                            hashMap.put(ORDER_NO,order_no);

                            hashMap.put(INVOICE_NO,invoice_no);
                            //hashMap.put(DRIVER_ID,driver_id);
                            hashMap.put(SHIPPING_COUNTRY_CODE,shipping_country_code);
                            hashMap.put(SHIPPING_PHONE,shipping_phone);
                            hashMap.put(PAYABLE_AMOUNT,payable_amount);
                            hashMap.put(ADJUSTMENT,adjustment);

                            hashMap.put(TOTAL_PRICE,order_total);
                            hashMap.put(CREATE_DATE,created_at);

                            hashMap.put(RESCHEDULE_DATE,reschedule_date1);
                            hashMap.put(RESCHEDULE_TIME,reschedule_time);
                            hashMap.put(INTERNAL_STATUS,internal_status);

                            hashMap.put(TOTAL_ITEM,total_items);
                            hashMap.put(PAYMENT_MODE,payment_method);
                            hashMap.put(STATUS,status);
                            hashMap.put(CURR_CODE,currency_code);
                            hashMap.put(SHIPPING_PRICE,shipping_price);
                            orderArrayList.add(hashMap);


                        }

                        if (totalCount>orderArrayList.size()){
                            loading=true;
                            Log.e("loading","true");
                        }else {
                            loading=false;
                            Log.e("loading","false");
                        }
                        if (page.equals("1")){
                            recy_view_trend_prod.setAdapter(null);
                            adapter = new NewOrderAdapter(NewOrderActivity.this,orderArrayList);
                            recy_view_trend_prod.setAdapter(adapter);
                            recyLoadMore();
                        }else {
                            progress_circular.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();
                        }




                    }
                } catch (JSONException e) {
                    MethodClass.error_alert(NewOrderActivity.this);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (Objects.equals(page, "1")){
                    MethodClass.hideProgressDialog(NewOrderActivity.this);
                }
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(NewOrderActivity.this);
                } else {
                    MethodClass.error_alert(NewOrderActivity.this);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(NewOrderActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(NewOrderActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(NewOrderActivity.this).getString("token", ""));
//                    headers.put("Authorization", "Bearer " + "eyJoeXAiOiJKV1QiLCJhbGciOiJlUzl1Ni19.eylzdWIiOjU5LCJpc3MiOiJodHRwczovL2FwcC5zYW1hbGVuc2VzLmNvbS9hcGkvbG9naW4tZHJpdmVyliwiaWFOljoxNjIyNjM4MTc1LCJleHALOjE2MjUyMzAxNzUslm5iZi16MTYyMjYzODE3NSwianRpljoidjRUS2dRY1NUMm|2N3RyeCj9.OWUpb42B-5mTn4bretjzH_amOmBerGTAaZa5-BW7pps");
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(NewOrderActivity.this).addToRequestQueue(jsonObjectRequest);
    }


    private void recyLoadMore(){
        recy_view_trend_prod.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (totalCount>orderArrayList.size()){
                    LinearLayoutManager mLinearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                    if (dy > 0) {
                        Log.e("test","reached the last element of recyclerview");
                        visibleItemCount = mLinearLayoutManager.getChildCount();
                        totalItemCount = mLinearLayoutManager.getItemCount();
                        pastVisiblesItems = mLinearLayoutManager.findFirstVisibleItemPosition();
                        if (loading) {
                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false;
                                page=String.valueOf(Integer.parseInt(page)+1);
                                progress_circular.setVisibility(View.VISIBLE);
                                list(page);
                            }
                        }
                    }
                }

            }
        });
    }

}