package com.example.kaitlynwright.tidetable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import static com.example.kaitlynwright.tidetable.TideSQLiteHelper.*;

// Data Access Layer
public class Dal {
    private Context context = null;

    public Dal(Context context) {
        this.context = context;
    }

    public void loadTestData(String location) {
        //TideSQLiteHelper helper = new TideSQLiteHelper(context);
        //SQLiteDatabase db = helper.getWritableDatabase();
        loadDbFromXML("coos_bay_tides.xml");

        /*if (db.rawQuery("SELECT * FROM TIDE WHERE " + TIDE_LIST_ID
                + " = 1 ", null).getCount() == 0) {
            switch (location) {
                case "Cape Disappointment":
                    loadDbFromXML("cape_dis_annual.xml");
                    break;
                case "Depoe Bay":
                    loadDbFromXML("depoe_bay_annual.xml");
                    break;
                case "Coos Bay":
                    loadDbFromXML("coos_bay_annual.xml");
                    break;
            }
        }*/
    }


    // parse XML file date into db
    public void loadDbFromXML(String filename) {
        // Get data from XML
        TideItems tideItems = parseXmlFile(filename);

        // initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        // put tide data into database
        ContentValues cv = new ContentValues();

        for (TideItem item : tideItems) {
            cv.put(TIDE_LOCATION, item.getLocation());
            cv.put(DATE, item.getDate());
            cv.put(DAY, item.getDay());
            cv.put(TIME, item.getTime());
            cv.put(PREDFT, item.getPredFt());
            cv.put(PREDCM, item.getPredCm());
            cv.put(HIGHLOW, item.getHigh_low());

            db.insert(TIDE, null, cv);
        }
        db.close();
    }

    public Cursor getTideByLocation(String location, String date) {
        // Ensure there is data in database for location
        loadTestData(location);

        //Initialize database
        TideSQLiteHelper helper = new TideSQLiteHelper(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        // Get tide info for one location
        String query = "SELECT * FROM TIDE WHERE " + TIDE_LOCATION
                + " = " + location + "AND WHERE " + DATE + " = " + date;

        String[] variables = new String[]{location};
        return db.rawQuery(query, variables);
    }

    public TideItems parseXmlFile(String filename) {
        try {
            // get XML reader
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLReader xmlreader = parser.getXMLReader();

            //det content handler
            ParseHandler handler = new ParseHandler();
            xmlreader.setContentHandler(handler);

            //read file from local storage
            InputStream in = context.getAssets().open(filename);

            //parse data
            InputSource is = new InputSource(in);
            xmlreader.parse(is);

            //set the feed in activity
            TideItems items = handler.getItems();
            return items;
        } catch (Exception e) {
            Log.e("Tide reader", e.toString());
            return null;
        }

    }
}
