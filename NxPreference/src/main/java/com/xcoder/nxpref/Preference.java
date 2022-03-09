package com.xcoder.nxpref;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;

/**
 * A Preference implementation in view form which is similar to PreferenceScreen's preference.
 */
public class Preference extends FrameLayout {

    Context context;
    TextView title;
    TextView summary;
    RelativeLayout layout;
    String titleText;
    String summaryText;
    View divider;

    public Preference(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public Preference(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.Preference);
        titleText = attributes.getString(R.styleable.Preference_title);
        summaryText = attributes.getString(R.styleable.Preference_summery);
        attributes.recycle();
        init();
    }

    public Preference(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    public Preference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init();
    }

    private void init() {
        View view = inflate(context,R.layout.preference,this);
        title = view.findViewById(R.id.title);
        summary = view.findViewById(R.id.summary);
        divider = view.findViewById(R.id.divider);
        layout = view.findViewById(R.id.root);

        if (summaryText == null){
            summary.setVisibility(GONE);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) title.getLayoutParams();
            params.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            title.setLayoutParams(params);
        }
        title.setText(titleText);
        summary.setText(summaryText);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewGroup group = (ViewGroup) getParent();
        View before = group.getChildAt(group.indexOfChild(this) - 1);

        if (!(before instanceof Preference))
            divider.setVisibility(GONE);

    }

    public void setTitle(String title){
        this.title.setText(title);
    }

    public void setSummary(String summary){
        this.summary.setText(summary);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        layout.setOnClickListener(l);
    }
}
