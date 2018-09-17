package com.example.feng.xgs.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

/**
 * Created by feng on 2018/5/9 0009.
 */

public class QrCodeUtil {



    public static Bitmap createBitmap(Context context, String info, int width){
        // 把输入的文本转为二维码
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");

        int qrWidth = DensityUtils.dp2px(context, 220);
        int qrHeight = qrWidth;

        BitMatrix bitMatrix = null;
        try {
            bitMatrix = new QRCodeWriter().encode(info,
                    BarcodeFormat.QR_CODE, qrWidth, qrHeight, hints);
        } catch (WriterException e) {
            e.printStackTrace();
            Log.d("TAG", "initQrCode: bitMatrix创建失败");
        }

        if(bitMatrix == null){
            return null;
        }

        int[] pixels = new int[qrWidth * qrHeight];
        for (int y = 0; y < qrHeight; y++) {
            for (int x = 0; x < qrWidth; x++) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * qrWidth + x] = 0xff000000;
                } else {
                    pixels[y * qrWidth + x] = 0xffffffff;
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(qrWidth, qrHeight,
                Bitmap.Config.ARGB_8888);

        bitmap.setPixels(pixels, 0, qrWidth, 0, 0, qrWidth, qrHeight);
//        if (bitmap != null) {
            return bitmap;
//        }

    }
}
