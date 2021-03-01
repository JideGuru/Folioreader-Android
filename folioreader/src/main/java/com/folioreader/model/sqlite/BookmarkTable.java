package com.folioreader.model.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author syed afshan on 24/12/20.
 */

public class BookmarkTable {

    public static final String TABLE_NAME = "bookmark_table";

    public static final String ID = "_id";
    public static final String bookID = "bookID";
    public static final String date = "date";
    public static final String name = "name";
    public static final String readlocator = "readlocator";

    public static SQLiteDatabase Bookmarkdatabase;

    public BookmarkTable(Context context) {
        FolioDatabaseHelper dbHelper = new FolioDatabaseHelper(context);
        Bookmarkdatabase = dbHelper.getWritableDatabase();
    }

    public static final String SQL_CREATE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT" + ","
            + bookID + " TEXT" + ","
            + date + " TEXT" + ","
            + name + " TEXT" + ","
            + readlocator + " TEXT" + ")";

    public static final String SQL_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public final boolean insertBookmark(String new_bookID, String new_date, String new_name, String new_cfi) {
        ContentValues values = new ContentValues();
        values.put(bookID, new_bookID);
        values.put(date, new_date);
        values.put(readlocator, new_cfi);
        values.put(name, new_name);

        return Bookmarkdatabase.insert(TABLE_NAME, null, values) > 0;

    }

    public static final ArrayList<HashMap> getBookmarksForID(String id, Context context) {
        if(Bookmarkdatabase == null){
            FolioDatabaseHelper dbHelper = new FolioDatabaseHelper(context);
            Bookmarkdatabase = dbHelper.getWritableDatabase();
        }
        ArrayList<HashMap> bookmarks = new ArrayList<>();
        Cursor c = Bookmarkdatabase.rawQuery("SELECT * FROM "
                + TABLE_NAME + " WHERE " + bookID + " = \"" + id + "\"", null);
        while (c.moveToNext()) {
            HashMap<String, String> name_value = new HashMap<String, String>();
            name_value.put("name", c.getString(c.getColumnIndex(name)));
            name_value.put("readlocator", c.getString(c.getColumnIndex(readlocator)));
            name_value.put("date", c.getString(c.getColumnIndex(date)));


            bookmarks.add(name_value);
        };
        c.close();
        return bookmarks;
    }

    public static final boolean deleteBookmark(String arg_date, String arg_name, Context context){
        if(Bookmarkdatabase == null){
            FolioDatabaseHelper dbHelper = new FolioDatabaseHelper(context);
            Bookmarkdatabase = dbHelper.getWritableDatabase();
        }
        String query = "SELECT " + ID + " FROM " + TABLE_NAME + " WHERE " + date + " = \"" + arg_date + "\"" + "AND " + name + " = \"" + arg_name + "\"";
        Cursor c = Bookmarkdatabase.rawQuery(query, null);

        int id = -1;
        while (c.moveToNext()) {
            id = c.getInt(c.getColumnIndex(BookmarkTable.ID));
        }
        c.close();
        return DbAdapter.deleteById(TABLE_NAME, ID, String.valueOf(id));
    }

}
