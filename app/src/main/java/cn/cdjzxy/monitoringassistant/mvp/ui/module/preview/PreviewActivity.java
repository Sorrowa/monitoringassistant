package cn.cdjzxy.monitoringassistant.mvp.ui.module.preview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PreviewAdapter;
import cn.cdjzxy.monitoringassistant.widgets.PhotoViewPager;

public class PreviewActivity extends AppCompatActivity {
    @BindView(R.id.pager)
    PhotoViewPager photoViewPager;
    @BindView(R.id.text_back)
    TextView textViewBack;
    @BindView(R.id.text_delete)
    TextView textViewDelete;
    @BindView(R.id.frame_layout)
    FrameLayout frameLayout;

    private Unbinder mUnbinder;

    private PreviewAdapter adapter;
    private List<SamplingFile> list;

    public static final String PREVIEW_PHOTOS = "preview_photos";//传递的图片信息
    public static final int BACK_RESULT_CODE = 2000;//从此页面退出
    public static final String POSITION = "position";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        if (getIntent() == null) {
            finish();
            return;
        }
        mUnbinder = ButterKnife.bind(this);
        Bundle bundle = getIntent().getBundleExtra(PREVIEW_PHOTOS);
        list = bundle.getParcelableArrayList(PREVIEW_PHOTOS);
        if (list == null) list = new ArrayList<>();
        //  int position = getIntent().getIntExtra(POSITION, 0);
        adapter = new PreviewAdapter(list, this);
        photoViewPager.setAdapter(adapter);
    }

    @OnClick({R.id.text_delete, R.id.text_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_back:
                sendBackResult();
                break;
            case R.id.text_delete:
                int i = photoViewPager.getCurrentItem();
                if (i < list.size()) {
                    list.remove(i);
                }
                if (list.size() == 0) {
                    sendBackResult();
                } else {
                    adapter.setList(list);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        sendBackResult();
    }

    /**
     * 当前页面退出
     */
    private void sendBackResult() {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(PreviewActivity.PREVIEW_PHOTOS, (ArrayList<SamplingFile>) list);
        intent.putExtra(PreviewActivity.PREVIEW_PHOTOS, bundle);
        setResult(BACK_RESULT_CODE, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY)
            mUnbinder.unbind();
        this.mUnbinder = null;
    }
}
