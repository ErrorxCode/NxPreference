package xcode.api.phoneverification;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.SubscriptionManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * This is an easy-2-use API for implementing OTP verification in your app. You don't need to write boilerplate code for requesting or verifying the code.
 * Just need to call respective method with 1 or 3 arguments and rest, the library will manage. The main + point of this API is that it does not require internet to run, instead
 * it requires sim subscription to send sms. Only the - point is that, it will add 2 more permission to your manifest :( . One for sure is {@link Manifest.permission#SEND_SMS}
 * and another {@link Manifest.permission#RECEIVE_SMS} if you use auto-verification.
 *
 * @author Rahil khan
 * @version 3.0
 * @apiNote The use of this API is based on one assumption, that user at least have one sim with subscription to send or receive SMS.
 */
public class PhoneVerification {
    protected static String verificationCode;
    private static boolean isExpired;
    protected static OnCodeSentCallback requestCallback;
    protected static OnVerifyCallback verifyCallback;

    /**
     * Request verification code. The verification code is sent to the provided number, if sent successfully then <code>onSuccessful()</code> method is called.
     * If user don't have subscription to send sms or if sms is not sent for any reason then, {@code onFailed(VerificationException e)} will be called.
     * Here, explicit means that this will ask the user to select sim for sending sms in case there is no default sim set.
     * Verification code gets EXPIRED after the <code>5 min</code>. This use user's mobile number for requesting verification code hence, carrier charge may apply.
     * Moreover it requires {@link android.Manifest.permission#SEND_SMS} permission.
     *
     * @param context The context of the activity
     * @param callback The callback that is called when verification request is been sent
     * @param number  Receivers Address for the verification code
     */
    @RequiresPermission(Manifest.permission.SEND_SMS)
    public static void requestVerificationExplicit(@NonNull Context context, @NonNull String number,@NonNull OnCodeSentCallback callback){
        verificationCode = generateVerificationCode();
        PhoneVerification.requestCallback = callback;
        SmsManager smsManager = SmsManager.getDefault();
        PendingIntent intent = PendingIntent.getBroadcast(context,0,new Intent("SMS_SENT"),PendingIntent.FLAG_ONE_SHOT);
        smsManager.sendTextMessage(number, null,"Your verification cøde is : " + verificationCode + ". This is only valid for 5 minutes", intent, null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isExpired = true;
            }
        },5*60*1000);
        
    }
    /**
     * Same as {@link PhoneVerification#requestVerificationExplicit(Context, String, OnCodeSentCallback)} but with guarantee that, this will never ask the user to select sim. Here implicit means that this will use 1st slot sim by default for
     * requesting verification code.
     *
     * @param context The context of the activity
     * @param callback The callback that is called when verification request is been sent
     * @param number  Receivers Address for the verification code
     */
    @RequiresPermission(allOf = {Manifest.permission.SEND_SMS,Manifest.permission.READ_PHONE_STATE})
    public static void requestVerificationImplicit(@NonNull Context context, @NonNull String number,@NonNull OnCodeSentCallback callback){
        verificationCode = generateVerificationCode();
        PhoneVerification.requestCallback = callback;
        SubscriptionManager manager = context.getSystemService(SubscriptionManager.class);
        int id = manager.getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId();
        SmsManager smsManager = SmsManager.getSmsManagerForSubscriptionId(id);
        PendingIntent intent = PendingIntent.getBroadcast(context,0,new Intent("SMS_SENT"),PendingIntent.FLAG_UPDATE_CURRENT);
        context.registerReceiver(new SmsReceiver(),new IntentFilter("SMS_SENT"));
        smsManager.sendTextMessage(number, null,"Your verification cøde for " + context.getApplicationInfo().loadLabel(context.getPackageManager()).toString() + " is : " + verificationCode + ". This is only valid for 5 minutes", intent, null);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                isExpired = true;
            }
        },5*60*1000);
    }

    /**
     * Verify's the verification code. If user provided code is matching with verification code then <code>onSuccessful()</code> is called, Else {@code onFailed(VerificationException)} is called. The verification code which is sent to the provided mobile number
     * gets EXPIRED after <code>5 min</code> of sending it & need to be requested again.
     *
     * @param userCode  The <code>Code</code> Entered by the user.
     * @param callback  The callback that indicate success or failure of the process.
     */
    public static void verifyCode(@NonNull String userCode, @NonNull OnVerifyCallback callback){
        if (verificationCode == null) {
            throw new VerificationException("Attempt to verify before requesting verification code. " +
                    "If you have requested verification code then there can only be 2 reason\n" +
                    "1. The context which was passed when requesting verification code is killed or the activity get killed after requesting verification code.\n" +
                    "2. The requestVerification process did not succeed\n" +
                    "If none of the reason is applicable, then please create a issue on github");
        } else {
            if (isExpired)
                throw new VerificationException("Verification code has been expired. Code get expired after 5 min of sending.");
            if (userCode.equals(verificationCode)) {
                callback.onSuccessful();
            } else {
                callback.onFailed(new VerificationException("verification code is incorrect."));
            }
        }
    }

    /**
     * Same as {@link PhoneVerification#verifyCode(String, OnVerifyCallback)} where userCode is automatically taken by the system.
     * This will auto-verify the code as soon as it is received on the device.
     * This method needs {@link Manifest.permission#RECEIVE_SMS} to read the verification code automatically
     * @param context The context of the activity
     * @param callback The callback that indicate success or failure of the process.
     */
    @RequiresPermission(android.Manifest.permission.RECEIVE_SMS)
    public static void startAutoVerification(@NonNull Context context,@NonNull OnVerifyCallback callback){
        PhoneVerification.verifyCallback = callback;
        context.registerReceiver(new SmsReceiver(),new IntentFilter("android.provider.Telephony.SMS_RECEIVED"));
    }


    protected static String generateVerificationCode(){
        String numbers = "0123456789";
        Random random = new Random();

        char[] verificationCode = new char[6];
        for (int i = 0; i < 6; i++) {
            verificationCode[i] = numbers.charAt(random.nextInt(numbers.length()));
        }
        return new String(verificationCode);
    }
}