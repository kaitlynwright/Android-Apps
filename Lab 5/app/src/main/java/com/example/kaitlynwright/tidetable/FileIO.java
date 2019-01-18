package com.example.kaitlynwright.tidetable;

import android.content.Context;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import java.io.InputStream;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import android.util.Log;

public class FileIO {

    private final String FILENAME = "coos_bay_annual.xml";
    private Context context = null;

    public FileIO (Context context) {
        this.context = context;
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
            InputStream in = context.getAssets().open(FILENAME);

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