package com.donut.app.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonUtils
{
    public static String toJson(Object object, Type type)
    {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(object, type);
        return jsonStr;
    }

    public static <T> T fromJson(String jsonStr, Type type)
    {
        Gson gson = new Gson();
        T response = gson.fromJson(jsonStr, type);
        return response;
    }

}
