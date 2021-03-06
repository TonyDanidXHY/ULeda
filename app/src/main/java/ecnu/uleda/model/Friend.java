package ecnu.uleda.model;

import java.util.concurrent.RecursiveTask;

/**
 * Created by zhaoning on 2017/4/13.
 */

public class Friend {

    private String userId;
    private String userName;
    private String imageUrl;



    private String userTag;
    private int imageId;



    private String signature;


    public Friend(){
    }
    public Friend(String userid, String name,String userTag) {//TODO:头像的问题等会再说
        this.userId = userid;
        this.userName = name;
        this.imageUrl = imageUrl;
        this.userTag = userTag;
    }

    public String getUserId()
    {
        return userId;
    }

    public String getUserName() {
        return userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getUserTag() {
        return userTag;
    }

    public int getImageId() {
        return imageId;
    }

    public Friend setUserId(String userId) {
        this.userId = userId;
        return this;
    }

    public Friend setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public Friend setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public Friend setUserTag(String userTag) {
        this.userTag = userTag;
        return this;
    }
}