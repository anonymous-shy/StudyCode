package xyz.shy;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Shy on 2018/9/25
 */

public class TesT {

    public static void main(String[] args) {
        try {
            byte[] A = inputStream2ByteArray("E:\\20180922082315.txt");
            String md5 = "47f98ed02e168866769c959dc262dffc";
            byte[] B = md5.getBytes();
            for (int i = 0; i < B.length; i++) {
                System.out.println(B[i]);
            }
            int b = B[0];
            for(int i=1;i<A.length;i++){
                b^=A[i];
            }
            System.out.println(b);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static byte[] inputStream2ByteArray(String filePath) throws IOException {

        InputStream in = new FileInputStream(filePath);
        byte[] data = toByteArray(in);
        in.close();

        return data;
    }

    private static byte[] toByteArray(InputStream in) throws IOException {

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while ((n = in.read(buffer)) != -1) {
            out.write(buffer, 0, n);
        }
        return out.toByteArray();
    }
}
