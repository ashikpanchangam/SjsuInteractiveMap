package com.example.ashikspc.samplesearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by ASHIK'S PC on 10/26/2016.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomRecyclerHolder> {
    private Context context;
    private ArrayList<String> arrayList = null, stringArrayList;
    private OnItemClickListener listener;

    public CustomAdapter(Context context, ArrayList<String> items) {
        this.context = context;
        this.arrayList = items;
        this.stringArrayList = new ArrayList<String>();
        this.stringArrayList.addAll(items);
    }

    @Override
    public CustomAdapter.CustomRecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item, parent, false);

        return new CustomRecyclerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CustomAdapter.CustomRecyclerHolder holder, int position) {
        holder.item.setText(arrayList.get(position).toString());
    }

    public class CustomRecyclerHolder extends RecyclerView.ViewHolder{
        protected TextView item;

        public CustomRecyclerHolder(View v){
            super(v);
            item = (TextView) v.findViewById(R.id.itemNametxt);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(item.getText().toString());
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return this.arrayList.size();
    }

    public void filter(String filterString) {
        filterString = filterString.toLowerCase(Locale.getDefault());
        arrayList.clear();
        if(filterString.length() == 0) {
            arrayList.addAll(stringArrayList);

        }
        else {
            for(String item : stringArrayList){
                if(item.toLowerCase(Locale.getDefault()).contains(filterString)) {
                    arrayList.add(item );
                }
            }
        }
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;
    }

    public interface OnItemClickListener{
        public void onItemClick(String item);
    }
}
