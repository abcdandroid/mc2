package com.example.mechanic2.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mechanic2.R;
import com.example.mechanic2.interfaces.ConnectionErrorManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.Field;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.example.mechanic2.app.Connectivity.isConnected;

public class app {
    public static class main {
        public static final String URL = Application.getContext().getString(R.string.drk);
        public static String TAG = "mechanic";
    }

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
        for (String value : values) {

        }
    }

    public static void l(Integer... values) {
        for (int value : values) {

        }
    }

    public static String getEmojiByUnicode(int unicode) {
        return new String(Character.toChars(unicode));
    }


    public static void hideKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) Application.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public static void showKeyboard(EditText editText) {
        InputMethodManager imm = (InputMethodManager) Application.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }


     public static void validateConnection(Activity activity, SweetAlertDialog sweetAlertDialog, ConnectionErrorManager connectionErrorManager) {
        if (sweetAlertDialog != null)
            sweetAlertDialog.dismissWithAnimation();
        if (!isConnected(activity)) {
            sweetAlertDialog = new SweetAlertDialog(activity);
            sweetAlertDialog.changeAlertType(SweetAlertDialog.CUSTOM_IMAGE_TYPE);
            sweetAlertDialog.hideConfirmButton();
            View viewErrorConnection = LayoutInflater.from(activity).inflate(R.layout.view_error_connection, null, false);
            sweetAlertDialog.setCancelable(false);
            TextView retry = viewErrorConnection.findViewById(R.id.retry);


            SweetAlertDialog finalSweetAlertDialog = sweetAlertDialog;
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    validateConnection(activity,finalSweetAlertDialog,connectionErrorManager);
                }
            });

            sweetAlertDialog.setCustomView(viewErrorConnection);
            sweetAlertDialog.show();
        } else {
            connectionErrorManager.doAction();
        }
    }
    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Uri getImageUri(Bitmap inImage) {
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
    public static int dpToPixels(int dp,int a) {
        return (int) (dp * (Application.getContext().getResources().getDisplayMetrics().density));
    }

    public static int pxToDp(int dimension) {
        final float scale = Application.getContext().getResources().getDisplayMetrics().density;
        return (int) (dimension * scale + 0.5f);
    }

    public static int pxToDp(float dimension) {
        final float scale = Application.getContext().getResources().getDisplayMetrics().density;
        return (int) (dimension * scale + 0.5f);
    }

    public static void setMargins(View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    public static void setMargins(View v, int l ) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, l, l, l);
            v.requestLayout();
        }
    }



    private static int screenWidth = -1;
    private static int screenHeight = -1;

    public static int getScreenWidth(Context context) {
        if (screenWidth == -1) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }
        return screenWidth;
    }


    public static int getScreenHeight(Context context) {
        if (screenHeight == -1) {
            Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }
        return screenHeight;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        String newLine = System.getProperty("line.separator");

        result.append(this.getClass().getName());
        result.append(" Object {");
        result.append(newLine);


        Field[] fields = this.getClass().getDeclaredFields();


        for (Field field : fields) {
            result.append("  ");
            try {
                result.append(field.getName());
                result.append(": ");

                result.append(field.get(this));
            } catch (IllegalAccessException ex) {
                System.out.println(ex);
            }
            result.append(newLine);
        }
        result.append("}");

        return result.toString();
    }


    public static boolean appInstalledOrNot(String uri) {
        PackageManager pm = Application.getContext().getPackageManager();
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
        }

        return false;
    }


    public static void hideStatusBar( Activity activity) {



        if (Build.VERSION.SDK_INT < 16) {
            Window w = activity.getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        } else {
            View decorView = activity.getWindow().getDecorView();

            int visibility = View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(visibility);

            ActionBar actionBar = activity.getActionBar();
            actionBar.hide();

        }

    }

    public static void showStatusBar( Activity activity) {

        if (Build.VERSION.SDK_INT < 16) {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }

        else {
            View decorView = activity.getWindow().getDecorView();

            int visibility = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(visibility);

            ActionBar actionBar = activity.getActionBar();
            actionBar.show();

        }

    }

    public static void loadFragment(AppCompatActivity appCompatActivity, Fragment fragment) {
        FragmentTransaction fragmentTransaction = appCompatActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container , fragment);
        fragmentTransaction.commit();
    }

}
