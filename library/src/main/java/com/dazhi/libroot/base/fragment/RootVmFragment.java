package com.dazhi.libroot.base.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dazhi.libroot.R;
import com.dazhi.libroot.base.inte.InteRootView;
import com.dazhi.libroot.base.inte.InteRootVm;
import com.dazhi.libroot.ui.dialog.DialogLoad;
import com.dazhi.libroot.util.UtRoot;

/**
 * 功能：fragment基类
 * 描述：
 * 作者：WangZezhi
 * 邮箱：wangzezhi528@163.com
 * 创建日期：2018/3/23 17:13
 * 修改日期：2018/3/23 17:13
 */
public abstract class RootVmFragment<T extends InteRootVm> extends Fragment implements InteRootView {
    protected Activity activity;
    protected View view;
    protected T fragmentVm;
    //进度对话框
    private DialogLoad dialogLoading;
    //警告对话框
    private AlertDialog dialogMsgBox;

    /*==============抽象方法============*/
    /*获得布局id*/
    protected abstract int getLayoutId();
    /*初始化danger2依赖注入*/
    protected abstract void initConfig();


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity= (Activity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(getLayoutId(), container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //统一设置全局背景
        UtRoot.setLayoutBg(activity);
        //
        initConfig();
        initViewAndDataAndEvent();
    }

    protected void initViewAndDataAndEvent() {
        if(fragmentVm!=null){
            fragmentVm.attachVmView(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(fragmentVm!=null){
            fragmentVm.detachVmView();
            fragmentVm=null;
        }
    }

    /**=======================================
     * 作者：WangZezhi  (2018/2/26  14:51)
     * 功能：IBaseView实现方法
     * 描述：
     *=======================================*/
    @Override
    public void loadingShow(String msg) {
        loadingShut();
        if(activity==null){
            return;
        }
        dialogLoading=new DialogLoad(activity, msg);
        dialogLoading.show();
    }
    @Override
    public void loadingShut() {
        if(dialogLoading!=null){
            dialogLoading.dismiss();
        }
        dialogLoading=null;
    }


    @Override
    public void msgBoxShow(String msg) {
        if (dialogMsgBox != null && dialogMsgBox.isShowing()) {
            dialogMsgBox.dismiss();
        }
        if(activity==null){
            return;
        }
        dialogMsgBox = new AlertDialog.Builder(activity)
                //.setTitle(getString(R.string.libroot_dialogedit_title))
                .setMessage(msg)
                .setPositiveButton(getString(R.string.libroot_dialogedit_ent), null)
                .setCancelable(false)
                .create();
        dialogMsgBox.show();
    }

    @Override
    public void msgBoxShow(String msg, DialogInterface.OnClickListener onClickListener) {
        if (dialogMsgBox != null) {
            dialogMsgBox.dismiss();
        }
        if(activity==null){
            return;
        }
        dialogMsgBox = new AlertDialog.Builder(activity)
                //.setTitle(getString(R.string.libroot_dialogedit_title))
                .setMessage(msg)
                .setPositiveButton(R.string.libroot_dialogedit_ent, onClickListener)
                .setCancelable(false)
                .create();
        dialogMsgBox.show();
    }

    @Override
    public void msgBoxShow(String msg, String strEsc, String strEnt, DialogInterface.OnClickListener onClickListener) {
        if (dialogMsgBox != null) {
            dialogMsgBox.dismiss();
        }
        if(activity==null){
            return;
        }
        dialogMsgBox = new AlertDialog.Builder(activity)
                //.setTitle(getString(R.string.libroot_dialogedit_title))
                .setMessage(msg)
                //取消
                .setNegativeButton(strEsc, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogMsgBox.dismiss();
                    }
                })
                //确定
                .setPositiveButton(strEnt, onClickListener)
                .setCancelable(false)
                .create();
        dialogMsgBox.show();
    }


}


