package com.example.feng.xgs.main.find.broadcast;

import android.media.MediaRecorder;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.example.feng.xgs.R;
import com.example.feng.xgs.chatui.util.Utils;

import java.io.File;

import it.sauronsoftware.jave.AudioAttributes;
import it.sauronsoftware.jave.Encoder;
import it.sauronsoftware.jave.EncoderException;
import it.sauronsoftware.jave.EncodingAttributes;
import it.sauronsoftware.jave.InputFormatException;

/**
 * Created by Administrator on 2018/8/21.
 */

public class MediaRecorderUtils {

    private static MediaRecorder recorder;
    static MediaRecorderUtils mediaRecorderUtils;
    static ImageView mimageView;
    private String path;
    private String path2;

    /**
     * 获得单例对象，传入一个显示音量大小的imageview对象，如不需要显示可以传null
     */
    public static MediaRecorderUtils getInstence(ImageView imageView) {
        if (mediaRecorderUtils == null) {
            mediaRecorderUtils = new MediaRecorderUtils();
        }
        mimageView = imageView;
        return mediaRecorderUtils;
    }

    /**
     * 获得音频路径
     */
    public String getPath() {
        changeToMp3(path, path2);
        return path;
    }

    /**
     * 初始化
     */
    private void init() {

        recorder = new MediaRecorder();// new出MediaRecorder对象
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        // 设置MediaRecorder的音频源为麦克风
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        // 设置MediaRecorder录制的音频格式
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        // 设置MediaRecorder录制音频的编码为amr.
        File file = new File(Utils.IMAGE_SDCARD_MADER);
        if (!file.exists()) {
            file.mkdirs();
        }
        path = Utils.IMAGE_SDCARD_MADER + Utils.getVoiceFileName() + "stock.mp3";
        path2= Utils.IMAGE_SDCARD_MADER + Utils.getVoiceFileName() + "stock.mp3";
        recorder.setOutputFile(path);
        // 设置录制好的音频文件保存路径
        try {
            recorder.prepare();// 准备录制
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录音
     */
    public void MediaRecorderStart() {
        init();
        try {
            recorder.start();
            flag = true;
            if (mimageView != null) {
                updateMicStatus();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("chen", "录制失败");
        }
    }

    /**
     * 停止录音
     */
    public void MediaRecorderStop() {
        try {
            recorder.stop();
            recorder.release(); //释放资源
            flag = false;
            mimageView = null;
            recorder = null;
        } catch (Exception e) {
            e.toString();
        }

    }

    /**
     * 删除已录制的音频
     */
    public void MediaRecorderDelete() {
        File file = new File(path);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    ;public static void changeToMp3(String sourcePath, String targetPath) {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        AudioAttributes audio = new AudioAttributes();
        Encoder encoder = new Encoder();

        audio.setCodec("libmp3lame");
        EncodingAttributes attrs = new EncodingAttributes();
        attrs.setFormat("mp3");
        attrs.setAudioAttributes(audio);

        try {
            encoder.encode(source, target, attrs);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InputFormatException e) {
            e.printStackTrace();
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }


    private final Handler mHandler = new Handler();
    private Runnable mUpdateMicStatusTimer = new Runnable() {
        public void run() {
            updateMicStatus();
        }
    };
    private int BASE = 1;
    private int SPACE = 1000;// 间隔取样时间
    private boolean flag = true;

    /**
     * 更新话筒状态
     */
    private void updateMicStatus() {
        if (recorder != null) {
            double ratio = (double) recorder.getMaxAmplitude() / BASE;
            double db = 0;// 分贝
            if (ratio > 1) {
                db = 20 * Math.log10(ratio);
            }
            int i = (int) db / 10;
            switch (i) {
                case 1:
                    mimageView.setImageResource(R.drawable.icon_voice_left1);
                    break;
                case 2:
                    mimageView.setImageResource(R.drawable.icon_voice_left2);
                    break;
                case 3:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
                case 4:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
                case 5:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
                case 6:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
                case 7:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
                case 8:
                    mimageView.setImageResource(R.drawable.icon_voice_left3);
                    break;
            }
            if (flag) {
                mHandler.postDelayed(mUpdateMicStatusTimer, SPACE);
            }
        }
    }

}
