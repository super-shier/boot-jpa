package com.shier.common.boot.jpa.model;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.shier.common.boot.jpa.common.utils.StringUtil;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JpaAtlasModel implements Serializable {
    /**
     * 经纬度
     */
    private String ingLat;
    /**
     * ip
     */
    private String ip;
    /**
     * 所属省
     */
    private String province;
    /**
     * 省code
     */
    private Integer provinceCode;
    /**
     * 所属市
     */
    private String city;
    /**
     * 市code
     */
    private Integer cityCode;
    /**
     * 所属地区
     */
    private String area;
    /**
     * 地区code
     */
    private Integer areaCode;
    /**
     * 街道乡镇
     */
    private String streetName;
    /**
     * 街道乡镇code
     */
    private Integer streetCode;
    /**
     * 地址
     */
    private String address;

    public String getAddress() {
        if (StringUtil.isNotBlank(address)) {
            return address;
        } else if (StringUtil.isNotBlank(province) || StringUtil.isNotBlank(city) || StringUtil.isNotBlank(area)) {
            return province + city + area;
        }
        return "地址未知";
    }

    @Override
    public String toString() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ingLat", ingLat);
        jsonObject.put("ip", ip);
        jsonObject.put("province", province);
        jsonObject.put("provinceCode", provinceCode);
        jsonObject.put("city", city);
        jsonObject.put("cityCode", cityCode);
        jsonObject.put("area", area);
        jsonObject.put("areaCode", areaCode);
        jsonObject.put("streetName", streetName);
        jsonObject.put("streetCode", streetCode);
        jsonObject.put("address", address);
        return jsonObject.toString();
    }
}
