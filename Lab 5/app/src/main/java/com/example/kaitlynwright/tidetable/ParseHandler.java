package com.example.kaitlynwright.tidetable;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class ParseHandler extends DefaultHandler {
    private TideItems tideItems;
    private TideItem item;

    private boolean inDate, inDay, inTime, inPredCm, inPredFt, inHighLow;

    public TideItems getItems() {
        return tideItems;
    }

    @Override
    public void startDocument() throws SAXException {
        tideItems = new TideItems();
    }

    @Override
    public void startElement(String namespaceURI, String localName, String qName,
                             Attributes atts) throws SAXException {

        if (qName.equals("item")) {
            item = new TideItem();
        }

        if(qName.equals("date")) { inDate = true; }
        if(qName.equals("day")) { inDay = true; }
        if(qName.equals("time")) { inTime = true; }
        if(qName.equals("pred_in_cm")) { inPredCm = true; }
        if(qName.equals("pred_in_ft")) { inPredFt = true; }
        if(qName.equals("highlow")) { inHighLow = true; }
    }

    @Override
    public void endElement(String namespaceURI ,String localName, String qName)
        throws SAXException {
        if(qName.equals("item")) {
            tideItems.add(item);
            item = null;
        }

        if(qName.equals("date")) { inDate = false; }
        if(qName.equals("day")) { inDay = false; }
        if(qName.equals("time")) { inTime = false; }
        if(qName.equals("pred_in_cm")) { inPredCm = false; }
        if(qName.equals("pred_in_ft")) { inPredFt = false; }
        if(qName.equals("highlow")) { inHighLow = false; }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (inDate) item.setDate(new String(ch, start, length));
        if (inDay) item.setDay(new String(ch, start, length));
        if (inTime) item.setTime(new String(ch, start, length));
        if (inPredCm) item.setPredCm(new String(ch, start, length));
        if (inPredFt) item.setPredFt(new String(ch, start, length));
        if (inHighLow) item.setHigh_low(new String(ch, start, length));
    }

}
