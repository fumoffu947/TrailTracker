package com.example.phijo967.lab2listfrag;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends  Activity implements ListItemFragment.OnFragmentInteractionListener
        , DetailedItemFragment.OnFragmentInteractionListener{

    public static Boolean dualFrag = false;
    private RetainedFragment retainedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FragmentManager fm = getFragmentManager();
        retainedFragment = (RetainedFragment) fm.findFragmentByTag("data");
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build(); // fixar sa man far gora internet anrop pa annan trad
        StrictMode.setThreadPolicy(policy);
        if (findViewById(R.id.item_list) == null) {
            dualFrag = false;
            getFragmentManager().beginTransaction().
                    replace(R.id.container, new ListItemFragment(), "listFrag").commit();
        } else {
            dualFrag = true;
        }
        if (retainedFragment == null) {
            retainedFragment = new RetainedFragment();
            fm.beginTransaction().add(retainedFragment, "data").commit();
            retainedFragment.setData(false);
        }
        if (!retainedFragment.getData()) {
            retainedFragment.setData(true);
            JSONArray seq = null;
            try {
                seq = NetworkCalls.getGroups(NetworkCalls.doNetworkCall(""));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int index = 0; index < seq.length(); index++) {
                try {
                    NetworkCalls.createAddContent(seq.getString(index), index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentInteraction(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(DetailedItemFragment.ARG_PARAM1, id);
        DetailedItemFragment fragment = new DetailedItemFragment();
        fragment.setArguments(arguments);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void switchBack() {
        getFragmentManager().beginTransaction()
                .replace(R.id.container, new ListItemFragment())
                .commit();
    }




}
