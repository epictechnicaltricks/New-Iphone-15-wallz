package uz.applewallz.activity;


import android.Manifest;
import android.app.AlertDialog;
import android.app.WallpaperManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import uz.applewallz.FileUtil;
import uz.applewallz.R;
import uz.applewallz.Util;

public class ShowWallz extends AppCompatActivity {

    ImageView wallz_img;
    LinearLayout wallz_progress;

    LinearLayout rl,bg;

    final String[] listItems = new String[]{"Home Screen", "Lock Screen", "Both Home and Lock"};

    final int[] checkedItem = {0};

    String isSelected="";

    WallpaperManager wallpaperManager;

    Handler h;

    LottieAnimationView animationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_wallz);

        h = new Handler();
        wallz_img = findViewById(R.id.wall_img);
        animationView = findViewById(R.id.animationView);
       // animationView.setVisibility(View.VISIBLE);
       // animationView.pauseAnimation();


        wallz_progress = findViewById(R.id.wallz_progress);
        rl = findViewById(R.id.wallz_img_layout);

        bg = findViewById(R.id.bg);

        wallpaperManager = WallpaperManager.getInstance(this);


        Util.showAnimationName(rl, "shubham");


        Window w = getWindow(); // in Activity's onCreate() for instance
        w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);



        init();



    }

    void init()
    {

        wallz_progress.setVisibility(View.GONE);
        bg.setVisibility(View.GONE);


        Log.d("url111", getIntent().getStringExtra("wallz_url"));

        String url = getIntent().getStringExtra("wallz_url");
        String final_url;
        if(Util.isConnected(ShowWallz.this)) {
            final_url = url.replace("q=80&w=400",Util.High_ImgQuality());

        } else {
            final_url = url;
            Toast.makeText(this, "No internet!", Toast.LENGTH_SHORT).show();
        }

        final Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.fade_in);

        final Animation zoom_out = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_oit);


        Glide.with(getApplicationContext()).load(
                        Uri.parse(final_url))
                .error(R.drawable.no_internet)

                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Drawable> target, boolean b) {

                     //  Toast.makeText(ShowWallz.this, "", Toast.LENGTH_SHORT).show();
                        wallz_img.setImageDrawable(getResources().getDrawable(R.drawable.no_internet));

                        bg.setVisibility(View.VISIBLE);
                        wallz_img.startAnimation(zoom_out);
                        bg.startAnimation(slide_up);

                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable drawable, Object o, Target<Drawable> target, DataSource dataSource, boolean b) {
                        wallz_progress.setVisibility(View.GONE);
                       // Toast.makeText(ShowWallz.this, "loaded", Toast.LENGTH_SHORT).show();


                        wallz_img.startAnimation(zoom_out);
                        bg.setVisibility(View.VISIBLE);
                       bg.startAnimation(slide_up);

                        wallz_img.setImageDrawable(drawable);
                        return false;
                    }
                })
                .placeholder(R.drawable.hold_tight)
                .thumbnail(0.01f)
                .into(wallz_img);

        Block_Screenshot();

    }


    public void set_Wallz(View view)
    {
        show_wallpaperset();
    }

    public void download_Wallz(View view)
    {

        Toast.makeText(this, "SUCCESS", Toast.LENGTH_SHORT).show();
        if(Build.VERSION.SDK_INT >= 33)
        {

            long time= System.currentTimeMillis();
            String path_location = FileUtil
                    .getPublicDir(Environment.DIRECTORY_DOWNLOADS)
                    .concat("/#"+getString(R.string.app_name)+"/");

            _save_image_to_storage(wallz_img,time+".png",path_location,100);


        } else
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ) {
              new AlertDialog.Builder(ShowWallz.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK)
                      .setTitle("STORAGE Permission required to download!")
                      .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              ActivityCompat.requestPermissions(ShowWallz.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                          }
                      }).setCancelable(false).setNegativeButton("OPEN SETTINGS", new DialogInterface.OnClickListener() {
                          @Override
                          public void onClick(DialogInterface dialogInterface, int i) {
                              Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                              Uri uri = Uri.fromParts("package", getPackageName(), null);
                              intent.setData(uri);
                              startActivity(intent);
                          }
                      }).create().show();
                   }  else
                   {
                       long time= System.currentTimeMillis();
                       String path_location = FileUtil
                               .getPublicDir(Environment.DIRECTORY_DOWNLOADS)
                               .concat("/#"+getString(R.string.app_name)+"/");

                       _save_image_to_storage(wallz_img,time+".png",path_location,100);


                   }

        }





    }


    public void share_Wallz(View view)
    {
        Toast.makeText(this, "Thank you ♥ for sharing..", Toast.LENGTH_SHORT).show();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) wallz_img.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();
        share_Image_with_Text(bitmap);
    }

    public void review_Wallz(View view)
    {
        Toast.makeText(this, "ssdadsaddssadsa234243", Toast.LENGTH_SHORT).show();
    }

    void Block_Screenshot () {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    private void share_Image_with_Text(Bitmap bitmap) {
        Uri uri = get_path_image_for_Share(bitmap);

        Intent intent = new Intent(Intent.ACTION_SEND);


        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "More Wallpapers \n https://play.google.com/store/apps/details?id="+getPackageName());
        intent.putExtra(Intent.EXTRA_SUBJECT, "Share Wallpaper");
        intent.setType("image/png");
        startActivity(Intent.createChooser(intent, "Share on"));
    }


    // Retrieving the url to share
    private Uri get_path_image_for_Share(Bitmap bitmap) {
        File imagefolder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            imagefolder.mkdirs();
            File file = new File(imagefolder, "Wallpaper.png");
            FileOutputStream outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
            uri = FileProvider.getUriForFile(this, "uz.applewallz.fileprovider", file);
        } catch (Exception e) {
            Toast.makeText(this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return uri;
    }



    void show_wallpaperset()
    {
        // instance of alert dialog to build alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowWallz.this, AlertDialog.THEME_DEVICE_DEFAULT_DARK);
        //builder.setIcon(R.drawable.wallpaper);
        builder.setTitle("Set as");
        builder.setSingleChoiceItems(listItems, checkedItem[0], (dialog, which) -> {

            checkedItem[0] = which;
            isSelected = listItems[which];




        });


        builder.setPositiveButton("SET", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(isSelected.equals(""))
                {
                    isSelected = "Home Screen";
                }


                switch (isSelected)
                {

                    case "Home Screen" :
                        Bitmap bitmap3 = ((BitmapDrawable)wallz_img.getDrawable()).getBitmap();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                wallpaperManager.setBitmap(bitmap3,null, true, WallpaperManager.FLAG_SYSTEM);
                            //    success();

                            } catch (IOException e) {
                                Toast.makeText(ShowWallz.this, "Failed "+e, Toast.LENGTH_SHORT).show();

                                e.printStackTrace();
                            }
                        }

                        break;
                    case "Lock Screen" :

                        try {

                            Bitmap bitmap = ((BitmapDrawable)wallz_img.getDrawable()).getBitmap();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                wallpaperManager.setBitmap(bitmap,null, true, WallpaperManager.FLAG_LOCK);
                                success();
                            }


                        }
                        catch (Exception g) { g.printStackTrace();
                            Toast.makeText(ShowWallz.this, "Failed "+ g, Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "Both Home and Lock" :

                        Bitmap bitmap = ((BitmapDrawable)wallz_img.getDrawable()).getBitmap();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                wallpaperManager.setBitmap(bitmap,null, true, WallpaperManager.FLAG_LOCK);

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                        Bitmap bitmap2 = ((BitmapDrawable)wallz_img.getDrawable()).getBitmap();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                            try {
                                wallpaperManager.setBitmap(bitmap2,null, true, WallpaperManager.FLAG_SYSTEM);
                                success();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }


                        break;

                }

            }
        });

        // set the negative button to do some actions
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        // show the alert dialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setGravity(Gravity.NO_GRAVITY);
    }

    void success()
    {



        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                animationView.setVisibility(View.GONE);
                animationView.pauseAnimation();
             /*   new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                    }
                }, 3000);*/

            }
        }, 5600);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animationView.setVisibility(View.VISIBLE);
                animationView.playAnimation();

            }
        }, 2000);


        //Toast.makeText(ShowWallz.this, "Congratulations", Toast.LENGTH_SHORT).show();

    }

    public void _save_image_to_storage (final ImageView _view, final String _name,  final String _save_path, final double _quality) {

        try{
            BitmapDrawable _imageviewBD = (BitmapDrawable) _view.getDrawable();
            Bitmap _imageviewB = _imageviewBD.getBitmap();
            java.io.FileOutputStream _imageviewFOS = null;
            java.io.File _imageviewF = Environment.getExternalStorageDirectory();
            java.io.File _imageviewF2 = new java.io.File(_save_path);
            _imageviewF2.mkdirs();
            java.io.File _imageviewF3 = new java.io.File(_imageviewF2, _name);
            _imageviewFOS = new java.io.FileOutputStream(_imageviewF3);
            _imageviewB.compress(Bitmap.CompressFormat.PNG, (int) _quality, _imageviewFOS);
            _imageviewFOS.flush();
            _imageviewFOS.close();
            Intent _imageviewI = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            _imageviewI.setData(Uri.fromFile(_imageviewF)); sendBroadcast(_imageviewI);
        }catch(Exception e){
        }
        try {
            android.media.MediaScannerConnection.scanFile(ShowWallz.this,new String[]{new java.io.File(_save_path + _name).getPath()}, new String[]{"image/jpeg"}, null);
        } catch(Exception e) {}
        try {
            android.media.MediaScannerConnection.scanFile(ShowWallz.this,new String[]{new java.io.File(_save_path + _name).getPath()}, new String[]{"image/png"}, null);
        } catch (Exception e) {}


        Toast.makeText(this, "Path: Download/#"+getResources().getString(R.string.app_name)+"/", Toast.LENGTH_LONG).show();

    }


}