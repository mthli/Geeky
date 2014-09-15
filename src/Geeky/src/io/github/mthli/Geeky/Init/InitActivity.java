package io.github.mthli.Geeky.Init;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import io.github.mthli.Geeky.Main.MainActivity;
import io.github.mthli.Geeky.R;

import java.util.ArrayList;
import java.util.List;

public class InitActivity extends Activity {
    private RequestQueue requests;

    private static final int LIMIT = 10;
    private ArrayList<String> allCards = new ArrayList<String>();

    private List<String> getCardItems(String str) {
        String[] arr = str.split(getString(R.string.div_gpcard_list));
        arr = arr[0].split(getString(R.string.div_gpcard));
        List<String> cards = new ArrayList<String>();
        for(int i = 1; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, arr[i].length() - 6);
            cards.add(arr[i]);
        }

        return cards;
    }

    private void getHomePage() {
        String url = getString(R.string.homepage_url);

        StringRequest requestLeft = new StringRequest(
                url + 1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        List<String> list = getCardItems(str);
                        for (String s : list) {
                            allCards.add(s);
                        }
                        if (allCards.size() >= LIMIT) {
                            Intent intent = new Intent(InitActivity.this, MainActivity.class);
                            intent.putStringArrayListExtra(
                                    getString(R.string.init_intent_list),
                                    allCards
                            );
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        flag = false;
                        Toast.makeText(
                                InitActivity.this,
                                getString(R.string.init_text_status_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
        requests.add(requestLeft);

        StringRequest requestRight = new StringRequest(
                url + 2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        List<String> list = getCardItems(str);
                        for (String s : list) {
                            allCards.add(s);
                        }
                        if (allCards.size() >= LIMIT) {
                            Intent intent = new Intent(InitActivity.this, MainActivity.class);
                            intent.putStringArrayListExtra(
                                    getString(R.string.init_intent_list),
                                    allCards
                            );
                            startActivity(intent);
                            overridePendingTransition(0, 0);
                            finish();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        flag = false;
                        Toast.makeText(
                                InitActivity.this,
                                getString(R.string.init_text_status_failed),
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
        );
        requests.add(requestRight);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            int color = getResources().getColor(R.color.gpcard_image_bg_green);
            manager.setTintColor(color);
        }

        requests = Volley.newRequestQueue(this);

        getHomePage();
    }

    private boolean flag = true;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (flag) {
                /* Do nothing */
            } else {
                finish();
            }
        }

        return false;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if(newConfig.orientation== Configuration.ORIENTATION_LANDSCAPE) {
            /* Do nothing */
        }
        else{
            /* Do nothing */
        }
    }
}
