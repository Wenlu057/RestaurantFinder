package com.wdu.restaurantfinder;

import android.os.Handler;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class HttpRequest {
    public interface Callback{
        void onResponse(JSONArray response);
    }

    public static void get(final Callback callback, final String latitude, final String longitude) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                final JSONArray response;
                try {
                    response = new YelpAPI().request(latitude,longitude);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }catch (JSONException e){
                    throw new RuntimeException(e);
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onResponse(response);
                    }
                });
            }
        }).start();
    }
}
