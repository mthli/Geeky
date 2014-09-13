package io.github.mthli.Geeky.Main;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainFragment extends ProgressFragment {
    private View view;
    private ListBuddiesLayout buddies;
    private CircularAdapter adapterToday;
    private CircularAdapter adapterHistory;
    private List<ArticleItem> listToday = new ArrayList<ArticleItem>();
    private List<ArticleItem> listHistory = new ArrayList<ArticleItem>();

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private boolean isFirst = true;
    private boolean isLastest = true;

    private RequestQueue requests;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.main_fragment);

        view = getContentView();
        setContentEmpty(false);
        setContentShown(true);

        buddies = (ListBuddiesLayout) view.findViewById(R.id.buddies);
        adapterToday = new CircularAdapter(getActivity(), listToday);
        adapterHistory = new CircularAdapter(getActivity(), listHistory);
        buddies.setAdapters(adapterToday, adapterHistory);

        preferences = getActivity().getSharedPreferences(
                getString(R.string.main_sp),
                Context.MODE_PRIVATE
        );
        editor = preferences.edit();

        requests = Volley.newRequestQueue(view.getContext());

        getHomePage(1);
    }

    private void getHomePage(int pageNum) {
        String url = getString(R.string.homepage_url);
        url = url + pageNum;
        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        /* Do something */
                        getCardItems(s);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        /* Do something */
                    }
                }
        );
        requests.add(request);
    }

    private void getCardItems(String str) {
        String[] arr = str.split(getString(R.string.div_gpcard_list));
        arr = arr[0].split(getString(R.string.div_gpcard));
        List<String> cards = new ArrayList<String>();
        for(int i = 1; i < arr.length; i++) {
            arr[i] = arr[i].substring(0, arr[i].length() - 6);
            cards.add(arr[i]);
        }
        parser(true, cards);
    }

    private void parser(boolean flag, List<String> cards) {
        if (flag) {
            listToday.clear();
            for (int i = 0; i < cards.size(); i++) {
                /* Do something */
                Document document = Jsoup.parse(cards.get(i));
                Elements elemImgLink = document.getElementsByClass(getString(R.string.class_img_link));
                Elements elemTitle = document.getElementsByClass(getString(R.string.class_title));
                Elements elemContent = document.getElementsByClass(getString(R.string.class_abstract));

                String title = elemTitle.text();
                String content = elemContent.text();
                Date dateToday = new Date();
                SimpleDateFormat format = new SimpleDateFormat(getString(R.string.article_item_date_format));
                String date = format.format(dateToday);
                String imgLink = elemImgLink.attr(getString(R.string.attr_data_src)).split("\\?")[0];
                String articleLink = elemImgLink.attr(getString(R.string.attr_href));

                /* Do something */
                listToday.add(
                        new ArticleItem(
                                title,
                                content,
                                date,
                                imgLink,
                                articleLink,
                                true,
                                null
                        )
                );
            }
            adapterToday.notifyDataSetChanged();
        } else {
            /* Do something */
        }
    }
}
