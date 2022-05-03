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
import com.sama.samadrivers.Adapter.DeliverdOrderAdapter;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.sama.samadrivers.Common.Constant.ADJUSTMENT;
import static com.sama.samadrivers.Common.Constant.CREATE_DATE;
import static com.sama.samadrivers.Common.Constant.CURR_CODE;
import static com.sama.samadrivers.Common.Constant.DELIVERY_DATE;
import static com.sama.samadrivers.Common.Constant.DELIVERY_TIME;
import static com.sama.samadrivers.Common.Constant.INVOICE_NO;
import static com.sama.samadrivers.Common.Constant.ORDER_ID;
import static com.sama.samadrivers.Common.Constant.ORDER_NO;
import static com.sama.samadrivers.Common.Constant.PAYABLE_AMOUNT;
import static com.sama.samadrivers.Common.Constant.PAYMENT_MODE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_COUNTRY_CODE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_PHONE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_PRICE;
import static com.sama.samadrivers.Common.Constant.SHIPPING_STATUS;
import static com.sama.samadrivers.Common.Constant.STATUS;
import static com.sama.samadrivers.Common.Constant.TOTAL_ITEM;
import static com.sama.samadrivers.Common.Constant.TOTAL_PRICE;
import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.setBottomFun2;

public class DeliveredOrderActivity extends AppCompatActivity {
    private RecyclerView recy_view_trend_prod;
    int pastVisiblesItems, visibleItemCount, totalItemCount;
    boolean loading = true;
    ArrayList<HashMap<String,String>> orderArrayList;
    DeliverdOrderAdapter adapter;
    String page="1";
    int totalCount=0;
    ProgressBar progress_circular;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_new_order);
        TextView title=findViewById(R.id.title);
        title.setText(getString(R.string.delivered_order));
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
        params.put("order_status","D");
        params.put("page",page);
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("jsonObject",jsonObject.toString());
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (Objects.equals(page, "1")){
                    MethodClass.hideProgressDialog(DeliveredOrderActivity.this);
                }
                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(DeliveredOrderActivity.this, response);
                    if (resultResponse != null) {
                        totalCount=resultResponse.getInt("orders_count");
                        Log.e("orders_count",""+totalCount);
                        JSONArray orders=resultResponse.getJSONArray("orders");
                        for (int i = 0; i < orders.length(); i++) {
                            JSONObject object=orders.getJSONObject(i);
                            String id=object.getString("id");
                            String order_no=object.getString("order_no");

                            String invoice_no=object.getString("invoice_id");
                            String shipping_country_code=object.getString("shipping_country_code");
                            String shipping_phone=object.getString("shipping_phone");


                            String order_total=object.getString("order_total");
                            String created_at=object.getString("created_at");
                            String delivery_date=object.getString("delivery_date");
                            String delivery_time=object.getString("delivery_time");

                            String total_items=object.getString("total_items");
                            String payment_method=object.getString("payment_method");
                            String status=object.getString("status");
                            JSONObject currency = object.getJSONObject("currency");
                            String currency_code = currency.getString("currency_code");
                            String shipping_price = object.getString("shipping_price");
                            String payable_amount = object.getString("payable_amount");
                            String adjustment = object.getString("adjustment");

                            HashMap<String,String> hashMap=new HashMap<>();
                            hashMap.put(ORDER_ID,id);
                            hashMap.put(ORDER_NO,order_no);

                            hashMap.put(INVOICE_NO,invoice_no);
//                            hashMap.put(SHIPPING_STATUS,shipping_status);
                            hashMap.put(SHIPPING_COUNTRY_CODE,shipping_country_code);
                            hashMap.put(SHIPPING_PHONE,shipping_phone);
                            hashMap.put(PAYABLE_AMOUNT,payable_amount);
                            hashMap.put(ADJUSTMENT,adjustment);

                            hashMap.put(TOTAL_PRICE,order_total);
                            hashMap.put(CREATE_DATE,created_at);
                            hashMap.put(DELIVERY_DATE,delivery_date);
                            hashMap.put(DELIVERY_TIME,delivery_time);

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
                            adapter = new DeliverdOrderAdapter(DeliveredOrderActivity.this,orderArrayList);
                            recy_view_trend_prod.setAdapter(adapter);
                            recyLoadMore();
                        } else {
                            progress_circular.setVisibility(View.GONE);
                            adapter.notifyDataSetChanged();

                        }

                    }
                } catch (JSONException e) {
                    MethodClass.error_alert(DeliveredOrderActivity.this);
                    e.printStackTrace();
                    Log.e("JSONException", e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                 Log.e("error2", error.getMessage());
                if (Objects.equals(page, "1")){
                    MethodClass.hideProgressDialog(DeliveredOrderActivity.this);
                }
                if (error.toString().contains("ConnectException")) {
                    MethodClass.network_error_alert(DeliveredOrderActivity.this);
                } else {
                    MethodClass.error_alert(DeliveredOrderActivity.this);
                }
            }
        }) {
            @Override
            public Map getHeaders() throws AuthFailureError {
                HashMap headers = new HashMap();
                headers.put("Content-Type", "application/json");
                headers.put("X-localization", PreferenceManager.getDefaultSharedPreferences(DeliveredOrderActivity.this).getString("lang", "en"));
                if (PreferenceManager.getDefaultSharedPreferences(DeliveredOrderActivity.this).getBoolean("is_logged_in", false)) {
                    headers.put("Authorization", "Bearer " + PreferenceManager.getDefaultSharedPreferences(DeliveredOrderActivity.this).getString("token", ""));
                }
                Log.e("getHeaders: ", headers.toString());
                return headers;
            }
        };
        MySingleton.getInstance(DeliveredOrderActivity.this).addToRequestQueue(jsonObjectRequest);
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