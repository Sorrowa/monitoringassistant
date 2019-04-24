package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;

import com.wonders.health.lib.base.base.BaseHolder;
import com.wonders.health.lib.base.http.imageloader.ImageLoader;
import com.wonders.health.lib.base.http.imageloader.glide.ImageConfigImpl;
import com.wonders.health.lib.base.utils.ArtUtils;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseFileAdapter;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

public class NoiseFileHolder extends BaseHolder<NoiseSamplingFile> {
    @BindView(R.id.iv_photo)
    ImageView mIvPhoto;
    @BindView(R.id.iv_select)
    ImageView ivSelect;
    private NoiseFileAdapter.ItemClickListener listener;
    private ImageLoader mImageLoader;

    public NoiseFileHolder(View itemView, NoiseFileAdapter.ItemClickListener listener) {
        super(itemView);
        mImageLoader = ArtUtils.obtainAppComponentFromContext(itemView.getContext()).imageLoader();
        this.listener = listener;
    }

    @Override
    public void setData(NoiseSamplingFile data, int position) {
        if (data.getIsSelect()) {
            ivSelect.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            ivSelect.setImageResource(R.mipmap.ic_cb_nor);
        }
        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.selectLister(position, v);
                }
            }
        });
        mImageLoader.loadImage(itemView.getContext(),
                ImageConfigImpl
                        .builder()
                        .errorPic(R.mipmap.ic_default)
                        .placeholder(R.mipmap.ic_default)
                        .url(data.getFilePath())
                        .imageView(mIvPhoto)
                        .build());
        mIvPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckUtil.isNull(listener)) {
                    listener.itemLister(position, view);
                }
            }
        });
    }
}
