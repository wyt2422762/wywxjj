package com.fdkj.wywxjj.api.model.area;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 接口返回的区域信息实体类
 *
 * @author wyt
 */
@Data
@Accessors(chain = true)
public class Area {
    private String ID;
    private String Name;
    private String ParentId;
    private String ShortName;
    private String LevelType;
    private String CityCode;
    private String ZipCode;
    private String MergerCode;
    private String MergerName;
    private String Lng;
    private String Lat;
    private String Pinyin;
    private String FirstChar;
    private String shorthand;
    /**
     * 下级数据
     */
    private List<Area> children;

}
