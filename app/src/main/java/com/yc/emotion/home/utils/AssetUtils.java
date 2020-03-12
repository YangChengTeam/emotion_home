package com.yc.emotion.home.utils;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AssetUtils {
    public static String getAssetData(Context context) {
        try {
            InputStream is = context.getAssets().open("liveData.json");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder result = new StringBuilder();

            while ((line = br.readLine()) != null) {
                result.append(line);
            }
            br.close();
            is.close();
            return result.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
