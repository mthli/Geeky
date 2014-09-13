package io.github.mthli.Geeky.Main;

import android.app.ActionBar;
import android.os.Bundle;
import android.view.View;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devspark.progressfragment.ProgressFragment;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import io.github.mthli.Geeky.Article.ArticleItem;
import io.github.mthli.Geeky.Article.CircularAdapter;
import io.github.mthli.Geeky.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainFragment extends ProgressFragment {
    private View view;
    private ListBuddiesLayout buddies;
    private CircularAdapter adapterLeft;
    private CircularAdapter adapterRight;
    private List<ArticleItem> listLeft = new ArrayList<ArticleItem>();
    private List<ArticleItem> listRight = new ArrayList<ArticleItem>();

    private ActionBar actionBar;

    private RequestQueue requests;

    private static final int LIMIT = 10;
    List<String> allCards = new ArrayList<String>();

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
            listLeft.add(list.get(i));
            listRight.add(list.get(i + 5));
        }
        adapterLeft.notifyDataSetChanged();
        adapterRight.notifyDataSetChanged();
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
                            setBuddiesData(parser(allCards));
                            actionBar.show();
                            actionBar.setTitle(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        /* Do something */
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
                            setBuddiesData(parser(allCards));
                            actionBar.show();
                            actionBar.setTitle(null);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        /* Do something */
                    }
                }
        );
        requests.add(requestRight);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.main_fragment);

        view = getContentView();
        setContentEmpty(false);
        setContentShown(true);

        buddies = (ListBuddiesLayout) view.findViewById(R.id.buddies);
        adapterLeft = new CircularAdapter(getActivity(), listLeft);
        adapterRight = new CircularAdapter(getActivity(), listRight);
        buddies.setAdapters(adapterLeft, adapterRight);

        actionBar = getActivity().getActionBar();

        requests = Volley.newRequestQueue(view.getContext());

        getHomePage();
    }
}
