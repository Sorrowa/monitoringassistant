package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.noise.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.model.LatLng;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.app.Constant;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;

import static cn.cdjzxy.monitoringassistant.utils.FileUtils.saveInOI;

public class NoiseMapActivity extends BaseTitileActivity<ApiPresenter> implements IView {
    @BindView(R.id.map_view)
    MapView mapView;

    // 定位相关
    LocationClient mLocClient;
    /**
     * 当前地点击点
     */
    private LatLng currentPt;

    private String FILE_PATH, FILE_NAME;
    private boolean isFirstLoc = true; // 是否首次定位

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case Message.RESULT_FAILURE:
                    hideLoading();
                    showMessage("地图截屏无响应,请重新尝试");
                    break;
            }
        }
    };

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        SDKInitializer.initialize(getApplicationContext());
        return R.layout.activity_noise_map;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        mapView.getMap().setMyLocationEnabled(true);
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(new MyLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        initListener();
        FILE_PATH = Constant.FILE_DIR + Constant.PNG_DIR + "/noise/";
        FILE_NAME = System.currentTimeMillis() + ".png";
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("测点示意图——地图");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBack("");
            }
        });
    }

    private void initListener() {
        mapView.getMap().setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                currentPt = latLng;
                showPointItem();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mapView.getMap().setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                currentPt = latLng;
                showPointItem();
            }
        });
    }

    /**
     * 显示选择的点绘制样式
     */
    private void showPointItem() {
        List<String> stringList = new ArrayList<>();
        stringList.add("噪声源");
        stringList.add("敏感点噪声监测点");
        stringList.add("期货噪声监测点");
        OptionsPickerView pickerView = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                BitmapDescriptor descriptor;
                switch (options1) {
                    case 0://噪声源
                        descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_noise_source);
                        break;
                    case 1:
                        descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_noise_point);
                        break;
                    case 2://敏感点噪声监测点
                        descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_noise_monitor);
                        break;
                    default:
                        descriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_noise_source);
                        break;
                }
                MarkerOptions ooA = new MarkerOptions().position(currentPt).icon(descriptor);
                mapView.getMap().addOverlay(ooA);
            }
        })
                .setTitleText("请选择污染方案")
                .setContentTextSize(20)//设置滚轮文字大小
//                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                .setSelectOptions(0, 1)//默认选中项
                .setOutSideCancelable(true).build();
        pickerView.setPicker(stringList);
        pickerView.show();
    }

    @OnClick({R.id.linear_clean, R.id.linear_save})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.linear_clean:
                mapView.getMap().clear();
                break;
            case R.id.linear_save:
                showLoadingDialog("请稍等");
                snapshot();
                handler.sendEmptyMessageAtTime(Message.RESULT_FAILURE, 3000);
                break;
        }
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void showMessage(@NonNull String message) {

    }

    @Override
    public void handleMessage(@NonNull Message message) {

    }

    public void snapshot() {
        mapView.getMap().snapshot(new BaiduMap.SnapshotReadyCallback() {
            public void onSnapshotReady(Bitmap snapshot) {
                new SaveToFileTask().execute(snapshot);
                handler.removeMessages(Message.RESULT_FAILURE);
//                File file = new File(FILE_PATH + FILE_NAME);
//                FileOutputStream out;
//                try {
//                    out = new FileOutputStream(file);
//                    if (snapshot.compress(
//                            Bitmap.CompressFormat.PNG, 100, out)) {
//                        out.flush();
//                        out.close();
//                    }
//                    FILE_PNG_PATH = file.getPath();
//                    closeLoadingDialog();
//                    onBack();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        });
    }

    public void onBack(String s) {
        Intent intent = new Intent();
        intent.putExtra("filePngPath", s);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        onBack("");
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听

        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mapView.getMap().setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        super.onDestroy();
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }

            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mapView.getMap().animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    class SaveToFileTask extends AsyncTask<Bitmap, Void, File> {

        @Override
        protected File doInBackground(Bitmap... bitmaps) {
            return saveInOI(bitmaps[0], FILE_PATH, FILE_NAME, 100);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected void onPostExecute(File file) {
            super.onPostExecute(file);
            hideLoading();
            if (file.exists())
                showMessage("保存成功");
            else
                showMessage("保存失败!");
            onBack(file.getPath());
        }
    }

}
