package io.github.mthli.Geeky.Article;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

public class CircularAdapter extends CircularLoopAdapter {
    private Context context;
    private List<ArticleItem> list;

    private RequestQueue requests;

    private DisplayMetrics metrics;
    private int screenWidth;
    private int screenHeight;

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

        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;

        ArticleItem item = getItem(position);
        if (item.getFlag()) {
            setImageView(holder.image, item);
        } else {
            holder.image.setImageBitmap(item.getBitmap());
        }
        holder.title.setText(item.getTitle());
        holder.content.setText(item.getContent());
        holder.date.setText(item.getDate());

        return view;
    }

    private Bitmap fixBitmap(Bitmap bitmap) {
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        float percent = ((float) screenWidth) / ((float) bitmapWidth);
        Matrix matrix = new Matrix();
        matrix.postScale(percent, percent);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
    }

    private void setImageView(final ImageView view, final ArticleItem item) {
        ImageRequest request = new ImageRequest(
                item.getImgLink(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        Animation animation = AnimationUtils.loadAnimation(
                                context,
                                R.anim.image_alpha
                        );
                        if (bitmap.getWidth() > screenWidth || bitmap.getHeight() > screenHeight) {
                            bitmap = fixBitmap(bitmap);
                        }
                        view.setImageBitmap(bitmap);
                        view.startAnimation(animation);
                        item.setFlag(false);
                        item.setBitmap(bitmap);
                    }
                },
                0,
                0,
                Bitmap.Config.ARGB_8888,
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Animation animation = AnimationUtils.loadAnimation(
                                context,
                                R.anim.image_alpha
                        );
                        BitmapDrawable bm = (BitmapDrawable) context.getResources().getDrawable(R.drawable.cover);
                        view.setImageBitmap(bm.getBitmap());
                        view.startAnimation(animation);
                        item.setFlag(false);
                        item.setBitmap(bm.getBitmap());
                    }
                }
        );
        requests.add(request);
    }
}
