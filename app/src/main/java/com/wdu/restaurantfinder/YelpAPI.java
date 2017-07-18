package com.wdu.restaurantfinder;


import org.json.*;
import java.io.IOException;
import okhttp3.*;
import okio.*;

/**
 * Created by wdu on 7/17/2017.
 */

public class YelpAPI {
    public JSONArray request(String latitude,String longitude )throws JSONException, IOException {
        String accessToken=null;
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, "client_id=iYzbRy7B_Ho3NX-1rXvRLQ&client_secret=SCA4VP5nFEaTBSSjwsTaTbWLpT9SqfdFsMjon5eRFD0SSoG6MjQ0dpnZhDcxytvS&grant_type=client_credentials");
        Request request = new Request.Builder()
                .url("https://api.yelp.com/oauth2/token")
                .post(body)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "8d9de8ad-800c-50e1-fb4a-46fcb5f2f209")
                .addHeader("content-type", "application/x-www-form-urlencoded")
                .build();

        try {
            Response response = client.newCall(request).execute();
            JSONObject jsonObjectToken = new JSONObject(response.body().string().trim());
            accessToken = jsonObjectToken.getString("access_token");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



        OkHttpClient client2 = new OkHttpClient();
        String term = "restaurants";                       // term
        Request request2 = new Request.Builder()
                .url("https://api.yelp.com/v3/businesses/search?term=" + term + "&latitude=" + latitude + "&longitude="+longitude+"")
                .get()
                .addHeader("authorization", "Bearer"+" "+accessToken)
                .addHeader("cache-control", "no-cache")
                .addHeader("postman-token", "b5fc33ce-3dad-86d7-6e2e-d67e14e8071b")
                .build();

        try {
            Response response2 = client2.newCall(request2).execute();
            JSONObject jsonObject = new JSONObject(response2.body().string().trim());       // parser
            JSONArray myResponse = (JSONArray)jsonObject.get("businesses");
            return myResponse;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return  null;
    }
}
