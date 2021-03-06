package com.dazhi.libroot.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.dazhi.libroot.R;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UtRoot {
    private UtRoot() {
        throw new UnsupportedOperationException("you can't instantiate me");
    }

    @SuppressLint("StaticFieldLeak")
    private static Context context;
    public static void initApp(Context context) {
        UtRoot.context = context;
    }

    /**
     * 获取ApplicationContext
     * @return ApplicationContext
     */
    public static Context getAppContext() {
        if (context != null) {
            return context;
        }
        throw new NullPointerException("you should init first");
    }

    /**=======================================
     * 作者：WangZezhi  (2018/5/23  14:59)
     * 功能：  获得xml资源
     * 描述：
     *=======================================*/
    public static String getString(@StringRes int id) {
        return context.getString(id);
    }
    public static int getColor(@ColorRes int iId){
        return ResourcesCompat.getColor(context.getResources(), iId, null);
    }
    public static Drawable getDrawable(@DrawableRes int iId){
        return ResourcesCompat.getDrawable(context.getResources(), iId, null);
    }

    /**=======================================
     * 作者：WangZezhi  (2018/7/11  09:09)
     * 功能：
     * 描述：getDimension()返回的是float，其余两个返回的是int
     * getDimensionPixelSize()返回的是实际数值的四舍五入
     * getDimensionPixelOffset返回的是实际数值去掉后面的小数点;
     * 三个函数返回的都是像素(即dip值乘以屏幕密度),如果你在资源文件中定义的长度单位不是dip，
     * 而是px的话，程序会直接抛出异常或原样输出，具体需自己测试。
     *=======================================*/
    public static int getPx(@DimenRes int intId){
        return context.getResources().getDimensionPixelSize(intId);
    }

    /**********************************************
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dpToPx(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f); //加0.5f相当于四舍五入
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int pxToDp(float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**=======================================
     * 作者：WangZezhi  (2018/5/24  15:18)
     * 功能：设置最顶层view背景
     * 描述：getDecorView 获得window最顶层的View
     *=======================================*/
    public static void setLayoutBg(Activity activity){
        if(activity==null){
            return;
        }
        View view = activity.getWindow().getDecorView();
        view.setBackgroundColor(UtStatusBar.getLayoutBgColor());
    }

    /**=======================================
     * 作者：WangZezhi  (2018/4/27  14:15)
     * 功能：设置屏幕的宽高
     * 描述：Theme.Dialog View高度；在setContentView(id)之后添加以下代码
     *=======================================*/
    public static void setWHActivity(Activity activity, float floScaleW, float floScaleH) {
        try {
            Window window = activity.getWindow();
            Display display = window.getWindowManager().getDefaultDisplay();
            android.view.WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = (int) (display.getWidth() * floScaleW);
            wmLp.height = (int) (display.getHeight() * floScaleH);
            window.setAttributes(wmLp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**=======================================
     * 作者：WangZezhi  (2018/10/17  09:54)
     * 功能：
     * 描述：当floScaleH是0时，设置floScaleW==floScaleH
     *=======================================*/
    public static void setWHDialog(Dialog dialog, float floScaleW, float floScaleH){
        try {
            Window window = dialog.getWindow();
            if(window==null){
                return;
            }
            Display display = window.getWindowManager().getDefaultDisplay();
            android.view.WindowManager.LayoutParams wmLp = window.getAttributes();
            wmLp.width = (int) (display.getWidth() * floScaleW);
            // 当floScaleH是0时，设置floScaleW==floScaleH
            if(floScaleH==0){
                wmLp.height = wmLp.width;
            }else {
                wmLp.height = (int) (display.getHeight() * floScaleH);
            }
            window.setAttributes(wmLp);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**=======================================
     * 作者：WangZezhi  (2018/3/23  15:10)
     * 功能：
     * 描述：
     *=======================================*/
    public static String getPackName(){
        return context.getPackageName();
    }
    public static int getVersionCode() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackName(), 0);
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; //表示未找到
        }
    }
    public static String getVersionName() {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(getPackName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "unknown";
        }
    }
    /**=======================================
     * 作者：WangZezhi  (2018/3/23  15:15)
     * 功能：
     * 描述：
     *=======================================*/
    /**
     * 调用此方法获得设备号
     */
    public static String getDeviceId() {
        String IMEI = "";
        try {
            TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                return "";
            }
            IMEI = telephonyManager.getDeviceId();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return IMEI;
    }

    /**=======================================
     * 作者：WangZezhi  (2018/3/10  10:42)
     * 功能： 时间比较
     * 描述：
     * @param strDateSta
     * @param strDateEnd
     * @return 1大于 0等于 -1小于 250异常
     *=======================================*/
    public static int timeCompare(String strDateSta, String strDateEnd){
        final int intFail=250; //比较失败
        if(TextUtils.isEmpty(strDateSta) || TextUtils.isEmpty(strDateEnd)){
            return intFail;
        }
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date dateSta, dateEnd;
        try {
            dateSta=sdf.parse(strDateSta);
            dateEnd=sdf.parse(strDateEnd);
        } catch (ParseException e) {
            e.printStackTrace();
            return intFail;
        }
        long lonDiff=dateEnd.getTime() - dateSta.getTime();
        if(lonDiff > 0){
            return 1;
        }
        if(lonDiff==0){
            return 0;
        }
        return -1;
    }

    /**
     * 作者：WangZezhi
     * 功能：获得几天前的时间（如果是今天零晨，则填0）
     * 详情：
     * @param iDaysAgo 几天前
     * @return 时间戳
     */
    public static String getDateStrByDaysAgo(int iDaysAgo) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - iDaysAgo);
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(calendar.getTime());
    }

    public static long timeStandardToStamp(String strTimeStandard){
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long lonTime=0;
        try {
            lonTime=sdf.parse(strTimeStandard).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return lonTime;
        }
        return lonTime;
    }

    /**=======================================
     * 作者：WangZezhi  (2018/3/22  11:29)
     * 功能：
     * 描述：
     * @param strDate : "yyyy-MM-dd"
     *=======================================*/
    public static String getDateTimeEnd(String strDate){
        return strDate+" 23:59:59";
    }

    public static String getDateTimeStr(){
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }


    /**=======================================
     * 作者：WangZezhi  (18-1-12  下午3:47)
     * 功能：保留两位小数
     * 描述：
     *=======================================*/
    public static String toPointAfterTwoStr(double dValue){
        return new BigDecimal(dValue).setScale(2, BigDecimal.ROUND_HALF_EVEN).toString()+"元";
    }

    /**=======================================
     * 作者：WangZezhi  (2018/6/4  17:23)
     * 功能：SnackBar
     * 描述：参数view是当前布局中随便一个view；最好是布局的根view
     *=======================================*/
    private static void snackbarShow(@NonNull View view,
                                     String strMsge,
                                     int iDuration,
                                     String strBtTitle,
                                     View.OnClickListener onClickListener){
        //构建snackbar
        Snackbar snackbar=Snackbar.make(view, strMsge, iDuration);
        //click的字体颜色
        snackbar.setActionTextColor(Color.parseColor("#2D9C93"));
        //获得view
        View viewSnack=snackbar.getView();
        //设置背景
        viewSnack.setBackgroundColor(Color.parseColor("#AAAAAA"));
        //内容字体颜色
        TextView tvSnackbarText = viewSnack.findViewById(android.support.design.R.id.snackbar_text);
        tvSnackbarText.setTextColor(Color.WHITE);
        //设置点击监听
        if(!TextUtils.isEmpty(strBtTitle) && onClickListener!=null){
            snackbar.setAction(strBtTitle, onClickListener);
        }
        //显示
        snackbar.show();
    }

    public static void snackbarShort(@NonNull View view,
                                     String strMsge,
                                     String strBtTitle,
                                     View.OnClickListener onClickListener){
        snackbarShow(view, strMsge, Snackbar.LENGTH_SHORT,
                strBtTitle, onClickListener);
    }
    public static void snackbarShort(@NonNull View view, String strMsge){
        snackbarShow(view, strMsge, Snackbar.LENGTH_SHORT,
                null, null);
    }


    public static void snackbarLong(@NonNull View view,
                                     String strMsge,
                                     String strBtTitle,
                                     View.OnClickListener onClickListener){
        snackbarShow(view, strMsge, Snackbar.LENGTH_LONG,
                strBtTitle, onClickListener);
    }
    public static void snackbarLong(@NonNull View view, String strMsge){
        snackbarShow(view, strMsge, Snackbar.LENGTH_LONG,
                null, null);
    }

    public static void snackbarIndefinite(@NonNull Activity activity, String strMsge){
        // 这种方式可以间接的获得当前activity的顶层容器
        View view=((ViewGroup)activity.findViewById(android.R.id.content)).getChildAt(0);
        //构建snackbar
        final Snackbar snackbar=Snackbar.make(view, strMsge, Snackbar.LENGTH_INDEFINITE);
        //click的字体颜色
        snackbar.setActionTextColor(Color.parseColor("#2D9C93"));
        //获得view
        View viewSnack=snackbar.getView();
        //设置背景
        viewSnack.setBackgroundColor(Color.parseColor("#AAAAAA"));
        //内容字体颜色
        TextView tvSnackbarText = viewSnack.findViewById(android.support.design.R.id.snackbar_text);
        tvSnackbarText.setTextColor(Color.WHITE);
        //设置点击监听
        snackbar.setAction(getString(R.string.libroot_dialogedit_esc), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        //显示
        snackbar.show();
    }

    /**
     * 功能:<br/>
     * &nbsp;&nbsp;&nbsp;用于在线程中显示Toast的界面，用于提示信息显示.<br/>
     *
     * @param context    (上下文).
     * @param strMessage (需要显示的消息).
     * @param iDuration  显示的等待时间,可选值:
     *                   {@link Toast#LENGTH_SHORT LENGTH_SHORT} 和
     *                   {@link Toast#LENGTH_LONG LENGTH_LONG}<br/>
     * @param //   显示Toast的风格，比如居中等 {@link Gravity#CENTER_HORIZONTAL CENTER_HORIZONTAL}
     * @see Toast
     */
    private static void toastInThreadShow(final Context context,
                                         final String strMessage,
                                          final int iDuration) {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //自定义toast布局
                View layout = LayoutInflater.from(UtRoot.getAppContext())
                        .inflate(R.layout.libroot_toast, null);
                TextView tvLibRootToast = layout.findViewById(R.id.tvLibRootToast);
                //设置显示文本
                tvLibRootToast.setText(strMessage);
                //构建toast
                Toast toast = Toast.makeText(context, strMessage, iDuration);
                toast.setView(layout);
                toast.show();
            }
        });
    }

    public static void toastShort(final String strMsg) {
        toastInThreadShow(context, strMsg, Toast.LENGTH_SHORT);
    }
    public static void toastShort(@StringRes int intStrId) {
        String strMsg=getString(intStrId);
        toastShort(strMsg);
    }

    public static void toastLong(final String strMsg) {
        toastInThreadShow(context, strMsg, Toast.LENGTH_LONG);
    }
    public static void toastLong(@StringRes int intStrId) {
        String strMsg=getString(intStrId);
        toastLong(strMsg);
    }


}