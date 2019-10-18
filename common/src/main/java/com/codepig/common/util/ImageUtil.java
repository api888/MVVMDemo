package com.codepig.common.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 图片压缩工具类
 */
public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();
    /**
     * 保存文件的最大字节数
     * 128KB:131072
     * 256KB:262144
     * 512KB:524288
     */
    private static final int MAX_SIZE = 524288;// 最大的图片大小
    private static final int WIDTH = 1500;// 规定图片的宽度.适应PC要求
    private static final int HEIGHT = 2000;// 规定图片的高度.适应PC要求

    private static final int WIDTH_SMALL = 320;// 小图片的宽度
    private static final int HEIGHT_SMALL = 480;// 小图片的高度

    private static final int DIVISOR = 1048576;// 1024x1024

    /**
     * scale compress
     *
     * @param filePath
     * @return
     */
    public static File getCompressFile(String filePath) {
        return getCompressFile(filePath, MAX_SIZE);
    }

    /**
     * 压缩图片
     *
     * @param path
     * @return
     */
    public static File getCompressFile(String path, int max_size) {
        if (CommonUtils.isEmpty(path)) {
            return null;
        }
        BitmapFactory.Options options_first = new BitmapFactory.Options();
        options_first.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bitmap_org = BitmapFactory.decodeFile(path, options_first);
        if (bitmap_org != null && bitmap_org.getByteCount() > 0) {
            if (bitmap_org.getByteCount() > max_size) { // 图片超过规定尺寸

                // 现判断图片的尺寸是否超过规定的最大值
                float matrix = calculateMatrixSize(bitmap_org.getWidth(), bitmap_org.getHeight());

                // 缩放法压缩
                Bitmap bitmap = matrixCompress(bitmap_org, matrix);

                //图片是否旋转
                int degree = readPictureDegree(path);
                //校正图片
                bitmap = rotaingImageView(degree,bitmap);

                return saveTempBitmapFile(bitmap, bitmap_org, true);
            } else {
                bitmap_org.recycle();
                return new File(path);
            }
        }
        return null;
    }

    /**
     * @param width  需要对比的宽
     * @param height 需要对比的高
     *               举例原图width:1920,height:1080;规定 width:840,height:1500;matrix:0.78
     * @return 缩放比例
     */
    public static float calculateMatrixSize(float width, float height) {
        float matrix_return = 1.0F;
        if (width > height) { // 宽屏
            float matrix_w = HEIGHT / width; // 0.78
            float matrix_h = WIDTH / height; // 0.77
            matrix_return = matrix_w > matrix_h ? matrix_w : matrix_h;
        } else { // 竖屏
            float matrix_w = WIDTH / width;
            float matrix_h = HEIGHT / height;
            matrix_return = matrix_w > matrix_h ? matrix_w : matrix_h;
        }
        matrix_return = matrix_return > 1.0F ? 1.0F : matrix_return;
        return matrix_return;
    }

    /**
     * @param bitmap
     * @param matrix_f
     * @return
     */
    public static Bitmap matrixCompress(final Bitmap bitmap, final float matrix_f) {
        Bitmap bitmap_return;
        Matrix matrix = new Matrix();
        matrix.setScale(matrix_f, matrix_f);
        bitmap_return = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//        bitmap.recycle();
        return bitmap_return;
    }

    /**
     * quanlity compress
     *
     * @return
     */
    public static Bitmap getCcompressImage(final byte[] data) {
        if (data == null) {
            return null;
        }
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeByteArray(data, 0, data.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, WIDTH_SMALL, HEIGHT_SMALL);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(new ByteArrayInputStream(data), null, options);
    }

    /**
     * 通过图片的长款，计算缩放比例
     *
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int in_sample_size = 1;
        int height = options.outHeight;
        int width = options.outWidth;
        if (width > height) {
            int temp = height;
            height = width;
            width = temp;
        }
        int i = 1;
        while (true) {
            if ((width >> i <= reqWidth) && (height >> i <= reqHeight)) {
                in_sample_size = (int) Math.pow(2, i);
                break;
            }
            i++;
        }
        return in_sample_size;
    }

    public static File saveTempBitmapFile(@NonNull Bitmap bitmap, @NonNull Bitmap bitmap_old, boolean isScale) {
        File f = null;
        try {
            f = File.createTempFile(System.currentTimeMillis() + "", ".jpg");
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, isScale ? 90 : 100, out);
            out.flush();
            out.close();
            bitmap.recycle();
            bitmap_old.recycle();
            return f;
        } catch (Exception e) {
            e.printStackTrace();
            return f;
        }
    }
    /**
     * 读取图片属性：旋转的角度
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 得到资源文件中图片的Uri
     * @param context 上下文对象
     * @param id 资源id
     * @return Uri
     */
    public static String getUriFromDrawableRes(Context context, int id) {
        Resources resources = context.getResources();
        String path = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                + resources.getResourcePackageName(id) + "/"
                + resources.getResourceTypeName(id) + "/"
                + resources.getResourceEntryName(id);
        return path;
    }
}
