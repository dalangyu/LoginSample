package zhuoyan.com.loginsample.contract;

import com.base.baselibrary.base.BaseMvpView;
import com.base.baselibrary.base.IPresenter;

public class LoginContract {

    public interface View extends  BaseMvpView{
        String getName();
        String getPwd();
        void toHome();
    }

    public interface Presenter extends IPresenter<View>{
        void login();
    }

}
