package cn.cdjzxy.monitoringassistant.mvp.ui.module.wander;

import android.content.Intent;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.PermissionUtil;

import java.util.List;

import butterknife.BindView;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 任务二维码扫描界面
 */
public class WanderScanActivity extends BaseTitileActivity<ApiPresenter> implements IView {
    @BindView(R.id.qr)
    ZXingView mQR;

    private AppComponent mAppComponent;

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_wander_scan;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        requestPermission();
        mQR.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                vibrate();
                showLoading();
                mQR.startSpotDelay(2 * 1000);
                mPresenter.getQrInfo(Message.obtain(WanderScanActivity.this, new Object()), result);//服务器验证二维码是否存在
            }

            @Override
            public void onCameraAmbientBrightnessChanged(boolean isDark) {
                // 这里是通过修改提示文案来展示环境是否过暗的状态，接入方也可以根据 isDark 的值来实现其他交互效果
                String tipText = mQR.getScanBoxView().getTipText();
                String ambientBrightnessTip = "\n环境过暗，请打开闪光灯";
                if (isDark) {
                    if (!tipText.contains(ambientBrightnessTip)) {
                        mQR.getScanBoxView().setTipText(tipText + ambientBrightnessTip);
                    }
                } else {
                    if (tipText.contains(ambientBrightnessTip)) {
                        tipText = tipText.substring(0, tipText.indexOf(ambientBrightnessTip));
                        mQR.getScanBoxView().setTipText(tipText);
                    }
                }
            }

            @Override
            public void onScanQRCodeOpenCameraError() {
                showMessage("打开摄像机失败，请授权使用摄像机");
            }
        });
    }

    private void requestPermission() {
        PermissionUtil.launchCamera(new PermissionUtil.RequestPermission() {
                                        @Override
                                        public void onRequestPermissionSuccess() {
                                            mQR.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
                                            mQR.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
                                        }

                                        @Override
                                        public void onRequestPermissionFailure(List<String> permissions) {

                                        }

                                        @Override
                                        public void onRequestPermissionFailureWithAskNeverAgain(List<String> permissions) {

                                        }
                                    },
                new RxPermissions(this), mAppComponent.rxErrorHandler());
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        mAppComponent = ArtUtils.obtainAppComponentFromContext(this);
        return new ApiPresenter(mAppComponent);
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("流转任务扫描");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(this, message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        hideLoading();
        switch (message.what) {
            case Message.RESULT_OK:
                QrMoreInfo qrMoreInfo = (QrMoreInfo) message.obj;
                if (!CheckUtil.isNull(qrMoreInfo)) {
                    Intent intent = new Intent(this, WebActivity.class);
                    if (qrMoreInfo.getType() == 2) {
                        //获取用户信息
                        UserInfo user = UserInfoHelper.get().getUserInfo();
                        //相对路径
                        String url = user.getWebUrl() + qrMoreInfo.getContent() + "&userId=" + user.getId();
                        intent.putExtra(WebFragment.URL_KEY, url);
                    } else if (qrMoreInfo.getType() == 3) {
                        //绝对路径
                        intent.putExtra(WebFragment.URL_KEY, qrMoreInfo.getContent());
                    }

                    startActivity(intent);
                    finish();
                }
                break;
            case Message.RESULT_FAILURE:
                //延时2秒后重新开始扫描，避免重复扫描成功
                mQR.startSpotDelay(2 * 1000);
                //显示扫描框
                mQR.showScanRect();
                break;
        }
    }

    @Override
    public void showLoading() {
        showLoadingDialog("正在验证二维码，请稍后。。。", false);
    }

    @Override
    public void hideLoading() {
        closeLoadingDialog();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
    }

    @Override
    public void onDestroy() {
        mQR.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();

        //开始扫描，跳转到web后跳转回来再次启动扫描
        mQR.startCamera(); // 打开后置摄像头开始预览，但是并未开始识别
        mQR.startSpotAndShowRect(); // 显示扫描框，并且延迟0.1秒后开始识别
    }


    @Override
    public void onStop() {
        super.onStop();
        mQR.stopCamera();
    }

}
