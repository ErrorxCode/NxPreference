package com.easypreference;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This library helps you to save your activity or fragment state persistently. They will be same on app restart as they were before,
 * like if you have checked some checkboxes or switches or any other <code>CompoundButton</code> then, this class will save their state & you can restore them on <code>onCreate()</code>
 * of your activity or fragment. The components of which the state are saved are referred as <code>Preference</code>. This library uses <code>SharedPreference</code> to store data.
 *
 * @author Rahil khan
 * @version 1.0
 * @apiNote This library will only work on CompoundButtons and RadioGroup. See <a href="https://github.com/ErrorxCode/EasyPreference">Github page</a> for more info.
 */
public class EasyPreference {

    private static final ArrayList<View> list = new ArrayList<>();
    private static String PREFERENCE;

    /**
     * Call this method when you want to save activity state. This will save the component state as it was when this method is called.
     * This will not update the saved state which were modified after calling this method.
     *
     * You can restore them using {@link EasyPreference#restoreActivityState(Activity)}.
     * The best way to use this is to call this method is <code>onStop()</code> of your activity to save state automatically.
     * @param activity The calling activity of which you want to save the state.
     */
    public static void saveActivityState(@NonNull Activity activity) {
        PREFERENCE = "easyPref:" + activity.getPackageName();
        SharedPreferences.Editor editor = activity.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE).edit();
        for (View view : getAllChild(activity.findViewById(android.R.id.content))){
            if (view instanceof RadioGroup)
                editor.putInt(getId(view), ((RadioGroup) view).getCheckedRadioButtonId());
            else if (view instanceof CompoundButton)
                editor.putBoolean(getId(view),((CompoundButton) view).isChecked());
            else if (view instanceof EditText)
                editor.putString(getId(view),((EditText) view).getText().toString());
        }
        editor.apply();
    }


    /**
     * Call this method when you want to restore activity state. This will restore the component states which were saved using {@link EasyPreference#saveActivityState(Activity)}.
     * This should be called in <code>onCreate()</code> of your activity to restore state automatically.
     * @param activity The calling activity of which you want to save the state.
     */
    public static void restoreActivityState(@NonNull Activity activity) {
        PREFERENCE = "easyPref:" + activity.getPackageName();
        SharedPreferences preferences = activity.getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        for (View view : getAllChild(activity.findViewById(android.R.id.content))){
            if (view instanceof RadioGroup)
                ((RadioGroup) view).check(preferences.getInt(getId(view),((RadioGroup) view).getCheckedRadioButtonId()));
            else if (view instanceof CompoundButton)
                ((CompoundButton) view).setChecked(preferences.getBoolean(getId(view),((CompoundButton) view).isChecked()));
            else if (view instanceof EditText)
                ((EditText) view).setText(preferences.getString(getId(view),null));
        }
    }


    /**
     * Call this method when you want to save fragment state. This will save the component state as it was when this method is called.
     * This will not update the saved state which were modified after calling this method.
     * You can restore them using {@link EasyPreference#restoreFragmentState(View)}.
     * The best way to use this is to call this method is <code>onStop()</code> of your fragment to save state automatically.
     * @param layout The layout view of the fragment i.e returned by {@code onCreateView()}.
     */
    public static void saveFragmentState(@NonNull View layout) {
        PREFERENCE = "easyPref:" + layout.getContext().getPackageName();
        SharedPreferences.Editor editor = layout.getContext().getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE).edit();
        for (View view : getAllChild((ViewGroup) layout.getRootView())){
            if (view instanceof RadioGroup)
                editor.putInt(getId(view), ((RadioGroup) view).getCheckedRadioButtonId());
            else if (view instanceof CompoundButton)
                editor.putBoolean(getId(view),((CompoundButton) view).isChecked());
            else if (view instanceof EditText)
                editor.putString(getId(view),((EditText) view).getText().toString());
        }
        editor.apply();
    }


    /**
     * Call this method when you want to restore fragment state. This will restore the component states which were saved using {@link EasyPreference#saveFragmentState(View)}.
     * This should be called in <code>onCreateView()</code> of your fragment to restore state automatically.
     * @param layout The layout view of the fragment i.e returned by {@code onCreateView()}.
     */
    public static void restoreFragmentState(@NonNull View layout) {
        PREFERENCE = "easyPref:" + layout.getContext().getPackageName();
        SharedPreferences preferences = layout.getContext().getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        for (View view : getAllChild((ViewGroup) layout.getRootView())){
            if (view instanceof RadioGroup)
                ((RadioGroup) view).check(preferences.getInt(getId(view),((RadioGroup) view).getCheckedRadioButtonId()));
            else if (view instanceof CompoundButton)
                ((CompoundButton) view).setChecked(preferences.getBoolean(getId(view),((CompoundButton) view).isChecked()));
            else if (view instanceof EditText)
                ((EditText) view).setText(preferences.getString(getId(view),null));
        }
    }


    /**
     * Returns the SharedPreference name used by this library to save states. {@code null} if no state is saved or restored yet.
     * @return The SharedPreference name. This may be {@code null}.
     */
    public static @Nullable String getSharedPreferenceName(){
        return PREFERENCE;
    }


    private static ArrayList<View> getAllChild(ViewGroup layout){
        int child = layout.getChildCount();
        for(int i = 0; i < child; i++){
            View childView = layout.getChildAt(i);
            if (childView instanceof ViewGroup && !(childView instanceof RadioGroup))
                getAllChild((ViewGroup) childView);
            else if (childView instanceof CompoundButton || childView instanceof RadioGroup || childView instanceof EditText){
                if (!"exclude".equals(childView.getTag())){
                    if (childView.getId() == -1)
                        throw new NoPreferenceIdException(childView);
                    else
                        list.add(childView);
                }
            }
        }
        return list;
    }


    /**
     * Saves the state of particular view. This view must be either RadioGroup or CompoundButton or Edittext.
     * @param view The view that is to be saved.
     * @throws ViewNotSavableException if view is not savable.
     */
    public static void saveViewState(View view){
        SharedPreferences.Editor editor = view.getContext().getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE).edit();
        if (view instanceof RadioGroup)
            editor.putInt(getId(view), ((RadioGroup) view).getCheckedRadioButtonId());
        else if (view instanceof CompoundButton)
            editor.putBoolean(getId(view),((CompoundButton) view).isChecked());
        else if (view instanceof EditText)
            editor.putString(getId(view),((EditText) view).getText().toString());
        else
            throw new ViewNotSavableException(getId(view));

        editor.apply();
    }


    /**
     * Restore the state of view that was saved using {@link EasyPreference#saveViewState(View)}. This view will be automatically restored when restoring activity or fragment.
     * @param view the view which is to be restored.
     */
    public static void restoreViewState(View view){
        SharedPreferences preferences = view.getContext().getSharedPreferences(PREFERENCE,Context.MODE_PRIVATE);
        if (view instanceof RadioGroup)
            ((RadioGroup) view).check(preferences.getInt(getId(view),((RadioGroup) view).getCheckedRadioButtonId()));
        else if (view instanceof CompoundButton)
            ((CompoundButton) view).setChecked(preferences.getBoolean(getId(view),((CompoundButton) view).isChecked()));
        else if (view instanceof EditText)
            ((EditText) view).setText(preferences.getString(getId(view),null));
    }


    private static String getId(View view){
        return view.getResources().getResourceName(view.getId());
    }
}
