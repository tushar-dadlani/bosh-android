package io.pivotal.bosh;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by pivotal on 12/14/15.
 */
public class HashMapAdapter extends BaseAdapter {

    private HashMap<String, Integer> mData = new HashMap<String, Integer>();
    private String[] mKeys;


    private Context context;

    // the context is needed to inflate views in getView()
    public HashMapAdapter(Context context, HashMap<String, Integer> data) {
        this.context = context;
        mData  = data;
        mKeys = mData.keySet().toArray(new String[data.size()]);
    }



    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(mKeys[position]);
    }


    public void setItem(int position, Integer value) {
         mData.put(mKeys[position],value);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(final int pos, View convertView, ViewGroup parent) {

        String key = mKeys[pos];
        final String value = getItem(pos).toString();
        LayoutInflater vi = null;
        View v = convertView;
        if(v == null) {
            vi = LayoutInflater.from(context);
            v = vi.inflate(R.layout.job_row, null);
        }

        final TextView name, instanceCount;
        Button increment, decrement;

        increment = (Button)v.findViewById(R.id.increment);
        decrement = (Button)v.findViewById(R.id.decrement);



        name = (TextView) v.findViewById(R.id.job_name);
        instanceCount = (TextView) v.findViewById(R.id.instance_count);



        name.setText(key);
        instanceCount.setText(value);



        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d("Clicked", "+ was clicked");
                Log.d("Items", mData.toString());
                Log.d("Position", Integer.toString(pos));
                Log.d("getValue", getItem(pos).toString());
                int newCount = (Integer)getItem(pos) + 1;
                setItem(pos, newCount);
                Log.d("postSetValue", getItem(pos).toString());
                notifyDataSetChanged();
            }
        });

        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Clicked","- was clicked");
                Log.d("Items",mData.toString());
                Log.d("Position", Integer.toString(pos));
                Log.d("getValue", getItem(pos).toString());

                int newCount = (Integer)getItem(pos) - 1;
                if (newCount > 0) {
                    setItem(pos, newCount);
                }
                else {
                    setItem(pos, 0);
                }

                Log.d("postSetValue", getItem(pos).toString());
                notifyDataSetChanged();
            }
        });
        return v;
    }

}