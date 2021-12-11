package com.easypreference;

public class ViewNotSavableException extends IllegalArgumentException{
    protected ViewNotSavableException(String id){
        super("The view " + id + " is not savable. See the list of savable views at documentation https://github.com/ErrorxCode/EasyPreference");
    }
}
