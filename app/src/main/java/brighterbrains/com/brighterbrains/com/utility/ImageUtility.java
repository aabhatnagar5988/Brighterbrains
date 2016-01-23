package brighterbrains.com.brighterbrains.com.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by DELL on 1/23/2016.
 */
public class ImageUtility {

    Context context;

    public ImageUtility(Context context) {
        this.context = context;
    }

    //-------Gets the path of the selected image from the gallery-------------
    public String getImagePath(Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ",
                new String[] { document_id }, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor
                .getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }
    //----------getImagePath ends here-----------------------------

    //-----Maintains the aspect ratio of the image-----------------
    private Bitmap maintainAspect(Bitmap bm, int reqH) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        int wid = (width * reqH) / height;
        Bitmap b = Bitmap.createScaledBitmap(bm, wid, reqH, false);


        return b;
    }
    //-------maintainAspect body ends here---------------------------

    //-----Get the image from the file path------------------------------
    //-----Copy image in the brighterbrains folder-----------------------
    //-----Displays the bitmap mantaining the aspect ratio of the image--
    //-----Exif Rotation corrected---------------------------------------

    public Bitmap getImage(String path,boolean isThumb) {
        Bitmap bm1=null;

        try {
            InputStream inputStream = new FileInputStream(path);// You can get

            byte[] bytes;
            byte[] buffer = new byte[8192];
            int bytesRead;
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            try {
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            bytes = output.toByteArray();
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

            try {
                ExifInterface ei = new ExifInterface(path);
                int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_NORMAL);
                Matrix matrix = new Matrix();
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        bm1 = Bitmap.createBitmap(bm, 0, 0,
                                bm.getWidth(), bm.getHeight(),
                                matrix, true);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        bm1 = Bitmap.createBitmap(bm, 0, 0,
                                bm.getWidth(), bm.getHeight(),
                                matrix, true);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        bm1 = Bitmap.createBitmap(bm, 0, 0,
                                bm.getWidth(), bm.getHeight(),
                                matrix, true);
                        break;
                    default:
                        bm1 = Bitmap.createBitmap(bm, 0, 0,
                                bm.getWidth(), bm.getHeight(),
                                matrix, true);
                        break;
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }

            if(!isThumb)
            bm1=maintainAspect(bm1,500);

            else
                bm1=Bitmap.createScaledBitmap(bm1,150,150,false);


        } catch (Exception e) {

        }

        return bm1;
    }

    //----getImage body ends here---------------------------

    //----Returns the new Image file with count updated----
    public File getImageFile()
    {
        String fileName="";
        SharedPreferences prefs1 = PreferenceManager
                .getDefaultSharedPreferences(context);
        int newid = prefs1.getInt("id", 1);

        fileName = "Item" + newid;
        prefs1.edit().putInt("id",(newid+1)).commit();

        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/saved_images_brighterbrains");

        if (!myDir.exists())
            myDir.mkdirs();

        String fname = fileName + ".jpg";
        File file = new File(myDir, fname);

        try {
            if (file.exists() == false) {
                file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        return file;
    }
    //----------------getImageFile() body ends here----------------


    //------Copy selected image to brighter minds folder--------------------
    public void copyFile(File sourceFile, File destFile) throws IOException {
        if (!sourceFile.exists()) {
            return;
        }

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }
    }
    //-----------copyFile body inds here------------------------------
}
