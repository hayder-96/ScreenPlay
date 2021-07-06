package com.screenplay.myscreenplay.VollySinglistion;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import com.screenplay.myscreenplay.Model.User;

public class VollySing {

    private static final String SHARED_TOKEN="shared_token";
    private static final String NAME_SHARED="name_shared";
    private static final String EMAIL_SHARED="email_shared";
    private static final String KEY_ID="key_id";
    private static final String TOKEN_SHARED="token";


    private static VollySing vollySing_instanse;
    private static Context context;

    public VollySing(Context context) {
        this.context=context;
    }

    public static synchronized VollySing getInstanse(Context context){
        if (vollySing_instanse==null){
            vollySing_instanse=new VollySing(context);
        }

        return vollySing_instanse;
    }

    public void saveUser(User user){

        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_TOKEN,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString(NAME_SHARED,user.getName());
        editor.putString(EMAIL_SHARED,user.getEmail());
        editor.putInt(KEY_ID,user.getId());
        editor.putString(TOKEN_SHARED,user.getToken());
        Toast.makeText(context,"save token done",Toast.LENGTH_SHORT).show();
        editor.apply();
    }
    public boolean is_Login(){

        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_TOKEN,Context.MODE_PRIVATE);

        return sharedPreferences.getString(TOKEN_SHARED,null)!=null;
    }
    public User getToken(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_TOKEN,Context.MODE_PRIVATE);

        return new User(sharedPreferences.getString(TOKEN_SHARED,null));

    }
    public void user_Logout(){
        SharedPreferences sharedPreferences=context.getSharedPreferences(SHARED_TOKEN,Context.MODE_PRIVATE);

        Editor editor = sharedPreferences.edit();
        editor.clear();
         editor.apply();

    }
}
