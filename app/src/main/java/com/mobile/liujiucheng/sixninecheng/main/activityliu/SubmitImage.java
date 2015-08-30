package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import java.io.ByteArrayOutputStream;
import android.util.Base64;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

public class SubmitImage {
	// 计算图片的缩放值
		public static int calculateInSampleSize(BitmapFactory.Options options,
				int reqWidth, int reqHeight) {
			final int height = options.outHeight;
			final int width = options.outWidth;
			int inSampleSize = 1;

			if (height > reqHeight || width > reqWidth) {
				final int heightRatio = Math.round((float) height
						/ (float) reqHeight);
				final int widthRatio = Math.round((float) width / (float) reqWidth);
				inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
			}
			return inSampleSize;
		}

		// 根据路径获得图片并压缩，返回bitmap用于显示
		public static Bitmap getSmallBitmap(String filePath) {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(filePath, options);
			options.inSampleSize = calculateInSampleSize(options, 480, 800);
			options.inJustDecodeBounds = false;

			return BitmapFactory.decodeFile(filePath, options);
		}

		// 把bitmap转换成String
		@SuppressLint("NewApi")
		public static String bitmapToString(String filePath, Bitmap mbm) {
			Bitmap bm;
			if (filePath != null && !filePath.isEmpty()) {
				bm = getSmallBitmap(filePath);
			} else {
				bm = mbm;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.JPEG, 40, baos);
			byte[] b = baos.toByteArray();
			return Base64.encodeToString(b, Base64.DEFAULT);
		}
		
		// 把Drawable转换成Bitmap
		public static Bitmap drawableToBitmap(Drawable drawable) {       
	        Bitmap bitmap = Bitmap.createBitmap(
	                                        drawable.getIntrinsicWidth(),
	                                        drawable.getIntrinsicHeight(),
	                                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
	                                                        : Bitmap.Config.RGB_565);
	        Canvas canvas = new Canvas(bitmap);
	        //canvas.setBitmap(bitmap);
	        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
	        drawable.draw(canvas);
	        return bitmap;

	}
}
