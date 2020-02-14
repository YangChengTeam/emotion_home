package com.yc.emotion.home.message.manager;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

public class HistoryDataManager {

//    public final static String DefaultAccessKey = "OTgjNDRiNmU3MjItNGI5YS00ODIyLTgxM2YtYjM3YmUwOWY3MmEwIzRiYTk1OGQwLWYzODQtNGM3NS1hYzY4LWY1N2U5Y2JmZjA0MCM1MTcxYWU2ZWE4OWI2NWI4MjM1YTUxYzI0OGNlYWM5MA==";
    public final static String DefaultAccessKey = "MTQzODEjYjk1YzMwNGYtZmZkZi00ZDMyLWI0M2QtMWE1ODhjNTQzZDBjIzE3MGY0N2FiLTE0NmUtNDgzMi1hYTRmLTMyZTk4YmRiNzNlNiMyYzg2NjFiZjg4YWE5OTFhOWJkYmFmMGFlYjIxMjgzYw==";



    public class HistoryDataItem {
        public String accessKey;
        public String robotName;
    }

    private static HistoryDataManager instance;

    public static synchronized HistoryDataManager getInstance() {
        if (instance == null) instance = new HistoryDataManager();
        return instance;
    }
    //AccessKey

    public String getRecentAccessKey(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String accessKey = sharedPreferences.getString(key, DefaultAccessKey);
        return accessKey;
    }

    public void setRecentAccessKey(Context context, String key, String accessKey) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString(key, accessKey);
        sharedPreferencesEditor.commit();
    }

    public List<HistoryDataItem> getHistoryDataList(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String accessKeyHistory = sharedPreferences.getString("AccessKeyHistory", "");

        List<HistoryDataItem> historyDataList = new ArrayList<HistoryDataItem>();
        for (String historyItem : accessKeyHistory.split(";")) {
            String[] items = historyItem.split(":");
            if (items.length < 2) continue;

            HistoryDataItem item = new HistoryDataItem();
            item.accessKey = items[0];
            item.robotName = items[1];
            historyDataList.add(item);
        }

        return historyDataList;
    }

    public void addHistory(Context context, String accessKey, String robotName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        String accessKeyHistory = sharedPreferences.getString("AccessKeyHistory", "");

        StringBuilder historyDataList = new StringBuilder();
        historyDataList.append(accessKey);
        historyDataList.append(':');
        historyDataList.append(robotName);

        for (String historyItem : accessKeyHistory.split(";")) {
            if (historyItem.length() > 0 && !historyItem.startsWith(accessKey)) {
                historyDataList.append(';');
                historyDataList.append(historyItem);
            }
        }

        SharedPreferences.Editor sharedPreferencesEditor = sharedPreferences.edit();
        sharedPreferencesEditor.putString("AccessKeyHistory", historyDataList.toString());
        sharedPreferencesEditor.commit();
    }
}
