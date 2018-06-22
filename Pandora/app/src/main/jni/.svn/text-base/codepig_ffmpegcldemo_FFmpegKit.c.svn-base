#include <stdio.h>
#include "codepig_ffmpegcldemo_FFmpegKit.h"
#include "ffmpeg.h"
#include <android_log.h>
//#include "logjam.h"



JNIEXPORT jint JNICALL Java_codepig_ffmpegcldemo_FFmpegKit_run(JNIEnv *env, jclass obj, jobjectArray commands)
{
    __android_log_print(ANDROID_LOG_INFO, "1111111111111111", "%s", "2222222222222222222222222222222222222222222222");

    int argc = (*env)->GetArrayLength(env, commands);
    char *argv[argc];
    int i;
    for (i = 0; i < argc; i++) {
        jstring js = (jstring) (*env)->GetObjectArrayElement(env, commands, i);
        argv[i] = (char*) (*env)->GetStringUTFChars(env, js, 0);
        __android_log_print(ANDROID_LOG_INFO, "1111111111111111", "%s", argv[i]);
    }

//    jclass jcls = (*env)->NewGlobalRef(env,obj);
//    jmethodID methodID = (*env)->GetStaticMethodID(env, jcls, "onProgress", "(I)V");
    //调用该方法
//    (*env)->CallStaticVoidMethod(env, jcls, methodID,11111);
    run(argc, argv, env, obj);
    LOGD("----------run---------");
    return 0;
}
