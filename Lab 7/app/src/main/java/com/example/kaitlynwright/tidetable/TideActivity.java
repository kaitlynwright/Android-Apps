package com.example.kaitlynwright.tidetable;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import java.net.URL;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import android.database.Cursor;
import android.util.Log;
import android.widget.TextView;

public class TideActivity extends AppCompatActivity
        implements OnItemClickListener {

    private TideItems tideItems;

    private Dal dal = new Dal(this);
    Cursor cursor = null;
    String locationSelection = "Coos Bay";
    String dateSelection = "2018/01/01";
    SimpleCursorAdapter adapter = null;

    // widget
    private ListView items;
    private TextView title;

    SharedPreferences savedValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        //widgets
        title = (TextView) findViewById(R.id.listTitle);
        items = (ListView) findViewById(R.id.itemsListView);

        //saved values
        savedValues = getSharedPreferences("savedValues", MODE_PRIVATE);
        locationSelection = savedValues.getString("location", "Coos Bay");
        dateSelection = savedValues.getString("date", "2018/01/01");
        title.setText("Tides for " + locationSelection + ": " + dateSelection);

        // read in xml tide file
        FileIO io = new FileIO(getApplicationContext(), locationSelection);
        tideItems = io.readFile();

        //adapter data
        ArrayList<HashMap<String, String>> data = new
                ArrayList<>();

        String topLine, bottomLine;
        for (TideItem item : tideItems) {
            if (item.getDate().equals(dateSelection)) {
                HashMap<String, String> map = new HashMap<>(2);

                topLine = String.format("%s %s", item.getDate(), item.getDay());

                if (item.getHigh_low().equals("H"
                )) {
                    bottomLine = String.format("High: %s", item.getTime());
                } else {
                    bottomLine = String.format("Low: %s", item.getTime());
                }

                map.put("title", topLine);
                map.put("info", bottomLine);

                data.add(map);
            }
        }

        //adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data, R.layout.listview_item,
                new String[]{"title", "info"},
                new int[]{R.id.title, R.id.info}
                );

        items.setAdapter(adapter);
        items.setOnItemClickListener(this);

    }

    //event handler for clicking listView item
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        TideItem item = tideItems.get(position);
        Toast.makeText(this,
                String.format("%s ft, %s cm", item.getPredFt(), item.getPredCm()),
                Toast.LENGTH_LONG).show();
    }


    private void getTide(String location, String date) {
        cursor = dal.getTideFromDb(location, date);
        if (cursor.getCount() == 0) {
            new RestTask().execute(location, date);
        } else {
            displayTides();
        }
    }

    private void displayTides() {
        adapter = new SimpleCursorAdapter(
                this,
                R.layout.listview_item,
                cursor,
                new String[]{ SQLiteHelper.DAY + " " + SQLiteHelper.DATE,
                            SQLiteHelper.HIGHLOW },
                new int[] { R.id.title, R.id.info },
                0 );
        items.setAdapter(adapter);
    }

    // REST SERVICE - not working, so only 2018 tides from asset files are available in app
    public class RestTask extends AsyncTask<String, Void, TideItems> {
        private String location;
        private String date;

        @Override
        protected TideItems doInBackground(String... params) {
            //build the string used for REST query
            String apiKey = " ";

            String baseURL = "https://opendap.co-ops.nos.noaa.gov/axis/webservices/" +
                    "highlowtidepred/index.jsp";

            location = params[0];
            date = params[1];
            String query = "/tide/q/" + location + "/" + date + ".xml";

            TideItems items = null;
            try {
                URL url = new URL(baseURL);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestProperty("User-Agent", "tidetable-app");
                connection.setRequestMethod("GET");
                connection.connect();
                InputStream in = connection.getInputStream();

                if (in != null) {
                    items = dal.parseXmlStream(in);
                }
                connection.disconnect();
            } catch (Exception e) {
                Log.e("RestTask", "doInBackground error: " + e.getLocalizedMessage());
            }
            return items;
        }

        @Override
        protected void onPostExecute(TideItems items) {
            if(items != null && items.size() != 0) {
                dal.putTideIntoDb(items);
                cursor = dal.getTideFromDb(location, date);
                displayTides();
            }
        }

    }
}
