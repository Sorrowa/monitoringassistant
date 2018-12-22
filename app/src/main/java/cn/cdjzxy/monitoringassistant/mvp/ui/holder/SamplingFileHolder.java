package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.http.imageloader.ImageLoader;
import com.wonders.health.lib.base.http.imageloader.glide.ImageConfigImpl;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.List;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.base.Methods;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

/**
 * 主页tab
 */

public class SamplingFileHolder extends BaseHolder<SamplingFile> {

    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.iv_delete_photo)
    ImageView mIvDeletePhoto;

    private SamplingFileAdapter.OnSamplingFileListener onSamplingFileListener;
    private ImageLoader                                mImageLoader;

    public SamplingFileHolder(View itemView, SamplingFileAdapter.OnSamplingFileListener onSamplingFileListener) {
        super(itemView);
        mImageLoader = ArtUtils.obtainAppComponentFromContext(itemView.getContext()).imageLoader();
        this.onSamplingFileListener = onSamplingFileListener;
    }

    @Override
    public void setData(SamplingFile data, final int position) {

        if (position == 0) {
            mIvPhoto.setImageResource(R.mipmap.ic_add_photo);
            mIvPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CheckUtil.isNull(onSamplingFileListener)) {
                        onSamplingFileListener.onChoosePhoto();
                    }
                }
            });
            mIvDeletePhoto.setVisibility(View.GONE);
        } else {
            mIvDeletePhoto.setVisibility(View.VISIBLE);
            mImageLoader.loadImage(itemView.getContext(),
                    ImageConfigImpl
                            .builder()
                            .errorPic(R.mipmap.ic_default)
                            .placeholder(R.mipmap.ic_default)
                            .url(data.getFilePath())
                            .imageView(mIvPhoto)
                            .build());
            mIvDeletePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!CheckUtil.isNull(onSamplingFileListener)) {
                        onSamplingFileListener.onDeletePhoto(position);
                    }
                }
            });
        }


    }

    @Override
    protected void onRelease() {
        this.mIvPhoto = null;
        this.mIvDeletePhoto = null;
    }


}
