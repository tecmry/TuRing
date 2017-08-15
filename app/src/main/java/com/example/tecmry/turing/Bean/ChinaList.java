package com.example.tecmry.turing.Bean;


/*
public class ChinaList extends Fragment {
    private MyDataBaseHelper db;
    //对于点击的类型消息的index
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY= 1 ;
    public static final int LEVEL_COUNTY= 2;
    //对控件进行初始化（定义）；
    private ProgressDialog progressDialog;
    private ListView listView;
    private TextView textView;
    private Button backbutton;
    private ArrayAdapter<String> adapter;
    private List<String> datalist=new ArrayList<String>();


用来存放省一级别的类目

    private List<Province>  provinceList;


用来存放市一级的类目

    private List<City> cityList;


用来存放区县一级的类目

    private List<County> countyList;

    private Province selectedprovince;

    private City selectedcity;

    private int currentlevels;

    private static MyDataBaseHelper dbhelper;



对所在的cityId，provinceID 起到一个标记的作用

    private int citycode;

    private int provincecode;

    private HttpURLConnection httpURLConnection;

    private URL url_cancel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.citylist,container,false);
        listView=(ListView)view.findViewById(R.id.list_view);
        textView=(TextView)view.findViewById(R.id.title_text);
        backbutton=(Button)view.findViewById(R.id.back_button);
        //获取Context的方法有争议 getActivity（）.getApplicationContext（）
        adapter=new ArrayAdapter<>(this.getContext(),android.R.layout.simple_list_item_1,datalist);
        listView.setAdapter(adapter);
        dbhelper = new MyDataBaseHelper(getActivity().getApplicationContext(),"Weather.db",null,3);
        adapter.notifyDataSetChanged();
        return  view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(currentlevels==LEVEL_PROVINCE){
                    selectedprovince = provinceList.get(position);
                    queryCities();
                }else if(currentlevels==LEVEL_CITY){
                    selectedcity = cityList.get(position);
                    queryCounty();
                }
            }
        });
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentlevels==LEVEL_COUNTY){
                    queryCities();
                }else if(currentlevels==LEVEL_CITY){
                    queryProvince();
                }
            }
        });
        queryProvince();
    }


        queryxxxx方法在获取数据时对缓存进行判断如果有的话从数据库中调用若没用从网上进行下载并存入数据库减少用户流量的使用



    private void queryProvince(){
        textView.setText("中国");
        backbutton.setVisibility(View.GONE);
        SQLiteDatabase db=dbhelper.getWritableDatabase();
        //对表进行遍历将province这一列放进datalist中；
        Cursor cursor=db.query("province",null,null,null,null,null,null);
        datalist.clear();
        if (cursor.moveToFirst()){
            do {
                datalist.add(cursor.getString(cursor.getColumnIndex("province")));
                Province province=new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvince(cursor.getString(cursor.getColumnIndex("province")));
                provinceList.add(province);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(datalist.size()>0){
            Log.d("MainActivity","成功读入省级城市");
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevels=LEVEL_PROVINCE;
        }else {
            URL url =null;
            try {
                url=new URL("http://guolin.tech/api/china");
                quryfromSever(url,"Province");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
    }
    private void queryCities(){
        textView.setText(selectedprovince.getProvince());
        backbutton.setVisibility(View.VISIBLE);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("city",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do {
               datalist.add(cursor.getString(cursor.getColumnIndex("city")));
                City city=new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCity(cursor.getString(cursor.getColumnIndex("city")));
                cityList.add(city);
            }while (cursor.moveToNext());
        }
        cursor.close();
        if(datalist.size()>0){
            Log.d("MainActivity","成功读入市级城市");
            adapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentlevels=LEVEL_CITY;
        }else {
            URL url= null;
            provincecode=selectedprovince.getId();
            try {
                url = new URL("http://guolin.tech/api/china/"+provincecode);
                quryfromSever(url,"City");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }
    private void queryCounty(){
        textView.setText(selectedcity.getCity());
        backbutton.setVisibility(View.VISIBLE);
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        Cursor cursor = db.query("county",null,null,null,null,null,null);
        if (cursor.moveToFirst()){
            do {
            datalist.add(cursor.getString(cursor.getColumnIndex("county")));
                County county = new County();
                county.setId(cursor.getInt(cursor.getColumnIndex("id")));
                county.setCounty(cursor.getString(cursor.getColumnIndex("county")));
                county.setWeathercode(cursor.getInt(cursor.getColumnIndex("weathercode")));
                countyList.add(county);
            }while (cursor.moveToNext());
        }
        if(datalist.size()>0){
            Log.d("MainActivity","成功录入区/县");
            listView.setSelection(0);
            currentlevels=LEVEL_COUNTY;
        }else {
            URL url= null;
            citycode = selectedcity.getId();
            try {
                url = new URL("http://guolin.tech/api/china/"+ provincecode + "/"+citycode);
                quryfromSever(url,"County");
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

   private static boolean handleProvice(String response){
        SQLiteDatabase db=null;
        if(!TextUtils.isEmpty(response)){
            try {
                db = dbhelper.getWritableDatabase();
                JSONArray jsonArray = new JSONArray(response);
                ContentValues contentValues=new ContentValues();
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject =jsonArray.getJSONObject(i);
                    contentValues.put("province",jsonObject.getString("name"));
                    contentValues.put("id",jsonObject.getInt("id"));
                    db.insert("province",null,contentValues);
                    contentValues.clear();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private void quryfromSever(URL url, final String type){
        url_cancel = url;
        System.out.println(url_cancel);
       new Thread(new Runnable() {
           @Override
           public void run() {
               try {
                   httpURLConnection=(HttpURLConnection)url_cancel.openConnection();
                   httpURLConnection.setRequestMethod("POST");
                  // httpURLConnection.setDoOutput(true);
                   httpURLConnection.setDoInput(true);
                   httpURLConnection.setReadTimeout(5000);
                   httpURLConnection.setConnectTimeout(5000);
                   httpURLConnection.setRequestProperty("Content-Type","application/json");
                   int requsecode = httpURLConnection.getResponseCode();
                   System.out.println(requsecode);
                   if (requsecode==200) {
                       InputStream inputStream = httpURLConnection.getInputStream();
                       String json = Utils.readString(inputStream);
                       System.out.println("vdkavhjdahvj" + json);
                       boolean result = false;
                       if ("Province".equals(type)) {
                           result = handleProvice(json);
                       } else if ("City".equals(type)) {
                           result = handleCity(type, selectedprovince.getId());
                       } else if ("County".equals(type)) {
                           result = handleCounty(type, selectedcity.getId());
                       }
                       if (result) {
                           getActivity().runOnUiThread(new Runnable() {
                               @Override
                               public void run() {
                                   closeProgressDialog();
                                   if ("Province".equals(type)) {
                                       queryProvince();
                                   } else if ("City".equals(type)) {
                                       queryCities();
                                   } else if ("County".equals(type)) {
                                       queryCounty();
                                   }
                               }
                           });
                       }
                   }else {
                       try {
                           throw new NetworkErrorException("response is"+requsecode);
                       } catch (NetworkErrorException e) {
                           e.printStackTrace();
                       }
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
       }).start();
    }


    @Override
    public void onFailure(Call call, IOException e){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                closeProgressDialog();
                Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void showProgressDialog(){
        if (progressDialog==null){
            progressDialog =new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载.....");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }
    private void closeProgressDialog(){
        if (progressDialog!=null){
            progressDialog.dismiss();
        }
    }
    private static boolean handleCity(String response,int provincecode){
        SQLiteDatabase db =null;
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray = new JSONArray(response);
                db=dbhelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                for(int i = 0 ;i<jsonArray.length();i++){
                    JSONObject jsonObject=jsonArray.getJSONObject(i);
                    contentValues.put("city",jsonObject.getString("name"));
                    contentValues.put("id",jsonObject.getString("id"));
                    contentValues.put("provincecode",provincecode);
                    db.insert("city",null,contentValues);
                    contentValues.clear();
                }
                return  true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    private static boolean handleCounty(String response,int citycode){
        SQLiteDatabase db = null;
        if(!TextUtils.isEmpty(response)){
            try {
                JSONArray jsonArray=new JSONArray(response);
                db=dbhelper.getWritableDatabase();
                ContentValues contentValues = new ContentValues();
                for(int i = 0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    contentValues.put("county",jsonObject.getString("name"));
                    contentValues.put("id",jsonObject.getInt("id"));
                    contentValues.put("citycode",citycode);
                    contentValues.put("weathercode",jsonObject.getString("weather_id"));
                    db.insert("county",null,contentValues);
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return  false;
    }

        }


*/
