package jp.wmyt.test.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Created by JP10733 on 2014/05/07.
 */
public class Common {
    static public int getInt32FromFile(File file) throws IOException {
        FileInputStream in = new FileInputStream(file);
        byte[] byteData = new byte[4];
        in.read(byteData, 0, 4);
        ByteBuffer buffer = ByteBuffer.wrap(byteData);
        buffer.order(ByteOrder.BIG_ENDIAN);

        return buffer.getInt();
    }
}
