package com.example.user.mya.Utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Utils {

	public static final int SIZETYPE_MB = 3;// 获取文件大小单位为MB的double�?

	/**
	 * 设置ListView高度
	 * 
	 * @param listView
	 *            要设置高度的ListView
	 */
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	/**
	 * 引用String配置文件将int转化为String
	 * 
	 * @param i
	 * @return
	 */
	public static String changeinttoString(Context mContext, int i) {

		return (String) mContext.getResources().getText(i);

	}

	/**
	 * 下载图片
	 * 
	 * @param url
	 * @return
	 */
	public static Bitmap getUrlImage(String url) {
		Bitmap img = null;
		try {
			URL picurl = new URL(url);
			// 获得连接
			HttpURLConnection conn = (HttpURLConnection) picurl
					.openConnection();
			conn.setConnectTimeout(6000);// 设置超时
			conn.setDoInput(true);
			conn.setUseCaches(false);// 不缓�?
			conn.connect();
			InputStream is = conn.getInputStream();// 获得图片的数据流
			img = BitmapFactory.decodeStream(is);
			is.close();

		} catch (Exception e) {

			e.printStackTrace();

		}

		return img;

	}

	/**
	 * 判断某个服务是否正在运行的方�?
	 * 
	 * @param mContext
	 * @param serviceName
	 *            是包�?服务的类名（例如：net.loonggg.testbackstage.TestService�?
	 * @return true代表正在运行，false代表服务没有正在运行
	 */
	public static boolean isServiceWork(Context mContext, String serviceName) {
		boolean isWork = false;
		ActivityManager myAM = (ActivityManager) mContext
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> myList = myAM.getRunningServices(40);
		if (myList.size() <= 0) {
			return false;
		}
		for (int i = 0; i < myList.size(); i++) {
			String mName = myList.get(i).service.getClassName().toString();
			System.out.println("服务============" + mName);
			if (mName.equals(serviceName)) {
				isWork = true;
				break;
			}
		}
		return isWork;
	}

	/**
	 * 获得哪天
	 * 
	 * @return
	 */
	public static String getDay() {
		String monthString = "";
		String dayString = "";
		String hhString = "";
		String mmString = "";
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int mm = calendar.get(Calendar.MINUTE);
		if (month < 10) {
			monthString = "0" + month;
		} else {
			monthString = String.valueOf(month);
		}
		if (day < 10) {
			dayString = "0" + day;
		} else {
			dayString = String.valueOf(day);
		}
		if (hour < 10) {
			hhString = "0" + hour;
		} else {
			hhString = String.valueOf(hour);
		}
		if (mm < 10) {
			mmString = "0" + mm;
		} else {
			mmString = String.valueOf(mm);
		}
		return year + "-" + monthString + "-" + dayString + " " + hhString
				+ ":" + mmString;
	}

	/**
	 * 获得文件的长�?
	 * 
	 * @param filePath
	 * @param sizeType
	 * @return
	 */
	public static double getFileOrFilesSize(String filePath, int sizeType) {

		File file = new File(filePath);

		long blockSize = 0;

		try {

			if (file.isDirectory()) {

				blockSize = getFileSize(file);

			} else {

				blockSize = getFileSize(file);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}

		return FormetFileSize(blockSize, sizeType);

	}

	/**
	 * 获得文件的大�?
	 * 
	 * @param fileS
	 * @param sizeType
	 * @return
	 */
	private static double FormetFileSize(long fileS, int sizeType)

	{

		DecimalFormat df = new DecimalFormat("#.00");

		double fileSizeLong = 0;

		switch (sizeType) {

		// case SIZETYPE_B:
		//
		// fileSizeLong=Double.valueOf(df.format((double) fileS));

		// break;
		//
		// case SIZETYPE_KB:
		//
		// fileSizeLong=Double.valueOf(df.format((double) fileS / 1024));

		// break;

		case SIZETYPE_MB:

			fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));

			break;

		// case SIZETYPE_GB:
		//
		// fileSizeLong=Double.valueOf(df.format((double) fileS / 1073741824));
		//
		// break;

		default:

			break;

		}

		return fileSizeLong;

	}

	/**
	 * 服务是否�?��
	 * 
	 * @param ip
	 * @return
	 */
	public static boolean isOpenService(String ip) {
		InetAddress address;
		try {
			address = InetAddress.getByName(ip);
			return address.isReachable(5000);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	private static final String Util_LOG = makeLogTag(Utils.class);

	public static String makeLogTag(Class<?> cls) {
		return cls.getName();
	}

	private static Context contextx;

	public static void init(Context c) {
		contextx = c;
	}

	/**
	 * 弹出框提示
	 * 
	 * @param a
	 */
	public static Toast toast = null;

	public static void showToast(Context context, String str) {
		if (toast == null) {
			toast = Toast.makeText(context, str, Toast.LENGTH_SHORT);
		} else {
			toast.setText(str);
		}
		toast.show();// 重新显示吐司
	}

	/**
	 * 网络连接失败弹出信息
	 * 
	 * @param context
	 */
	public static void showOnlinError(Context context) {
		toast = Toast.makeText(context, "联网失败!请检查网络!", Toast.LENGTH_SHORT);
		toast.show();// 重新显示吐司
	}

	/**
	 * 是否存在SD卡?
	 * 
	 * @return
	 */
	public static boolean hasSdcard() {
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 创建目录
	 * 
	 * @param context
	 * @param dirName
	 *            文件夹名�?
	 * @return
	 */
	public static File createFileDir(Context context, String dirName) {
		String filePath;
		// 如SD卡已存在，则存储；反之存在data目录�?
		if (hasSdcard()) {
			// SD卡路�?
			filePath = Environment.getExternalStorageDirectory()
					+ File.separator + dirName;
		} else {
			filePath = context.getCacheDir().getPath() + File.separator
					+ dirName;
		}
		File destDir = new File(filePath);
		if (!destDir.exists()) {
			boolean isCreate = destDir.mkdirs();
			Log.i(Util_LOG, filePath + " has created. " + isCreate);
		}
		return destDir;
	}

	/**
	 * 删除文件（若为目录，则�?归删除子目录和文件）
	 * 
	 * @param file
	 * @param delThisPath
	 *            true代表删除参数指定file，false代表保留参数指定file
	 */
	public static void delFile(File file, boolean delThisPath) {
		if (!file.exists()) {
			return;
		}
		if (file.isDirectory()) {
			File[] subFiles = file.listFiles();
			if (subFiles != null) {
				int num = subFiles.length;
				// 删除子目录和文件
				for (int i = 0; i < num; i++) {
					delFile(subFiles[i], true);
				}
			}
		}
		if (delThisPath) {
			file.delete();
		}
	}

	/**
	 * 获取文件大小，单位为byte（若为目录，则包括所有子目录和文件）
	 * 
	 * @param file
	 * @return
	 */
	public static long getFileSize(File file) {
		long size = 0;
		if (file.exists()) {
			if (file.isDirectory()) {
				File[] subFiles = file.listFiles();
				if (subFiles != null) {
					int num = subFiles.length;
					for (int i = 0; i < num; i++) {
						size += getFileSize(subFiles[i]);
					}
				}
			} else {
				size += file.length();
			}
		}
		return size;
	}

	/**
	 * 保存Bitmap到指定目�?
	 * 
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件�?
	 * @param bitmap
	 * @throws IOException
	 */
	public static void savaBitmap(File dir, String fileName, Bitmap bitmap) {
		if (bitmap == null) {
			return;
		}
		File file = new File(dir, fileName);
		try {
			file.createNewFile();
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.JPEG, 100, fos);
			fos.flush();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 判断某目录下文件是否存在
	 * 
	 * @param dir
	 *            目录
	 * @param fileName
	 *            文件�?
	 * @return
	 */
	public static boolean isFileExists(File dir, String fileName) {
		return new File(dir, fileName).exists();
	}

	/**
	 * 判断wifi是否连接状态
	 */
	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm != null
				&& cm.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI;
	}

	/**
	 * 判断是否网络连接
	 */
	public static boolean isOnline(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo info = manager.getActiveNetworkInfo();
		if (info != null && info.isConnected()) {
			return true;
		}
		return false;
	}
	public static  String getDate(){
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
		return df.format(new Date());
	}
	public static boolean Nonull(String result) {
		// TODO Auto-generated method stub
		if (result != null && !result.equals("") && !result.equals("null")
				&& !result.equals("[]")) {
			return true;
		} else {
			return false;
		}
	}



}
