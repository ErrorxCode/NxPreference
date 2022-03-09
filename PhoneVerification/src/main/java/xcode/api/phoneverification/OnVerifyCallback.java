package xcode.api.phoneverification;

public interface OnVerifyCallback{
    void onSuccessful();
    void onFailed(VerificationException e);
}