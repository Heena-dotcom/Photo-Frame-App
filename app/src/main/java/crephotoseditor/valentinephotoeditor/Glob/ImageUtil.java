package crephotoseditor.valentinephotoeditor.Glob;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

import crephotoseditor.valentinephotoeditor.R;
import crephotoseditor.valentinephotoeditor.model.FrameListModel;


public class ImageUtil {
    public static Bitmap textBitmap;
    public static ArrayList<FrameListModel> frameList;

    public static String RootPath = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static ArrayList<FrameListModel> setListForFrame() {
        frameList = new ArrayList<FrameListModel>();

        frameList.add(new FrameListModel(R.drawable.frame1, R.drawable.frame1));
        frameList.add(new FrameListModel(R.drawable.frame2, R.drawable.frame2));
        frameList.add(new FrameListModel(R.drawable.frame3, R.drawable.frame3));
        frameList.add(new FrameListModel(R.drawable.frame4, R.drawable.frame4));
        frameList.add(new FrameListModel(R.drawable.frame5, R.drawable.frame5));
        frameList.add(new FrameListModel(R.drawable.frame6, R.drawable.frame6));
        frameList.add(new FrameListModel(R.drawable.frame7, R.drawable.frame7));
        frameList.add(new FrameListModel(R.drawable.frame8, R.drawable.frame8));
        frameList.add(new FrameListModel(R.drawable.frame9, R.drawable.frame9));
        frameList.add(new FrameListModel(R.drawable.frame10, R.drawable.frame10));
        frameList.add(new FrameListModel(R.drawable.frame11, R.drawable.frame11));
        frameList.add(new FrameListModel(R.drawable.frame12, R.drawable.frame12));
        frameList.add(new FrameListModel(R.drawable.frame13, R.drawable.frame13));
        frameList.add(new FrameListModel(R.drawable.frame14, R.drawable.frame14));
        frameList.add(new FrameListModel(R.drawable.frame15, R.drawable.frame15));
        frameList.add(new FrameListModel(R.drawable.frame16, R.drawable.frame16));
        frameList.add(new FrameListModel(R.drawable.frame17, R.drawable.frame17));
        frameList.add(new FrameListModel(R.drawable.frame18, R.drawable.frame18));
        frameList.add(new FrameListModel(R.drawable.frame19, R.drawable.frame19));
        frameList.add(new FrameListModel(R.drawable.frame20, R.drawable.frame20));
        frameList.add(new FrameListModel(R.drawable.frame21, R.drawable.frame21));
        frameList.add(new FrameListModel(R.drawable.frame22, R.drawable.frame22));
        frameList.add(new FrameListModel(R.drawable.frame23, R.drawable.frame23));
        frameList.add(new FrameListModel(R.drawable.frame24, R.drawable.frame24));
        frameList.add(new FrameListModel(R.drawable.frame25, R.drawable.frame25));
        frameList.add(new FrameListModel(R.drawable.frame26, R.drawable.frame26));
        frameList.add(new FrameListModel(R.drawable.frame27, R.drawable.frame27));
        frameList.add(new FrameListModel(R.drawable.frame28, R.drawable.frame28));
        frameList.add(new FrameListModel(R.drawable.frame29, R.drawable.frame29));
        frameList.add(new FrameListModel(R.drawable.frame30, R.drawable.frame30));
        frameList.add(new FrameListModel(R.drawable.frame31, R.drawable.frame31));
        frameList.add(new FrameListModel(R.drawable.frame32, R.drawable.frame32));
        frameList.add(new FrameListModel(R.drawable.frame33, R.drawable.frame33));
        frameList.add(new FrameListModel(R.drawable.frame34, R.drawable.frame34));
        frameList.add(new FrameListModel(R.drawable.frame35, R.drawable.frame35));
        frameList.add(new FrameListModel(R.drawable.frame36, R.drawable.frame36));

        return frameList;
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(RootPath, "/" + path);
        if (!file.exists()) {
            file.mkdir();
            if (!file.mkdirs()) {
                ret = false;
            }
        } else {
        }
        return ret;
    }

    public static ArrayList<String> getfile(File dir, String fileType)//pass fileType as a music , video, etc.
    {

        ArrayList<String> fileList = new ArrayList<String>();

        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {

                if (listFile[i].isDirectory()) {
                    getfile(listFile[i], fileType);

                } else {
                    if ("image".equals(fileType)) {
                        if (listFile[i].getName().endsWith(".jpg")) {
                            fileList.add(listFile[i].toString());
                        }
                    }
                }
            }
            Collections.sort(fileList, Collections.<String>reverseOrder());
        }
        return fileList;
    }

    public static Boolean CheckNet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private static Bitmap b;
    private static Canvas c;

    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(-2, -2);
            b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
        b = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        c = new Canvas(b);
        v.layout(v.getLeft(), v.getTop(), v.getRight(), v.getBottom());
        v.draw(c);
        return b;
    }

    public static Bitmap CropBitmapTransparency(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if (alpha > 0)   // pixel is not 100% transparent
                {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }
        if ((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }
}
