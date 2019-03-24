package cn.cdjzxy.monitoringassistant.widgets;

import android.view.View;

/**
 * Created by gb on 2019/03/02.
 */

public class ViewClickProxy implements View.OnClickListener {

    private View.OnClickListener m_originListener;
    private long m_lastclick = 0;
    private long m_timeMs = 2000; //ms
    private IClickAgain m_clickAgain;

    public ViewClickProxy( View.OnClickListener originListener, long timeMs, IClickAgain clickAgain ) {
        this.m_originListener = originListener;
        this.m_timeMs = timeMs;
        this.m_clickAgain = clickAgain;
    }

    public ViewClickProxy( View.OnClickListener originListener ) {
        this.m_originListener = originListener;
    }

    @Override
    public void onClick( View v ) {
        if ( ( System.currentTimeMillis() - m_lastclick ) >= m_timeMs ) {
            m_originListener.onClick( v );
            m_lastclick = System.currentTimeMillis();
        } else {
            if ( m_clickAgain != null ) {
                m_clickAgain.onClickAgain();
            }
        }
    }

    public interface IClickAgain {
        void onClickAgain();//click again
    }
}
