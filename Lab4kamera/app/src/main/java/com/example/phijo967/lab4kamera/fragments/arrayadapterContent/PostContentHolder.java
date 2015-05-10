package com.example.phijo967.lab4kamera.fragments.arrayadapterContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostContentHolder {

    /**
     * An array of sample (dummy) items.
     */
    public static List<PostItem> ITEMS = new ArrayList<PostItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static Map<String, PostItem> ITEM_MAP = new HashMap<String, PostItem>();

    static {
        // Add 3 sample items.
        addItem(new PostItem("1", "Item 1"));
        addItem(new PostItem("2", "Item 2"));
        addItem(new PostItem("3", "Item 3"));
    }

    private static void addItem(PostItem item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

}
