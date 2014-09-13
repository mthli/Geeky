package io.github.mthli.Geeky.Database;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

public class Article {
    public static final String TABLE = "ARTICLES";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String DATE = "DATE";
    public static final String LINK = "LINK";
    public static final String BITMAP = "BITMAP";

    public static final String CREATE_SQL = "CREATE TABLE "
            + TABLE
            + " ("
            + " TITLE text,"
            + " CONTENT text,"
            + " DATE text,"
            + " LINK text,"
            + " BITMAP blob"
            + ")";

    private String title;
    private String content;
    private String date;
    private String link;
    private Bitmap bitmap;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return this.date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public static String bitmapToString(Bitmap bm) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] bytes = stream.toByteArray();
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);

        return str;
    }

    public static Bitmap stringToBitmap(String str) {
        byte[] bytes = Base64.decode(str, Base64.DEFAULT);
        Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

        return bm;
    }
}
