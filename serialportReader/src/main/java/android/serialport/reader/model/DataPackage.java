package android.serialport.reader.model;

import android.serialport.reader.MainActivity;
import android.serialport.reader.utils.DataConstants;
import android.serialport.reader.utils.Utils;

/**
 * Created by ning on 17/9/6.
 */

public class DataPackage {

    public static final int OFFSET = 0;

    public long timestamp;

    public byte[] dataBytes = new byte[DataConstants.DATA_FRAME_LENGTH];

    public static final int DATA_START = 4;

    public DataPackage(long timestamp) {
        this.timestamp = timestamp;
    }

    public DataPackage(long timestamp, byte[] dataBytes) {
        this.timestamp = timestamp;
        this.dataBytes = dataBytes;
    }

    public boolean isCheckSumOK() {
        int sum = 0;
        for (int i = 1; i < dataBytes.length - 1; i++) {
            sum += dataBytes[i];
        }
        return sum == 0;
    }

    public byte[] getSaveDataBytes() {
        //// TODO: 2017/11/2  16-19   532~573 数据没处理 还有校验
        byte[] saveDatabytes = new byte[DataConstants.DATA_FRAME_SAVE_LENGTH];
        System.arraycopy(dataBytes, 0, saveDatabytes, 0, DataConstants.DATA_FRAME_LENGTH);
        saveDatabytes[2] = (byte)0x3A;
        saveDatabytes[3] = (byte)0x02;
        return saveDatabytes;
    }


    /**
     * 周期序号
     */
    public int getCycle() {
        return Utils.byteArrayToInt(dataBytes, 0);
    }

    /**
     * 谐波类型: B1：[7] 0-3次谐波,1-2次谐波
     */
    public int getWaveType() {
        byte b = dataBytes[4 + DATA_START];
        int shift = (b >> 7) & 0x01;
        return shift;
    }

    /**
     * 射频状态 :  [6]0-射频故障，1-射频正常
     */
    public int getWaveStatus() {
        byte b = dataBytes[4 + DATA_START];
        int shift = (b >> 6) & 0x01;
        return shift;
    }

    /**
     * 当前电量
     */
    public int getBatteryStatus() {
        return Utils.getUnsignedByte((byte)(dataBytes[4 + DATA_START] & 0x0f));
    }


    /**
     * 灵敏度设置值
     */
    public int getSettingSensitivity() {
        return Utils.getUnsignedByte(dataBytes[5 + DATA_START]);
    }

    /**
     * 工作模式设置值
     */
    public int getSettingWorkMode() {
        return Utils.getUnsignedByte(dataBytes[6 + DATA_START]);
    }

    /**
     * 功率设置值
     */
    public int getSettingPower() {
        return Utils.getUnsignedByte(dataBytes[7 + DATA_START]);
    }

    /**
     * 数字本振频率设置值
     */
    public int getSettingDigitalFreq() {
        return Utils.getUnsignedByte(dataBytes[8 + DATA_START]);
    }

    /**
     * 数字放大增益设置值
     */
    public int getSettingDigitalGain() {
        return Utils.getUnsignedByte(dataBytes[9 + DATA_START]);
    }


    /**
     * 谐波功率值
     */
    public int getWavePower() {
        int int1 = Utils.byteArrayToInt(dataBytes, 12 + DATA_START);
        return (int) (10 * Math.log10(int1) - OFFSET);
    }

    public int getWavePowerRegulated() {
        //int power = getWavePower();
        //int power = getWaveType() == 0 ? MainActivity.power_base3_reshape : MainActivity.power_base2_reshape;
        int power = getWaveType() == 0 ? (int)MainActivity.power_filt_base3_cur : (int)MainActivity.power_filt_base2_cur;
        int thBase = getWaveType() == 0 ? MainActivity.TH_base3 : MainActivity.TH_base2;
        float gain = getWaveType() == 0 ? MainActivity.Gain3 : MainActivity.Gain2;
        power = (int) (4 * (power - thBase) * gain + 10);
        if (power < 5)
            power = 5;
        else if (power > 100)
            power = 100;
        power = 5 * (int) ((power + 2.5f) / 5);
        return power;
    }

    /**
     * 谐波频谱
     */
    public double[] getHarmonicSpectrum() {
        double[] spectrum = new double[128];

        int mainOffset = 16 + DATA_START;

        for (int i = 0; i < 128; i++) {
            short data1 = Utils.byteArrayToShort(dataBytes, mainOffset + i * 4);
            short data2 = Utils.byteArrayToShort(dataBytes, mainOffset + i * 4 + 2);

            if (data1 * data1 + data2 * data2 == 0)
                spectrum[i] = 0;
            else
                spectrum[i] = 10 * Math.log10(data1 * data1 + data2 * data2) - OFFSET;
        }

        return spectrum;
    }


}
