package ecommerce.web.app.utils;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {
    public static byte[] compressFile(String content) {
        if (content != null) {
            byte[] byteData = Base64.getDecoder().decode(content);
            Deflater deflater = new Deflater();
            deflater.setLevel(Deflater.BEST_COMPRESSION);
            deflater.setInput(byteData);
            deflater.finish();

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(byteData.length);
            byte[] tmp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(tmp);
                outputStream.write(tmp, 0, size);
            }
            try {
                outputStream.close();
            } catch (Exception ignored) {
            }
            return outputStream.toByteArray();
        }
        return null;
    }

    public static String decompressFile(byte[] data) {
        if (data != null) {
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] tmp = new byte[4 * 1024];
            try {
                while (!inflater.finished()) {
                    int count = inflater.inflate(tmp);
                    outputStream.write(tmp, 0, count);
                }
                outputStream.close();
            } catch (Exception ignored) {
            }
            byte[] decompressedData = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(decompressedData);
        }
        return null;
    }
}
