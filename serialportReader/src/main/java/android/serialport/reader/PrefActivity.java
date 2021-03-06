package android.serialport.reader;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.provider.Settings;
import android.serialport.SerialPortFinder;
import android.serialport.reader.utils.DataConstants;
import android.serialport.reader.utils.TimePickerDialog;
import android.serialport.reader.utils.Utils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.Calendar;

/**
 * Created by ning on 17/9/13.
 */

public class PrefActivity extends PreferenceActivity  implements TimePickerDialog.TimePickerDialogInterface  {

    private Application mApplication;
    private SerialPortFinder mSerialPortFinder;

    TimePickerDialog timePickerDialog;

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
        //workmode.setSummary(workmode.getEntry());
        if (MainActivity.mWorkMode == MainActivity.WORK_MODE_STANDBY) {
            workmode.setSummary(workmode.getEntries()[0]);
        } else if (MainActivity.mWorkMode == MainActivity.WORK_MODE_ONLY_RX2) {
            workmode.setSummary(workmode.getEntries()[1]);
        } else if (MainActivity.mWorkMode == MainActivity.WORK_MODE_ONLY_RX3) {
            workmode.setSummary(workmode.getEntries()[2]);
        } else if (MainActivity.mWorkMode == MainActivity.WORK_MODE_BOTH_RX2_RX3) {
            workmode.setSummary(workmode.getEntries()[3]);
        }
        workmode.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_workmode, content)));
                MainActivity.mWorkMode = content;
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
                MainActivity.mSensitivity = content;
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
                MainActivity.mPower = content;
                return true;
            }
        });

        //数字放大增益设置
        /*final ListPreference SZFDZY = (ListPreference) findPreference("SZFDZY");
        SZFDZY.setSummary(SZFDZY.getValue());
        SZFDZY.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szfdzy, content)));
                MainActivity.mSZFDZY = content;
                return true;
            }
        });*/

        //数字本振频率设置
        /*final EditTextPreference SZBZPL = (EditTextPreference) findPreference("SZBZPL");
        SZBZPL.setSummary(SZBZPL.getText());
        SZBZPL.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                byte content = (byte) Integer.parseInt((String) newValue);
                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szbzpl, content)));
                MainActivity.mSZBZPL = content;
                return true;
            }
        });*/

        //SYBX
        final ListPreference SYBX = (ListPreference) findPreference("SYBX");
        SYBX.setSummary(SYBX.getValue());
        SYBX.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                MainActivity.maxDisplayLength = Integer.parseInt((String) newValue);
                return true;
            }
        });

        /*final ListPreference devices = (ListPreference) findPreference("DEVICE");
        String[] entries = mSerialPortFinder.getAllDevices();
        String[] entryValues = mSerialPortFinder.getAllDevicesPath();
        devices.setEntries(entries);
        devices.setEntryValues(entryValues);
        if (devices.getValue().length() > 0)
            devices.setSummary(devices.getValue());
        else
            devices.setSummary("/dev/ttyS5");
        devices.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });*/

        /*final ListPreference baudrates =  (ListPreference) findPreference("BAUDRATE");
        if (baudrates.getValue().length() > 0)
            baudrates.setSummary(baudrates.getValue());
        else
            baudrates.setSummary("926100");
        baudrates.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });*/

        //二次谐波门限设置
        /*final EditTextPreference thbase2 = (EditTextPreference) findPreference("thbase2");
        thbase2.setSummary(MainActivity.TH_base2 + "");
        thbase2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int thbase = Integer.parseInt((String) newValue);
                if (thbase >= 15 && thbase <= 60) {
                    preference.setSummary((String) newValue);
                    MainActivity.TH_base2 = thbase;
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        //3次谐波门限设置
        /*final EditTextPreference thbase3 = (EditTextPreference) findPreference("thbase3");
        thbase3.setSummary(MainActivity.TH_base3 + "");
        thbase3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                int thbase = Integer.parseInt((String) newValue);
                if (thbase >= 30 && thbase <= 60) {
                    preference.setSummary((String) newValue);
                    MainActivity.TH_base3 = thbase;
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        //二次谐波标定增益设置
        /*final EditTextPreference gain2 = (EditTextPreference) findPreference("gain2");
        gain2.setSummary(gain2.getText() != null ? gain2.getText() : "1.00");
        gain2.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                float gain = Float.parseFloat((String) newValue);
                if (gain >= 0.5f && gain <= 1.0f) {
                    preference.setSummary((String) newValue);
                    MainActivity.Gain2 = gain;
                    return true;
                } else {
                    return false;
                }
            }
        });*/
        //3次谐波标定增益设置
        /*final EditTextPreference gain3 = (EditTextPreference) findPreference("gain3");
        gain3.setSummary(gain3.getText() != null ? gain3.getText() : "0.8");
        gain3.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                float gain = Float.parseFloat((String) newValue);
                if (gain >= 0.5f && gain <= 1.0f) {
                    preference.setSummary((String) newValue);
                    MainActivity.Gain3 = gain;
                    return true;
                } else {
                    return false;
                }
            }
        });*/

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
        /**final Preference sdPref = findPreference("sd");
        sdPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //startActivity(new Intent(Settings.ACTION_INTERNAL_STORAGE_SETTINGS));
                startActivity(new Intent(Settings.ACTION_MEMORY_CARD_SETTINGS));
                return false;
            }
        });*/

        //filePath
        final EditTextPreference filePath = (EditTextPreference) findPreference("filePath");
        filePath.setSummary(MainActivity.filePath);
        filePath.setText(MainActivity.filePath);
        filePath.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                MainActivity.filePath = (String) newValue;
                return true;
            }
        });

        //datapacksize
        final EditTextPreference datapacksize = (EditTextPreference) findPreference("datapacksize");
        datapacksize.setSummary(MainActivity.datapackNumToSaveInFile + "");
        datapacksize.setText(MainActivity.datapackNumToSaveInFile + "");
        datapacksize.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                MainActivity.datapackNumToSaveInFile = Integer.parseInt((String) newValue);
                return true;
            }
        });

        //screenshotPath
        final EditTextPreference screenshotPath = (EditTextPreference) findPreference("screenshotPath");
        screenshotPath.setSummary(MainActivity.screenshotPath);
        screenshotPath.setText(MainActivity.screenshotPath);
        screenshotPath.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                MainActivity.screenshotPath = (String) newValue;
                return true;
            }
        });

        //cleardata
        final Preference cleardata = findPreference("cleardata");
        cleardata.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                String dirName = Environment.getExternalStorageDirectory() + MainActivity.filePath;
                File dir = new File(dirName);
                if (dir.exists() && dir.isDirectory()) {
                    Utils.deleteFile(dir);
                }
                return true;
            }
        });


        // checksum
        /*final ListPreference checksum =  (ListPreference) findPreference("checksum");
        checksum.setSummary(checksum.getEntry());
        checksum.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary((String) newValue);
                return true;
            }
        });*/

        //restoreDefalutSettings
        final Preference restoreDefalutSettings = findPreference("restoreDefalutSettings");
        restoreDefalutSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                new AlertDialog.Builder(PrefActivity.this).setTitle("恢复出厂设置").setMessage("确定吗？").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getSharedPreferences(getPackageName() + "_preferences", MODE_PRIVATE);
                                MainActivity.mWorkMode = MainActivity.WORK_MODE_BOTH_RX2_RX3;
                                sp.edit().putString("WORKMODE", "3").apply();
                                MainActivity.mSensitivity = 0x05;
                                sp.edit().putString("SENSITIVITY", "5").apply();
                                MainActivity.mPower = 0x05;
                                sp.edit().putString("POWER", "5").apply();
                                MainActivity.mSZBZPL = 0x00;
                                sp.edit().putString("SZBZPL", "0").apply();
                                MainActivity.mSZFDZY = 0x00;
                                sp.edit().putString("SZFDZY", "0").apply();
                                MainActivity.filePath = "/datapack";
                                sp.edit().putString("filePath", "/datapack").apply();
                                MainActivity.screenshotPath = "/datapackScreenShot";
                                sp.edit().putString("screenshotPath", "/datapackScreenShot").apply();
                                MainActivity.datapackNumToSaveInFile = 500;
                                sp.edit().putString("datapacksize", "500").apply();
                                MainActivity.maxDisplayLength = 500;
                                sp.edit().putString("SYBX", "500").apply();
                                //sp.edit().putString("DEVICE", "/dev/ttyS5").apply();
                                //sp.edit().putString("BAUDRATE", "921600").apply();

                                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_workmode, MainActivity.mWorkMode)));
                                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_sensitivity, MainActivity.mSensitivity)));
                                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_power, MainActivity.mPower)));
                                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szbzpl, MainActivity.mSZBZPL)));
                                EventBus.getDefault().post(new MainActivity.sendDataEvent(DataConstants.getControlCommandBytes(DataConstants.command_send_szfdzy, MainActivity.mSZFDZY)));

                                finish();
                            }
                        }).setNegativeButton("取消", null).show();
                return true;
            }
        });

        //advancedSettings
        final Preference advancedSettings = findPreference("advancedSettings");
        advancedSettings.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                final EditText et = new EditText(PrefActivity.this);

                new AlertDialog.Builder(PrefActivity.this).setTitle("请输入密码")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setView(et)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String input = et.getText().toString();
                                if (!input.equals("cetc50wt")) {
                                    Toast.makeText(getApplicationContext(), "密码错误！", Toast.LENGTH_LONG).show();
                                } else {
                                    Intent intent = new Intent();
                                    intent.setClass(PrefActivity.this, Pref2Activity.class);
                                    startActivity(intent);
                                }
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });


        //datetime
        final Preference datetime = findPreference("datetime");
        datetime.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
//                timePickerDialog = new TimePickerDialog(PrefActivity.this);
//                timePickerDialog.showDateAndTimePickerDialog();

                startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));
                return false;
            }
        });
    }

    @Override
    public void positiveListener() {
//        Calendar c = Calendar.getInstance();
//        int hour = timePickerDialog.getHour();
//        int minute = timePickerDialog.getMinute();
//        c.set(timePickerDialog.getYear(), timePickerDialog.getMonth(), timePickerDialog.getDay(),hour, minute);
//
//        long when = c.getTimeInMillis();
//
//        if(when / 1000 < Integer.MAX_VALUE){
//            ((AlarmManager)PrefActivity.this.getSystemService(Context.ALARM_SERVICE)).setTime(when);
//        }

    }

    @Override
    public void negativeListener() {

    }
}
