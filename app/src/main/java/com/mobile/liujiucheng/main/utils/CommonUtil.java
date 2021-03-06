package com.mobile.liujiucheng.main.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;


@SuppressLint("SimpleDateFormat")
@SuppressWarnings("deprecation")
public class CommonUtil {

	public static void showInfoDialog(Context context, String message) {
		showInfoDialog(context, message, "锟斤拷示ʾ", "确锟斤拷", null);
	}

	public static void showInfoDialog(Context context, String message,
			String titleStr, String positiveStr,
			DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder localBuilder = new AlertDialog.Builder(context);
		localBuilder.setTitle(titleStr);
		localBuilder.setMessage(message);
		if (onClickListener == null)
			onClickListener = new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {

				}
			};
		localBuilder.setPositiveButton(positiveStr, onClickListener);
		localBuilder.show();
	}

	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

	/**
	 * ��ָ��byte����ת����16�����ַ�
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * �жϵ�ǰ�Ƿ��п��õ������Լ��������� 0�������� 1��WIFI 2��CMWAP 3��CMNET
	 * 
	 * @param context
	 * @return
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo)
									|| "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * ��ȡ����ʱ��
	 * 
	 * @return ���ض�ʱ���ַ��ʽyyyy-MM-dd HH:mm:ss
	 */

	public static String getStringDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		return dateString;
	}

	/**
	 * ����ֻ�ķֱ��ʴ� dp �ĵ�λ ת��Ϊ px(����)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * ����ֻ�ķֱ��ʴ� px(����) �ĵ�λ ת��Ϊ dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static int getScreenPicHeight(int screenWidth, Bitmap bitmap) {
		int picWidth = bitmap.getWidth();
		int picHeight = bitmap.getHeight();
		int picScreenHeight = 0;
		picScreenHeight = (screenWidth * picHeight) / picWidth;
		return picScreenHeight;
	}

	/**
	 * 
	 * @param context
	 * @param button
	 * @param nornalImageFileName
	 * @param pressedImageFileName
	 * @throws Exception
	 */
/*	public static void bindViewSelector(Context context, final View view,
			String nornalImageurl, final String pressedImageUrl) {
		final StateListDrawable stateListDrawable = new StateListDrawable();
		final BitmapUtils utils = new BitmapUtils(context);
		utils.display(view, nornalImageurl, new BitmapLoadCallBack<View>() {

			@Override
			public void onLoadCompleted(View container, String uri,
					Bitmap bitmap, BitmapDisplayConfig config,
					BitmapLoadFrom from) {
				Drawable normalDrawable = new BitmapDrawable(bitmap);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_active },
						normalDrawable);
				stateListDrawable.addState(new int[] {
						android.R.attr.state_focused,
						android.R.attr.state_enabled }, normalDrawable);
				stateListDrawable.addState(
						new int[] { android.R.attr.state_enabled },
						normalDrawable);
				utils.display(container, pressedImageUrl,
						new BitmapLoadCallBack<View>() {

							@Override
							public void onLoadCompleted(View container,
									String uri, Bitmap bitmap,
									BitmapDisplayConfig config,
									BitmapLoadFrom from) {
								stateListDrawable.addState(new int[] {
										android.R.attr.state_pressed,
										android.R.attr.state_enabled },
										new BitmapDrawable(bitmap));

								view.setBackgroundDrawable(stateListDrawable);

							}

							@Override
							public void onLoadFailed(View container,
									String uri, Drawable drawable) {

							}
						});
			}

			@Override
			public void onLoadFailed(View container, String uri,
					Drawable drawable) {

			}
		});
	}*/

	private static Drawable createDrawable(Drawable d, Paint p) {

		BitmapDrawable bd = (BitmapDrawable) d;
		Bitmap b = bd.getBitmap();
		Bitmap bitmap = Bitmap.createBitmap(bd.getIntrinsicWidth(),
				bd.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		canvas.drawBitmap(b, 0, 0, p); // �ؼ���룬ʹ���µ�Paint��ԭͼ��

		return new BitmapDrawable(bitmap);
	}

	public static StateListDrawable createSLD(Context context, Drawable drawable) {
		StateListDrawable bg = new StateListDrawable();
		int brightness = 50 - 127;
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
				brightness,// �ı�����
				0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });

		Paint paint = new Paint();
		paint.setColorFilter(new ColorMatrixColorFilter(cMatrix));

		Drawable normal = drawable;
		Drawable pressed = createDrawable(drawable, paint);
		bg.addState(new int[] { android.R.attr.state_pressed, }, pressed);
		bg.addState(new int[] { android.R.attr.state_focused, }, pressed);
		bg.addState(new int[] { android.R.attr.state_selected }, pressed);
		bg.addState(new int[] {}, normal);
		return bg;
	}

	public static Bitmap getImageFromAssetsFile(Context ct, String fileName) {
		Bitmap image = null;
		AssetManager am = ct.getAssets();
		try {
			InputStream is = am.open(fileName);
			image = BitmapFactory.decodeStream(is);
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;

	}

//	public static <Params, Progress, Result> void executeAsyncTask(
//			AsyncTask<Params, Progress, Result> task, Params... params) {
//		if (Build.VERSION.SDK_INT >= 11) {
//			task.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
//		} else {
//			task.execute(params);
//		}
//	}

	public static String getUploadtime(long created) {
		StringBuffer when = new StringBuffer();
		int difference_seconds;
		int difference_minutes;
		int difference_hours;
		int difference_days;
		int difference_months;
		long curTime = System.currentTimeMillis();
		difference_months = (int) (((curTime / 2592000) % 12) - ((created / 2592000) % 12));
		if (difference_months > 0) {
			when.append(difference_months + "��");
		}

		difference_days = (int) (((curTime / 86400) % 30) - ((created / 86400) % 30));
		if (difference_days > 0) {
			when.append(difference_days + "��");
		}

		difference_hours = (int) (((curTime / 3600) % 24) - ((created / 3600) % 24));
		if (difference_hours > 0) {
			when.append(difference_hours + "Сʱ");
		}

		difference_minutes = (int) (((curTime / 60) % 60) - ((created / 60) % 60));
		if (difference_minutes > 0) {
			when.append(difference_minutes + "����");
		}

		difference_seconds = (int) ((curTime % 60) - (created % 60));
		if (difference_seconds > 0) {
			when.append(difference_seconds + "��");
		}

		return when.append("ǰ").toString();
	}

//	public static boolean hasToken(Context ct) {
//		String token = SharePrefUtil.getString(ct, "token", "");
//		if (TextUtils.isEmpty(token)) {
//			return false;
//		} else {
//			return true;
//		}
//	}

	@SuppressLint("SimpleDateFormat")
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// ��ȡListView��Ӧ��Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()������������Ŀ
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // ��������View �Ŀ��
			totalHeight += listItem.getMeasuredHeight(); // ͳ������������ܸ߶�
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()��ȡ�����ָ���ռ�õĸ߶�
		// params.height���õ����ListView������ʾ��Ҫ�ĸ߶�
		listView.setLayoutParams(params);
	}
	/**
	 * ��ʽ������
	 * @return
	 */
	public static String formatDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return sdf.format(new Date());
	}
	/**
	 * ��һ��float�͵���װ���ɱ�����λС��
	 * @param value
	 * @return
	 */
	public static float getPersistFloat(float value){
		BigDecimal b = new BigDecimal(value);  
		float f1 = (float) b.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();  
		return f1;
	}
	
	/**
	 * ��һ��floatͨ������������һ������
	 * @param value
	 * @return
	 */
	public static int getPersistInteger(double value){
		BigDecimal b = new BigDecimal(value);  
		int f1 = b.setScale(1, BigDecimal.ROUND_HALF_UP).intValue();  
		return f1;
	}
}
