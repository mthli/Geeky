package io.github.mthli.Geeky.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Helper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Articles.db";
    private static final int DATABASE_VERSION = 1;

    public Helper(Context context) {
        super(
                context,
                DATABASE_NAME,
                null,
                DATABASE_VERSION
        );
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(Article.CREATE_SQL);
    }

    @Override
    public void onUpgrade(
            SQLiteDatabase database,
            int oldVersion,
            int newVersion
    ) {
        /* Do nothing */
    }
}
