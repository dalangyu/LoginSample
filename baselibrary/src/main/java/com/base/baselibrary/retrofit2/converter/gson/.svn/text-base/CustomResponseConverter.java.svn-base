package com.base.baselibrary.retrofit2.converter.gson;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by 15600 on 2017/9/20.
 */
public class CustomResponseConverter<T> implements Converter<ResponseBody, T> {
    String err = "java.lang.IllegalStateException: Expected BEGIN_OBJECT but was STRING at line 1";
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    public CustomResponseConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String ret = "";
        String msg = "";
        String body = value.string();


        try {
            JSONObject json = new JSONObject(body);

            Log.e("Gson测试", "responsebody=" + value);

            ret = json.optString("retCode");
            msg = json.optString("retMessage", "");
            adapter.fromJson(body);
        } catch (JsonSyntaxException e) {
            body = catchJson(e.getMessage(), ret, msg);
        } catch (JSONException e) {
            body = catchJson(e.getMessage(), ret, msg);
        } finally {
            value.close();
            Log.e("Gson测试", "最终的值" + body);
            return adapter.fromJson(body);
        }
    }


    private String catchJson(String eMessage, String ret,
                             String msg) {
        if (eMessage.indexOf(err) != -1) {

            Log.e("Gson测试", "进来了");
            try {
                JSONObject json = new JSONObject();
                json.put("retCode", ret);
                json.put("retMessage", msg);

                json.put("body", new JSONObject());
                return json.toString();
            } catch (JSONException e1) {
                Log.e("Gson测试", "有报错了吗");
                throw new RuntimeException(eMessage);
            }
        } else {
            throw new RuntimeException(eMessage);
        }
    }

}
