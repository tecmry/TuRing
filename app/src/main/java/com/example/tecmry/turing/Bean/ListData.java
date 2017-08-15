package com.example.tecmry.turing.Bean;

public class ListData {
    public static final int SEND = 1;
    public static final int GET = 0;
    public  int Type;
    public String Content;
    public ListData(String Content,int Type){
       this.Content=Content;
        this.Type=Type;
    }
    public String getContent(){
        return Content;
    }
    public int getType(){
        return Type;
    }

}
