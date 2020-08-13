package com.xiayiye5.sdktestdemo;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.mchsdk.open.AnnounceTimeCallBack;
import com.mchsdk.open.GPExitResult;
import com.mchsdk.open.GPUserResult;
import com.mchsdk.open.IGPExitObsv;
import com.mchsdk.open.IGPUserObsv;
import com.mchsdk.open.LogoutCallback;
import com.mchsdk.open.MCApiFactory;
import com.mchsdk.open.OrderInfo;
import com.mchsdk.open.PayCallback;
import com.xiayiye5.sdktestdemo.loginverify.TestProcess;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* 注册用户注销监听
         * 1.当用户点击切换账号后不登录直接退出APP后下次在进来需要重新登录
         * 2.当用户点击切换账号后不退出APP登陆了其他账号后再次退出APP下次进来会自动登录上次登录的新账号
         */
        MCApiFactory.getMCApi().initLogoutCallback(logoutCallback);
        /*
         * 自动登录的方法
         * 自动登录说明：如果cp想要实现自动登录功能可以调用下面的方法即可
         * 自动登录有效期为7天,7天内没有登录会自动失效。需要自己手动点击登录才可以登录成功
         */
        MCApiFactory.getMCApi().autoLogin(this, loginCallback);
    }

    /**
     * 用户账号注销回调
     */
    private LogoutCallback logoutCallback = new LogoutCallback() {
        @Override
        public void logoutResult(String result) {
            if (TextUtils.isEmpty(result)) {
                return;
            }
            if ("1".equals(result)) {
                Log.i(TAG, "注销成功");
//                btn_login.setText("登录");
                MCApiFactory.getMCApi().stopFloating(MainActivity.this);
                //调用登录接口
                MCApiFactory.getMCApi().startlogin(MainActivity.this, loginCallback);
            } else {
                Log.e(TAG, "注销失败");
            }

        }
    };
    /**
     * 点击登录按钮的回调
     * 自动登录的回调
     * 使用token登录的回调
     */
    private IGPUserObsv loginCallback = new IGPUserObsv() {
        @Override
        public void onFinish(GPUserResult result) {
            switch (result.getmErrCode()) {
                case GPUserResult.USER_RESULT_LOGIN_FAIL:
                    Log.w(TAG, "登录失败");
                    break;
                case GPUserResult.USER_RESULT_LOGIN_SUCC:
                    //todo 所需要的信息 从result.getUserBean();里面取就可以
                    int uid11 = result.getUserBean().getUser_id();
                    //用户头像
                    String avatar = result.getUserBean().getAvatar();
                    // 登录用户id
                    String uid = result.getAccountNo();
                    //用户token
                    String token = result.getToken();
                    //用户账号
                    String account = result.getAccount();
                    //用户实名认证状态： 2认证通过且成年 -1该用户不存在 0未认证 1未通过认证  3认证通过但未成年
                    int ageStatus = result.getAgeStatus();
                    //用户实名认证生日信息
                    String birthday = result.getBirthday();
//                    btn_login.setText(account);

                    // 将这里返回的用户id和token返回给cp服务器，cp服务器发送给sdk服务器验证登录结果
                    // 以下代码模拟服cp服务器访问sdk服务器登录验证,这个验证需要写在cp服务器
                    TestProcess process = new TestProcess();
                    process.token = token;
                    process.user_id = uid;
                    process.post(verifyLoginhandler);
                    //设置防沉迷回调
                    MCApiFactory.getMCApi().initAnnounceTimeCallBack(announceTimeCallBack);
                    break;
                default:
                    break;
            }
        }
    };
    private Handler verifyLoginhandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Log.w(TAG, "验证结果:" + (String) msg.obj);
        }
    };
    /**
     * 防沉迷时间回掉
     */
    private AnnounceTimeCallBack announceTimeCallBack = new AnnounceTimeCallBack() {
        @Override
        public void callback(String result) {
            Log.i(TAG, "result: " + result);
            if (TextUtils.isEmpty(result)) {
                return;
            }
            if ("1".equals(result)) {
                //第一次到时通知
                Log.i(TAG, "第一次到时通知");
            }
            if ("2".equals(result)) {
                //第二次到时通知,玩家进入疲劳游戏时间
                Log.i(TAG, "第二次到时通知");
            }
            if ("3".equals(result)) {
                Log.e(TAG, "时间到，停止计时。");
            }
        }
    };
    /**
     * 支付结果回调
     */
    private PayCallback payCallback = new PayCallback() {
        @Override
        public void callback(String errorcode) {
            if (TextUtils.isEmpty(errorcode)) {
                return;
            }
            Log.w(TAG, errorcode);
            if ("0".equals(errorcode)) {
                Toast.makeText(MainActivity.this, "支付成功", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(MainActivity.this, "支付失败", Toast.LENGTH_LONG).show();
            }
        }
    };

    public void startPay(View view) {
        OrderInfo order = new OrderInfo();
        order.setProductName("钻石礼包");                //游戏道具名称
        order.setProductDesc("钻石可用于购买游戏内道具");  //游戏道具描述
        order.setAmount(100);                     //游戏道具价格（单位分）
        order.setGameVersion("1.2.3");                     //游戏版本
        order.setExtendInfo(String.valueOf(System.currentTimeMillis())); //透传参数，建议传订单号（当前demo用系统时间模拟订单号，正式接入时请传订单号）
        MCApiFactory.getMCApi().payNew(MainActivity.this, order, payCallback);
    }

    public void startLogin(View view) {
        if (!MCApiFactory.getMCApi().isLogin()) {
            //调用登录方法
            MCApiFactory.getMCApi().startlogin(MainActivity.this, loginCallback);
        }
    }

    public void logout(View view) {
        MCApiFactory.getMCApi().exitDialog(MainActivity.this, mExitObsv);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            MCApiFactory.getMCApi().exitDialog(MainActivity.this, mExitObsv);
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    /**
     * 退出程序回调
     */
    private IGPExitObsv mExitObsv = new IGPExitObsv() {
        @Override
        public void onExitFinish(GPExitResult exitResult) {
            switch (exitResult.mResultCode) {
                case GPExitResult.GPSDKExitResultCodeError:
                    Log.e(TAG, "退出回调:调用退出弹框失败");
                    break;
                case GPExitResult.GPSDKExitResultCodeExitGame:
                    Log.e(TAG, "退出回调:退出方法回调");
                    MCApiFactory.getMCApi().stopFloating(MainActivity.this); //关闭悬浮球
                    //退出程序
                    finish();
                    System.exit(0);
                    break;
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        MCApiFactory.getMCApi().onStop(MainActivity.this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        MCApiFactory.getMCApi().stopFloating(MainActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //请求后台设置的悬浮球开关、图片等
        MCApiFactory.getMCApi().onResume(MainActivity.this);
    }
}
