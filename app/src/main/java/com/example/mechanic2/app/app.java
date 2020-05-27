package com.example.mechanic2.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.FileUtils;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class app {
    public static class main {
        public static final String URL = "http://drkamal3.com/Mechanic/index.php/";
        public static String TAG = "mechanic";
    }

    public static String adminBaseUrl = "http://drkamal3.com/Mechanic/index.php?route=getAdminMedias&lastId=";

    public static void t(String text) {
        Toast.makeText(Application.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    public static void t(int text) {
        Toast.makeText(Application.getContext(), text + "", Toast.LENGTH_SHORT).show();
    }


    public static void l(String text) {
        Log.e(main.TAG, text);
    }


    public static void l(int text) {
        Log.e(main.TAG, text + "");
    }

    public static void l(String... values) {
        for (String value:values) {
            app.l(value+"*");
        }
    }
    public static void l(Integer... values) {
        for (int value:values) {
            app.l(value+"*");
        }
    }

    public static  String getEmojiByUnicode(int unicode){
        return new String(Character.toChars(unicode));
    }


    public static void hideKeyboard(EditText editText){
        InputMethodManager imm = (InputMethodManager) Application.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(EditText editText){
        InputMethodManager  imm = (InputMethodManager) Application.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Uri getImageUri( Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(Application.getContext().getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static MultipartBody.Part prepareImagePart(String partName, Uri fileUri) {
        File file = new File(Objects.requireNonNull(fileUri.getPath()));
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("image/png"),
                        file
                );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    public static MultipartBody.Part prepareAudioPart(String partName, Uri fileUri) {
        File file = new File(Objects.requireNonNull(fileUri.getPath()));
        RequestBody requestFile =
                RequestBody.create(
                        MediaType.parse("audio/3gp"),
                        file
                );
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
    public static float dpToPixels(int dp) {
        return dp * (Application.getContext().getResources().getDisplayMetrics().density);
    }
    public static int pxToDp(int dimension){
        final float scale = Application.getContext().getResources().getDisplayMetrics().density;
        return (int) (dimension * scale + 0.5f);
    }

    public static int pxToDp(float dimension){
        final float scale = Application.getContext().getResources().getDisplayMetrics().density;
        return (int) (dimension * scale + 0.5f);
    }

    /*
    *   @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(new String[0]),
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
             if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toArray(new String[0]),
                REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }   */

    private static int screenWidth = -1;
    private static int screenHeight = -1;
    public static int getScreenWidth(Context context) {
        if (screenWidth == -1) {
            Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }

    /**
     * get the screen height in pixels
     * @param context
     * @return
     */
    public static int getScreenHeight(Context context) {
        if (screenHeight == -1) {
            Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }


}
