package com.wonders.health.lib.base.widget.dialogplus;

import android.support.annotation.NonNull;

/**
 * Invoked when dialog is completely dismissed. This listener takes the animation into account and waits for it.
 * <p>
 * <p>It is invoked after animation is completed</p>
 */
public interface OnDismissListener {
    void onDismiss(@NonNull DialogPlus dialog);
}
