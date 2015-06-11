/**
 * @ClassName:     CustomDialogBuilder.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月26日 
 */

package com.nan.ia.app.widget;

import android.content.Context;
import android.graphics.Color;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.nan.ia.app.R;

public class CustomDialogBuilder extends NiftyDialogBuilder {
	private CustomDialogBuilder(Context context, int theme) {
		super(context, theme);
	}

	static Context sTmpContext;
	static CustomDialogBuilder sInstance;
	
    public static CustomDialogBuilder getInstance(Context context) {

        if (sInstance == null || !sTmpContext.equals(context)) {
            synchronized (NiftyDialogBuilder.class) {
                if (sInstance == null || !sTmpContext.equals(context)) {
                	sInstance = new CustomDialogBuilder(context, R.style.dialog_untran);
                }
            }
        }
        
        sTmpContext = context;
        
        sInstance.toDefault(sTmpContext);
        return sInstance;
    }
    
	public void toDefault(Context context) {
		withDialogColor(Color.WHITE);
		withTitle(null);
		withTitleColor(context.getResources().getColor(R.color.main_color));
		withMessageColor(context.getResources().getColor(R.color.main_color));
		withDividerColor(context.getResources().getColor(R.color.main_color));
		isCancelable(true);
		isCancelableOnTouchOutside(false);
		withDuration(300);
		withEffect(Effectstype.SlideBottom);
	}

	@Override
	public NiftyDialogBuilder withMessage(int textResId) {
		withMessage(sTmpContext.getString(textResId));
		return this;
	}

	@Override
	public NiftyDialogBuilder withMessage(CharSequence msg) {
		return super.withMessage("\n" + msg + "\n");
	}
}
