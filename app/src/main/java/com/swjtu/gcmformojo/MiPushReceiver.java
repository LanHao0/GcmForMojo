package com.swjtu.gcmformojo;

import android.content.Context;
import android.util.Log;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.swjtu.gcmformojo.MyApplication.MYTAG;
import static com.swjtu.gcmformojo.MyApplication.deviceMiToken;
import static com.swjtu.gcmformojo.MyApplication.mySettings;

/**
 * Created by HeiPi on 2017/3/14.
 */

public class MiPushReceiver extends PushMessageReceiver {

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {
        Log.v(MYTAG,
                "onReceiveRegisterResult is called. " + message.toString());
        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                deviceMiToken = cmdArg1;
                Log.v(MYTAG,
                        "小米推送token：" + deviceMiToken);
            } else {
                Log.v(MYTAG,
                        "小米推送注册失败!" );
            }
        } else {
            log = message.getReason();
        }

    }


    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {

        String remoteMessageOrign = message.toString();
     //   Log.d(MYTAG, remoteMessageOrign);

        try
        {
            JSONObject remoteMessage = new JSONObject(message.getContent());

            if(!remoteMessage.has("isAt")) remoteMessage.put("isAt","0");
            if(!remoteMessage.has("senderType")) remoteMessage.put("senderType","1");


            //SharedPreferences Settings =        context.getSharedPreferences(PREF, Context.MODE_PRIVATE);
            String tokenSender = mySettings.getString("push_type","GCM");
            if(tokenSender.equals("MiPush")) {
                Log.d(MYTAG, "小米推送："+remoteMessageOrign);
                MessageUtil.MessageUtilDo(context,remoteMessage.getString("msgId"),remoteMessage.getString("type"),remoteMessage.getString("senderType"),remoteMessage.getString("title"),remoteMessage.getString("message"),remoteMessage.getString("isAt"));

            }

        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

}

