package com.loopeer.android.apps.startuptools.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by KorHsien on 2015/3/23.
 */
public class Product implements Serializable {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_CATEGORY_ID = "category_id";
    public static final String FIELD_SORT = "sort";
    public static final String FIELD_NAME = "name";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";
    public static final String FIELD_DESC = "desc";
    public static final String FIELD_LOGO = "logo";
    public static final String FIELD_SCREENSHOT = "screenshot";
    public static final String FIELD_WEBSITE = "website";
    public static final String FIELD_LINK = "link";


    @DatabaseField(columnName = FIELD_ID, dataType = DataType.INTEGER, id = true)
    public int id;

    @DatabaseField(columnName = FIELD_CATEGORY_ID, dataType = DataType.INTEGER)
    @SerializedName("category_id")
    public int categoryId;

    @DatabaseField(columnName = FIELD_SORT, dataType = DataType.INTEGER)
    public int sort;

    @DatabaseField(columnName = FIELD_NAME, dataType = DataType.STRING)
    public String name;

    @DatabaseField(columnName = FIELD_TITLE, dataType = DataType.STRING)
    public String title;

    @DatabaseField(columnName = FIELD_SUBTITLE, dataType = DataType.STRING)
    public String subtitle;

    @DatabaseField(columnName = FIELD_DESC, dataType = DataType.STRING)
    public String desc;

    @DatabaseField(columnName = FIELD_LOGO, dataType = DataType.STRING)
    @SerializedName("logo")
    public String logoUrl;

    @DatabaseField(columnName = FIELD_SCREENSHOT, dataType = DataType.STRING)
    @SerializedName("screenshot")
    public String screenshotUrl;

    @DatabaseField(columnName = FIELD_WEBSITE, dataType = DataType.STRING)
    @SerializedName("website")
    public String websiteUrl;

    @DatabaseField(columnName = FIELD_LINK, dataType = DataType.STRING)
    @SerializedName("android_link")
    public String linkUrl;

}
