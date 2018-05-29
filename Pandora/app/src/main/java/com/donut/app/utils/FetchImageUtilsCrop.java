package com.donut.app.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.ImageColumns;
import android.support.v4.app.Fragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FetchImageUtilsCrop {

    private static final int PHOTO_PICKED_WITH_DATA = 6021;

    private static final int PHOTO_FROM_GALLERY = 6022;

    private static final int PHOTO_FROM_CAMERA = 6023;

    private static int DEFAULT_IMAGE_WIDTH_SIZE = 320;

    private static int DEFAULT_IMAGE_HEIGHT_SIZE = 320;

    private int photox = DEFAULT_IMAGE_WIDTH_SIZE;

    private int photoy = DEFAULT_IMAGE_HEIGHT_SIZE;

    private OnPickFinishedCallback callback;

    private File mCurrentPhotoFile;

    private Activity mActivity;

    private Fragment mFragment;

    private Uri mUri;

    public FetchImageUtilsCrop(Activity activity) {
        mActivity = activity;
    }

    public FetchImageUtilsCrop(Fragment fragment) {
        mFragment = fragment;
    }

    public interface OnPickFinishedCallback {
        void onPickSuccessed(Bitmap bm, String filePath);

        void onPickFailed();
    }

    public interface OnActionPickFinishedCallback {
        void onPickSuccessed(Bitmap bm, String filePath, int actionId);

        void onPickFailed();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PHOTO_PICKED_WITH_DATA:
                if (resultCode == Activity.RESULT_OK) {
                    String path = mCurrentPhotoFile.getAbsolutePath();
                    Bitmap bm = PictureUtil.getSmallBitmap(path);
                    if (bm != null) {
                        callback.onPickSuccessed(bm, path);
                        return;
                    } else {
                        if (data.getExtras() != null) {
                            Bitmap img = (Bitmap) data.getExtras().get("data");
                            if (img != null) {
                                callback.onPickSuccessed(img, path);
                                return;
                            }
                        } else {
                            callback.onPickFailed();
                            return;
                        }
                    }
                }
                break;
            case PHOTO_FROM_CAMERA:
                if (resultCode == Activity.RESULT_OK) {
                    doCropPhoto(mUri);
                }
                break;
            case PHOTO_FROM_GALLERY:
                if (resultCode == Activity.RESULT_OK) {
                    doCropPhoto(data.getData());
                }
                break;
        }
    }

    private void doCropPhoto(Uri mUri) {
        try {
            // Launch gallery to crop the photo
            Intent intent = getCropImageIntent(mUri);
            if (mActivity != null) {
                mActivity.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            } else if (mFragment != null) {
                mFragment.startActivityForResult(intent, PHOTO_PICKED_WITH_DATA);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doTakePhoto(OnPickFinishedCallback callback) {
        this.callback = callback;
        doTakePhoto(DEFAULT_IMAGE_WIDTH_SIZE, DEFAULT_IMAGE_HEIGHT_SIZE);
    }

    public void doTakePhoto(OnPickFinishedCallback callback, int width, int height) {
        this.callback = callback;
        doTakePhoto(width, height);
    }

    public void doTakePhoto(final OnActionPickFinishedCallback callback, final int actionId) {
        OnPickFinishedCallback mCallback = new OnPickFinishedCallback() {
            @Override
            public void onPickSuccessed(Bitmap bm, String filePath) {
                callback.onPickSuccessed(bm, filePath, actionId);
            }

            @Override
            public void onPickFailed() {
                callback.onPickFailed();
            }
        };

        doTakePhoto(mCallback);
    }

    private void doTakePhoto(int width, int height) {
        this.photox = width;
        this.photoy = height;
        try {
            // Launch camera to take photo for selected contact
            final Intent intent = getTakePickIntent();
            if (mActivity != null) {
                mActivity.startActivityForResult(intent, PHOTO_FROM_CAMERA);
            } else {
                mFragment.startActivityForResult(intent, PHOTO_FROM_CAMERA);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void doPickPhotoFromGallery(OnPickFinishedCallback callback) {
        this.callback = callback;
        doPickPhotoFromGallery(DEFAULT_IMAGE_WIDTH_SIZE,
                DEFAULT_IMAGE_HEIGHT_SIZE);
    }

    public void doPickPhotoFromGallery(OnPickFinishedCallback callback, int width, int height) {
        this.callback = callback;
        doPickPhotoFromGallery(width, height);
    }

    public void doPickPhotoFromGallery(final OnActionPickFinishedCallback callback, final int actionId) {
        OnPickFinishedCallback mCallback = new OnPickFinishedCallback() {
            @Override
            public void onPickSuccessed(Bitmap bm, String filePath) {
                callback.onPickSuccessed(bm, filePath, actionId);
            }

            @Override
            public void onPickFailed() {
                callback.onPickFailed();
            }
        };
        doPickPhotoFromGallery(mCallback);
    }

    private void doPickPhotoFromGallery(int width, int height) {
        this.photox = width;
        this.photoy = height;
        try {
            final Intent intent = getPhotoPickIntent();
            if (mActivity != null) {
                mActivity.startActivityForResult(intent, PHOTO_FROM_GALLERY);
            } else if (mFragment != null) {
                mFragment.startActivityForResult(intent, PHOTO_FROM_GALLERY);
            }
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Intent getTakePickIntent() {
        Context context = null;
        if (mActivity != null) {
            context = mActivity;
        } else if (mFragment != null) {
            context = mFragment.getContext();
        }
        mUri = createImagePathUri(context);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
        return intent;
    }

    private Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        return intent;
    }

    private Intent getCropImageIntent(Uri photoUri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", photox);
        intent.putExtra("aspectY", photoy);
        intent.putExtra("outputX", photox);
        intent.putExtra("outputY", photoy);
        intent.putExtra("return-data", false);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        Context context = null;
        if (mActivity != null) {
            context = mActivity;
        } else if (mFragment != null) {
            context = mFragment.getContext();
        }
        String PHOTO_DIR = FileUtils.getCacheDir(context);
        mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());
        if (mCurrentPhotoFile.exists()) {
            mCurrentPhotoFile.delete();
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCurrentPhotoFile));
        return intent;
    }

    /**
     * Create a file name for the icon photo using current time.
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyyMMdd_HHmmss", Locale.getDefault());
        return dateFormat.format(date) + ".jpg";
    }

    /**
     * Constructs an intent for capturing a photo and storing it in a temporary
     * file.
     */
    private static Uri createImagePathUri(Context context) {
        Uri imageFilePath = null;
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(
                "yyyyMMdd_HHmmss", Locale.CHINA);
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(3);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.DATE_TAKEN, time);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            imageFilePath = context.getContentResolver().insert(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            try {
                imageFilePath = context.getContentResolver().insert(
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
            } catch (Exception e) {
                ToastUtil.showShort(context, "抱歉，您禁用了拍照功能");
            }

        }
        return imageFilePath;
    }

    private static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) {
            return null;
        }
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }
}

