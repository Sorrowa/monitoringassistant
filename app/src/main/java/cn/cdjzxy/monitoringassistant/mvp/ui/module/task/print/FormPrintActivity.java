package cn.cdjzxy.monitoringassistant.mvp.ui.module.task.print;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.wonders.health.lib.base.base.DefaultAdapter;
import com.wonders.health.lib.base.utils.ArtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.adapter.PrinterAdapter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.base.BaseTitileActivity;
import cn.cdjzxy.monitoringassistant.utils.BluetoothUtils;

/**
 * 表单打印
 */

public class FormPrintActivity extends BaseTitileActivity<ApiPresenter> {

    @BindView(R.id.recyclerview)
    RecyclerView recyclerview;

    private PrinterAdapter mPrinterAdapter;
    private BluetoothUtils bluetoothUtils;
    private boolean mScanning = false;
    private List<BluetoothDevice> mBlueList=new ArrayList<>();
    private BluetoothAdapter mBluetoothAdapter;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("表单打印");
        titleBar.setRightTextDrawable(R.mipmap.ic_form_print);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //bluetoothUtils.startScanDevices();
            }
        });
    }

    @Nullable
    @Override
    public ApiPresenter obtainPresenter() {
        return new ApiPresenter(ArtUtils.obtainAppComponentFromContext(this));
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_form_print;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        registerBlueCast();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // Get a set of currently paired devices
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                mBlueList.add(device);
            }
        }
        /*
        bluetoothUtils = BluetoothUtils.getInstance();
        bluetoothUtils.initBluetooth(this);
        if (!bluetoothUtils.isSupportBlueTooth()) {
            ArtUtils.makeText(this, "设备不支持蓝牙4.0");
        } else {
            bluetoothUtils.getBluetoothAdapter().enable();
            bluetoothUtils.startScanDevices();
        }
        bluetoothUtils.setCallback(new BluetoothUtils.BlueCallback() {
            @Override
            public void CallbackList(List<BluetoothDevice> mBlueList) {
                ArtUtils.makeText(FormPrintActivity.this, mBlueList.toString());
                mPrinterAdapter.refreshInfos(mBlueList);
            }
        });
        */


        initData();
    }

    /**
     * init data
     */
    private void initData() {
        ArtUtils.configRecyclerView(recyclerview, new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean canScrollVertically() {
                return true;
            }
        });

        mPrinterAdapter = new PrinterAdapter(mBlueList);
        mPrinterAdapter.setOnItemClickListener(new DefaultAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int viewType, Object data, int position) {

            }
        });
        recyclerview.setAdapter(mPrinterAdapter);
    }

    private void registerBlueCast(){
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
    }

    private void scanDevices() {
        if (mScanning) {
            mScanning = false;
            mBluetoothAdapter.cancelDiscovery();
            return;
        }

        // Indicate scanning in the title
        setProgressBarIndeterminateVisibility(true);
        setTitle(com.micheal.print.R.string.scaning);
        // Turn on sub-title for new devices

        // If we're already discovering, stop it
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        // Request discover from BluetoothAdapter
        mBluetoothAdapter.startDiscovery();
        mScanning = true;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mScanning = false;
                //btDeviceScan.setText("扫描完成");
                mBluetoothAdapter.cancelDiscovery();
            }
        }, 10000);

    }

    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    if (!mBlueList.contains(device)) {
                        mBlueList.add(device);
                    }
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                    .equals(action)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle(com.micheal.print.R.string.select_bluetooth_device);
            }
            mPrinterAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        if (mFindBlueToothReceiver != null){
            this.unregisterReceiver(mFindBlueToothReceiver);
        }
    }
}
