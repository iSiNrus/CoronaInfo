package ru.quat63.coronainfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class BottomMenuView extends LinearLayout {

    private ImageButton ib_info;
    private ImageButton ib_home;
    private ImageButton ib_profile;
    private ImageButton ib_health;

    public BottomMenuView(Context context) {
        super(context);
        init(null, 0);
    }

    public BottomMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public BottomMenuView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.BottomMenuView, defStyle, 0);

        a.recycle();

        LayoutInflater inf = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inf.inflate(R.layout.bottom_menu, this, true);


        ib_info = findViewById(R.id.item_info);
        ib_health = findViewById(R.id.item_health);
        ib_home = findViewById(R.id.item_home);
        ib_profile = findViewById(R.id.item_profile);

        ib_profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                if(checkTransition(ProfileActivity.class))
                    getContext().startActivity(intent);
            }
        });

        ib_home.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HomeActivity.class);
                if (checkTransition(HomeActivity.class))
                    getContext().startActivity(intent);
            }
        });

        ib_health.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HealthActivity.class);
                if (checkTransition(HealthActivity.class))
                    getContext().startActivity(intent);
            }
        });

        ib_info.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InfoActivity.class);
                if (checkTransition(InfoActivity.class))
                    getContext().startActivity(intent);
            }
        });
    }

    private boolean checkTransition(Class activity)
    {
        return !getContext().getClass().equals(activity);
    }

}
