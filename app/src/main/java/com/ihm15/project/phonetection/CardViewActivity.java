package com.ihm15.project.phonetection;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import dialogs.CustomMessageDialog;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showsDetectionModeSelector();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        showDetectionModeSelectorMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()){
            case R.id.action_info:
                actionInfoClicked();
                break;
            case R.id.action_settings:
                actionSettingsClicked();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //SEEHEIM-DIALOGUE//////////////////////////////////////////////////////////////////////////////

    public void actionInfoClicked(){
        showInfoDialog();
    }

    public void actionSettingsClicked(){
        showSettingsActivity();
    }

    //SEEHEIM-PRESENTATION//////////////////////////////////////////////////////////////////////////
    private void showsDetectionModeSelector(){
        setContentView(R.layout.activity_main);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new MainFragment())
                .commit();
    }

    private void showSettingsActivity(){
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showInfoDialog(){
        CustomMessageDialog cmd = new CustomMessageDialog(R.drawable.ic_info_black_36dp, getString(R.string.info_dialog),
                getString(R.string.info_dialog_message), getString(R.string.ok_button), null, null);
        cmd.show(getSupportFragmentManager(), "info");
    }

    private void showDetectionModeSelectorMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
    }

}
