package com.example.tecmry.turing.Fragment;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tecmry.turing.Adapter.MyAdapter;
import com.example.tecmry.turing.Bean.ListData;
import com.example.tecmry.turing.Bean.PsChat;
import com.example.tecmry.turing.Bean.RsChat;
import com.example.tecmry.turing.Bean.TuringChat;
import com.example.tecmry.turing.R;
import com.example.tecmry.turing.View.CircleView;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_CANCELED;


public class Talk extends Fragment implements View.OnClickListener {
  private HttpURLConnection httpURLConnection;
    private String str;
    private RecyclerView recyclerView;
    private Button button;
    private  EditText editText;
    private MyAdapter.ViewHolder viewHolder;
    private CircleView CV_right;
    private CircleView Cv_left;
    private static final int take_photo = 1;
    private static final int choose_photo = 2;
    private static final int core_photo= 3;
    private static final int right = 1;
    private static final int left = 2;
    private int  code;
    private List<ListData> list=new ArrayList<>();
    private MyAdapter myAdapter;
    private static final String filename= "head.jpg";
    private static String path_left = "/sdcard/myCircle_left/";
    private static String path_right = "/sdcard/myCircle_right/";
    private static String image_filename;
    private static String TAG = "TAG";
    /**
     * 对接口进行定义
      */
    private static TuringChat turingChat;
    /**
     * key : APIKEY
     * info : 今天天气怎么
     * loc : 北京市中关村
     *  userid :  123456
     */

    private String key;
    private String info;
    private String loc;
    private String userid;

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
      View view=inflater.inflate(R.layout.talk_fr,container,false);
       recyclerView=(RecyclerView)view.findViewById(R.id.Rv);
        LinearLayoutManager linearlaoutmanager = new LinearLayoutManager(this.getContext());
        editText=(EditText)view.findViewById(R.id.edittext);
        button=(Button)view.findViewById(R.id.send);
        myAdapter = new MyAdapter(list);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(linearlaoutmanager);
        button.setOnClickListener(this);
        myAdapter.setItemClickListner(new MyAdapter.OnItemClickListner() {
            @Override
            public void OnItemClickListner(View view, int position) {
                /**
                 * 在RecyclerView外部获取控件的方法（坑);
                 * */
                Cv_left = (CircleView) view.findViewById(R.id.CV_Left);
                CV_right = (CircleView)view.findViewById(R.id.CV_Right);

                switch(view.getId()){
                    case R.id.CV_Left:
                       Toast.makeText(getContext(),"你点击了左边的头像",Toast.LENGTH_SHORT).show();
                        /**
                         * 在对于选择聊天的发送方和接收方时的保存图片不同路径时采用 code来进行分别
                         * */
                       code= left;
                      showDialog();
                        break;
                    case R.id.CV_Right:
                        Toast.makeText(getContext(),"你点击了右边的头像",Toast.LENGTH_SHORT).show();
                        code = right;
                        showDialog();
                        break;
                }
            }
        });
       //做测试使用 ChatMessagePost();
        return view;
    }
//用最早的方法进行网络的获取
/**
    public void GetData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    str=editText.getText().toString();
                    ListData listData =new ListData(str,ListData.SEND);
                    list.add(listData);
                    URL url = new URL("http://www.tuling123.com/openapi/api");
                    JSONObject jsonObject= new JSONObject();
                    jsonObject.put("key","8cdf60aa6f8c45289e4f1b72a2fe7fc5");
                    jsonObject.put("info",str);
                    jsonObject.put("userid","132456");
                    String string=String.valueOf(jsonObject);
                    httpURLConnection = (HttpURLConnection)url.openConnection();
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setRequestProperty("Content-Type","application/json");
                    OutputStream outputStream =httpURLConnection.getOutputStream();
                    outputStream.write(string.getBytes());
                    int code = httpURLConnection.getResponseCode();
                    System.out.println("返回码"+code);
                    if(code==200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        String json = Utils.readString(inputStream);
                        JSONObject jsonObject1 = new JSONObject(json);
                        ListData listData1 =new ListData(jsonObject1.getString("text"),ListData.GET);
                        list.add(listData1);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            myAdapter.notifyItemInserted(list.size()-1);
                            recyclerView.scrollToPosition(list.size()-1);
                            editText.setText("");
                        }
                    });
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
 */

    public void showDialog(){
        Dialog dialog =new AlertDialog.Builder(getContext())
                .setTitle("更换头像")
                .setMessage("选择一种方式更换头像")
                .setPositiveButton("拍照", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setPhoto();
                    }
                }) .setNegativeButton("相册", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPhoto();
                    }
                }).create();
        dialog.show();
    }
    public void setPhoto() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            if (isSdCardExiting()) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getUrl());
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
                startActivityForResult(intent, take_photo);
            } else {
                Toast.makeText(getContext(), "可能没有插入SD卡", Toast.LENGTH_LONG).show();
            }
        }else {
            new AlertDialog.Builder(getContext())
                    .setMessage("申请相机权限")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.CAMERA}, take_photo);
                        }
                    }).show();
        }
    }
    public void getPhoto()
    {
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
        intent.putExtra("outputX", 100);
        intent.putExtra("outputY", 100);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, choose_photo);
    }
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            saveBitmap(photo);
            /**
             * 这样不能获取到CircleView实例；
             * */
            //Can't speak something find something
       if (code==left){
           Cv_left.setImageBitmap(photo);
           //Log.d(TAG,photo.toString());
       }else if (code==right){
           CV_right.setImageBitmap(photo);
           //Log.d(TAG,photo.toString());
       }
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getContext(), "取消", Toast.LENGTH_LONG).show();
            return;
        }
        switch (requestCode) {
            case take_photo:
                if (isSdCardExiting()) {
                    File filetemp = new File(Environment.getExternalStorageDirectory() + "/head.jpg");
                    photoClip(Uri.fromFile(filetemp));
                }else {
                    Toast.makeText(getContext(),"没找到SD卡，无法储存照片",Toast.LENGTH_LONG).show();
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
                Toast.makeText(getContext(),"已申请权限",Toast.LENGTH_LONG).show();
            } else {
                /**
                 * 用户勾选了不在询问
                 * 提示用户手动打开权限
                 */
                if (!ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                    Toast.makeText(getContext(), "相机权限已经禁止", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
    private void saveBitmap(Bitmap bitmap){
        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED))
        { // 检测sd是否可用
            return;
        }
        FileOutputStream b = null;
       if (code==left)
       {
           File file_left = new File(path_left);
           file_left.mkdirs();// 创建文件夹
          image_filename = path_left + "head.jpg";// 图片名字
       }else
       {
           File file_right = new File(path_right);
           file_right.mkdirs();
           image_filename = path_right + "head.jpg";
       }
        try
        {
            b = new FileOutputStream(image_filename);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.send:
                //GetData();
                /*
                *采用Retrofit进行请求
                * 从代码长度上来说差不多，减少了对json数据重复的操作
                *
                 *  */
                ChatMessagePost();
                break;
        }
    }

        public void ChatMessagePost(){
         new Thread(new Runnable() {
             @Override
             public void run() {
                 str=editText.getText().toString();
                 ListData listData =new ListData(str,ListData.SEND);
                 list.add(listData);
                 Retrofit retrofit = new Retrofit.Builder()
                         .baseUrl("http://www.tuling123.com/")
                         .addConverterFactory(GsonConverterFactory.create())
                         .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                         .build();
                 PsChat.PsInfo info = new PsChat.PsInfo();
                 info.setKey("8cdf60aa6f8c45289e4f1b72a2fe7fc5");
                 info.setInfo(str);
                 info.setUserid("123456");
                 Gson gson = new Gson();
                 String postmessage= gson.toJson(info);
                 System.out.println(postmessage);
                 final RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),postmessage);
                 TuringChat turingChat =retrofit.create(TuringChat.class);
                 turingChat.savedMessage(requestBody)
                         .subscribeOn(Schedulers.newThread())
                         .observeOn(Schedulers.io())
                         .doOnNext(new Action1<RsChat>() {
                             @Override
                             public void call(RsChat rsChat) {
                                 Log.d("TAG","Don't do things in onNext");
                             }
                         })
                         .observeOn(AndroidSchedulers.mainThread())
                         .subscribe(new Subscriber<RsChat>() {
                             @Override
                             public void onCompleted() {
                                 //执行完onNext方法
                             }

                             @Override
                             public void onError(Throwable e) {
                                //执行出现了错误
                                 Log.d(TAG,e.toString());
                             }

                             @Override
                             public void onNext(RsChat rsChat) {
                                 //执行成功之后
                                 if (rsChat.getCode()==100000){
                                     System.out.println("返回的结果是"+rsChat.getText());
                                     Log.d("onResponse",rsChat.getText());
                                     ListData listData1 =new ListData(rsChat.getText(),ListData.GET);
                                     list.add(listData1);

                                 }else {
                                     Toast.makeText(getContext(),"网络连接可能出现了一些问题"+"返回码为："
                                             +rsChat.getCode(),Toast.LENGTH_SHORT).show();
                                 }
                                 getActivity().runOnUiThread(new Runnable() {
                                     @Override
                                     public void run() {
                                         myAdapter.notifyItemInserted(list.size()-1);
                                         recyclerView.scrollToPosition(list.size()-1);
                                         editText.setText("");
                                     }
                                 });
                             }
                         });
                 /**
                 Call<RsChat> call =turingChat.savedMessage(requestBody);
                call.enqueue(new Callback<RsChat>() {
                    @Override
                    public void onResponse(Call<RsChat> call, Response<RsChat> response) {
                        if (response.body().getCode()==100000){
                            System.out.println("返回的结果是"+response.body().getText());
                            Log.d("onResponse",response.body().getText());
                            ListData listData1 =new ListData(response.body().getText(),ListData.GET);
                            list.add(listData1);
                        }else {
                            Toast.makeText(getContext(),"网络连接可能出现了一些问题"+"返回码为："
                                    +response.body().getCode(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<RsChat> call, Throwable t) {

                    }
                });
                 getActivity().runOnUiThread(new Runnable() {
                     @Override
                     public void run() {
                         myAdapter.notifyItemInserted(list.size()-1);
                         recyclerView.scrollToPosition(list.size()-1);
                         editText.setText("");
                     }
                 });
                  **/
             }
         }).start();

        }

}

