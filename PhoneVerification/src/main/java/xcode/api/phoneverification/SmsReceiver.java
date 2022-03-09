package xcode.api.phoneverification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {

    @SuppressLint("WrongConstant")
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("SMS_SENT".equals(intent.getAction())){
            int code = getResultCode();
            if (code == Activity.RESULT_OK)
                PhoneVerification.requestCallback.onSuccessful(PhoneVerification.verificationCode);
            else
                PhoneVerification.requestCallback.onFailed(new VerificationException("Verification sms not sent. Result code : " + code + ". This result code is field of SmsManager class"));

            context.unregisterReceiver(this);
        } else {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            SmsMessage message = SmsMessage.createFromPdu((byte[]) objects[0]);
            String text = message.getMessageBody();
            if (text.contains(PhoneVerification.verificationCode)){
                PhoneVerification.verifyCallback.onSuccessful();
                context.unregisterReceiver(this);
            }
        }
    }
}
