package com.donut.app.mvp.shakestar.ffmpegdemo;

import java.util.ArrayList;

/**
 * Created by yoyo on 2018/3/5.
 */
public class FFmpegFuncion {

    public static ArrayList<String> getMediaInfo(String path){
        ArrayList<String> param = new ArrayList<String>();
        param.add("ffmpeg");
        param.add("-d");
        param.add("-i");
        param.add(path);
        return param;
    }

    public static ArrayList<String> joinVideo(String src1, String src2, String dest){
        ArrayList<String> param = new ArrayList<String>();
        param.add("ffmpeg");
        param.add("-i");
        param.add(src1);
        param.add("-i");
        param.add(src2);
        param.add("-filter_complex");
        param.add("[0:v]pad=iw*2:ih[a];[a][1:v]overlay=w:0");
        param.add(dest);
        return param;
    }

    public static ArrayList<String> concatVideo(String inputFile, String dest){
        ArrayList<String> param = new ArrayList<String>();
        param.add("ffmpeg");
        param.add("-f");
        param.add( "concat");
        param.add("-safe");
        param.add("0");
        param.add("-i");
        param.add(inputFile);
        param.add("-c");
        param.add("copy");
        param.add(dest);
        return param;
    }

    /**
     *
     * @param src
     * @param width
     * @param height
     * @param fps  24 25    23.98����д��ntsc-film   29.97����д��ntsc
     * @param dest
     * @return
     */
    public static ArrayList<String> formatVideo(String src, String width, String height, String fps, String dest){

        ArrayList<String> param = new ArrayList<String>();
        param.add("ffmpeg");
        param.add("-d");
        param.add("-i");
        param.add(src);
        param.add("-s");
        param.add(width + "x" + height);
        param.add("-r");
        if(fps.startsWith("23.9")) {
            param.add("ntsc-film");
        } else if(fps.startsWith("29.9")) {
            param.add("ntsc");
        }else{
            param.add(fps);
        }
//        param.add("-b");
//        param.add("630k");
//        param.add("-b:a");
//        param.add("48k");
//        param.add("-maxrate");
//        param.add("630k");
//        param.add("-acodec");
//        param.add("aac");
//        param.add("-ac");
//        param.add("2");
//        param.add("-ar");
//        param.add("22050");
        param.add(dest);
        return param;
    }

}
