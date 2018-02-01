package com.example.drago.schedule_sms;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by SherryDang on 11/22/2017.
 */

public class ContactListAdapter extends BaseAdapter {
    private Context context;
    private List <Contact> contacSMStList;

    public ContactListAdapter(Context applicationContext, List<Contact> contactSMSList) {
        this.context = applicationContext;
        this.contacSMStList = contactSMSList;
    }

    @Override
    public int getCount() {
        return contacSMStList.size();
    }

    @Override
    public Object getItem(int position) {
        return contacSMStList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_scheduled_list, null);
        TextView namextx = (TextView) view.findViewById(R.id.nametxt);
        TextView messagetxt = (TextView) view.findViewById(R.id.messagetxt);
        TextView datetxt = (TextView) view.findViewById(R.id.datetxt);
        TextView timetxt = (TextView) view.findViewById(R.id.timetxt);

        //set data to textview
        namextx.setText(contacSMStList.get(position).getName());
        messagetxt.setText(contacSMStList.get(position).getMessage());
        datetxt.setText(contacSMStList.get(position).getDate());
        timetxt.setText(contacSMStList.get(position).getTime());

        //maynot need it
        view.setTag(contacSMStList.get(position).getId());

        return view;

    }
}
