package com.yc.emotion.home.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * 敏感词过滤 工具类   -- 【匹配度高，可以使用】
 * 《高效精准》敏感字&词过滤：http://blog.csdn.net/hubiao_0618/article/details/45076871
 *
 * @author hubiao
 * @version 0.1
 * @CreateDate 2015年4月16日 15:28:32
 */
public class SensitiveWord {
    private static StringBuilder replaceAll;//初始化
    private String encoding = "UTF-8";
    private static String replceStr = "*";
    private static int replceSize = 500;
    private String fileName = "CensorWords.txt";
    private List<String> arrayList;
    public static Set<String> sensitiveWordSet;//包含的敏感词列表，过滤掉重复项
    public static List<String> sensitiveWordList;//包含的敏感词列表，包括重复项，统计次数

    private static List<String> words = new ArrayList<>();

    /**
     * 初始化敏感词库
     */
    public static void initWords(Context context) {
        replaceAll = new StringBuilder(replceSize);
        for (int x = 0; x < replceSize; x++) {
            replaceAll.append(replceStr);
        }
        try {
            InputStream is = context.getAssets().open("keyword.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = br.readLine()) != null) {
                words.add(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * @param str 将要被过滤信息
     * @return 过滤后的信息
     */
    public static String filterInfo(Context context, String str) {


        if (words.size() == 0) {
            initWords(context);
        }
        String result = "";
        if (RegexUtils.isMatch("\\d{1}", str)) {
            result = str;
            return result;
        }

        for (String word : words) {
            if (str.contains(word)) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < word.length(); i++) {
                    sb.append("*");
                }
                result = str.replace(word, sb.toString());
                break;
            }
            if (word.contains(str) || word.equals(str)) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < str.length(); i++) {
                    sb.append("*");
                }
                result = sb.toString();
                break;
            }
        }

        return result;
    }

}

