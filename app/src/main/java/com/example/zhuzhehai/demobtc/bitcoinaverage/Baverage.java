package com.example.zhuzhehai.demobtc.bitcoinaverage;

import android.util.Log;
import com.example.zhuzhehai.demobtc.model.BitconInfo;
import com.example.zhuzhehai.demobtc.utils.ModelUtils;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Baverage {

    private static final String TAG = "BA API";

    public static final int COUNT_PER_LOAD = 12;

    private static final String API_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTCUSD";

    private static final TypeToken<BitconInfo> Bitcon_Infor = new TypeToken<BitconInfo>(){};


    private static OkHttpClient client = new OkHttpClient();

    private static String accessToken;

    private static Request.Builder authRequestBuilder(String url) {
        return new Request.Builder()//method is "GET"
                .addHeader("Authorization", "Bearer " + accessToken) // -H
                .url(url);//确定url 有效?
    }
        // 为什么throws DribbbleException ???????????????
    private static Response makeRequest(Request request) throws BaverageException {
        try {
            Response response = client.newCall(request).execute(); // 向网站发送请求(OkHttpClient)
            return response;
        } catch (IOException e) {
            throw new BaverageException(e.getMessage());
        }
    }

    private static Response makeGetRequest(String url) throws BaverageException {
        Request request = new Request.Builder().url(url).build(); //通过authRequest build request;
        return makeRequest(request);// 通过request 向网站发送请求(makeRequest) 得到response;
    }

    //解析网站传回的数据 得到需要的data. 传入 response 和 type
    private static <T> T parseResponse(Response response,
                                       TypeToken<T> typeToken) throws BaverageException {

        String responseString;
        try {
            responseString = response.body().string();//get string
        } catch (IOException e) {
            throw new BaverageException(e.getMessage());
        }

        Log.d(TAG, responseString);

        try {
            return ModelUtils.toObject(responseString, typeToken);//string to object
        } catch (JsonSyntaxException e) {
            throw new BaverageException(responseString);
        }
    }

    public static BitconInfo getBitCon() throws BaverageException {
        return parseResponse(makeGetRequest(API_URL), Bitcon_Infor);
    }
}
