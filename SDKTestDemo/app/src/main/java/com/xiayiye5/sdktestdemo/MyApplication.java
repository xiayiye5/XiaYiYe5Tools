package com.xiayiye5.sdktestdemo;

import android.app.Application;
import android.content.Context;

import com.mchsdk.open.MCApiFactory;
import com.mchsdk.paysdk.splash.XiaYiYeSDK;

/**
 * @author xiayiye5
 * 2020年6月5日16:42:48
 */
public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //如果你的项目已经有了自定义的application,
        // 可以在你自定义的application的attachBaseContext方法中初始化即可
        // 下面初始化顺序请保持原样不要修改
        initData();
        XiaYiYeSDK.init();
        // 初始化方法 (第二个参数默认false即可)
        MCApiFactory.getMCApi().init(this, false);
    }

    /**
     * sdk 初始化相关
     */
    private void initData() {
        // 第一个参数和第二个参数：渠道id和渠道名称，一般联运不会特殊要求，这里填写固定值“0”和“自然注册”即可
        // 第三、第四、第五个参数分别为： 游戏id、游戏名称、游戏appid，一般联运会要求填写对应游戏的真实参数以方便在联运后台区分对接是否成功
        // 第六、第七个参数：访问秘钥和联运SDK服务器地址联运提供
        MCApiFactory.getMCApi().setParams("0",
                // 渠道名称
                "自然注册",
                // 游戏id(编号)
                "312",
                // 游戏名称
                "旅行精灵(安卓版)",
                // 游戏appid
                "8AA2705833266B56E",
                // 访问秘钥： (SDK访问服务器时的加密key)
                "VloXGQAwNBsAOgoe",
                // 支付地址
                "https://gwxprd.gfan.com/sdk/v1/");
    }
}
