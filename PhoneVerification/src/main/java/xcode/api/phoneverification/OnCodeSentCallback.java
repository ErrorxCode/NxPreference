package xcode.api.phoneverification;

public interface OnCodeSentCallback {
    void onSuccessful(String OTP);

    void onFailed(VerificationException e);
}
