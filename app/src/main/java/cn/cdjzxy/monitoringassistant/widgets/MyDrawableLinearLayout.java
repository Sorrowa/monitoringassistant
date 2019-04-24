package cn.cdjzxy.monitoringassistant.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cdjzxy.monitoringassistant.R;

public class MyDrawableLinearLayout extends LinearLayout {
    private static final String TAG = "MyDrawableLinearLayout";
    private TextView leftTextView;
    private EditText editText;
    private TextView rightTextView;
    private boolean isShowRightTextView;
    private Drawable hasDataDrawable, noDataDrawable;
    private View.OnClickListener leftTextViewOnClickListener;
    public boolean isHasData = false;

    public MyDrawableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        initAttr(context, attrs);
    }

    public MyDrawableLinearLayout(Context context) {
        super(context);
        init();
    }

    private void initAttr(Context context, @Nullable AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MyDrawableLinearLayout);
        leftTextView.setText(a.getString(R.styleable.MyDrawableLinearLayout_leftText));
        isShowRightTextView = a.getBoolean(R.styleable.MyDrawableLinearLayout_isShowRightTextView, false);
        String s = a.getString(R.styleable.MyDrawableLinearLayout_rightText);
        int hasData = a.getResourceId(R.styleable.MyDrawableLinearLayout_leftHasDataDrawable, -1);
        int noData = a.getResourceId(R.styleable.MyDrawableLinearLayout_leftNoDataDrawable, -1);

        setHasDataDrawable(hasData);
        setNoDataDrawable(noData);
        textDataDrawable(s);
        if (isShowRightTextView) {
            editText.setVisibility(GONE);
            rightTextView.setVisibility(VISIBLE);
            rightTextView.setText(s);
            setRightTextHint(a.getString(R.styleable.MyDrawableLinearLayout_rightTextHint));
            setRightTextViewDrawableRight(a.getResourceId(R.styleable.MyDrawableLinearLayout_drawableRight, -1));
        } else {
            editText.setVisibility(VISIBLE);
            editText.setText(s);
            rightTextView.setVisibility(GONE);
            setEditTextHint(a.getString(R.styleable.MyDrawableLinearLayout_rightEditTextHint));
        }
        a.recycle();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.my_item_layout, this, true);
        leftTextView = findViewById(R.id.text_view_left);
        editText = findViewById(R.id.edit_text);
        rightTextView = findViewById(R.id.text_view);
        leftTextView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftTextViewOnClickListener != null) {
                    leftTextViewOnClickListener.onClick(v);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // editText.setText(s);
                if (s == null || s.toString().equals("")) {
                    if (isHasData) {
                        textDataDrawable("");
                        isHasData = false;
                    }
                } else {
                    if (!isHasData) {
                        textDataDrawable("11");
                        isHasData = true;
                    }
                }
            }
        });

    }

    public void textDataDrawable(String s) {
        Drawable drawable;
        if (s == null || s.equals("")) {
            if (noDataDrawable == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getContext().getDrawable(R.mipmap.icon_no_data);
                } else {
                    drawable = getContext().getResources().getDrawable(R.mipmap.icon_no_data);
                }
            } else {
                drawable = noDataDrawable;
            }

        } else {
            if (hasDataDrawable == null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable = getContext().getDrawable(R.mipmap.icon_yes_data);
                } else {
                    drawable = getContext().getResources().getDrawable(R.mipmap.icon_yes_data);
                }
            } else {
                drawable = hasDataDrawable;
            }
        }
        leftTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, null, null, null);
    }

    public void setRightTextViewDrawableRight(int i) {
        if (i == -1) return;
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = getContext().getDrawable(i);
        } else {
            drawable = getContext().getResources().getDrawable(i);
        }
        rightTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, drawable, null);
    }

    public void setHasDataDrawable(Drawable hasDataDrawable) {
        this.hasDataDrawable = hasDataDrawable;
    }

    public void setNoDataDrawable(Drawable noDataDrawable) {
        this.noDataDrawable = noDataDrawable;
    }


    public void setHasDataDrawable(int hasDataDrawable) {
        if (hasDataDrawable == -1) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.hasDataDrawable = getContext().getDrawable(hasDataDrawable);
        } else {
            this.hasDataDrawable = getContext().getResources().getDrawable(hasDataDrawable);
        }
    }

    public void setNoDataDrawable(int noDataDrawable) {
        if (noDataDrawable == -1) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            this.noDataDrawable = getContext().getDrawable(noDataDrawable);
        } else {
            this.noDataDrawable = getContext().getResources().getDrawable(noDataDrawable);
        }
    }

    public void setLeftDrawable(Drawable noDataDrawable, Drawable hasDataDrawable) {
        this.noDataDrawable = noDataDrawable;
        this.hasDataDrawable = hasDataDrawable;
    }


    public void setLeftDrawable(int noDataDrawable, int hasDataDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (noDataDrawable != -1) {
                this.noDataDrawable = getContext().getDrawable(noDataDrawable);
            }
            if (hasDataDrawable != -1) {
                this.hasDataDrawable = getContext().getDrawable(hasDataDrawable);
            }
        } else {
            if (noDataDrawable != -1) {
                this.noDataDrawable = getContext().getResources().getDrawable(noDataDrawable);
            }
            if (hasDataDrawable != -1) {
                this.hasDataDrawable = getContext().getResources().getDrawable(hasDataDrawable);
            }
        }

    }

    public TextView getLeftTextView() {
        return leftTextView;
    }

    public EditText getEditText() {
        return editText;
    }

    public TextView getRightTextView() {
        return rightTextView;
    }

    public boolean isShowRightTextView() {
        return isShowRightTextView;
    }

    public Drawable getHasDataDrawable() {
        return hasDataDrawable;
    }

    public Drawable getNoDataDrawable() {
        return noDataDrawable;
    }


    public void setRightTextHint(CharSequence hint) {
        rightTextView.setHint(hint);
    }

    public void setRightTextHint(int hint) {
        rightTextView.setHint(hint);
    }

    public String getEditTextStr() {
        return editText == null ? "" : editText.getText().toString();
    }

    public String getRightTextViewStr() {
        return rightTextView == null ? "" : rightTextView.getText().toString();
    }

    public String getLeftTextViewStr() {
        return leftTextView == null ? "" : leftTextView.getText().toString();
    }

    public void setRightTextStr(CharSequence str) {
        rightTextView.setText(str);
        textDataDrawable(str == null ? "" : str.toString());
    }

    public void setRightTextStr(int str) {
        rightTextView.setText(str);
        textDataDrawable(str + "");
    }

    public void setEditTextHint(CharSequence hint) {
        editText.setHint(hint);
    }

    public void setEditTextHint(int hint) {
        editText.setHint(hint);
    }

    public void setEditTextStr(CharSequence str) {
        editText.setText(str);
        textDataDrawable(str == null ? "" : str.toString());
    }

    public void setEditTextStr(int str) {
        editText.setText(str);
        textDataDrawable(str + "");
    }

    public void setLeftTextViewHint(CharSequence hint) {
        leftTextView.setHint(hint);
    }

    public void setLeftTextViewHint(int hint) {
        leftTextView.setHint(hint);
    }

    public void setLeftTextViewStr(CharSequence str) {
        leftTextView.setText(str);
    }

    public void setLeftTextViewStr(int str) {
        leftTextView.setText(str);
    }

    public void setLeftTextViewOnClickListener(OnClickListener listener) {
        leftTextViewOnClickListener = listener;
    }
}
