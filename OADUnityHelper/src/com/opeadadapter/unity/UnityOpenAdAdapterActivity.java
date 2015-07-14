
//  Copyright (c) 2015 Artem Babenko. All rights reserved.

package com.opeadadapter.unity;

import com.openadadapter.OpenAdAdapter;
import com.unity3d.player.UnityPlayerActivity;

import android.os.Bundle;
import android.util.Log;

public class UnityOpenAdAdapterActivity extends UnityPlayerActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.i("AndLib", "OnCreate");
		super.onCreate(savedInstanceState);
		OpenAdAdapter.onCreate(this);
	}

	@Override
	public void onStart() {
		super.onStart();
		OpenAdAdapter.onStart(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		OpenAdAdapter.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		OpenAdAdapter.onPause(this);
	}

	@Override
	public void onStop() {
		super.onStop();
		OpenAdAdapter.onStop(this);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		OpenAdAdapter.onDestroy(this);
	}

	@Override
	public void onBackPressed() {
		if (OpenAdAdapter.onBackPressed(this))
			return;
		super.onBackPressed();
	}
}
