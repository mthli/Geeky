package io.github.mthli.Geeky.Article;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.jpardogo.listbuddies.lib.adapters.CircularLoopAdapter;
import io.github.mthli.Geeky.R;

import java.util.List;
import java.util.Random;

public class CircularAdapter extends CircularLoopAdapter {
    private Context context;
    private List<ArticleItem> list;

    private RequestQueue requests;

    public CircularAdapter(
            Context context,
            List<ArticleItem> list
    ) {
        super();

        this.context = context;
        this.list = list;

        requests = Volley.newRequestQueue(context);
    }

    @Override
    protected int getCircularCount() {
        return list.size();
    }

    @Override
    public ArticleItem getItem(int position) {
        return list.get(getCircularPosition(position));
    }

    private class Holder {
        ImageView image;
        TextView title;
        TextView content;
        TextView date;
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup parent
    ) {
        Holder holder;
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.gpcard, parent, false);

            holder = new Holder();
            holder.image = (ImageView) view.findViewById(R.id.article_image);
            holder.title = (TextView) view.findViewById(R.id.article_title);
            holder.content = (TextView) view.findViewById(R.id.article_content);
            holder.date = (TextView) view.findViewById(R.id.article_date);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        ArticleItem item = getItem(position);
        if (item.getFlag()) {
            /* Do something */
            setImageView(holder.image, item.getImgLink());
        } else {
            /* Do something */
        }
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(item.getDate());

        return view;
    }

    private void setImageView(final ImageView view, String imgLink) {
        ImageRequest request = new ImageRequest(
                imgLink,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Animation animation = AnimationUtils.loadAnimation(
                                context,
                                R.anim.image_anim
                        );
                        view.startAnimation(animation);
                        view.setImageBitmap(bitmap);
                    }
                },
                0,
                0,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        /* Do something */
                    }
                }
        );
        requests.add(request);
    }
}
