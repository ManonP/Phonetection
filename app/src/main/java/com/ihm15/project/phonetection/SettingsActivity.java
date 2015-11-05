package com.ihm15.project.phonetection;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import dialogs.CustomMessageDialog;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showsSettings();
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_info:
                actionInfoClicked();
                break;
            case R.id.action_mode_selector:
                actionDetectionModeSelectorClicked();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    public void actionInfoClicked(){
        showInfoDialog();
    }

    public void actionDetectionModeSelectorClicked(){
        showCardViewActivity();
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    private void showsSettings(){
        setContentView(R.layout.activity_settings);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    private void showCardViewActivity(){
        onBackPressed();
    }

    private void showInfoDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_info_black_36dp, getString(R.string.info_dialog),
                getString(R.string.info_dialog_message), getString(R.string.ok_button), null, null);
        cmd.show(getSupportFragmentManager(), "info");
    }
}
