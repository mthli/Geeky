package io.github.mthli.Geeky.WebView;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import io.github.mthli.Geeky.R;

public class WebViewActivity extends FragmentActivity {
    private WebViewFragment fragment;

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            int color = getResources().getColor(R.color.gpcard_image_bg_green);
            manager.setTintColor(color);
        }

        ActionBar actionBar = getActionBar();
        actionBar.setTitle(null);
        actionBar.setDisplayHomeAsUpEnabled(true);

        fragment = (WebViewFragment) getSupportFragmentManager().findFragmentById(R.id.webview_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.webview_menu, menu);

        preferences = getSharedPreferences(
                getString(R.string.init_sp),
                MODE_PRIVATE
        );
        editor = preferences.edit();

        MenuItem item = menu.findItem(R.id.webview_menu_match);
        if (preferences.getString(getString(R.string.init_sp_match), "true").equals("true")) { //
            fragment.setFlag(true);
            item.setChecked(true);
        } else {
            fragment.setFlag(false);
            item.setChecked(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case android.R.id.home:
                if (!flag) {
                    finish();
                }
                break;
            case R.id.webview_menu_match:
                if (item.isChecked()) {
                    item.setChecked(false);
                    editor.putString(
                            getString(R.string.init_sp_match),
                            "false"
                    );
                    editor.commit();
                    fragment.setFlag(false);
                    fragment.getArticlePage();
                } else {
                    item.setChecked(true);
                    editor.putString(
                            getString(R.string.init_sp_match),
                            "true"
                    );
                    editor.commit();
                    fragment.setFlag(true);
                    fragment.getArticlePage();
                }
                break;
            case R.id.webview_menu_refresh:
                if (!flag) {
                    flag = true;
                    fragment.getArticlePage();
                }
                break;
            case R.id.webview_menu_share:
                String url = getString(R.string.website_url)
                        +getIntent().getStringExtra(getString(R.string.main_intent_url));
                Intent shareTo = new Intent();
                shareTo.setAction(Intent.ACTION_SEND);
                shareTo.putExtra(Intent.EXTRA_TEXT, url);
                shareTo.setType(getString(R.string.webview_menu_share_type));
                startActivity(
                        Intent.createChooser(
                                shareTo,
                                getString(R.string.webview_menu_share_to)
                        )
                );
                break;
            default:
                break;
        }

        return true;
    }

    private boolean flag = true;

    public boolean getFlag() {
        return flag;
    }
    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!flag) {
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

