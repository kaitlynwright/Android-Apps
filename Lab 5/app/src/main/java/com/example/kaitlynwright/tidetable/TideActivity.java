package com.example.kaitlynwright.tidetable;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class TideActivity extends AppCompatActivity
        implements OnItemClickListener {

    private TideItems tideItems;

    // widget
    private ListView items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        String topLine, bottomLine;

        // read in xml tide file
        FileIO io = new FileIO(getApplicationContext());
        tideItems = io.readFile();

        //adapter data
        ArrayList<HashMap<String, String>> data = new
                ArrayList<>();

        for (TideItem item : tideItems) {
            HashMap<String, String> map = new HashMap<>(2);
            topLine = String.format("%s %s", item.getDate(), item.getDay());

            if (item.getHigh_low().equals("H")) {
                bottomLine = String.format("High: %s", item.getTime());
            } else {
                bottomLine = String.format("Low: %s", item.getTime());
            }

            map.put("title", topLine);
            map.put("info", bottomLine);

            data.add(map);
        }

        //adapter
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data, R.layout.listview_item,
                new String[]{"title", "info"},
                new int[]{R.id.title, R.id.info}
                );

        items = (ListView) findViewById(R.id.itemsListView);
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

}
