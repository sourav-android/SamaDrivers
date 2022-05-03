package com.sama.samadrivers.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sama.samadrivers.Helper.MethodClass;
import com.sama.samadrivers.Helper.MySingleton;
import com.sama.samadrivers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "SplashActivity";
    int k = 0;
    private MediaPlayer mMediaPlayer;
    private GifImageView gifView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*mMediaPlayer = new MediaPlayer();
        mMediaPlayer = MediaPlayer.create(this, R.raw.sound2);
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //mMediaPlayer.setLooping(true);
        mMediaPlayer.start();*/

        /*try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(),
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(messageDigest.digest(), Base64.DEFAULT));
            }
        }
        catch (PackageManager.NameNotFoundException e) {

        }
        catch (NoSuchAlgorithmException e) {

        }*/
        /*AppSignatureHelper appSignatureHelper = new AppSignatureHelper(SplashActivity.this);
        Log.e("TAG", appSignatureHelper.getAppSignatures().get(0) );
        Log.e("haskArray", String.valueOf(appSignatureHelper.getAppSignatures()));*/

        // GOOGLE PLAY APP SIGNING SHA-1 KEY:- 37:01:C9:50:A2:55:5B:9B:62:1D:7E:41:1F:96:A6:90:4D:22:20:7F
       /* byte[] sha1 = {
                0x37, 0x01, (byte) 0xC9, 0x50, (byte) 0xA2, 0x55, 0x5B, (byte) 0x9B, 0x62, 0x1D, 0x7E, 0x41, 0x1F, (byte) 0x96, (byte) 0xA6, (byte) 0x90, 0x4D, 0x22, 0x20, 0x7F
        };
        System.out.println("keyhashGooglePlaySignIn:" + Base64.encodeToString(sha1, Base64.NO_WRAP));
*/
//        gifView = findViewById(R.id.gifView);
//        Glide.with(this)
//                .load(R.drawable.sama5)
//                .listener(new RequestListener<Drawable>() {
//                    @Override
//                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                        if (resource instanceof GifDrawable) {
//                            ((GifDrawable) resource).setLoopCount(1);
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    ((GifDrawable) resource).stop();
//                                    if (k>1){
//                                        finish();
//                                    } else {
//                                        if (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("is_logged_in", false)) {
//                                            Intent I = new Intent(SplashActivity.this, NewOrderActivity.class);
//                                            I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(I);
//                                            finish();
//                                        } else {
//                                            Intent I = new Intent(SplashActivity.this, LoginActivity.class);
//                                            I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                            startActivity(I);
//                                            finish();
//                                        }
//                                    }
//
//                                }
//                            }, 4400);
//
//                        }
//                        return false;
//                    }
//                })
//                .into(gifView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (k>1){
                    finish();
                } else {
                    if (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("is_logged_in", false)) {
//                        String log_in = String.valueOf((PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("is_logged_in", false)));
//                        String ufname = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("fname", ""));
//                        String ulname = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("lname", ""));
//                        String utoken = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("token", ""));
//                        String user_id = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("user_id", ""));
//                        String uemail = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("email", ""));
//
//                        Toast.makeText(SplashActivity.this,"is_logged_in:"+ log_in + "\nfname:" + ufname + "\nlname:" + ulname + "\nuser_id:" + user_id + "\nemail:" + uemail + "\ntoken:" + utoken,Toast.LENGTH_LONG).show();

                        Intent I = new Intent(SplashActivity.this, NewOrderActivity.class);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(I);
                        finish();
                    } else {

//                        String log_in = String.valueOf((PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getBoolean("is_logged_in", false)));
//                        String ufname = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("fname", ""));
//                        String ulname = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("lname", ""));
//                        String utoken = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("token", ""));
//                        String user_id = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("user_id", ""));
//                        String uemail = (PreferenceManager.getDefaultSharedPreferences(SplashActivity.this).getString("email", ""));
//
//                        Toast.makeText(SplashActivity.this,"is_logged_in:"+ log_in + "\nfname:" + ufname + "\nlname:" + ulname + "\nuser_id:" + user_id + "\nemail:" + uemail + "\ntoken:" + utoken,Toast.LENGTH_LONG).show();

                        Intent I = new Intent(SplashActivity.this, LoginActivity.class);
                        I.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(I);
                        finish();
                    }
                }

            }
        }, 2000);

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        String token = task.getResult();
                        Log.e(TAG + "=token", token);
                    }
                });

    }


    public void onBackPressed() {
        Log.e("My Tags", "onBackPressed");
        k++;
        if (k == 1) {
            Toast.makeText(SplashActivity.this, R.string.double_click_to_exit, Toast.LENGTH_SHORT).show();

        } else {
            //mMediaPlayer.stop();
            finish();
        }
    }

}