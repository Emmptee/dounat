package codepig.ffmpegcldemo;

import android.content.Intent;
import android.util.Log;

import com.donut.app.mvp.shakestar.ffmpegdemo.FFmpegService;


public class FFmpegKit {
    public static boolean inited = false;

    static{
        //synchronized (FFmpegKit.class) {
//            if(!inited) {
                Log.e("MYTAG", "加载开始11111");
                System.loadLibrary("ffmpeg");
                Log.e("MYTAG","加载中22222");
                System.loadLibrary("ffmpeginvoke");
                Log.e("MYTAG","加载结束3333");
//                inited = true;
//            }
//        }
    }    public static int execute(String[] commands){
        return run(commands);
    }

    public native static int run(String[] commands);

    public static void onProgress(int frame) {
        Intent intent = new Intent("com.myaya.ffmpegbroadcast");
        intent.putExtra("fun", "onProgress");
        intent.putExtra("frame", frame);
        fFmpegService.sendBroadcast(intent);
    }
    public static void onEnd() {
        Intent intent = new Intent("com.myaya.ffmpegbroadcast");
        intent.putExtra("fun", "onEnd");
        intent.putExtra("isEnd", true);
        fFmpegService.sendBroadcast(intent);
    }

    static FFmpegService fFmpegService;

    public static void init(FFmpegService service){
        fFmpegService = service;
    }

    public static void notifyMedia(int duration, double fps, String fileName){
        Intent intent = new Intent("com.myaya.ffmpegbroadcast");
        intent.putExtra("fun", "notifyMedia");
        intent.putExtra("fps", fps);
        intent.putExtra("duration", duration);
        intent.putExtra("fileName", fileName);
        fFmpegService.sendBroadcast(intent);
    }
}
