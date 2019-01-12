package cn.cdjzxy.monitoringassistant.mvp.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.wonders.health.lib.base.base.BaseHolder;

import butterknife.BindView;
import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.mvp.model.entity.sampling.LabelInfo;

public class LabelHolder extends BaseHolder<LabelInfo> {

    @BindView(R.id.ivChoose)
    ImageView ivChoose;
    @BindView(R.id.tvTaskName)
    TextView tvTaskName;
    @BindView(R.id.tvNumber)
    TextView tvNumber;
    @BindView(R.id.tvFrequecyNo)
    TextView tvFrequecyNo;
    @BindView(R.id.tvType)
    TextView tvType;
    @BindView(R.id.tvSampingCode)
    TextView tvSampingCode;
    @BindView(R.id.tvMonitemName)
    TextView tvMonitemName;
    @BindView(R.id.ivQRCode)
    ImageView ivQRCode;
    @BindView(R.id.tvRemark)
    TextView tvRemark;
    @BindView(R.id.tv_cb1)
    TextView tv_cb1;
    @BindView(R.id.tv_cb2)
    TextView tv_cb2;

    LabelInfo data = null;

    public LabelHolder(View itemView) {
        super(itemView);

        ivChoose.setOnClickListener(chooseOnClick);
    }

    @Override
    public void setData(LabelInfo data, int position) {
        this.data = data;

        if (data == null) {
            return;
        }

        this.tvTaskName.setText(data.getTaskName());
        this.tvNumber.setText(data.getNumber());
        this.tvFrequecyNo.setText(data.getFrequecyNo());
        this.tvType.setText(data.getType());
        this.tvSampingCode.setText(data.getSampingCode());
        this.tvMonitemName.setText(data.getMonitemName());
        this.ivQRCode.setImageBitmap(data.getQrCodeImg());
        this.tvRemark.setText(data.getRemark());
        this.tv_cb1.setText(data.getCb1());
        this.tv_cb2.setText(data.getCb2());

        this.updateChooseStatus();
    }

    @Override
    protected void onRelease() {
        this.ivChoose.setOnClickListener(null);
        this.ivChoose = null;
        this.chooseOnClick = null;
        this.tvTaskName = null;
        this.tvNumber = null;
        this.tvFrequecyNo = null;
        this.tvType = null;
        this.tvSampingCode = null;
        this.tvMonitemName = null;
        this.ivQRCode = null;
        this.tvRemark = null;
        this.tv_cb1 = null;
        this.tv_cb2 = null;

        //主动释放位图内存
        if (this.data != null) {
            this.data.dispose();
        }
    }

    private void updateChooseStatus() {
        if (data.isChoose()) {
            ivChoose.setImageResource(R.mipmap.ic_cb_checked);
        } else {
            ivChoose.setImageResource(R.mipmap.ic_cb_nor);
        }
    }

    View.OnClickListener chooseOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            data.setChoose(!data.isChoose());
            updateChooseStatus();
        }
    };
}
