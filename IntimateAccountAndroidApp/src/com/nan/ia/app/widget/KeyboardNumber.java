/**
 * @ClassName:     NumberKeyboard.java
 * @Description:   TODO(用一句话描述该文件做什么) 
 * 
 * @author         weijiangnan create on 2015年5月28日 
 */

package com.nan.ia.app.widget;

import java.text.DecimalFormat;

import com.nan.ia.app.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class KeyboardNumber extends RelativeLayout {
	SimpleDigitalcalculator mCalculator = new SimpleDigitalcalculator();
	KeyboardNumberListener mListener = null;
	String mValue = "0";
	TextView textOk = null;

	public KeyboardNumber(Context context) {
		super(context);
		
		initialize(context, null);
	}
	
	public KeyboardNumber(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		initialize(context, attrs);
	}
	
	public KeyboardNumber(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		initialize(context, attrs);
	}
	
	private void initialize(final Context context, AttributeSet attrs) {
		LayoutInflater.from(context).inflate(R.layout.keyboard_number, this);
		OnClickListener keyClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String tag = (String) v.getTag();
				if (tag.equals("OK")) {
					if (null != mListener) {
						mListener.onOKClicked(mCalculator.getCurrentAcceptValue(), mCalculator.getCurrentValue());
					}
					
					return;
				}
				
				// 接受字符，计算
				mCalculator.accept(tag);
				
				// 更新OK符号
				if (mCalculator.hasOperation()) {
					textOk.setTag("=");
					textOk.setText("=");
				} else {
					textOk.setTag("OK");
					textOk.setText("OK");
				}
				
				String acceptValue = mCalculator.getCurrentAcceptValue();
				if (mValue != acceptValue) {
					mValue = acceptValue;
					if (null != mListener) {
						mListener.onValueChanged(acceptValue, mCalculator.getCurrentValue());
					}
				}
			}
		};
		
		ViewGroup layout = (ViewGroup) this.getChildAt(0);
		for (int i = 0; i < layout.getChildCount(); i++) {
			ViewGroup subLayout = (ViewGroup) layout.getChildAt(i);
			for (int j = 0; j < subLayout.getChildCount(); j++) {
				// 监听点击事件
				subLayout.getChildAt(j).setOnClickListener(keyClickListener);
			}
		}
		
		textOk = (TextView) findViewById(R.id.keybord_number_txt_ok);
	}
	
	public void initKeyboardNumber(float initialValue, int maxIntegerCount, int maxDecimalCount) {
		mCalculator.initialDcalculator(initialValue, maxIntegerCount, maxDecimalCount);
		mValue = mCalculator.formatToStringValue(initialValue);
	}
	
	public void setListener(KeyboardNumberListener listener) {
		mListener = listener;
	}
	
	public static interface KeyboardNumberListener {
		public void onValueChanged(String enterValue, float value);
		public void onOKClicked(String enterValue, float value);
	}
	
	public static class SimpleDigitalcalculator {
		String mOperator = "";
		float mWaitForOperationValue;
		String mStrValue;
		
		// 最大小数数
		int mMaxDecimalCount = 2;
		// 最大整数数
		int mMaxIntegerCount = 8;
		
		public void accept(String ch) {
			switch (ch) {
			case "0":
				if (getCurrentValue() == 0 && mStrValue.indexOf(".") == -1) {
					// 值已经是0并且数没有在小数点后，不收
					break;
				}
			case "1":
			case "2":
			case "3":
			case "4":
			case "5":
			case "6":
			case "7":
			case "8":
			case "9":
				// 超出长度，不收
				if ((mStrValue.indexOf(".") != -1 &&
						(mStrValue.length() - mStrValue.indexOf(".") - 1) >= mMaxDecimalCount)) {
					break;
				} else if (mStrValue.indexOf(".") == -1) {
					if (mStrValue.startsWith("-") && mStrValue.length() >= mMaxIntegerCount + 1) {
						break;
					} else if (!mStrValue.startsWith("-") && mStrValue.length() >= mMaxIntegerCount) {
						break;
					}
				}
				
				if (mStrValue.length() == 1 && mStrValue.startsWith("0")) {
					// 如果只有一位并且是0，用新数字替换掉
					mStrValue = ch;
				} else {
					mStrValue += ch;
				}
				
				break;
				
			case ".":
				if (mStrValue.indexOf(".") == -1 && mMaxDecimalCount > 0) {
					// 排除只能添加一个点 , 并且没有小数部分则不收
					mStrValue += ".";
				}
				break;
				
			case "-":
			case "+":
				// 已有运算，先运算
				mWaitForOperationValue = operationValue();
				// 更新运算符
				mOperator = ch;
				// 当前值清空
				mStrValue = "0";
				break;
				
			case "=":
				operationValue();
				break;
			case "C":
				mOperator = "";
				mWaitForOperationValue = 0;
				mStrValue = "0";
				break;
			case "delete":
				if (null != mStrValue && mStrValue.length() > 1) {
					mStrValue = mStrValue.substring(0, mStrValue.length() - 1);

					if (mStrValue.equals("-")) {
						// 最后剩"-"，置0
						mStrValue = "0";
					}
				} else {
					mStrValue = "0";
				}
				
				break;
			default:
				break;
			}
		}
		
		private float operationValue() {
			float value = getCurrentValue();
			if (hasOperation()) {
				
				switch (mOperator) {
				case "-":
					value = mWaitForOperationValue - getCurrentValue();
					break;
					
				case "+":
					value = mWaitForOperationValue + getCurrentValue();
					break;
				default:
					break;
				}
			}
			
			// 更新计算值
			mStrValue = formatToStringValue(value);
			
			// 重置
			mWaitForOperationValue = 0;
			mOperator = "";
			return value;
		}
		
		public boolean hasOperation() {
			return null != mOperator && !mOperator.isEmpty();
		}
		
		public String getCurrentAcceptValue() {
			return mStrValue;
		}
		
		public float getCurrentValue() {
			if (null == mStrValue ||
					mStrValue == "" ||
					mStrValue.equals(".") ||
					mStrValue.equals("-") ||
					mStrValue.equals("+")) {
				return 0;
			}
			
			return Float.valueOf(mStrValue);
		}
		
		public String formatToStringValue(float value) {
			DecimalFormat df = new DecimalFormat("0.##");
			return df.format(value);
		}
		
		public void initialDcalculator(float initialValue, int maxIntegerCount, int maxDecimalCount) {
			mStrValue = formatToStringValue(initialValue);
			mMaxIntegerCount = maxIntegerCount;
			mMaxDecimalCount = maxDecimalCount;
		}
	}
}
