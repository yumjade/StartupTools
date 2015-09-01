package com.loopeer.android.apps.startuptools.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;

import java.io.Serializable;

/**
 * Created by KorHsien on 2015/3/24.
 */
public class Category implements Serializable {

    public static final String FIELD_ID = "_id";
    public static final String FIELD_SORT = "sort";
    public static final String FIELD_PARENT_ID = "parent_id";
    public static final String FIELD_TITLE = "title";
    public static final String FIELD_SUBTITLE = "subtitle";

    @DatabaseField(columnName = FIELD_ID, dataType = DataType.INTEGER, id = true)
    public int id;

    @DatabaseField(columnName = FIELD_PARENT_ID, dataType = DataType.INTEGER)
    @SerializedName("parent_id")
    public int parentId;

    @DatabaseField(columnName = FIELD_SORT, dataType = DataType.INTEGER)
    public int sort;

    @DatabaseField(columnName = FIELD_TITLE, dataType = DataType.STRING)
    public String title;

    @DatabaseField(columnName = FIELD_SUBTITLE, dataType = DataType.STRING)
    public String subtitle;

}
