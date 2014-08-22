package com.kongfuzi.student;

import java.io.File;

import org.holoeverywhere.ThemeManager;
import org.holoeverywhere.app.Application;
import org.holoeverywhere.preference.SharedPreferences;

import android.content.Context;
import android.os.Environment;
import com.kongfuzi.student.internal.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

public class MainApplication extends Application{
  
  public static File filesPath;
  public static SharedPreferences prefs;
  public static String secretKey;
  public static ImageLoader imageLoader;

  
  static {
    ThemeManager.setDefaultTheme(ThemeManager.LIGHT);
  }
  
  @Override
  public void onCreate() {
    super.onCreate();

    filesPath = new File(Environment.getExternalStorageDirectory(), "KongfuziStudent");
    filesPath.mkdirs();
    prefs = getDefaultSharedPreferences();

    secretKey = prefs.getString(Constants.SECRET_KEY, "null");
    initImageLoader(getApplicationContext());
    imageLoader = ImageLoader.getInstance();
   
  }

  public void initImageLoader(Context context) {
    File cacheDir = StorageUtils.getOwnCacheDirectory(getApplicationContext(),
        MainApplication.filesPath.getAbsolutePath());

    DisplayImageOptions defaultOption = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisc(true)
        .considerExifParams(false).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
        .displayer(new FadeInBitmapDisplayer(300)).resetViewBeforeLoading(true)
        .showImageForEmptyUri(R.drawable.ic_launcher).showImageOnFail(R.drawable.ic_launcher).build();

    ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).memoryCacheExtraOptions(480, 800)
        .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
        .discCache(new UnlimitedDiscCache(cacheDir)).discCacheFileNameGenerator(new HashCodeFileNameGenerator())
        .defaultDisplayImageOptions(defaultOption).tasksProcessingOrder(QueueProcessingType.LIFO).build();
    ImageLoader.getInstance().init(config);
  }
}
