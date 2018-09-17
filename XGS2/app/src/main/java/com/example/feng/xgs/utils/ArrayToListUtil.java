package com.example.feng.xgs.utils;

import android.text.TextUtils;

import java.util.ArrayList;

/**
 * Created by feng on 2018/5/17 0017.
 * 数组转集合
 */

public class ArrayToListUtil {

    public static ArrayList<String> array2List(String result){
        if(TextUtils.isEmpty(result)){
            return null;
        }

        ArrayList<String> list = new ArrayList<>();
        String[] array = result.split(",");
        for (int i = 0; i < array.length; i++) {
            list.add(array[i]);
        }

        return list;
    }
}
