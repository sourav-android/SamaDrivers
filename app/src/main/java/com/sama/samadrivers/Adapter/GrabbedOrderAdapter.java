package com.sama.samadrivers.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.google.android.material.card.MaterialCardView;
import com.labters.lottiealertdialoglibrary.ClickListener;
import com.labters.lottiealertdialoglibrary.DialogTypes;
import com.labters.lottiealertdialoglibrary.LottieAlertDialog;
import com.sama.samadrivers.Activity.GrabbedOrderActivity;
import com.sama.samadrivers.Activity.OrderDetailsActivity;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.sama.samadrivers.Common.Constant.ADJUSTMENT;
import static com.sama.samadrivers.Common.Constant.CREATE_DATE;
import static com.sama.samadrivers.Common.Constant.CURR_CODE;
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
import static com.sama.samadrivers.Helper.MethodClass.openWhatsAppConversation;

public class GrabbedOrderAdapter extends RecyclerView.Adapter<GrabbedOrderAdapter.ViewHolder> {
    Activity activity;
    public ArrayList<HashMap<String, String>> map_list;

    public GrabbedOrderAdapter(Activity activity, ArrayList<HashMap<String, String>> map_list) {
        this.activity = activity;
        this.map_list = map_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.grabbed_order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final HashMap<String, String> map = map_list.get(position);
        holder.order_no.setText(activity.getString(R.string.order_no) + map.get(ORDER_NO));

        if (MethodClass.checkNull(map.get(INVOICE_NO)).equals("")){
//            holder.invoice_no.setVisibility(View.GONE);
            holder.invoice_no.setText(activity.getString(R.string.invoice_no) + "");
        } else {
            holder.invoice_no.setText(activity.getString(R.string.invoice_no) + map.get(INVOICE_NO));
        }

        if(map.get(PAYMENT_MODE).equals("C")){
            holder.total_price.setText(map.get(PAYABLE_AMOUNT) + " " + map.get(CURR_CODE));
        } else if(map.get(PAYMENT_MODE).equals("O")) {
            holder.total_price.setText(map.get(PAYABLE_AMOUNT) + " " + map.get(CURR_CODE));
        } else {
            holder.total_price.setText(map.get(ADJUSTMENT) + " " + map.get(CURR_CODE));
        }

//        holder.total_price.setText(map.get(TOTAL_PRICE) + " " + map.get(CURR_CODE));

        holder.item_count.setText("(" + activity.getString(R.string.item_colon) + map.get(TOTAL_ITEM) + ")");

        if (map.get(PAYMENT_MODE).equals("C")) {
            holder.paybale_mode.setText(activity.getString(R.string.cod_breaket));
            holder.paybale_mode.setTextColor(activity.getResources().getColor(R.color.red_clr));
        } else if (map.get(PAYMENT_MODE).equals("O")) {
//            holder.paybale_mode.setText(activity.getString(R.string.online_breaket));
            holder.paybale_mode.setText(activity.getString(R.string.paid_breaket));
            holder.paybale_mode.setTextColor(activity.getResources().getColor(R.color.green_txt_clr));
        } else if (map.get(PAYMENT_MODE).equals("W")) {
//            holder.paybale_mode.setText(activity.getString(R.string.wallet_breaket));
            holder.paybale_mode.setText(activity.getString(R.string.paid_breaket));
            holder.paybale_mode.setTextColor(activity.getResources().getColor(R.color.green_txt_clr));
        }

        if (map.get(STATUS).equals("N")) {
            holder.status.setText(activity.getString(R.string.new_str));
            holder.status.setTextColor(activity.getResources().getColor(R.color.blue_clr));
            //holder.trackorder.setVisibility(View.VISIBLE);
            //holder.reorder.setVisibility(View.GONE);
        } else if (map.get(STATUS).equals("C")) {
            holder.status.setText(activity.getString(R.string.cancelled));
            //holder.trackorder.setVisibility(View.GONE);
            //holder.reorder.setVisibility(View.VISIBLE);
            holder.status.setTextColor(activity.getResources().getColor(R.color.red_clr));
        } else if (map.get(STATUS).equals("R")) {
            holder.status.setText(activity.getString(R.string.ready_to_ship));
            holder.change_status.setText(activity.getString(R.string.shipped));
            //holder.trackorder.setVisibility(View.VISIBLE);
            holder.change_status.setBackgroundColor(activity.getResources().getColor(R.color.orange_clr));
            holder.status.setTextColor(activity.getResources().getColor(R.color.orange_clr));
        } else if (map.get(STATUS).equals("S")) {
            holder.status.setText(activity.getString(R.string.shipped_status));
            holder.change_status.setText(activity.getString(R.string.delivered));
            //holder.trackorder.setVisibility(View.VISIBLE);
            holder.change_status.setBackgroundColor(activity.getResources().getColor(R.color.green_txt_clr));
            holder.status.setTextColor(activity.getResources().getColor(R.color.orange_clr));
        } else if (map.get(STATUS).equals("D")) {
            holder.status.setText(activity.getString(R.string.delivered_status));
            //holder.trackorder.setVisibility(View.GONE);
            holder.change_status.setBackgroundColor(activity.getResources().getColor(R.color.green_txt_clr));
            holder.status.setTextColor(activity.getResources().getColor(R.color.green_txt_clr));
        }

        try {
            String datestr = map.get(CREATE_DATE);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(datestr);
            holder.order_date.setText(activity.getString(R.string.order_on) + android.text.format.DateFormat.format("dd, MMM yyyy", date));
        } catch (Exception e) {

        }

        if(map.get(INTERNAL_STATUS).equals("RESCHEDULED")){

            try {
                String datestr2 = map.get(RESCHEDULE_DATE) + " " + map.get(RESCHEDULE_TIME);
                DateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date2 = dateFormat2.parse(datestr2);
                holder.reschedule_date.setText(activity.getString(R.string.reschedule_on)+ android.text.format.DateFormat.format("dd, MMM yyyy HH:mm:ss", date2));
                holder.reschedule_date.setTextColor(activity.getResources().getColor(R.color.blue_clr));
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        else
            holder.reschedule_date.setVisibility(View.GONE);


        if (Double.parseDouble(map.get(SHIPPING_PRICE)) != 0) {
            holder.shipping_price_tv.setText(activity.getString(R.string.shippin_price_coln) + (map.get(SHIPPING_PRICE) + " " + map.get(CURR_CODE)));
            holder.shipping_price_tv.setVisibility(View.VISIBLE);
        } else {
            holder.shipping_price_tv.setVisibility(View.GONE);
        }

        holder.change_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LottieAlertDialog alertDialog = new LottieAlertDialog.Builder(activity, DialogTypes.TYPE_QUESTION)
                        .setTitle("Change Status")
                        .setDescription("Do you want to "+holder.change_status.getText().toString().trim()+" order?")
                        .setPositiveText(activity.getString(R.string.ok))
                        .setNegativeText(activity.getString(R.string.no))
                        .setPositiveListener(new ClickListener() {
                            @Override
                            public void onClick(LottieAlertDialog lottieAlertDialog) {
                                lottieAlertDialog.dismiss();
                                changeStatus(map.get(ORDER_ID),holder.change_status,position);
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
        });

        holder.callUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone_no = map.get(SHIPPING_COUNTRY_CODE) + map.get(SHIPPING_PHONE);
                Uri u = Uri.parse("tel:" + phone_no);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, u);
                activity.startActivity(callIntent);

            }
        });

        holder.callWhatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone_no = map.get(SHIPPING_COUNTRY_CODE) + map.get(SHIPPING_PHONE);

                PackageManager packageManager = activity.getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send/?phone="+phone_no+"+&text=*%D8%B9%D8%B2%D9%8A%D8%B2%D9%8A%20%D8%A7%D9%84%D8%B9%D9%85%D9%8A%D9%84*%20%D8%A3%D9%86%D8%A7%20%D9%85%D9%86%D8%AF%D9%88%D8%A8%20%D8%B4%D8%B1%D9%83%D8%A9%20*_%D8%B3%D9%85%D8%A7%20%D9%84%D9%84%D8%B9%D8%AF%D8%B3%D8%A7%D8%AA%20%D8%A7%D9%84%D9%84%D8%A7%D8%B5%D9%82%D8%A9_*%20,%20%D8%A3%D8%B1%D8%BA%D8%A8%20%D9%81%D9%8A%20%D8%AA%D9%88%D8%B5%D9%8A%D9%84%20%D8%B4%D8%AD%D9%86%D8%AA%D9%83%20%20%D8%A5%D9%84%D9%8A%D9%83%20%D8%A7%D9%84%D9%8A%D9%88%D9%85.%20%D9%8A%D8%B1%D8%AC%D9%89%20%D8%A7%D9%84%D8%AA%D9%83%D8%B1%D9%85%20%D8%A8%D8%A5%D8%B1%D8%B3%D8%A7%D9%84%20%D9%85%D9%88%D9%82%D8%B9%D9%83%20%D9%84%D9%8A%D8%AA%D9%85%20%D8%AA%D9%88%D8%B5%D9%8A%D9%84%20%D8%A7%D9%84%D8%B4%D8%AD%D9%86%D8%A9%20%D8%A8%D8%A3%D8%B3%D8%B1%D8%B9%20%D9%88%D9%82%D8%AA%20%D8%A3%D8%AA%D9%85%D9%86%D9%89%20%D9%84%D9%83%20%D9%8A%D9%88%D9%85%D8%A7%D9%8B%20%D8%B3%D8%B9%D9%8A%D8%AF%D8%A7%D9%8B%20.%20%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6%E2%80%A6......%20*Dear%20Customer,*%20I%20am%20a%20courier%20from%20*_Same%20Contact%20Lenses_*%20%20,%20I%20will%20deliver%20your%20order%20to%20your%20address%20Today.%20Could%20you%20please%20share%20your%20location%20to%20reach%20you%20faster.%20Have%20a%20nice%20day!" + URLEncoder.encode("", "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        activity.startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

//                openWhatsAppConversation(activity,phone_no,"Hi");

            }
        });


        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, OrderDetailsActivity.class);
                intent.putExtra("order_id", map.get(ORDER_ID));
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return map_list == null ? 0 : map_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView order_no, invoice_no, order_date, reschedule_date, total_price, paybale_mode, item_count, status, shipping_price_tv;
        private MaterialCardView item;
        private Button change_status, callUser, callWhatsapp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            order_no = itemView.findViewById(R.id.order_no);

            invoice_no = itemView.findViewById(R.id.invoice_no);
            order_date = itemView.findViewById(R.id.order_date);
            reschedule_date = itemView.findViewById(R.id.rescdle_date);
            total_price = itemView.findViewById(R.id.total_price);
            paybale_mode = itemView.findViewById(R.id.paybale_mode);
            item_count = itemView.findViewById(R.id.item_count);
            status = itemView.findViewById(R.id.status);
            change_status = itemView.findViewById(R.id.change_status);
            shipping_price_tv = itemView.findViewById(R.id.shipping_price_tv);

            callUser = itemView.findViewById(R.id.callCust);
            callWhatsapp = itemView.findViewById(R.id.callWhatsapp);
            shipping_price_tv = itemView.findViewById(R.id.shipping_price_tv);
        }
    }

    public void changeStatus(String order_id,Button grab_button,int position) {
        if (!isNetworkConnected(activity)) {
            MethodClass.network_error_alert(activity);
            return;
        }
        MethodClass.showProgressDialog(activity);
        String server_url = "";
//        server_url = activity.getString(R.string.SERVER_URL) + "change-status";
        server_url = activity.getString(R.string.SERVER_BASE_URL) + "change-status";

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id", order_id);
        JSONObject jsonObject = MethodClass.Json_rpc_format(params);
        Log.e("server_url", server_url);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, server_url, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                MethodClass.hideProgressDialog(activity);
                Log.e("respUser", response.toString());
                try {
                    JSONObject resultResponse = MethodClass.get_result_from_webservice(activity, response);
                    if (resultResponse != null) {
                        if (resultResponse.getString("order_status").equals("S")){
                            notifyItemcha(position);
                        }else if (resultResponse.getString("order_status").equals("D")){
                            removeAt(position);
                        }
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

    public void removeAt(int position) {
        map_list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, map_list.size());
    }

    public void notifyItemcha(int position) {
        map_list.get(position).put(STATUS,"S");
        notifyItemChanged(position);
        notifyItemRangeChanged(position, map_list.size());
    }
}
