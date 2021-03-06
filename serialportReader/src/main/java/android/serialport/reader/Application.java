/*
 * Copyright 2009 Cedric Priscal
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 */

package android.serialport.reader;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.serialport.SerialPort;
import android.serialport.SerialPortFinder;
import android.serialport.reader.utils.CrashHandler;

import com.yanzhenjie.andserver.server.CoreService;

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;

public class Application extends android.app.Application {

    private static Context mApplicationContext;

    public static Context getGlobalContext() {
        return mApplicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;

        // AndServer run in the service.
        startService(new Intent(this, CoreService.class));

        // 注册crashHandler
        CrashHandler.getInstance().init();
    }

    public SerialPortFinder mSerialPortFinder = new SerialPortFinder();
    private SerialPort mSerialPort = null;

    public SerialPort getSerialPort()
        throws SecurityException, IOException, InvalidParameterException {
        if (mSerialPort == null) {
            /* Read serial port parameters */

            String packageName = getPackageName();
            SharedPreferences sp = getSharedPreferences(packageName + "_preferences", MODE_PRIVATE);
            //String path = sp.getString("DEVICE", "/dev/ttyS5");
            String path = "/dev/ttyS5";//这里写死
            //int baudrate = Integer.decode(sp.getString("BAUDRATE", "921600"));
            int baudrate = 921600;//这里写死

            MainActivity.filePath = sp.getString("filePath", "/datapack");
            MainActivity.screenshotPath = sp.getString("screenshotPath", "/datapackScreenShot");
            MainActivity.datapackNumToSaveInFile = Integer.parseInt(sp.getString("datapacksize", "500"));
            MainActivity.maxDisplayLength = Integer.parseInt(sp.getString("SYBX", "500"));

			/* Check parameters */
            if ((path.length() == 0) || (baudrate == -1)) {
                throw new InvalidParameterException();
            }

			/* Open the serial port */
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
        }
        return mSerialPort;
    }

    public void closeSerialPort() {
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
    }
}
