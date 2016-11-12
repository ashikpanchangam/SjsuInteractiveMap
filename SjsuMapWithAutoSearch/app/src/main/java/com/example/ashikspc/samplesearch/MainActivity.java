package com.example.ashikspc.samplesearch;

import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ArrayAdapterSearchView searchView;
    private LinearLayout recycleLayout;
    private ArrayList<String> buildingList = new ArrayList<String>();
    private RecyclerView recyclerView;
    private CustomAdapter adapter;
    private Toolbar toolbar1, toolbar2;
    private FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar1 = (Toolbar) findViewById(R.id.nested_toolbar_1);
        toolbar2 = (Toolbar) findViewById(R.id.nested_toolbar_2);

        frameLayout = (FrameLayout) findViewById(R.id.nested_parentframe);

        setSupportActionBar(toolbar2);
        getSupportActionBar().setTitle("Search SJSU Maps");
        toolbar1.setNavigationIcon(R.mipmap.ic_launcher);

        toolbar2.setTitleTextColor(ContextCompat.getColor(this,android.R.color.tertiary_text_light));

        recycleLayout = (LinearLayout) findViewById(R.id.recyclerViewLayout);

        recyclerView = (RecyclerView) findViewById(R.id.cardsList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        String[] buildings = new String[]{"BBC",
                "Engineering Building",
                "King Library",
                "South Parking Garage",
                "Student Union",
                "Yoshihiro Uchida Hall"
        };

        //Add the buildings to the building list array
        for(int i = 0; i < buildings.length; i++){
            buildingList.add(buildings[i]);
        }

        if(!buildingList.isEmpty()){
            adapter = new CustomAdapter(this, buildingList);
            recyclerView.setAdapter(adapter);

            //recycleLayout.setVisibility(View.VISIBLE);
            adapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(String item) {
                    searchView.setQuery(item, false);
                    recycleLayout.setVisibility(View.GONE);
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);

        MenuItem searchItem = menu.findItem(R.id.search_menu_item);

        searchView = (ArrayAdapterSearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setLayoutParams(new ActionBar.LayoutParams(Gravity.LEFT));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query)
            {
                if (query.length() != 0)
                {
                    if (null != adapter)
                    {
                        adapter.filter(query.toString());               //This will filter the RecyclerView
                        recycleLayout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                    }
                }
                else
                {
                    recyclerView.setVisibility(View.GONE);
                }
                return false;
            }
        });

        searchView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String length = searchView.getText();
                searchView.setSelection(length.length());
            }
        });

        return true;
    }
}