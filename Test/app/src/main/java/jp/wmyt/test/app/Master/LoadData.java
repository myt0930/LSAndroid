package jp.wmyt.test.app.Master;

import android.util.Log;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by miyata on 2014/05/06.
 */
public class LoadData {
    private DataInputStream _inData;
    private int _readOffset;
    private int _version;

    public LoadData(FileInputStream in){
        _inData = new DataInputStream(in);
        _readOffset = 0;
        _version = getInt32();
    }

    public int getInt16(){
        byte[] byteData = new byte[2];
        try{
            _inData.read(byteData, 0, 2);
            ByteBuffer buffer = ByteBuffer.wrap(byteData);
            buffer.order(ByteOrder.BIG_ENDIAN);

            int number = buffer.getShort();
            return number;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("",e.getMessage());
            return 0;
        }
    }

    public int getInt32(){
        byte[] byteData = new byte[4];
        try{
            _inData.read(byteData, 0, 4);
            ByteBuffer buffer = ByteBuffer.wrap(byteData);
            buffer.order(ByteOrder.BIG_ENDIAN);

            int number = buffer.getInt();
            return number;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("",e.getMessage());
            return 0;
        }
    }

    public String getString16(){
        byte[] byteData = new byte[2];
        try{
            _inData.read(byteData, 0, 2);
            ByteBuffer buffer = ByteBuffer.wrap(byteData);
            buffer.order(ByteOrder.BIG_ENDIAN);

            int number = buffer.getShort();

            //文字列の取得
            byteData = new byte[number];
            _inData.read(byteData, 0, number);

            String str = new String(byteData, "UTF8");
            return str;
        }catch (Exception e){
            e.printStackTrace();
            Log.e("",e.getMessage());
            return null;
        }
    }
}
