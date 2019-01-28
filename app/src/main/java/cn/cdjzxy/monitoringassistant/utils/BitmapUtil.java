package cn.cdjzxy.monitoringassistant.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    /**
     * 压缩图片
     *
     * @param image      图片
     * @param targetSize 指定大小，单位（Byte）
     * @return
     */
    public static int Compress(Bitmap image, int targetSize) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int options = 100;

        //质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        image.compress(Bitmap.CompressFormat.JPEG, options, baos);

        //循环判断如果压缩后图片是否大于指定大小或质量是否大于10%
        while (baos.size() > targetSize && options > 2) {
            //重置baos即清空baos
            baos.reset();

            if (options > 50) {
                options -= 20;
            } else if (options > 10) {
                options -= 10;
            } else {
                options -= 2;
            }

            //这里压缩options%，把压缩后的数据存放到baos中
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        baos.reset();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        //把压缩后的数据baos存放到ByteArrayInputStream中
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
//
//        //把ByteArrayInputStream数据生成图片
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);

        return options;
    }

    /**
     * 保存图片到指定路径
     *
     * @param bmp
     * @param path
     * @param name
     */
    public static File SaveBitmapToPath(Bitmap bmp, String path, String name, int targetOptions) {
        FileOutputStream out = null;

        try {
            ////目录转化成文件夹
            File dirFile = new File(path);

            //如果不存在，那就建立这个文件夹
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }

            //文件夹有啦，就可以保存图片啦
            File file = new File(path, name);
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, targetOptions, out);
            out.flush();
            out.close();

            return file;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 压缩图片使用,采用BitmapFactory.decodeFile。这里是尺寸压缩
     */

    public static Bitmap Compress(String imagePath, int targetSize) {
        // 配置压缩的参数
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true; //获取当前图片的边界大小，而不是将整张图片载入在内存中，避免内存溢出
        BitmapFactory.decodeFile(imagePath, options);
        options.inJustDecodeBounds = false;

        options.inSampleSize = calculateInSampleSize(options, 750, 500);
        Bitmap newBitmap = BitmapFactory.decodeFile(imagePath, options); // 解码文件
//
//        int width = newBitmap.getWidth();
//        int height = newBitmap.getHeight();
//
//        int size = newBitmap.getByteCount();
//        while (size > targetSize && r > 0.02) {
//            if (newBitmap != null) {
//                newBitmap.recycle();
//                newBitmap = null;
//            }
//            if (r > 0.5) {
//                r -= 0.1;
//            } else {
//                r -= 0.02;
//            }
//
//            int w = (int) (width * r);
//            int h = (int) (height * r);
//
//            //inSampleSize的作用就是可以把图片的长短缩小inSampleSize倍，所占内存缩小inSampleSize的平方
//            int newSampleSize = calculateInSampleSize(options, w, h);
//            if (newSampleSize <= options.inSampleSize) {
//                continue;
//            }
//
//            options.inSampleSize = newSampleSize;
//
//            newBitmap = BitmapFactory.decodeFile(imagePath, options); // 解码文件
//            size = newBitmap.getByteCount();
//        }

        return newBitmap;
    }

    /**
     * 计算出所需要压缩的大小
     *
     * @param options
     * @param reqWidth  我们期望的图片的宽，单位px
     * @param reqHeight 我们期望的图片的高，单位px
     * @return
     */
    private static int caculateSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int sampleSize = 1;
        int picWidth = options.outWidth;
        int picHeight = options.outHeight;
        if (picWidth > reqWidth || picHeight > reqHeight) {
            int halfPicWidth = picWidth / 2;
            int halfPicHeight = picHeight / 2;
            while (halfPicWidth / sampleSize > reqWidth || halfPicHeight / sampleSize > reqHeight) {
                sampleSize *= 2;
            }
        }
        return sampleSize;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            //使用需要的宽高的最大值来计算比率
            final int suitedValue = reqHeight > reqWidth ? reqHeight : reqWidth;
            final int heightRatio = Math.round((float) height / (float) suitedValue);
            final int widthRatio = Math.round((float) width / (float) suitedValue);

            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;//用最大
        }

        return inSampleSize;
    }
}
