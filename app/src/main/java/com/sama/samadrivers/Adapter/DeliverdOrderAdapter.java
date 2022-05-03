package com.sama.samadrivers.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.opengl.Visibility;
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
import com.sama.samadrivers.Activity.OrderDetailsActivity;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import static com.sama.samadrivers.Common.Constant.STATUS;
import static com.sama.samadrivers.Common.Constant.TOTAL_ITEM;
import static com.sama.samadrivers.Common.Constant.TOTAL_PRICE;
import static com.sama.samadrivers.Helper.MethodClass.isNetworkConnected;
import static com.sama.samadrivers.Helper.MethodClass.openWhatsAppConversation;

public class DeliverdOrderAdapter extends RecyclerView.Adapter<DeliverdOrderAdapter.ViewHolder> {
    Activity activity;
    public ArrayList<HashMap<String, String>> map_list;

    public DeliverdOrderAdapter(Activity activity, ArrayList<HashMap<String, String>> map_list) {
        this.activity = activity;
        this.map_list = map_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.delivered_order_item, parent, false);
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
            //holder.trackorder.setVisibility(View.VISIBLE);
            holder.status.setTextColor(activity.getResources().getColor(R.color.green_txt_clr));
        } else if (map.get(STATUS).equals("S")) {
            holder.status.setText(activity.getString(R.string.shipped_status));
            //holder.trackorder.setVisibility(View.VISIBLE);
            holder.status.setTextColor(activity.getResources().getColor(R.color.orange_clr));
        } else if (map.get(STATUS).equals("D")) {
            holder.status.setText(activity.getString(R.string.delivered_status));
            //holder.trackorder.setVisibility(View.GONE);
            holder.status.setTextColor(activity.getResources().getColor(R.color.green_txt_clr));
        }

        try {
            String datestr = map.get(CREATE_DATE);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = dateFormat.parse(datestr);
            holder.order_date.setText(activity.getString(R.string.order_on) + android.text.format.DateFormat.format("dd, MMM yyyy", date));
        } catch (Exception e) {

        }

        try {
            String dlv_datestr = map.get(DELIVERY_DATE)+" "+ map.get(DELIVERY_TIME);
//            String dlv_time_datestr = map.get(DELIVERY_TIME);
            DateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat1.parse(dlv_datestr);
//            Log.d("Delivery_Date_Time:",dlv_datestr);
            holder.delivery_date.setText(activity.getString(R.string.deliver_on) + android.text.format.DateFormat.format("dd, MMM yyyy HH:mm:ss", date1));
        } catch (Exception e) {

        }

        if (Double.parseDouble(map.get(SHIPPING_PRICE)) != 0) {
            holder.shipping_price_tv.setText(activity.getString(R.string.shippin_price_coln) + (map.get(SHIPPING_PRICE) + " " + map.get(CURR_CODE)));
            holder.shipping_price_tv.setVisibility(View.VISIBLE);
        } else {
            holder.shipping_price_tv.setVisibility(View.GONE);
        }

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
        private TextView order_no, invoice_no, order_date, delivery_date, delivery_time, total_price, paybale_mode, item_count, status, shipping_price_tv;
        private MaterialCardView item;
        Button callUser, callWhatsapp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item);
            order_no = itemView.findViewById(R.id.order_no);

            invoice_no = itemView.findViewById(R.id.invoice_no);
            order_date = itemView.findViewById(R.id.order_date);

            delivery_date = itemView.findViewById(R.id.delivery_date);
            delivery_time = itemView.findViewById(R.id.delivery_time);
            total_price = itemView.findViewById(R.id.total_price);
            paybale_mode = itemView.findViewById(R.id.paybale_mode);
            item_count = itemView.findViewById(R.id.item_count);
            status = itemView.findViewById(R.id.status);
            shipping_price_tv = itemView.findViewById(R.id.shipping_price_tv);

            callUser = itemView.findViewById(R.id.callCust);
            callWhatsapp = itemView.findViewById(R.id.callWhatsapp);
        }
    }
}
