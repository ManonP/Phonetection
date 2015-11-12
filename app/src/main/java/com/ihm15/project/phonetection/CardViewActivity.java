package com.ihm15.project.phonetection;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import dialogs.CustomMessageDialog;

/**
 * Created by Manon on 17/10/2015.
 */
public class CardViewActivity extends AppCompatActivity {

    private static final String MAIN_FRAGMENT = "MAIN_FRAGMENT";
    public boolean withAlarm = false;
    public int mode = -1;

    public boolean isWithAlarm(){
        return withAlarm;
    }

    public int getMode(){
        return mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        Data.getInstance(this);

        withAlarm = intent.getBooleanExtra(Data.EXTRA_WITH_ALARM, false);
        mode = intent.getIntExtra(Data.EXTRA_MODE, -1);
        showsDetectionModeSelector();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Data.getInstance(this);

        mode = intent.getIntExtra(Data.EXTRA_MODE, -1);


        MainFragment mf = (MainFragment) getFragmentManager().findFragmentByTag(MAIN_FRAGMENT);
        if (mf != null){
            mf.alarmTrigger(mode);
        }

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

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean result;
        switch (event.getKeyCode()) {
            case KeyEvent.KEYCODE_VOLUME_DOWN :
                result = true;
                break;
            case KeyEvent.KEYCODE_VOLUME_UP :
                result = true;
                break;
            default:
                result = super.dispatchKeyEvent(event);
        }
        return result;
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
                .replace(android.R.id.content, new MainFragment(), MAIN_FRAGMENT)
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
