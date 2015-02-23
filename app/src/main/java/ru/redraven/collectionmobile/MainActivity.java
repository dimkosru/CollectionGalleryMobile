package ru.redraven.collectionmobile;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ru.redraven.collectionmobile.model.Bar;


public class MainActivity extends ActionBarActivity {
    GridView list;
    LazyImageLoadAdapter adapter;
    Activity act;
    Bar[] myBars;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        act = this;
    }


    public void onItemClick(int mPosition)

    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

        Toast.makeText(MainActivity.this,
                myBars[mPosition].getName() + "\r\n" +
                        sdf.format(myBars[mPosition].getDate()) + "\r\n" +
                        myBars[mPosition].getWeight() + "\r\n" +
                        myBars[mPosition].getAdditional() + "\r\n" +
                        myBars[mPosition].getIdFactory(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new HttpRequestTask().execute();
    }

    private class HttpRequestTask extends AsyncTask<Void, Void, Bar[]> {
        @Override
        protected Bar[] doInBackground(Void... params) {
            try {
                final String url = "http://192.168.1.33:8080/rest/bars";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                RestTemplate template = new RestTemplate();
                ResponseEntity<Bar[]> responseEntity = restTemplate.getForEntity(url, Bar[].class);

                Bar[] bars = responseEntity.getBody();
                return bars;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bar[] bars) {

            myBars = bars;

            list = (GridView) findViewById(R.id.gridView);
            List<String> mStrings = new ArrayList<>();
            for (Bar bar : bars) {
                mStrings.add("http://192.168.1.33:8080/resources/" + String.valueOf(bar.getIdBar()) + ".png");
            }

            // Create custom adapter for listview
            adapter = new LazyImageLoadAdapter(act, mStrings);

            //Set adapter to listview
            list.setAdapter(adapter);

            Button b = (Button) findViewById(R.id.button);
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.imageLoader.clearCache();
                    adapter.notifyDataSetChanged();
                }
            });
        }
    }
}
