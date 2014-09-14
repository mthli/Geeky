package io.github.mthli.Geeky.WebView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devspark.progressfragment.ProgressFragment;
import io.github.mthli.Geeky.R;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebViewFragment extends ProgressFragment {
    private View view;
    private WebView webView;
    private Intent intent;

    private RequestQueue requests;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setContentView(R.layout.webview_fragment);

        view = getContentView();
        webView = (WebView) view.findViewById(R.id.webview);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setLayoutAlgorithm(
                WebSettings.LayoutAlgorithm.NARROW_COLUMNS.NORMAL
        );
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setDisplayZoomControls(false);

        requests = Volley.newRequestQueue(view.getContext());

        intent = getActivity().getIntent();

        getArticlePage();
    }

    public void getArticlePage() {
        setContentEmpty(false);
        setContentShown(false);

        String base = getString(R.string.website_url);
        String url = base
                + intent.getStringExtra(getString(R.string.main_intent_url));

        StringRequest request = new StringRequest(
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String str) {
                        ((WebViewActivity) getActivity()).setFlag(false);
                        load(parser(str));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        ((WebViewActivity) getActivity()).setFlag(false);
                        setContentEmpty(true);
                        setEmptyText(R.string.webview_text_status_failed);
                        setContentShown(true);
                    }
                }
        );
        requests.add(request);
    }

    private String parser(String str) {
        Document document = Jsoup.parse(str);

        Element element = document.getElementById(
                getString(R.string.id_article)
        );

        element.select(
                getString(R.string.div_share)
        ).remove();

        Elements s = element.select(
                getString(R.string.tag_img)
        );
        String src = s.get(0).attr(getString(R.string.attr_data_origin_src));

        for (Element e : s) {
            e.removeAttr(getString(R.string.attr_srcset));
        }

        element.select(
                getString(R.string.tag_figure)
        ).remove();

        String content = getString(R.string.tag_image_begin)
                + src
                + getString(R.string.tag_image_end)
                + element.html();

        content = content.replace(
                getString(R.string.attr_data_origin_src),
                getString(R.string.attr_src)
        );

        return content;
    }

    private void load(String content) {
        webView.loadDataWithBaseURL(
                MarkdownStyle.BASE_URL,
                MarkdownStyle.getMarkdownStyle(content),
                null,
                getString(R.string.webview_encoding),
                null
        );
        setContentEmpty(false);
        setContentShown(true);
    }
}
