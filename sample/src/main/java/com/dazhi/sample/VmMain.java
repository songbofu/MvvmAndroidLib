package com.dazhi.sample;

import android.arch.lifecycle.MutableLiveData;

import com.dazhi.libroot.base.vm.RootViewModel;
import com.dazhi.libroot.util.UtThread;
import com.dazhi.sample.db.BnPerson;
import com.dazhi.sample.db.FactoryDaoPerson;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能：Room框架数据库操作实例
 * 描述：
 * 作者：WangZezhi
 * 邮箱：wangzezhi528@163.com
 * 创建日期：2018/12/8 14:57
 * 修改日期：2018/12/8 14:57
 */
public class VmMain extends RootViewModel<MainActivity> {
    // 用于观察数据库所有消息变动
    private MutableLiveData<List<BnPerson>> dbLsBnPerson=new MutableLiveData<>();

    public MutableLiveData<List<BnPerson>> getDbLsBnPerson() {
        return dbLsBnPerson;
    }


    /**=======================================
     * 作者：WangZezhi  (2018/12/8  16:02)
     * 功能：批量插入
     * 描述：
     *=======================================*/
    public void insertLsBnPerson(){
        // 测试数据
        final List<BnPerson> lsBnPerson=new ArrayList<>();
        for (int i=1,j=10; i<j; i++){
            lsBnPerson.add(new BnPerson("name0"+i));
        }

        // 数据库操作
        UtThread.runSingleThread(new Runnable() {
            @Override
            public void run() {
                // 插入测试数据
                FactoryDaoPerson.self().getDaoPerson().dbInsertLsBnPerson(lsBnPerson);
                // 更新测试数据
                dbLsBnPerson.postValue(FactoryDaoPerson.self().getDaoPerson().dbGetAllBnPerson());
            }
        });
    }

    public void deleteLsBnPerson(final List<BnPerson> lsBnPerson){
        UtThread.runSingleThread(new Runnable() {
            @Override
            public void run() {
                FactoryDaoPerson.self().getDaoPerson().dbDeleteLsBnPerson(lsBnPerson);
                // 更新测试数据
                dbLsBnPerson.postValue(FactoryDaoPerson.self().getDaoPerson().dbGetAllBnPerson());
            }
        });
    }


}
