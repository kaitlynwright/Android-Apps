package com.example.kaitlynwright.tidetable;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.InputSource;
import android.util.Log;
import android.database.Cursor;


public class Dal {
    private Context context = null;

    public Dal(Context context) {
        this.context = context;
    }

    protected TideItems parseXmlStream(InputStream in) {
        TideItems items = null;
        if (in != null) {
            try {
                // get the XML reader
                SAXParserFactory factory = SAXParserFactory.newInstance();
                SAXParser parser = factory.newSAXParser();
                XMLReader xmlreader = parser.getXMLReader();

                // set content handler
                ParseHandler handler = new ParseHandler();
                xmlreader.setContentHandler(handler);

                // parse the data
                InputSource is = new InputSource(in);
                xmlreader.parse(is);
                items = handler.getItems();
                Log.d("Dal", "items count: " + Integer.toString(items.size()));
            } catch (Exception e) {
                Log.e("Tide", "parseXMLStream error: " + e.toString());
            }
        }

        return items;
    }


    protected void putTideIntoDb(TideItems items) {

        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues cv = new ContentValues();

        for (TideItem item : items) {
            cv.put(SQLiteHelper.HIGHLOW, item.getHigh_low());
            cv.put(SQLiteHelper.DATE, item.getDate());
            cv.put(SQLiteHelper.DAY, item.getDay());
            cv.put(SQLiteHelper.TIME, item.getTime());
            cv.put(SQLiteHelper.PREDFT, item.getPredFt());
            cv.put(SQLiteHelper.PREDCM, item.getPredCm());
            cv.put(SQLiteHelper.LOCATION, item.getLocation());
        }

    }

    public Cursor getTideFromDb(String location, String date) {

        // Initialize the database for reading
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Set up query for weather forcast for location at starting date
        String query = "SELECT * FROM " + SQLiteHelper.TIDE + " WHERE " +
                SQLiteHelper. LOCATION + " = ? AND " + SQLiteHelper. DATE +
                " = ? ORDER BY " + SQLiteHelper.DATE;
        String[] variables = new String[]{ location, date };

        // Execute Query
        Cursor cursor = db.rawQuery(query, variables);

        return cursor;
    }

}
