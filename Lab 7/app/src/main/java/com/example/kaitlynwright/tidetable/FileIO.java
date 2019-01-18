package com.example.kaitlynwright.tidetable;

import android.content.Context;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.util.Log;

public class FileIO {

    private String filename;
    private Context context = null;

    public FileIO (Context context, String location) {

        this.context = context;
        if (location.equals("Coos Bay")) {
            filename = "coos_bay_annual.xml"; }
        else if (location.equals("Depoe Bay")) {
            filename = "depoe_bay_annual.xml"; }
        else if (location.equals("Cape Disappointment")) {
            filename = "cape_dis_annual.xml"; }
        //else { filename = "coos_bay_annual.xml"; }
    }

    public TideItems readFile() {
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
        }
        catch (Exception e) {
            Log.e("Tide reader", e.toString());
            return null;
        }
    }

}