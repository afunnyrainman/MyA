package com.example.user.mya.Receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.user.mya.Utils.Utils;

import cn.bmob.push.PushConstants;

/**
 * Created by user on 2016/11/24.
 */
public class MyPushMessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        if(intent.getAction().equals(PushConstants.ACTION_MESSAGE)){
            Log.e("dxq", "客户端收到推送内容："+intent.getStringExtra("msg"));
            Utils.showToast(context,"客户端收到推送内容："+intent.getStringExtra("msg"));
        }
    }

}