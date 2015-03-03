package com.example.phijo967.lab2listfrag;

import android.app.FragmentTransaction;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;


public class MainActivity extends ActionBarActivity implements ListItemFragment.OnFragmentInteractionListener
        , DetailedItemFragment.OnFragmentInteractionListener{

    public static Boolean dualFrag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.
                ThreadPolicy.Builder().permitAll().build(); // fixar sa man far gora internet anrop pa annan trad
        StrictMode.setThreadPolicy(policy);
        if (findViewById(R.id.item_list) == null) {
            getFragmentManager().beginTransaction().
                    add(R.id.container, new ListItemFragment()).commit();
        } else {
            dualFrag = true;
        }


        JSONArray seq = null;
        try {
            seq = NetworkCalls.getGroups(NetworkCalls.doNetworkCall(""));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int index = 0; index < seq.length(); index++) {
            try {
                NetworkCalls.createContent(seq.getString(index), index);
            } catch (JSONException e) {
                e.printStackTrace();
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
