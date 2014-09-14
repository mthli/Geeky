package io.github.mthli.Geeky.Main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import io.github.mthli.Geeky.Article.ArticleItem;
import io.github.mthli.Geeky.Article.CircularAdapter;
import io.github.mthli.Geeky.R;
import io.github.mthli.Geeky.WebView.MarkdownStyle;
import io.github.mthli.Geeky.WebView.WebViewActivity;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends Activity {
    private ListBuddiesLayout buddies;
    private CircularAdapter adapterLeft;
    private CircularAdapter adapterRight;
    private List<ArticleItem> listLeft = new ArrayList<ArticleItem>();
    private List<ArticleItem> listRight = new ArrayList<ArticleItem>();

    private static final int LIMIT = 10;
    private ArrayList<String> allCards;

    private List<ArticleItem> parser(List<String> cards) {
        List<ArticleItem> list = new ArrayList<ArticleItem>();

        for (String s : cards) {
            Document document = Jsoup.parse(s);
            Elements elemImgLink = document.getElementsByClass(getString(R.string.class_img_link));
            Elements elemTitle = document.getElementsByClass(getString(R.string.class_title));
            Elements elemTime = document.getElementsByClass(getString(R.string.class_publish_time));
            Elements elemContent = document.getElementsByClass(getString(R.string.class_abstract));

            String title = elemTitle.text();
            String date = elemTime.text();
            String content = elemContent.text();
            String imgLink = elemImgLink.attr(getString(R.string.attr_data_src)).split("\\?")[0];
            String articleLink = elemImgLink.attr(getString(R.string.attr_href));

            ArticleItem item = new ArticleItem(
                    title,
                    content,
                    date,
                    imgLink,
                    articleLink,
                    true,
                    null
            );
            list.add(item);
        }

        return list;
    }

    private void setBuddiesData(List<ArticleItem> list) {
        listLeft.clear();
        listRight.clear();
        for(ArticleItem item : list) {
            if (item.getDate().contains("-")) {
                String[] arr = item.getDate().split("-");
                arr[2] = arr[2].split(" ")[0];
                String date = arr[0]
                        + getString(R.string.article_item_date_year)
                        + arr[1]
                        + getString(R.string.article_item_date_mouth)
                        + arr[2]
                        + getString(R.string.article_item_date_day);
                item.setDate(date);
            }
        }
        Collections.sort(list);
        for (int i = 0; i < LIMIT / 2; i++) {
            listLeft.add(list.get(i + 5));
            listRight.add(list.get(i));
        }
        adapterLeft.notifyDataSetChanged();
        adapterRight.notifyDataSetChanged();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SystemBarTintManager manager = new SystemBarTintManager(this);
            manager.setStatusBarTintEnabled(true);
            int color = getResources().getColor(R.color.gpcard_image_bg_green);
            manager.setTintColor(color);
        }
        getActionBar().setTitle(null);

        buddies = (ListBuddiesLayout) findViewById(R.id.buddies);
        adapterLeft = new CircularAdapter(this, listLeft);
        adapterRight = new CircularAdapter(this, listRight);
        buddies.setAdapters(adapterLeft, adapterRight);

        allCards = getIntent().getStringArrayListExtra(
                getString(R.string.init_intent_list)
        );
        setBuddiesData(parser(allCards));

        buddies.setOnItemClickListener(new ListBuddiesLayout.OnBuddyItemClickListener() {
            @Override
            public void onBuddyItemClicked(AdapterView<?> parent, View view, int buddy, int position, long id) {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                switch (buddy) {
                    case 0:
                        intent.putExtra(
                                getString(R.string.main_intent_url),
                                listLeft.get(position).getArticleLink()
                        );
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra(
                                getString(R.string.main_intent_url),
                                listRight.get(position).getArticleLink()
                        );
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.main_menu_about:
                showAboutDialog();
                break;
            default:
                break;
        }

        return true;
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

    private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.about_label);

        String str = null;
        try {
            InputStream inputStream = getResources().getAssets().open(
                    getString(R.string.about_readme)
            );
            str = IOUtils.toString(inputStream);
        } catch (IOException i) {
            /* Do nothing */
        }

        WebView webView = new WebView(MainActivity.this);
        webView.loadDataWithBaseURL(
                MarkdownStyle.BASE_URL,
                str,
                null,
                getString(R.string.webview_encoding),
                null
        );

        builder.setNegativeButton(R.string.about_button_close, null);
        builder.setView(webView);
        builder.setInverseBackgroundForced(true);
        builder.setCancelable(true);
        builder.create();
        builder.show();
    }
}
