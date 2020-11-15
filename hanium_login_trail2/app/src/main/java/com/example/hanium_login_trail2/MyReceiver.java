package com.example.hanium_login_trail2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean isvalue = intent.getBooleanExtra(LocationManager.KEY_PROXIMITY_ENTERING, false);
        if(isvalue){
            Toast.makeText(context, "목표 지점 내에 있습니다.", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(context, "목표 지점에서 벗어났습니다.", Toast.LENGTH_LONG).show();
        }
    }
}
// ProximityAlert를 통해서 수신한 브로드캐스트메세지를 포함하며
//LocationManager.KEY_RPOXIMITY_ENTERING을 통해 전달받을 수 있으며 이 값이 true시 근접, false면 벗어났다는 의미