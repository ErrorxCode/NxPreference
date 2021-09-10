package com.easypreference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Header extends LinearLayout {

    Context context;
    TextView heading;
    View divider;

    public Header(@NonNull Context context) {
        super(context);
        throw new UnsupportedOperationException("This view is not intended be added dynamically. Declare this view in XML.");
    }

    public Header(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(attrs);
    }

    public Header(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
    }

    public Header(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        this.context = context;
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View view = inflate(context, R.layout.header,this);
        heading = view.findViewById(R.id.heading);
        divider = view.findViewById(R.id.divider);

        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HeaderView);
        heading.setText(attributes.getString(R.styleable.HeaderView_heading));
        heading.setTextColor(attributes.getColor(R.styleable.HeaderView_headingColor,heading.getCurrentTextColor()));
        divider.setVisibility(attributes.getBoolean(R.styleable.HeaderView_showDivider,true) ? VISIBLE : INVISIBLE);
        attributes.recycle();
    }
}
