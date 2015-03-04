package com.example.phijo967.lab2listfrag;

import android.app.Fragment;
import android.os.Bundle;

/**
 * Created by phijo967 on 2015-03-03.
 */
public class RetainedFragment extends Fragment {


    private Boolean data;

        // this method is only called once for this fragment
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(Boolean data) {
        this.data = data;
    }

    public Boolean getData() {
        return data;
    }

}
