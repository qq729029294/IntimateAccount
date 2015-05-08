package com.nan.utils;

import java.io.ByteArrayOutputStream;
import android.graphics.Bitmap;

public class Utils {
	static public byte[] bitmapToByteArray(Bitmap bm) {
		int size = bm.getWidth() * bm.getHeight() * 4;  
        ByteArrayOutputStream baos = new ByteArrayOutputStream(size);  
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);  
        byte[] imagedata = baos.toByteArray();
        
        return imagedata;
	}
}