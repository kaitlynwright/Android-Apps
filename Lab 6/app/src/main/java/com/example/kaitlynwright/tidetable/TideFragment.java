package com.example.kaitlynwright.tidetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.HashMap;

public class TideFragment extends Fragment
        implements AdapterView.OnItemClickListener {
    private TideItems tideItems;

    // widgets
    private ListView items;
    private TextView title;

    private SharedPreferences savedValues;

    private Dal dal = new Dal(getActivity());
    Cursor cursor = null;
    SimpleCursorAdapter adapter = null;
    String location;
    String date;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getActivity().setContentView(R.layout.activity_items);


        // read in xml tide file
        /*FileIO io = new FileIO(getApplicationContext(), "coos_bay_annual.xml");
        tideItems = io.readFile();*/

        savedValues = getActivity().getSharedPreferences("savedValues", Context.MODE_PRIVATE);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        String topLine, bottomLine;

        location = savedValues.getString("location", "Coos Bay");
        date = savedValues.getString("date", "2018/01/01");

        // inflate tide fragment
        View view = inflater.inflate(R.layout.activity_items_fragment,
                container, false);

        //default location
        dal.loadTestData(location);
        cursor = dal.getTideByLocation(location, date);

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
                getActivity(),
                data, R.layout.listview_item,
                new String[]{"title", "info"},
                new int[]{R.id.title, R.id.info}
        );

        title = (TextView) view.findViewById(R.id.listTitle);
        title.setText(String.format("2018 Tides for %s", location));
        items = (ListView) view.findViewById(R.id.itemsListView);
        items.setAdapter(adapter);
        items.setOnItemClickListener(this);

        return view;
    }

    //event handler for clicking listView item
    @Override
    public void onItemClick(AdapterView<?> parent, View view,
                            int position, long id) {
        TideItem item = tideItems.get(position);
        Toast.makeText(getActivity(), String.format("%s ft, %s cm", item.getPredFt(),
                item.getPredCm()), Toast.LENGTH_LONG).show();
    }
}

