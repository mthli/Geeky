package io.github.mthli.Geeky.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class Action {
    private Helper helper;
    private SQLiteDatabase database;

    public Action(Context context) {
        helper = new Helper(context);
    }

    public void openDatabase(boolean rw) {
        if(rw) {
            database = helper.getWritableDatabase();
        } else {
            database = helper.getReadableDatabase();
        }
    }

    public void closeDatabase() {
        helper.close();
    }

    public boolean check(String link) {
        Cursor cursor = database.query(
                Article.TABLE,
                new String[] {Article.LINK},
                Article.LINK + "=?",
                new String[] {link},
                null,
                null,
                null
        );

        if (cursor != null) {
            boolean result = false;
            if (cursor.moveToFirst()) {
                result = true;
            }
            cursor.close();
            return result;
        }
        return false;
    }

    public void add(Article article) {
        ContentValues values = new ContentValues();

        values.put(Article.TITLE, article.getTitle());
        values.put(Article.CONTENT, article.getContent());
        values.put(Article.DATE, article.getDate());
        values.put(Article.LINK, article.getLink());
        values.put(Article.BITMAP, Article.bitmapToString(article.getBitmap()));

        database.insert(Article.TABLE, null, values);
    }

    public void delete(String link) {
        database.execSQL("DELETE FROM "
                        + Article.TABLE
                        + " WHERE "
                        + Article.LINK
                        + " like \""
                        + link
                        + "\""
        );
    }

    public void deleteAll() {
        database.execSQL("DELETE FROM " + Article.TABLE);
    }

    public List<Article> listAll() {
        List<Article> list = new ArrayList<Article>();
        Cursor cursor = database.query(
                Article.TABLE,
                new String[] {
                        Article.TITLE,
                        Article.CONTENT,
                        Article.DATE,
                        Article.LINK,
                        Article.BITMAP
                },
                null,
                null,
                null,
                null,
                Article.LINK // ORDER BY
        );

        if (cursor == null) {
            return list;
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Article article = read(cursor);
            list.add(article);
            cursor.moveToNext();
        }
        cursor.close();

        return list;
    }

    private Article read(Cursor cursor) {
        Article article = new Article();
        article.setTitle(cursor.getString(0));
        article.setContent(cursor.getString(1));
        article.setDate(cursor.getString(2));
        article.setLink(cursor.getString(3));
        article.setBitmap(Article.stringToBitmap(cursor.getString(4)));

        return article;
    }
}
