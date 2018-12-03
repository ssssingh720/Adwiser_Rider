package ridersadvisor.com.utils;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by jonu.kumar on 17-01-2018.
 */

public class ImageUtility {

    public static Bitmap getBitmapFromUri(Context pContext, Uri pUri) {
        Bitmap bitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        bitmap = BitmapFactory.decodeFile(pUri.getPath(), options);
        if (bitmap != null) {
//            return bitmap;
            return rotateImageIfRequired(pContext, bitmap, pUri);
        }

        try {
            bitmap = MediaStore.Images.Media.getBitmap(pContext.getContentResolver(), pUri);
            if (bitmap != null) {
                return rotateImageIfRequired(pContext, bitmap, pUri);
//                return bitmap;
            }
        } catch (IOException e) {
            e.printStackTrace();
            try {
                ParcelFileDescriptor parcelFileDescriptor = pContext.getContentResolver().openFileDescriptor(pUri, "r");
                if (parcelFileDescriptor != null) {
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    if (bitmap != null) {
                        return rotateImageIfRequired(pContext, bitmap, pUri);
//                        return bitmap;
                    }
                    parcelFileDescriptor.close();
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        } catch (OutOfMemoryError ome) {
            ome.printStackTrace();
            Toast.makeText(pContext, "Out Of Memory Error", Toast.LENGTH_SHORT).show(); //jonu
        }catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(pContext, "Exception While Fetching Image", Toast.LENGTH_SHORT).show(); //jonu
        }
        return null;
    }

    /**
     * Rotate an image if required.
     *
     * @param img           The image bitmap
     * @param selectedImage Image URI
     * @return The resulted Bitmap after manipulation
     */
    private static Bitmap rotateImageIfRequired(Context context, Bitmap img, Uri selectedImage) {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(selectedImage);
            ExifInterface ei;
            if (Build.VERSION.SDK_INT > 23) {
                ei = new ExifInterface(input);
            } else {
                ei = new ExifInterface(selectedImage.getPath());
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270);
                default:
                    return img;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;
    }

   /* private Bitmap checkImageRotation(Uri pUri, Bitmap pBitmap) {
        ExifInterface ei = new ExifInterface(pUri.toString());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(pBitmap, 90);

            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(pBitmap, 180);

            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(pBitmap, 270);

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                return pBitmap;
        }
    }*/

    private static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public Bitmap convertByteArrayToBitmap(byte[] data) {
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public static String insertBitmapToAppFolder(Bitmap bitmap, Context pContext) {
        ContextWrapper cw = new ContextWrapper(pContext.getApplicationContext());
        File directory = cw.getDir("Adwise", Context.MODE_PRIVATE);
        if (!directory.exists()) {
            boolean isDirectoryMakeSuccessfully = directory.mkdir();
        }
        String picName = "Adwise_" + System.currentTimeMillis();
        File mypath = new File(directory, picName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
//            long bitmapSize = getBitmapByteCount(bitmap);
            Bitmap resizedBitmap = getResizedBitmap(bitmap, 1200);
//            long bitmapSize1 = getBitmapByteCount(resizedBitmap);
            resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        } catch (Exception e) {
            Log.e("SAVE_IMAGE_ERROR", e.getMessage(), e);
        }
        Log.d("SAVE FILE PATH : ", directory.getAbsolutePath() + "/" + picName);
        return directory.getAbsolutePath() + "/" + picName;
    }

    public static Bitmap loadImageFromStorage(String path, Context pContext) {
        try {
            ContextWrapper cw = new ContextWrapper(pContext.getApplicationContext());
           /* File path1 = cw.getDir("imageDir", Context.MODE_PRIVATE);
            File f = new File(path1, "profile.jpg");*/
            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();
        if (width > maxSize || height > maxSize) { // check either actual image is too large or not
            float bitmapRatio = (float) width / (float) height;
            if (bitmapRatio > 1) {
                width = maxSize;
                height = (int) (width / bitmapRatio);
            } else {
                height = maxSize;
                width = (int) (height * bitmapRatio);
            }
//            return Bitmap.createScaledBitmap(image, width, height, true);
            return Bitmap.createScaledBitmap(image, width, height, true);
        }
        return image;
    }

}
