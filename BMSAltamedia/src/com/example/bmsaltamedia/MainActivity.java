package com.example.bmsaltamedia;

import com.example.data.Validate;
import com.example.http.RequestTask;
import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity implements OnClickListener {
	EditText mEdit_User;
	EditText mEdit_Pass;
	final String regId = GCMRegistrar.getRegistrationId(this);
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		final Button btn_login = (Button) findViewById(R.id.btnlogin);
		btn_login.setOnClickListener(this);
		mEdit_User = (EditText) findViewById(R.id.txtUser);
		mEdit_Pass = (EditText) findViewById(R.id.txtPass);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btnlogin:
			submitDataLogin();
			break;
		default:
			break;
		}

	}

	private void submitDataLogin() {
		if (Validate.isEmpty(mEdit_User.getText().toString())) {
			Toast.makeText(this, "Hãy nhập tên đăng nhập", Toast.LENGTH_SHORT).show();
			return;
		}
		if(Validate.isEmpty(mEdit_Pass.getText().toString())){
			Toast.makeText(this, "Hãy nhập mật khẩu", Toast.LENGTH_SHORT).show();
			return;
		}
		TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		String url = "http://bmsled.altamedia.vn/demo/api.php?mod=ALTA_LOGIN&"
				+ "user=" + mEdit_User.getText().toString() + "&pass="
				+ mEdit_Pass.getText().toString() + "&ime="
				+ mngr.getDeviceId();
		Log.i("url", url);
		try {
			AsyncTask<String, String, String> jsonString = new RequestTask()
					.execute(url);
			Log.i("Json String", jsonString.get());
		} catch (Exception ex) {
			Log.d("ERR", ex.getMessage());
		}

	}

}
