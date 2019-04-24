package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.micheal.print.thread.ThreadPool;
import com.wonders.health.lib.base.mvp.IPresenter;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.simple.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.EventBusTags;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.NoiseSamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.SamplingFile;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.NoiseFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.SamplingFileAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseFragment;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.preview.PreviewActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.Glide4Engine;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.wastewater.WastewaterActivity;
import cn.cdjzxy.monitoringassistant.utils.DateUtils;
import uk.co.senab.photoview.PhotoView;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_INT_SOURCE_EDIT;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.NOISE_FRAGMENT_SOURCE_SHARE;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mPrivateData;
import static cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity.NoiseFactoryActivity.mSample;


/**
 * 噪声——附件列表
 */
public class NoiseOtherFileFragment extends BaseFragment implements IView {

    @BindView(R.id.linear_delete)
    LinearLayout linearDelete;
    @BindView(R.id.linear_add)
    LinearLayout linearAdd;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.img_preview)
    ImageView imageViewPreview;
    private NoiseFileAdapter sampleFileAdapter;
    private List<NoiseSamplingFile> sampleFiles;
    private List<String> selectList;


    private static final int REQUEST_CODE = 1023;
    private static final int PirView_request_Code = 1023;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noise_source_list, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        sampleFiles = new ArrayList<>();
        selectList = new ArrayList<>();
        initListData();
        initRecyclerView();
    }

    private void initRecyclerView() {
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        sampleFileAdapter = new NoiseFileAdapter(sampleFiles,
                new NoiseFileAdapter.ItemClickListener() {
                    @Override
                    public void selectLister(int p, View v) {
                        if (sampleFiles.get(p).getIsSelect()) {
                            sampleFiles.get(p).setIsSelect(false);
                            if (selectList.contains(sampleFiles.get(p).getLocalId())) {
                                selectList.remove(sampleFiles.get(p).getLocalId());
                            }
                        } else {
                            sampleFiles.get(p).setIsSelect(true);
                            if (!selectList.contains(sampleFiles.get(p).getLocalId())) {
                                selectList.add(sampleFiles.get(p).getLocalId());
                            }
                        }
                        sampleFileAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void itemLister(int p, View view) {
                        //  PhotoView photoView = new PhotoView(getContext());
                        Glide.with(getContext()).
                                load(sampleFiles.get(p).getFilePath()).
                                into(imageViewPreview);
                        imageViewPreview.setVisibility(View.VISIBLE);
                    }

                });
        recyclerView.setAdapter(sampleFileAdapter);
    }

    private void initListData() {
        List<SamplingFile> list = mSample.getSamplingFiless();
        if (list == null) list = new ArrayList<>();
        for (SamplingFile file : list) {
            NoiseSamplingFile noiseSamplingFile = new NoiseSamplingFile();
            noiseSamplingFile.setIsSelect(false);
            noiseSamplingFile.setFileName(file.getFileName());
            noiseSamplingFile.setFilePath(file.getFilePath());
            noiseSamplingFile.setId(file.getId());
            noiseSamplingFile.setLocalId(file.getLocalId());
            noiseSamplingFile.setUpdateTime(file.getUpdateTime());
            noiseSamplingFile.setIsUploaded(file.getIsUploaded());
            noiseSamplingFile.setSamplingId(file.getSamplingId());
            sampleFiles.add(noiseSamplingFile);
        }
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }


    public void showLoading(String msg) {
        showLoadingDialog(msg);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    @OnClick({R.id.linear_delete, R.id.linear_add, R.id.img_preview})
    public void onClick(View v) {
        hideSoftInput();
        switch (v.getId()) {
            case R.id.linear_delete:
                deleteData();
                break;
            case R.id.linear_add:
                choosePhoto(REQUEST_CODE);
                break;
            case R.id.img_preview:
                if (imageViewPreview.getVisibility() == View.VISIBLE) {
                    imageViewPreview.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void deleteData() {
        if (sampleFiles == null || sampleFiles.size() == 0) {
            showMessage("暂无数据可删除");
            return;
        }
        if (selectList == null || selectList.size() == 0) {
            showMessage("请先选择删除文件");
            return;
        }
        showLoading("正在删除");
        ThreadPool.getInstantiation().addTask(new Runnable() {
            @Override
            public void run() {
                List<SamplingFile> files = new ArrayList<>();
                for (String id : selectList) {
                    for (int i = 0; i < sampleFiles.size(); i++) {
                        if (sampleFiles.get(i).getId().equals(id)) {
                            sampleFiles.remove(i);
                        }
                    }
                }
                files.addAll(sampleFiles);
                selectList.clear();
                mSample.setSamplingFiless(files);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sampleFileAdapter.notifyDataSetChanged();
                        hideLoading();
                    }
                });
            }
        });
    }

    private void choosePhoto(int requestCode) {
        Matisse.from(this)
                .choose(MimeType.ofImage())
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,
                        "cn.cdjzxy.monitoringassistant.android7.fileprovider",
                        "MonitoringAssistant"))
                .maxSelectable(9)
                //.restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)
                .showSingleMediaType(true)
                .imageEngine(new Glide4Engine())
                .originalEnable(true)
                .autoHideToolbarOnSingleTap(true)
                .forResult(requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {//添加图片
            List<String> paths = Matisse.obtainPathResult(data);
            //   ArtUtils.makeText(getContext(), paths.toString());
            List<SamplingFile> list = new ArrayList<>();
            for (String path : paths) {
                NoiseSamplingFile samplingFile = new NoiseSamplingFile();
                File file = new File(path);
                samplingFile.setLocalId("FS-" + UUID.randomUUID().toString());
                samplingFile.setId("");
                samplingFile.setFilePath(path);
                samplingFile.setFileName(file.getName());
                samplingFile.setSamplingId(mSample.getId());
                samplingFile.setUpdateTime(DateUtils.getTime(new Date().getTime()));
                samplingFile.setIsSelect(false);
                sampleFiles.add(samplingFile);
            }
            list.addAll(sampleFiles);
            mSample.setSamplingFiless(list);
            sampleFileAdapter.notifyDataSetChanged();
        } else if (requestCode == PirView_request_Code
                && resultCode == PreviewActivity.BACK_RESULT_CODE) {//预览图片
        }
    }

    /**
     * 保存信息
     *
     * @return
     */
    public void savePrivateData() {
        if (mPrivateData != null && mSample != null) {
            if (sampleFiles != null && sampleFiles.size() > 0) {
                List<SamplingFile> list = new ArrayList<>();
                list.addAll(sampleFiles);
                mSample.setSamplingFiless(list);
            }
        }
    }
}
