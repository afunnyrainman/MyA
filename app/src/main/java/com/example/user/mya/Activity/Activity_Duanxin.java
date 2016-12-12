package com.example.user.mya.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.EditText;

import com.example.user.mya.MyApli.MyAplication;
import com.example.user.mya.R;
import com.example.user.mya.Utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.bmob.sms.BmobSMS;
import cn.bmob.sms.exception.BmobException;
import cn.bmob.sms.listener.RequestSMSCodeListener;

/**
 * Created by user on 2016/11/24.
 */
public class Activity_Duanxin extends  BaseActivity {
    private EditText ed;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duanxin);
        ed= (EditText) findViewById(R.id.ed);

        BmobSMS.initialize(this, MyAplication.BMOBAPPLICTIONID);

        SimpleDateFormat format =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sendTime = format.format(new Date());
        BmobSMS.requestSMS(this, "18811104090", "审核通过后的短信内容",sendTime,new RequestSMSCodeListener() {

            @Override
            public void done(Integer smsId,BmobException ex) {
                // TODO Auto-generated method stub
                if(ex==null){//
                    Utils.showToast(Activity_Duanxin.this,"短信发送成功，短信id："+smsId);
                    Log.e("bmob","短信发送成功，短信id："+smsId);//用于查询本次短信发送详情
                }else{

                    Log.e("bmob","errorCode = "+ex.getErrorCode()+",errorMsg = "+ex.getLocalizedMessage());
                }
            }
        });
    }
 /*   class MySMSCodeListener implements SMSCodeListener {

        @Override
        public void onReceive(String content) {
            if(ed != null){
                ed.setText(content);
            }
        }

    }
    public void onClick(View v){

    }*/
}
