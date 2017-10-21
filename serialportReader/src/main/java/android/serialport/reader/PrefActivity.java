package android.serialport.reader;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.serialport.SerialPortFinder;
import android.serialport.reader.utils.DataConstants;
import android.view.View;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ning on 17/9/13.
 */

public class PrefActivity extends PreferenceActivity {

    private Application mApplication;
    private SerialPortFinder mSerialPortFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_settings_serialport);

        findViewById(R.id.titlebar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mApplication = (Application) getApplication();
        mSerialPortFinder = mApplication.mSerialPortFinder;

        addPreferencesFromResource(R.xml.main_preferences);

        //工作模式
        final ListPreference workmode = (ListPreference) findPreference("WORKMODE");
        workmode.setSummary(workmode.getEntry());
        workmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_workmode, content)));
                return true;
            }
        });

        //灵敏度
        final ListPreference sensitivity = (ListPreference) findPreference("SENSITIVITY");
        sensitivity.setSummary(sensitivity.getValue());
        sensitivity.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_sensitivity, content)));
                return true;
            }
        });

        //功率设置
        final ListPreference power = (ListPreference) findPreference("POWER");
        power.setSummary(power.getValue());
        power.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_power, content)));
                return true;
            }
        });

        //数字放大增益设置
        final ListPreference SZFDZY = (ListPreference) findPreference("SZFDZY");
        SZFDZY.setSummary(SZFDZY.getValue());
        SZFDZY.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szfdzy, content)));
                return true;
            }
        });

        //数字本振频率设置
        final EditTextPreference SZBZPL = (EditTextPreference) findPreference("SZBZPL");
        SZBZPL.setSummary(SZBZPL.getText());
        SZBZPL.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szbzpl, content)));
                return true;
            }
        });

        //SYBX
        final ListPreference SYBX = (ListPreference) findPreference("SYBX");
        SYBX.setSummary(SYBX.getValue());
        SYBX.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                return true;
            }
        });

        // Devices
        final ListPreference devices = (ListPreference) findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        devices.setEntries(entries);
        devices.setEntryValues(entryValues);
        devices.setSummary(devices.getValue());
        devices.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });


        // Baud rates
        final ListPreference baudrates =  (ListPreference) findPreference("BAUDRATE");
        baudrates.setSummary(baudrates.getValue());
        baudrates.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });

        //wifi
        final Preference wifiPref = findPreference("wifi");
        wifiPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                return false;
            }
        });

        //sd
        final Preference sdPref = findPreference("sd");
        sdPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //startActivity(new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS));
                startActivity(new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS));
                return false;
            }
        });

        // checksum
        final ListPreference checksum =  (ListPreference) findPreference("checksum");
        checksum.setSummary(checksum.getEntry());
        checksum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });
    }
}
