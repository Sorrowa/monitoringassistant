package cn.cdjzxy.monitoringassistant.mvp.ui.module.easy;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.mvp.IPresenter;

import org.easydarwin.push.MediaStream;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.logic.UserInfoHelper;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import io.reactivex.Single;
import io.reactivex.functions.Consumer;

/**
 * 用于直播的activity
 */
public class EasyPusherActivity extends BaseTitileActivity {
    @BindView(R.id.pushing_state)
    TextView tvPushState;
    @BindView(R.id.check_push)
    CheckBox checkBoxPush;

    private static final int REQUEST_CAMERA_PERMISSION = 1000;
    private static final int REQUEST_MEDIA_PROJECTION = 1001;
    public static final String HOST = "118.123.7.228";//直播推送地地址
    private MediaStream mediaStream;

    private Single<MediaStream> getMediaStream() {
        Single<MediaStream> single = RxHelper.single(MediaStream.getBindedMediaStream(this, this), mediaStream);
        if (mediaStream == null) {
            return single.doOnSuccess(new Consumer<MediaStream>() {
                @Override
                public void accept(MediaStream ms) {
                    mediaStream = ms;
                }
            });
        } else {
            return single;
        }
    }

    private void initService() {
        // 启动服务...
        Intent intent = new Intent(this, MediaStream.class);
        startService(intent);

        getMediaStream().subscribe(new Consumer<MediaStream>() {
            @Override
            public void accept(final MediaStream ms) {
                ms.observePushingState(EasyPusherActivity.this, new Observer<MediaStream.PushingState>() {

                    @Override
                    public void onChanged(@Nullable MediaStream.PushingState pushingState) {
                        if (pushingState.screenPushing) {
                            tvPushState.setText("屏幕推送");
                        } else {
                            tvPushState.setText("推送");
                            if (ms.isCameraPushing()) {
                                checkBoxPush.setChecked(true);
                            } else {
                                checkBoxPush.setChecked(false);
                            }
                        }

                        tvPushState.append(":\t" + pushingState.msg);
                        if (pushingState.state > 0) {
                            tvPushState.append(pushingState.url);
                            tvPushState.append("\n");
//                            if ("avc".equals(pushingState.videoCodec)) {
//                                tvPushState.append("视频编码方式：" + "H264硬编码");
//                            } else if ("hevc".equals(pushingState.videoCodec)) {
//                                tvPushState.append("视频编码方式：" + "H265硬编码");
//                            } else if ("x264".equals(pushingState.videoCodec)) {
//                                tvPushState.append("视频编码方式：" + "x264");
//                            }
                        }

                    }
                });
                TextureView textureView = findViewById(R.id.texture_view);
                if (textureView.isAvailable()) {
                    ms.setSurfaceTexture(textureView.getSurfaceTexture());
                } else {
                    textureView.setSurfaceTextureListener(new SurfaceTextureListenerWrapper() {
                        @Override
                        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i1) {
                            ms.setSurfaceTexture(surfaceTexture);
                        }

                        @Override
                        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                            ms.setSurfaceTexture(null);
                            return true;
                        }
                    });
                }

                if (ActivityCompat.checkSelfPermission(EasyPusherActivity.this,
                        android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(EasyPusherActivity.this,
                                android.Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(EasyPusherActivity.this,
                            new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO}, REQUEST_CAMERA_PERMISSION);
                }
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) {
                Toast.makeText(EasyPusherActivity.this, "创建服务出错!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //屏幕转换
    public void onSwitchCamera(View view) {
        getMediaStream().subscribe(new Consumer<MediaStream>() {
            @Override
            public void accept(MediaStream mediaStream) {
                mediaStream.switchCamera();
            }
        });
    }

    public void onPushing(View view) {
        getMediaStream().subscribe(new Consumer<MediaStream>() {
            @Override
            public void accept(MediaStream mediaStream) {
                MediaStream.PushingState state = mediaStream.getPushingState();
                if (state != null && state.state > 0) { // 终止推送和预览
                    mediaStream.stopStream();
                    mediaStream.closeCameraPreview();
                } else {                                // 启动预览和推送.
                    mediaStream.openCameraPreview();
//                    String id = PreferenceManager.getDefaultSharedPreferences(EasyPusherActivity.this).getString("caemra-id", null);
//                    if (id == null) {
//                        double v = Math.random() * 1000;
//                        id = "c_" + (int) v;
//                        PreferenceManager.getDefaultSharedPreferences(EasyPusherActivity.this).edit().putString("caemra-id", id).apply();
//                    }
                    mediaStream.startStream(HOST, "10554", UserInfoHelper.get().getUserInfo().getId());
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION: {
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getMediaStream().subscribe(new Consumer<MediaStream>() {
                        @Override
                        public void accept(MediaStream mediaStream) {
                            mediaStream.notifyPermissionGranted();
                        }
                    });
                } else {
                    finish();
                }
                break;
            }
        }
    }

    // 推送屏幕.
//    public void onPushScreen(final View view) {
//        getMediaStream().subscribe(new Consumer<MediaStream>() {
//            @Override
//            public void accept(MediaStream mediaStream) {
//                if (mediaStream.isScreenPushing()) { // 正在推送，那取消推送。
//                    // 取消推送。
//                    mediaStream.stopPushScreen();
//                } else {    // 没在推送，那启动推送。
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {     // lollipop 以前版本不支持。
//                        return;
//                    }
//                    MediaProjectionManager mMpMngr = (MediaProjectionManager) getApplicationContext().getSystemService(MEDIA_PROJECTION_SERVICE);
//                    startActivityForResult(mMpMngr.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
//                    // 防止点多次.
//                    view.setEnabled(false);
//                }
//            }
//        });
//    }

    @Override
    protected void onActivityResult(int requestCode, final int resultCode, final Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            getMediaStream().subscribe(new Consumer<MediaStream>() {
                @Override
                public void accept(MediaStream mediaStream) {
                    mediaStream.pushScreen(resultCode, data, HOST, "554", "screen111");
                }
            });
        }
    }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        //推送直播
//        getMediaStream().subscribe(new Consumer<MediaStream>() {
//            @Override
//            public void accept(MediaStream mediaStream) throws Exception {
//                mediaStream.switchCamera();
//            }
//        });
//    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_easy_pusher;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        initService();
    }

    @Nullable
    @Override
    public IPresenter obtainPresenter() {
        return null;
    }

    @Override
    public void beforeSetTitleBar(TitleBarView titleBar) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("直播");
        titleBar.setOnLeftTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
