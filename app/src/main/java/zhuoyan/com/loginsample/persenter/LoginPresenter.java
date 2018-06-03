package zhuoyan.com.loginsample.persenter;

import com.base.baselibrary.base.RxPresenter;
import com.base.baselibrary.util.NetWorkUtils;
import com.base.baselibrary.util.PreferencesUtils;
import com.base.baselibrary.util.StringUtils;
import com.base.baselibrary.util.ThreadPoolProxy;
import com.base.baselibrary.util.ToastUtils;

import java.util.concurrent.CountDownLatch;

import zhuoyan.com.loginsample.App;
import zhuoyan.com.loginsample.contract.LoginContract;
import zhuoyan.com.loginsample.contract.LoginContract.View;
import zhuoyan.com.loginsample.modle.User;

public class LoginPresenter extends RxPresenter<View> implements LoginContract.Presenter {


    @Override
    public void login() {

        if(StringUtils.isEmpty(mView.getName())){
            mView.showErrorMsg("请填写用户名");
            return;
        }

        if(StringUtils.isEmpty(mView.getPwd())){
            mView.showErrorMsg("请填写密码");
            return;
        }

        //no连接
        if(!NetWorkUtils.isNetworkAvailable(mContext)){

            User mUser=PreferencesUtils.getBean(User.class);

            if(mUser==null){
                mView.showErrorMsg("您未保存过离线信息,请先通过网络登录获取");
                return;
            }

            if(!StringUtils.equals(mView.getName(),mUser.getUserName())){
                mView.showErrorMsg("用户名不存在，请联系管理员!");
                return;
            }

            if(!StringUtils.equals(mView.getPwd(),mUser.getUserPwd())){
               int errorCount= PreferencesUtils.getInt("error",0);
                switch (errorCount){
                    case 0:
                    case 1:
                        mView.showErrorMsg("密码错误，请重新输入!");
                        break;
                    case 2:
                        mView.showErrorMsg("密码错误，您还可以输入2次!");
                        break;
                    case 3:
                        mView.showErrorMsg("密码错误，您还可以输入1次!");
                        break;
                    case 4:
                        mView.showErrorMsg("错误次数过多，你的账号已被锁定!");
                        return;
                    default:
                        mView.showErrorMsg("未知错误!");
                }
                PreferencesUtils.putInt("error",errorCount+1);
                return;
            }
            mView.toHome();
            return;
        }



        mView.startLoading("正在获取数据...");

        ThreadPoolProxy.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //成功获取并保存数据
                    User mUser=new User("张三","123456","123456");
                    PreferencesUtils.putInt("error",0);
                    PreferencesUtils.putBean(mUser);
                    Thread.sleep(3000);
                    App.handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mView.stopLoading();
                            mView.toHome();
                        }
                    });

                } catch (InterruptedException ignored) { }
            }
        });


    }



}
