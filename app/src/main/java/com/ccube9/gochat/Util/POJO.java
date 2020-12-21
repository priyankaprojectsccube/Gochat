package com.ccube9.gochat.Util;

import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class POJO implements Serializable {

    String id;
    String followerid;
    String categoryname;
    String subcategoryname;
    String challengeid;
    String mainchallengeid;
    String usertype;
    String title;
    String challengetype;
    String image;
    String Description;
    String Location;
    String Latitude;
    String Longitude;
    String Date;
    String Challangesfor;
    String Associationname;
    String Detailofassociation;
    String Username;
    String email;
    String mobileno;
    String publishto;
    String favourite;
    String subscribecount;
    String Userprofpic;
    String  websitelink ;
    String  ibanstr ;
    String swiftcode ;

    public String getUserprofpic() {
        return Userprofpic;
    }

    public void setUserprofpic(String userprofpic) {
        Userprofpic = userprofpic;
    }

    public String getFavourite() {
        return favourite;
    }

    public void setFavourite(String favourite) {
        this.favourite = favourite;
    }

    public String getSubscribecount() {
        return subscribecount;
    }

    public void setSubcribeCount(String subscribecount) {
        this.subscribecount = subscribecount;
    }
    public String getPublishto() {
        return publishto;
    }

    public void setPublishto(String publishto) {
        this.publishto = publishto;
    }

    ArrayList<String> images;
    ArrayList<String> subcribeImages;
    ArrayList<String>  imagesid;
    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getMainchallengeid() {
        return mainchallengeid;
    }

    public void setMainchallengeid(String mainchallengeid) {
        this.mainchallengeid = mainchallengeid;
    }

    public String getEmail() {
        return email;
    }



    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }


    public ArrayList<String> getSubcribeImages() {
        return subcribeImages;
    }

    public void setSubcribeImages(ArrayList<String> subcribeImages) {
        this.subcribeImages = subcribeImages;
    }
    public ArrayList<String> getImagesId() {
        return imagesid;
    }

public void setImagesId(ArrayList<String> imgarrid) {
        this.imagesid = imgarrid;
}


    public String getChallengeid() {
        return challengeid;
    }

    public void setChallengeid(String challengeid) {
        this.challengeid = challengeid;
    }

    public String getChallangesfor() {
        return Challangesfor;
    }

    public void setChallangesfor(String challangesfor) {
        Challangesfor = challangesfor;
    }

    public String getAssociationname() {
        return Associationname;
    }

    public void setAssociationname(String associationname) {
        Associationname = associationname;
    }

    public String getDetailofassociation() {
        return Detailofassociation;
    }

    public void setDetailofassociation(String detailofassociation) {
        Detailofassociation = detailofassociation;
    }


    public String getWebsitelink() {
        return websitelink;
    }

    public void setWebsitelink(String website_link) {
        websitelink = website_link;
    }

    public String getIban() {
        return ibanstr;
    }

    public void setIban(String iban) {
        ibanstr = iban;
    }

    public String getSwiftcode() {
        return swiftcode;
    }

    public void setSWiftcode(String swift_code) {
        swiftcode = swift_code;
    }
    public String getChallengetype() {
        return challengetype;
    }

    public void setChallengetype(String challengetype) {
        this.challengetype = challengetype;
    }
    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }

    public String getSubcategoryname() {
        return subcategoryname;
    }
    public void setSubcategoryname(String subcategoryname) {
        this.subcategoryname = subcategoryname;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public  void setFollowerId(String followerid){
        this.followerid = followerid;
    }

    public String  getFollowerID(){
        return followerid;
    }
    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }



}
