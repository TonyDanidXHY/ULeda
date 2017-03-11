package ecnu.uleda;

import android.app.Activity;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * Created by Shensheng on 2017/2/9.
 * 用户配置信息类
 */

public class UserConfig {
    //TODO:该类遵循单例模式。用于在各大场合获取用户的配置信息，例如声音是否开启等。
    private static UserConfig sUserConfig = null;
    private boolean msoundIsOn;
    private String username;
    private String userpassword;
    private static final String USER_SETTINGS = "ueserSettings.json";
    private Context context;

    private UserConfig(Context context){
        this.context = context.getApplicationContext();
        String jsonData = Read();
        try{
            JSONObject j = new JSONObject(jsonData);
            msoundIsOn = j.getBoolean("soundIsOn");
            username = j.getString("username");
            userpassword = j.getString("userpassword");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getSavedUsernamePassword()
    {
        //TODO:读取用户密码
        return userpassword;
    }
    public String getSavedUsername()
    {
       return username;
    }
    public void setSavedUsernamePassword(String musername,String muserpassword)throws Exception
    {
        //TODO:保存用户名和密码
        username = musername;
        userpassword = muserpassword;
        saveConfigToFile();
    }

    public boolean soundIsOn(){
        //TODO:加入判断声音是否开启的代码

         return msoundIsOn;
    }

    public void setSoundOn(boolean set)throws Exception{
        //TODO:设置声音是否开启
        msoundIsOn = set;
        saveConfigToFile();
    }
    public String Read()
    {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try{
            in = context.openFileInput(USER_SETTINGS);
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line = reader.readLine()) != null)
            {
                content.append(line);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            if(reader != null)
            {
                try{
                    reader.close();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return content.toString();
    }
    public JSONObject toJSON() throws JSONException
    {
        JSONObject j = new JSONObject();
        j.put("soundIsOn",msoundIsOn);
        j.put("username",username);
        j.put("userpassword",userpassword);
        return j;
    }
    public void saveConfigToFile()throws FileNotFoundException{
        //TODO:将配置保存在文件中
        BufferedWriter w = null;
        try {
            FileOutputStream output = context.openFileOutput(USER_SETTINGS, Context.MODE_PRIVATE);
            w = new BufferedWriter(new OutputStreamWriter(output));
            JSONObject j = new JSONObject();
            j = toJSON();
            w.write(j.toString());
        }
        catch (Exception e)
        {
        e.printStackTrace();
        }
        finally {
            if(w != null)
            {
                try{
                    w.close();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

    }

    public static UserConfig getInstance(Context context){
        if(sUserConfig == null){
            sUserConfig  = new UserConfig(context);
        }
        return sUserConfig;
    }

}
