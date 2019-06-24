package cn.cdjzxy.monitoringassistant.mvp.ui.module.scanCode;


import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.wonders.health.lib.base.base.fragment.BaseFragment;
import com.wonders.health.lib.base.di.component.AppComponent;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;
import com.wonders.health.lib.base.utils.PermissionUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.qr.QrMoreInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfo;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.user.UserInfoAppRight;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebActivity;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.webview.WebFragment;
import cn.cdjzxy.monitoringassistant.utils.CheckUtil;

import static android.content.Context.VIBRATOR_SERVICE;
import static com.wonders.health.lib.base.utils.Preconditions.checkNotNull;

/**
 * 扫码
 */
public class ScanCodeFragment extends BaseFragment<ApiPresenter> implements IView {

    Unbinder unbinder;
    @BindView(R.id.qr)
    ZXingView mQR;

    private AppComponent mAppComponent;

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_scan, null);
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        requestPermission();
        mQR.setDelegate(new QRCodeView.Delegate() {
            @Override
            public void onScanQRCodeSuccess(String result) {
                vibrate();
                //todo:验证内容,扫描到的内容都没有问题
                mPresenter.getQrInfo(Message.obtain(ScanCodeFragment.this, new Object()), result);
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

//        mPresenter.getQrInfo(Message.obtain(ScanCodeFragment.this, new Object()), "0218100027");
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        ArtUtils.makeText(getContext(), message);
    }

    @Override
    public void handleMessage(@NonNull Message message) {
        checkNotNull(message);
        switch (message.what) {
            case Message.RESULT_OK:
                QrMoreInfo qrMoreInfo = (QrMoreInfo) message.obj;

                if (!CheckUtil.isNull(qrMoreInfo)) {
                    if (qrMoreInfo.getContent().indexOf("devInfo") != -1) {
                        if (!UserInfoHelper.get().isHavePermission(UserInfoAppRight.APP_Permission_Instrument_Lend_Num)) {
                            showNoPermissionDialog("才能进行表单查看。", UserInfoAppRight.APP_Permission_Instrument_Lend_Name);
                            return;
                        }
                    }

                    Intent intent = new Intent(getContext(), WebActivity.class);

                    if (qrMoreInfo.getType() == 2) {
                        //获取用户信息
                        UserInfo user = UserInfoHelper.get().getUserInfo();
                        //相对路径
                        intent.putExtra(WebFragment.URL_KEY, user.getWebUrl() + qrMoreInfo.getContent() + "&userId=" + user.getId());
                    } else if (qrMoreInfo.getType() == 3) {
                        //绝对路径
                        intent.putExtra(WebFragment.URL_KEY, qrMoreInfo.getContent());
                    }

                    startActivity(intent);
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

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        mAppComponent = ArtUtils.obtainAppComponentFromContext(getContext());
        return new ApiPresenter(mAppComponent);
    }

    @Override
    public void setData(@Nullable Object data) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        mQR.onDestroy();
        super.onDestroyView();
        unbinder.unbind();
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
                new RxPermissions(getActivity()), mAppComponent.rxErrorHandler());
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getActivity().getSystemService(VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(200);
        }
    }
}
