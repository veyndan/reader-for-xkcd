package com.example.veyndan.readerforxkcd.activity;

import android.support.v7.widget.Toolbar;

import com.example.veyndan.readerforxkcd.DatabaseHelper;
import com.example.veyndan.readerforxkcd.OrmLiteBaseCompatActivity;
import com.example.veyndan.readerforxkcd.R;

public class BaseActivity extends OrmLiteBaseCompatActivity<DatabaseHelper> {
    private Toolbar toolbar;

    protected Toolbar getToolbar() {
        if (toolbar == null) {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            if (toolbar != null) {
                setSupportActionBar(toolbar);
            }
        }
        return toolbar;
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        getToolbar();
    }
}
