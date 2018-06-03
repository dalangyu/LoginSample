package zhuoyan.com.loginsample;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.base.baselibrary.util.ToastUtils;

import zhuoyan.com.loginsample.base.activity.PActivity;
import zhuoyan.com.loginsample.contract.LoginContract;
import zhuoyan.com.loginsample.persenter.LoginPresenter;

public class MainActivity extends PActivity<LoginPresenter> implements LoginContract.View {

    private EditText etName;
    private EditText etPwd;
    private Button btnLogin;

    @Override
    protected int layoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {
        initView();
        initOnClick();
    }

    private void initView() {
        etName = findViewById(R.id.main_et_name);
        etPwd = findViewById(R.id.main_et_pwd);
        btnLogin = findViewById(R.id.main_login);
    }


    private void initOnClick() {

        //登录按钮操作
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }


    private void login() {
        mPresenter.login();
    }


    @Override
    public String getName() {
        return etName.getText().toString();
    }

    @Override
    public String getPwd() {
        return etPwd.getText().toString();
    }

    @Override
    public void toHome() {
        ToastUtils.showSingleToast("跳转进入首页。。");
    }


}
