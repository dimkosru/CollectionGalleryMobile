package ru.redraven.collectionmobile;

/**
 * Created by Дмитрий on 22.02.2015.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

//Adapter class extends with BaseAdapter and implements with OnClickListener
public class LazyImageLoadAdapter extends BaseAdapter implements OnClickListener{

    private Activity activity;
    private List<String> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;

    public LazyImageLoadAdapter(Activity a, List<String> d) {
        activity = a;
        data=d;
        inflater = (LayoutInflater)activity.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // Create ImageLoader object to download and show image in list
        // Call ImageLoader constructor to initialize FileCache
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{
        public ImageView image;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi=convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.listview_row, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.image=(ImageView)vi.findViewById(R.id.image);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        ImageView image = holder.image;

        //DisplayImage function from ImageLoader Class
        imageLoader.DisplayImage(data.get(position), image);

        /******** Set Item Click Listner for LayoutInflater for each row ***********/
        vi.setOnClickListener(new OnItemClickListener(position));
        return vi;
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }


    /********* Called when Item click in ListView ************/
    private class OnItemClickListener  implements OnClickListener{
        private int mPosition;

        OnItemClickListener(int position){
            mPosition = position;
        }

        @Override
        public void onClick(View arg0) {
            MainActivity sct = (MainActivity)activity;
            sct.onItemClick(mPosition);
        }
    }
}