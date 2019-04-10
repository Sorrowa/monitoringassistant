package cn.cdjzxy.monitoringassistant.mvp.ui.module.autograph.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.R;

public class AutographFragmentAdapter extends BaseAdapter {
    private List<String> list;
    private Context context;
    private LayoutInflater inflater;

    public AutographFragmentAdapter(Context context) {
        this.context = context;
        this.list = new ArrayList<>();
        this.inflater = LayoutInflater.from(context);
    }

    public AutographFragmentAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setList(List<String> l) {
        if (this.list == null) this.list = new ArrayList<>();
        else this.list.clear();
        this.list.addAll(l);
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int position) {
        return list == null ? position + "" : list.get(position) == null ? position + "" : list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_autograph_adapter, null);
        TextView textViewName = convertView.findViewById(R.id.text_view_name);
        textViewName.setText(getItem(position));
        return convertView;
    }
}
