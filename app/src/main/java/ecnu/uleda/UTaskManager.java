package ecnu.uleda;

import android.content.Context;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.tencent.mapsdk.raster.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Shensheng on 2017/1/19.
 * 任务管理者
 * 管理列表和地图上的任务
 */

public class UTaskManager {
    /*
    * 该类遵循单例模式。
    * 持有两个列表，一个表示任务列表中显示的任务
    * 一个表示地图上显示的任务
    * */
    private static UTaskManager sInstance=null;
    private UserOperatorController mUOC;
    private ArrayList<UTask> mTasksInList;
    private ArrayList<UTask> mTasksInMap;
    public static final String TIME_LAST="time";
    public static final String PRICE_DES="priceDes";
    public static final String PRICE_ASC="priceAsc";
    public static final String DISTANCE="distance";
    private String mSortBy=PRICE_DES;
    private String mTag="全部";
    private String mLocation="31.2296,121.403";
    public static UTaskManager getInstance(){
        if(sInstance==null){
            sInstance=new UTaskManager();
        }
        return sInstance;
    }
    public void setLocation(String loc){
        mLocation=loc;
    }
    public void setSortBy(String sortBy){
        mSortBy=sortBy;
    }
    public void setTag(String tag){
        mTag=tag;
    }
    public ArrayList<UTask> getTasksInList(){
        return mTasksInList;
    }

    private UTaskManager(){
        mTasksInList=new ArrayList<>();
        mTasksInMap=new ArrayList<>();
    }
    public ListAdapter setListView(ListView listView, Context context){
        //将一个ListView的内容设置为我们的任务
        ListAdapter la=new TaskListAdapter(context,mTasksInList);
        listView.setAdapter(la);
        return la;
    }
    public void refreshTaskInList()throws UServerAccessException{
        /*
        * TODO:访问服务器，更新任务列表。
        * */
        mUOC=UserOperatorController.getInstance();
        if(!mUOC.getIsLogined()){
            throw new UServerAccessException(UServerAccessException.UN_LOGIN);
        }else{
            try {
                JSONArray array = ServerAccessApi.getTaskList(
                        mUOC.getId(),
                        mUOC.getPassport(),
                        mSortBy,
                        "0",
                        "10",
                        mTag,
                        mLocation);
                mTasksInList.clear();
                int length=array.length();
                for(int i=0;i<length;i++){
                    JSONObject j=array.getJSONObject(i);
                    UTask task=new UTask()
                            .setPath( j.getString("path") )
                            .setTitle( j.getString("title") )
                            .setTag( j.getString("tag") )
                            .setPostDate(j.getLong("postdate"))
                            .setPrice(new BigDecimal(j.getString("price")))
                            .setAuthorID(j.getInt("author"))
                            .setAuthorUserName(j.getString("authorUsername"))
                            .setAuthorCredit(j.getInt("authorCredit"))
                            .setPostID(j.getString("postID"))
                            .setActiveTime(j.getLong("activetime"));
                    mTasksInList.add(task);
                }
            }catch (JSONException e){
                e.printStackTrace();
                System.exit(1);
            }catch (UServerAccessException e){
                if(e.getStatus()==416){
                    e.printStackTrace();
                    mTasksInList.clear();
                }else{
                    throw e;
                }
            }
        }
    }
    public void loadMoreTaskInList(int n)throws UServerAccessException{
        //TODO：从目前任务列表的最后一项开始向后从服务器获取n个任务项
        mUOC=UserOperatorController.getInstance();
        String start=mTasksInList.get(mTasksInList.size()-1).getPostID();
        if(!mUOC.getIsLogined()){
            throw new UServerAccessException(UServerAccessException.UN_LOGIN);
        }else{
            try {
                JSONArray array = ServerAccessApi.getTaskList(
                        mUOC.getId(),
                        mUOC.getPassport(),
                        mSortBy,
                        start,
                        String.valueOf(n),
                        mTag,
                        mLocation);
                int length=array.length();
                for(int i=0;i<length;i++){
                    JSONObject j=array.getJSONObject(i);
                    UTask task=new UTask()
                            .setPath( j.getString("path") )
                            .setTitle( j.getString("title") )
                            .setTag( j.getString("tag") )
                            .setPostDate(j.getLong("postdate"))
                            .setPrice(new BigDecimal(j.getString("price")))
                            .setAuthorID(j.getInt("author"))
                            .setAuthorUserName(j.getString("authorUsername"))
                            .setAuthorCredit(j.getInt("authorCredit"))
                            .setPostID(j.getString("postID"))
                            .setActiveTime(j.getLong("activetime"));
                    mTasksInList.add(task);
                }
            }catch (JSONException e){
                e.printStackTrace();
                System.exit(1);
            }
        }
    }
    public void init()throws UServerAccessException{
        //TODO:初始化任务列表
        refreshTaskInList();
        refreshTasksInMap();
    }

    public void refreshTasksInMap()throws UServerAccessException{
        //TODO:重新从服务器获取显示在地图上的任务

    }
    public ArrayList<UTask> getTasksInMap(){
        return mTasksInMap;
    }
}
