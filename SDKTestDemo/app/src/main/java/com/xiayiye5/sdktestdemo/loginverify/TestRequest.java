package com.xiayiye5.sdktestdemo.loginverify;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class TestRequest {

	private static final String TAG = "TestRequest";

	HttpUtils http;
	Handler mHandler;

	public TestRequest(Handler mHandler) {
		http = new HttpUtils();
		if (null != mHandler) {
			this.mHandler = mHandler;
		}
	}

	public void post(String url, RequestParams params) {
		if (TextUtils.isEmpty(url) || null == params) {
			Log.e(TAG, "fun#post url is null add params is null");
			return;
		}
		Log.e(TAG, "fun#post url = " + url);
		http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {

			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				String response = responseInfo.result;
				Log.w(TAG, "验证结果:" + response);
				try {
					JSONObject json = new JSONObject(response);
					Message msg = new Message();
					msg.obj = json.optString("msg","");
					if(null != mHandler){
						mHandler.sendMessage(msg);
					}
				} catch (JSONException e) {
				}

			}

			@Override
			public void onFailure(HttpException error, String msg) {
				Log.e(TAG, "onFailure" + msg);
			}
		});
	}
}
