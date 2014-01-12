package com.ijustyce.lock;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {

	private DevicePolicyManager policyManager;

	private ComponentName componentName;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
		
		policyManager = (DevicePolicyManager) 
				getSystemService(Context.DEVICE_POLICY_SERVICE);
		componentName = new ComponentName(this, LockReceiver.class);
	}

	public void btClick(View v) {

		switch (v.getId()) {

		case R.id.lock:
			LockScreen(v);
			break;
		case R.id.deactivate:
			Bind(v);
			break;
		default:
			break;
		}

	}

	private void LockScreen(View v) {
		
		if (policyManager.isAdminActive(componentName)) {
			
			Log.i("---lock---", "lock ");
			policyManager.lockNow();
			android.os.Process.killProcess(android.os.Process.myPid());
		} else {
			activeManager();
		}
	}

	private void Bind(View v) {
		
		if (componentName != null) {
			policyManager.removeActiveAdmin(componentName);
			activeManager();
		}
	}

	private void activeManager() {

		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "lock");
		startActivity(intent);
	}
}
