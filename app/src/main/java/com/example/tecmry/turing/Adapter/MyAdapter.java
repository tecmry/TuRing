package com.example.tecmry.turing.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.tecmry.turing.Bean.ListData;
import com.example.tecmry.turing.R;
import com.example.tecmry.turing.View.CircleView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<ListData> mList;
    private Bitmap mBitmap;

    private int Hour=0;
    private int minute=0 ;
    /**
     * 用来存放每个Item发出去的Hour，Minute
     * **/
    private int  Hour_House[] = new int[99];
    private int Minute_House[] = new int[99];

    private static String path_left = "/sdcard/myCircle_left/";
    private static String path_right= "/sdcard/myCircle_right/";

    public interface OnItemClickListner{
        void OnItemClickListner(View view,int position);
    }

    private OnItemClickListner listner;
    public void setItemClickListner(OnItemClickListner listner){
        this.listner=listner;
    }

    public MyAdapter(List<ListData> list)
    {
        this.mList = list;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout right_layout;
        LinearLayout left_layout;
        TextView left_Msg;
        TextView right_Msg;
        LinearLayout time_layout;
        TextView time_tv;
        CircleView Cv_left;
        CircleView Cv_right;

        public ViewHolder(View itemView) {
            super(itemView);
            right_layout = (LinearLayout) itemView.findViewById(R.id.right_layout);
            left_layout = (LinearLayout) itemView.findViewById(R.id.left_layout);
            right_Msg = (TextView) itemView.findViewById(R.id.right_Msg);
            left_Msg = (TextView) itemView.findViewById(R.id.left_Msg);
            time_layout = (LinearLayout) itemView.findViewById(R.id.ll_time);
            time_tv = (TextView) itemView.findViewById(R.id.tv_time);
            Cv_left = (CircleView)itemView.findViewById(R.id.CV_Left);
            Cv_right = (CircleView)itemView.findViewById(R.id.CV_Right);
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ListData listData = mList.get(position);
        Bitmap bt = BitmapFactory.decodeFile(path_left + "head.jpg");// 从SD卡中找头像，转换成Bitmap
        Bitmap bitmap= BitmapFactory.decodeFile(path_right+"head.jpg");
      //  OutTime(Hour_House,Minute_House,position);
        if (bt != null) {
            @SuppressWarnings("deprecation")
            Drawable drawable = new BitmapDrawable(bt);// 转换成drawable
            holder.Cv_left.setImageDrawable(drawable);
        } else {

        }
        if (bitmap!=null){
            @SuppressWarnings("deprecation")
             Drawable drawable = new BitmapDrawable(bitmap);
            holder.Cv_right.setImageDrawable(drawable);
        }
        if (listData.getType() == ListData.SEND) {
            holder.right_layout.setVisibility(View.VISIBLE);
            holder.left_layout.setVisibility(View.GONE);
            holder.right_Msg.setText(listData.getContent());
            Calendar calendar = Calendar.getInstance();
            Hour = calendar.get(Calendar.HOUR);
            Hour_House[position] = Hour;
            minute = calendar.get(Calendar.MINUTE);
            Minute_House[position] = minute;

            if (position!=0) {
                if ((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]) <= 5) {
                    holder.time_layout.setVisibility(View.GONE);
                    holder.time_tv.setVisibility(View.GONE);
                    System.out.println((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]));
                } else {
                    holder.time_layout.setVisibility(View.VISIBLE);
                    holder.time_tv.setVisibility(View.VISIBLE);
                    holder.time_tv.setText(getTime());
                    System.out.println((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]));
                }
            }else if (position==0){
                holder.time_layout.setVisibility(View.VISIBLE);
                holder.time_tv.setVisibility(View.VISIBLE);
                holder.time_tv.setText(getTime());
            }
        } else if (listData.getType() == ListData.GET) {
            holder.right_layout.setVisibility(View.GONE);
            holder.left_layout.setVisibility(View.VISIBLE);
            holder.left_Msg.setText(listData.getContent());
            holder.time_tv.setText(getTime());
            Calendar calendar = Calendar.getInstance();
            Hour = calendar.get(Calendar.HOUR);
            Hour_House[position] = Hour;
            minute = calendar.get(Calendar.MINUTE);
            Minute_House[position] = minute;
            System.out.println("收到的"+position);if (position!=0) {
                if ((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]) <= 5) {
                    holder.time_layout.setVisibility(View.VISIBLE);
                    holder.time_tv.setVisibility(View.VISIBLE);
                    holder.time_tv.setText(getTime());
                    System.out.println((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]));
                    Log.d("TAG", "执行了方法");
                } else {
                    holder.time_layout.setVisibility(View.GONE);
                    holder.time_tv.setVisibility(View.GONE);
                    holder.time_tv.setText(getTime());
                    Log.d("TAG", "执行了");
                    System.out.println((Hour_House[position] * 60 + Minute_House[position]) - (Hour_House[position - 1] * 60 + Minute_House[position - 1]));
                }
            }else if (position==1){
                holder.time_layout.setVisibility(View.GONE);
                holder.time_tv.setVisibility(View.GONE);
                holder.time_tv.setText(getTime());
            }
        }
        if (listner!=null){
            holder.Cv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.OnItemClickListner(v,position);
                }
            });
            holder.Cv_left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.OnItemClickListner(v,position);

                }
            });
        }
    }

    public String getTime() {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日  HH:mm");
        Date date = new Date(System.currentTimeMillis());
        String date_ = format.format(date);
        return date_;
    }


}
