package com.xiayiye5.sdktestdemo.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.mchsdk.paysdk.activity.MCControlResActivity;
import com.mchsdk.paysdk.common.Constant;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;
    String TAG = "WXEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("WXEntryActivity", "onCreate");
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        api = WXAPIFactory.createWXAPI(this, Constant.WXAPPID, false);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        Log.i("WXEntryActivity", "onNewIntent");
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(getIntent(), WXEntryActivity.this);// 必须调用此句话
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Log.i("WXEntryActivity", "onResp");
        String code = "";
        Log.i("WXEntryActivity", "resp.errCode：" + resp.errCode);
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:// 授权成功
                code = ((SendAuth.Resp) resp).code;
                Log.i("WXEntryActivity", "wx auth success");
                returnWXCode(code, resp.errCode + "");
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:// 取消授权
                Log.i("WXEntryActivity", "wx auth cacle");
                returnWXCode("", "");
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:// 没有授权
                Log.i("WXEntryActivity", "wx auth fail");
                returnWXCode("", "");
                break;
            default:
                break;
        }
        finish();
    }

    private void returnWXCode(String wxcode, String errorcode) {
        Intent intent = new Intent(this, MCControlResActivity.class);// 方法二

        Bundle bundle = new Bundle();
        bundle.putString("restype", "wxlogin");
        bundle.putString("errorcode", errorcode);
        bundle.putString("wxcode", wxcode);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}