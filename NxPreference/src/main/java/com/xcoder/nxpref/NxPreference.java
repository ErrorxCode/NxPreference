package com.xcoder.nxpref;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * This library helps you to save your activity or fragment state persistently. They will be same on app restart as they were before,
 * like if you have checked some checkboxes or switches or any other <code>CompoundButton</code> then, this class will save their state & you can restore them on <code>onCreate()</code>
 * of your activity or fragment. The components of which the state are saved are referred as <code>Preference</code>. This library uses <code>SharedPreference</code> to store data.
 *
 * @author Rahil khan
 * @version 1.5
 * @apiNote This library will only work on CompoundButtons and RadioGroup. See <a href="https://github.com/ErrorxCode/EasyPreference">Github page</a> for more info.
 */
public class NxPreference {
    private static final ArrayList<View> list = new ArrayList<>();

    /**
     * Call this method when you want to save activity state. This will save the component state as it was when this method is called.
     * This will not update the saved state which were modified after calling this method.
     *
     * You can restore them using {@link NxPreference#restoreActivityState(Activity)}.
     * The best way to use this is to call this method is <code>onStop()</code> of your activity to save state automatically.
     * @param activity The calling activity of which you want to save the state.
     */
    public static void saveActivityState(@NonNull Activity activity) {
        for (View view : getAllChild(activity.findViewById(android.R.id.content)))
            saveView(view);
    }


    /**
     * Call this method when you want to restore activity state. This will restore the component states which were saved using {@link NxPreference#saveActivityState(Activity)}.
     * This should be called in <code>onCreate()</code> of your activity to restore state automatically.
     * @param activity The calling activity of which you want to save the state.
     */
    public static void restoreActivityState(@NonNull Activity activity) {
        for (View view : getAllChild(activity.findViewById(android.R.id.content)))
            restoreView(view);
    }


    /**
     * Call this method when you want to save fragment state. This will save the component state as it was when this method is called.
     * This will not update the saved state which were modified after calling this method.
     * You can restore them using {@link NxPreference#restoreFragmentState(View)}.
     * The best way to use this is to call this method is <code>onStop()</code> of your fragment to save state automatically.
     * @param layout The layout view of the fragment i.e returned by {@code onCreateView()}.
     */
    public static void saveFragmentState(@NonNull View layout) {
        for (View view : getAllChild((ViewGroup) layout.getRootView()))
            saveView(view);
    }


    /**
     * Call this method when you want to restore fragment state. This will restore the component states which were saved using {@link NxPreference#saveFragmentState(View)}.
     * This should be called in <code>onCreateView()</code> of your fragment to restore state automatically.
     * @param layout The layout view of the fragment i.e returned by {@code onCreateView()}.
     */
    public static void restoreFragmentState(@NonNull View layout) {
        for (View view : getAllChild((ViewGroup) layout.getRootView())){
            restoreView(view);
        }
    }


    /**
     * Automatically saves state when activity reach ending lifecycle event and restores them when
     * activity starts.
     * @param activity {@link AppCompatActivity} The activity of which to save state.
     */
    public static void autoSaveState(@NonNull AppCompatActivity activity){
        saveActivityState(activity);
        activity.getLifecycle().addObserver((LifecycleEventObserver) (source, event) -> {
            if (event.equals(Lifecycle.Event.ON_STOP))
                restoreActivityState(activity);
        });
    }


    /**
     * Saves the state of particular view. This view must be either RadioGroup or CompoundButton or Edittext.
     * @param view The view that is to be saved.
     * @throws ViewNotSavableException if view is not savable.
     */
    public static void saveView(View view){
        SharedPreferences.Editor editor = view.getContext().getSharedPreferences("easyPref:" + view.getContext().getPackageName(),Context.MODE_PRIVATE).edit();
        if (view instanceof RadioGroup v)
            editor.putInt(getId(view),v.getCheckedRadioButtonId());
        else if (view instanceof CompoundButton v)
            editor.putBoolean(getId(view),v.isChecked());
        else if (view instanceof EditText v)
            editor.putString(getId(view),v.getText().toString());
        else if (view instanceof Spinner v)
            editor.putInt(getId(view),v.getSelectedItemPosition());
        else if (view instanceof ListView list && list.getTag().equals("savable")){
            ArrayAdapter<String> adapter = (ArrayAdapter) list.getAdapter();
            List<String> items = new ArrayList<>();
            for (int i = 0; i < adapter.getCount(); i++) {
                items.add(adapter.getItem(i));
            }
            editor.putStringSet(getId(view),new HashSet<>(items));
        } else
            throw new ViewNotSavableException(getId(view));

        editor.apply();
    }


    /**
     * Restore the state of view that was saved using {@link NxPreference#saveView(View)}. This view will be automatically restored when restoring activity or fragment.
     * @param view the view which is to be restored.
     * @throws IllegalStateException If there is a savable listview and this method is called before setting adapter to it.
     */
    public static void restoreView(View view){
        SharedPreferences preferences = view.getContext().getSharedPreferences("easyPref:" + view.getContext().getPackageName(),Context.MODE_PRIVATE);
        if (view instanceof RadioGroup v)
            v.check(preferences.getInt(getId(view),v.getCheckedRadioButtonId()));
        else if (view instanceof CompoundButton v)
            v.setChecked(preferences.getBoolean(getId(view),v.isChecked()));
        else if (view instanceof EditText v)
            v.setText(preferences.getString(getId(view),null));
        else if (view instanceof Spinner v)
            v.setSelection(preferences.getInt(getId(view),0));
        else if (view instanceof ListView list && "savable".equals(list.getTag())){
            ArrayAdapter<String> adapter = (ArrayAdapter) list.getAdapter();
            if (adapter == null)
                throw new IllegalStateException("Found a savable listview, but this method is called before setting adapter to it." +
                        " Please call this method either in last of onCreate() or after setting adapter to the listview");
            else {
                adapter.clear();
                adapter.addAll(preferences.getStringSet(getId(view),new HashSet<>()));
            }
        } else
            throw new ViewNotSavableException(getId(view));
    }



    private static ArrayList<View> getAllChild(ViewGroup layout){
        int child = layout.getChildCount();
        for(int i = 0; i < child; i++){
            View childView = layout.getChildAt(i);
            if (childView instanceof ViewGroup && !(childView instanceof RadioGroup || childView instanceof Spinner || childView instanceof ListView))
                getAllChild((ViewGroup) childView);
            else if (childView instanceof CompoundButton || childView instanceof RadioGroup || childView instanceof EditText || childView instanceof Spinner || childView instanceof ListView){
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

    private static String getId(View view){
        return view.getResources().getResourceName(view.getId());
    }
}
