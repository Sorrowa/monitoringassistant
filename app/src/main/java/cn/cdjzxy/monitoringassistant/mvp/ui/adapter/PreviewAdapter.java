package cn.cdjzxy.monitoringassistant.mvp.ui.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wonders.health.lib.base.http.imageloader.glide.ImageConfigImpl;

import java.util.ArrayList;
import java.util.List;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

public class PreviewAdapter extends PagerAdapter {
    private List<SamplingFile> list = new ArrayList<>();
    private Context context;
    PhotoViewAttacher attacher;
    private LayoutInflater layoutInflater;

    public PreviewAdapter(List<SamplingFile> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public PreviewAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();
    }

    public void setList(List<SamplingFile> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }

    @Override
    public void destroyItem(ViewGroup view, int position, Object object) {
        view.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        PhotoView photoView = new PhotoView(context);
        if (position < list.size()) {
            Glide.with(context).
                    load(list.get(position).getFilePath()).
                    into(photoView);
        }
        view.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return photoView;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
