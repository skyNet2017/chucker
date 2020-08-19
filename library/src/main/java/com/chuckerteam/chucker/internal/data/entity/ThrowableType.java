package com.chuckerteam.chucker.internal.data.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ThrowableType {

    public static final String TAG_CRASH = "crash";
    public static final String TAG_block = "block";
    public static final String TAG_leak = "leak";
    public static final String TAG_normal = "normal";


    public static ArrayList<String> types ;

    static {
        types = new ArrayList<>();
        types.add(TAG_CRASH);
        types.add(TAG_block);
        types.add(TAG_leak);
        types.add(TAG_normal);
    }

    public static void addType(String tag){
        if(tag == null || tag.equals("")){
            return;
        }
        if(types.contains(tag)){
            return;
        }
        types.add(tag);
    }
}
