package com.xcoder.nxpref;

import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

/**
 * Exception thrown when saving state of view which has no id.
 */
public class NoPreferenceIdException extends NullPointerException {

    public NoPreferenceIdException(View view) {
        super("No Id associated for the view. Every preference needs an id to save its state. Check if any preference do not have its id defined.\n" +
                "View text : " + (view instanceof TextView ? ((TextView) view).getText() : "This view do not contain any text") + "\n" +
                "View type : " + (view instanceof Switch ? "Switch" : view instanceof CheckBox ? "Checkbox" : view instanceof RadioGroup ? "RadioGroup" : "CompoundButton"));
    }
}
