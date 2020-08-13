package com.xiayiye5.sdktestdemo.loginverify;

import android.os.Handler;
import android.util.Log;

import com.lidroid.xutils.http.RequestParams;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * List of messages for player and manager
 * @author Administrator
 *
 */
public class TestProcess {

	private static final String TAG = "TestProcess";

	public String token;
	public String user_id;

	public void post(Handler handler) {
		RequestParams params = new RequestParams();
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("user_id", user_id);
			jsonObject.put("token", token);
			params.setBodyEntity(new StringEntity(jsonObject.toString()));
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

//		params.addBodyParameter();
//		params.addBodyParameter();

		if (null != handler) {
			TestRequest request = new TestRequest(handler);
			request.post("http://demo.vlcms.com/sdk.php/LoginNotify/login_verify",
					params);
		} else {
			Log.e(TAG, "fun#post handler is null or url is null");
		}
	}
}
