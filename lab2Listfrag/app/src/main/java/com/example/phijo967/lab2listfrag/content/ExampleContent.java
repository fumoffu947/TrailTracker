package com.example.phijo967.lab2listfrag.content;

import android.os.AsyncTask;

import com.example.phijo967.lab2listfrag.NetworkCalls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ExampleContent {

    /**
     * An array of sample  items.
     */
    public static List<TestItem> ITEMS = new ArrayList<TestItem>();

    /**
     * A map of sample  items, by ID.
     */
    public static Map<String, TestItem> ITEM_MAP = new HashMap<String, TestItem>();

    public static Boolean hasloaded = false;

    public static Boolean getHasloaded() {
        return hasloaded;
    }

    public static void setHasloaded(Boolean hasloaded) {
        ExampleContent.hasloaded = hasloaded;
    }

    /*static {

         //Add 3 sample items.
        addItem(new TestItem("1", "FaceBook","facebook likes are 5"));
        addItem(new TestItem("2", "Twitter","twitter members are manny"));
        addItem(new TestItem("3", "FlashBack","to manny anwsers"));
    }*/

    public static void addItem(TestItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    public static class TestItem {
        public String members;
        public String name;
        public String id;
        public String content;

        public TestItem(String id, String content, String members) {
            this.id = id;
            this.content = content;
            this.members = members;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}



