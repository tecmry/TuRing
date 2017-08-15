package com.example.tecmry.turing.Bean;

/**
 * Created by Tecmry on 2017/6/1.
 */

public class PsChat {
    private PsInfo psInfo;
    public void setPsInfo(PsInfo psInfo){
        this.psInfo= psInfo;
    }
    public PsInfo getPsInfo(){
        return psInfo;
    }
   public static class PsInfo{
       /**
        * key : APIKEY
        * city : 48268418
        * info : 今天天气怎么
        * loc : 北京市中关村
        *  userid :  123456
        */

       private String key;
       private String info;
       private String userid;
       private String loc;



       public String getKey() {
           return key;
       }

       public void setKey(String key) {
           this.key = key;
       }

       public String getInfo() {
           return info;
       }

       public void setInfo(String info) {
           this.info = info;
       }

       public String getLoc() {
           return loc;
       }

       public void setLoc(String loc) {
           this.loc = loc;
       }


       public String getUserid() {
           return userid;
       }

       public void setUserid(String userid) {
           this.userid = userid;
       }
   }
}
