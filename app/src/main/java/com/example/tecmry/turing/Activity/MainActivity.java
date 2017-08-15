package com.example.tecmry.turing.Activity;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVUser;
import com.example.tecmry.turing.Fragment.Talk;
import com.example.tecmry.turing.Fragment.Weather;
import com.example.tecmry.turing.R;
import com.example.tecmry.turing.View.CircleView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import static android.widget.Toast.LENGTH_SHORT;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {


    private Button mButton;
    private DrawerLayout mDrawesrLayout;
    private CircleView circleView;
    private Uri imageuri;
    private static final int take_photo= 1;
    private static final int core_photo=2;
    private static final int choose_photo=3;
    private NavigationView navigationView;
    private Uri uri_one;
    private Uri uri_two;
    private Uri uri_three;
    private String mCurrentPhotoPath;
    private static final File USER_ICON = new File(Environment.getExternalStorageDirectory(), "user_icon_MRY.jpg");
    private static final String filename= "head.jpg";
    private  String content;
    private Button getmButton;
    private Toolbar toolbar;
    private TextView userName;
    private TextView userEmail;
    private static String path = "/sdcard/myHead/";
   // private MyDataBaseHelper dbhelper;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
         setContentView(R.layout.activity_main);
        mButton = (Button)findViewById(R.id.send);
       // dbhelper=new MyDataBaseHelper(this,"Weather.db",null,2);
        //dbhelper.getWritableDatabase();
       toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("Turing");
        //谨记对导航栏滑出以及对Navigation的操作需要在setSupportActionBar(toolbar)后进行；
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationIcon(R.drawable.category);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawesrLayout.openDrawer(GravityCompat.START);
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()){
                case R.id.search:
                    break;
                case R.id.tv_city_name:
                    break;
            }
                return true;
            }
        });

       // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        mDrawesrLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
       navigationView =(NavigationView)findViewById(R.id.nav_view);
        View headerview= navigationView.inflateHeaderView(R.layout.nav_header);
        userEmail= (TextView) headerview.findViewById(R.id.mail);
        userName = (TextView)headerview.findViewById(R.id.username);
        if(AVUser.getCurrentUser().getEmail()==null){
            Toast.makeText(MainActivity.this,"注册的时候没填入邮箱哦",Toast.LENGTH_SHORT).show();
            userEmail.setText("tecmry6@gmail.com");
        }else {
            userEmail.setText(AVUser.getCurrentUser().getUsername());
        }

        userName.setText(AVUser.getCurrentUser().getUsername());
        circleView = (CircleView)headerview.findViewById(R.id.icon_image);
        Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            circleView.setImageDrawable(drawable);
        } else {
            /**
             * 如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD中
             *
             */
        }

        circleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             showDialog();
            }
        });

        ActionBar actionBar =getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.category);
        }

       // Avos();
    }



    @Override
    protected void onStop() {
        System.out.println("我进入stop进程了");
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.toolbar,menu);
            MenuItem searchItem = menu.findItem(R.id.search);
            SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
            searchView.setQueryHint("输入城市");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    content = query;
                    setCity(content);
                    System.out.println(content);
                    try {
                        getSupportFragmentManager().beginTransaction().replace(R.id.FL,new Weather()).commit();

                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });



            return true;
        }
    @Override
    /*
    *用来设置不同功能的Fragment在Toolbar上所需要的按钮
    *
    */
    public boolean onPrepareOptionsMenu(final Menu menu) {
        menu.findItem(R.id.search).setVisible(false);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.talk:
                        getSupportFragmentManager().beginTransaction().replace(R.id.FL,new Talk()).commit();
                        toolbar.setTitle("聊天");
                        menu.findItem(R.id.search).setVisible(false);
                        mDrawesrLayout.closeDrawers();
                        break;
                    case R.id.weather:
                     /*
                       这个页面修改toolbar的按钮；
                     */
                        toolbar.setTitle("天气");
                        try {
                            getSupportFragmentManager().beginTransaction().replace(R.id.FL,new Weather()).commit();
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        }
                        menu.findItem(R.id.search).setVisible(true);
                        mDrawesrLayout.closeDrawers();
                        break;
                    case R.id.local:
                        toolbar.setTitle("地点");
                        menu.findItem(R.id.search).setVisible(true);
                        break;
                    case R.id.out:
                        //这里进行系统退出操作；
                        Toast.makeText(MainActivity.this,"就这样恨心离开了????", LENGTH_SHORT).show();
                        AVUser.getCurrentUser().logOut();
                        startActivity(new Intent(MainActivity.this,Enter.class));
                        /**
                         * 将Activity杀死防止返回得到
                         */
                        finish();
                        break;

                }
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }

    public String getCity(){
            return  content;
        }
        public void setCity(String city){
            content= city;
        }



    /*
    public void Avos(){
        AVOSCloud.initialize(this,"lJGXtjDRUqFmsY7Kd4l2IkRC-gzGzoHsz","qfgqUbD3jW2eCfv1U8mdgV05");
    }
    */
    @Override
    public void onClick(View v) {
        //if(v.getId()==R.id.send){
         //   PostData();
      //  }
    }


    public void showDialog(){
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("更换头像")
                .setMessage("请选择一种方式更换头像")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPhoto();
                    }
                })
                .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPhoto();
                    }
                }).create();
        dialog.show();
    }
    public void setPhoto() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (isSdCardExiting()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUrl());
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, take_photo);
            } else {
                Toast.makeText(MainActivity.this, "可能没有插入SD卡", Toast.LENGTH_LONG).show();
            }
        }else {
            new AlertDialog.Builder(this)
                    .setMessage("申请相机权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.CAMERA}, take_photo);
                        }
                    }).show();
        }
    }

    public void getPhoto(){
        Intent intent = new Intent();
        // 获取本地相册方法一
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        //获取本地相册方法二
        //        intent.setAction(Intent.ACTION_PICK);
        //        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
        //                "image/*");
        startActivityForResult(intent, core_photo);
    }
    private void photoClip(Uri uri) {
        // 调用系统中自带的图片剪裁
        Intent intent = new Intent();
        intent.setAction("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, choose_photo);
    }
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo);
            circleView.setImageBitmap(photo);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(MainActivity.this, "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case take_photo:
                if (isSdCardExiting()) {
                    File filetemp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    photoClip(Uri.fromFile(filetemp));
                }else {
                    Toast.makeText(MainActivity.this,"没找到SD卡，无法储存照片",Toast.LENGTH_LONG).show();
                }
                break;
            case core_photo:
                if (data != null) {
                    photoClip(data.getData());
                }
                break;
            case choose_photo:
                if (data != null) {
                    setImageToHeadView(data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public boolean isSdCardExiting(){
        final String state= Environment.getExternalStorageState();
        if(state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }
    private Uri getUrl(){
        return Uri.fromFile(new File(Environment.getExternalStorageDirectory(),filename));
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == take_photo) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(MainActivity.this,"已申请权限",Toast.LENGTH_LONG).show();
            } else {
                /**
                 * 用户勾选了不在询问
                 * 提示用户手动打开权限
                 */
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                    Toast.makeText(this, "相机权限已经禁止", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void saveBitmap(Bitmap bitmap){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
        File file = new File(path);
        file.mkdirs();// 创建文件夹
        String fileName = path + "head.jpg";// 图片名字
        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭流
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}




