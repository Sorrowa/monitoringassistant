package cn.cdjzxy.monitoringassistant.widgets;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.cdjzxy.monitoringassistant.R;
import cn.cdjzxy.monitoringassistant.utils.HelpUtil;

public class IosDialog extends Dialog {

    public IosDialog( Context context ) {
        super( context );
    }

    public IosDialog( Context context, int theme ) {
        super( context, theme );
    }

    public static class Builder {
        private Context m_context;
        private String m_title;
        private String m_message;
        private String m_confirmBtnText;
        private String m_cancelBtnText;
        private View m_contentView;

        private OnClickListener m_confirmBtnClickListener;
        private OnClickListener m_cancelBtnClickListener;

        public Builder( Context context ) {
            this.m_context = context;
        }


        public Builder setMessage( String message ) {
            this.m_message = message;
            return this;
        }

        /**
         * Set the Dialog m_message from resource
         *
         * @param message
         * @return
         */
        public Builder setMessage( int message ) {
            this.m_message = (String) m_context.getText( message );
            return this;
        }

        /**
         * Set the Dialog m_title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle( int title ) {
            this.m_title = (String) m_context.getText( title );
            return this;
        }

        /**
         * Set the Dialog m_title from String
         *
         * @param title
         * @return
         */
        public Builder setTitle( String title ) {
            this.m_title = title;
            return this;
        }

        /**
         * @param v View
         * @return
         */
        public Builder setContentView( View v ) {
            this.m_contentView = v;
            return this;
        }

        /**
         * Set the positive button resource and it's m_listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton( int confirm_btnText,
                                          OnClickListener listener ) {
            this.m_confirmBtnText = (String) m_context
                    .getText( confirm_btnText );
            this.m_confirmBtnClickListener = listener;
            return this;
        }

        /**
         * Set the positive button and it's m_listener
         *
         * @param confirm_btnText
         * @return
         */
        public Builder setPositiveButton( String confirm_btnText,
                                          OnClickListener listener ) {
            this.m_confirmBtnText = confirm_btnText;
            this.m_confirmBtnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button resource and it's m_listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton( int cancel_btnText,
                                          OnClickListener listener ) {
            this.m_cancelBtnText = (String) m_context
                    .getText( cancel_btnText );
            this.m_cancelBtnClickListener = listener;
            return this;
        }

        /**
         * Set the negative button and it's m_listener
         *
         * @param cancel_btnText
         * @return
         */
        public Builder setNegativeButton( String cancel_btnText,
                                          OnClickListener listener ) {
            this.m_cancelBtnText = cancel_btnText;
            this.m_cancelBtnClickListener = listener;
            return this;
        }

        public IosDialog create() {
            LayoutInflater inflater = (LayoutInflater) m_context
                    .getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            // instantiate the dialog with the custom Theme
            final IosDialog dialog = new IosDialog( m_context, R.style.ios_dialog_style );
            View layout = inflater.inflate( R.layout.iosdialog, null );
            dialog.setContentView( layout );
            Window dialogWindow = dialog.getWindow();
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = (int)(HelpUtil.getWidth(m_context)/3);
            lp.height = (int)(HelpUtil.getHeight(m_context)/3);
            dialogWindow.setAttributes( lp );

            // set the dialog m_title
            ( (TextView) layout.findViewById( R.id.title ) ).setText( m_title );
            ( (TextView) layout.findViewById( R.id.title ) ).getPaint().setFakeBoldText( true );
            ;
            // set the confirm button
            if ( m_confirmBtnText != null ) {
                ( (Button) layout.findViewById( R.id.confirm_btn ) )
                        .setText( m_confirmBtnText );
                if ( m_confirmBtnClickListener != null ) {
                    ( (Button) layout.findViewById( R.id.confirm_btn ) )
                            .setOnClickListener( new View.OnClickListener() {
                                public void onClick( View v ) {
                                    m_confirmBtnClickListener.onClick( dialog,
                                            DialogInterface.BUTTON_POSITIVE );
                                }
                            } );
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById( R.id.confirm_btn ).setVisibility(
                        View.GONE );
            }
            // set the cancel button
            if ( m_cancelBtnText != null ) {
                ( (Button) layout.findViewById( R.id.cancel_btn ) )
                        .setText( m_cancelBtnText );
                if ( m_cancelBtnClickListener != null ) {
                    ( (Button) layout.findViewById( R.id.cancel_btn ) )
                            .setOnClickListener( new View.OnClickListener() {
                                public void onClick( View v ) {
                                    m_cancelBtnClickListener.onClick( dialog,
                                            DialogInterface.BUTTON_NEGATIVE );
                                }
                            } );
                }
            } else {
                // if no confirm button just set the visibility to GONE
                layout.findViewById( R.id.cancel_btn ).setVisibility(
                        View.GONE );
            }
            // set the content m_message
            if ( m_message != null ) {
                ( (TextView) layout.findViewById( R.id.message ) ).setText( m_message );
            } else if ( m_contentView != null ) {
                // if no m_message set
                // add the m_contentView to the dialog body
                ( (LinearLayout) layout.findViewById( R.id.layout ) )
                        .removeAllViews();
                ( (LinearLayout) layout.findViewById( R.id.layout ) ).addView(
                        m_contentView, new LayoutParams(
                                LayoutParams.WRAP_CONTENT,
                                LayoutParams.WRAP_CONTENT ) );
            }
            dialog.setCanceledOnTouchOutside( false );
            return dialog;
        }

    }

    public static void showDialog( Context mContext, String title, String message,String pStr,String nStr, OnClickListener pClickListener,OnClickListener nClickListener ) {
        Builder builder = new Builder( mContext );
        builder.setTitle( title );
        builder.setMessage( message );
        builder.setPositiveButton( pStr, pClickListener );
        builder.setNegativeButton( nStr, nClickListener);
        builder.create().show();
    }
}
