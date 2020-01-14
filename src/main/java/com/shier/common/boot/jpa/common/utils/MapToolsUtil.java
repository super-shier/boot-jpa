package com.shier.common.boot.jpa.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.shier.common.boot.jpa.model.JpaAtlasModel;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MapToolsUtil {
    private static Logger logger = LoggerFactory.getLogger(MapToolsUtil.class);
    //地球半径,单位千米
    private static final double EARTH_RADIUS = 6378.137;
    //百度地图key
    private static final String BAIDU_MAP_AK = "3E26FF499dcdfe2c9e5057f126f7274a";
    //高德地图key
    private static final String AMAP_AK = "678b972457f9ca83d111aecc9a31d0da";

    /**
     * 根据经纬度获取所在城市
     *
     * @param ingLat
     * @return
     */
    public static JpaAtlasModel getAddress(String ingLat) {
        if (Objects.isNull(ingLat)) return null;
        ingLat = ingLat.replace(" ", "");
        if (!ingLat.contains(",") || 2 != ingLat.split(",").length) return null;
        String longitude = ingLat.split(",")[0].trim();
        String latitude = ingLat.split(",")[1].trim();
        String province, city, cityCode, district;
        if (!isNumeric(longitude) || !isNumeric(latitude)) return null;
        String location = latitude + "," + longitude;
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak=" + BAIDU_MAP_AK + "&pois=1&location=" + location;
        Map<String, String> params = new HashMap<>();
        try {
            String name = HttpUtil.postForm(url, params);
            com.alibaba.fastjson.JSONObject names = (com.alibaba.fastjson.JSONObject) com.alibaba.fastjson.JSONObject.parse(name);
            com.alibaba.fastjson.JSONObject results = names.getJSONObject("result");
            cityCode = results.getString("cityCode");
            if (Objects.equals(cityCode, "0")) {
                return null;
            }
            com.alibaba.fastjson.JSONObject addressComponent = results.getJSONObject("addressComponent");
            province = addressComponent.getString("province");
            city = addressComponent.getString("city");
            district = addressComponent.getString("district");
            JpaAtlasModel atlasModel = new JpaAtlasModel();
            atlasModel.setIngLat(ingLat);
            String address;
            if (results.containsKey("formatted_address")) {
                address = results.getString("formatted_address");
            } else {
                address = province + city + district;
            }
            atlasModel.setAddress(address);
            if (StringUtil.isNotBlank(province)) atlasModel.setProvince(province);
            if (StringUtil.isNotBlank(city)) atlasModel.setCity(city);
            if (StringUtil.isNotBlank(district)) atlasModel.setArea(district);
            return atlasModel;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 高德地图api
     *
     * @param ingLat 经纬度
     * @return 地址详细
     */
    public static JpaAtlasModel getAddressByGaoDe(String ingLat) {
        if (StringUtil.isBlank(ingLat)) return null;
        String longitude = ingLat.split(",")[0].trim();
        String latitude = ingLat.split(",")[1].trim();
        if (!isNumeric(longitude) || !isNumeric(latitude)) {
            return null;
        }

        double latLon[] = GPSUtil.bd09_To_gps84(Double.valueOf(longitude), Double.valueOf(latitude));
        String location = latLon[0] + "," + latLon[1];
        String url = "https://restapi.amap.com/v3/geocode/regeo?location=" + location + "&output=json&extensions=base&key=" + AMAP_AK;
        com.alibaba.fastjson.JSONObject addressMsg = null;
        try {
            addressMsg = com.alibaba.fastjson.JSONObject.parseObject(HttpUtil.get(url));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Objects.isNull(addressMsg)) return null;
        if (!addressMsg.containsKey("status") || !Objects.equals(addressMsg.getInteger("status"), 1)) {
            System.out.println("address:" + addressMsg.toJSONString());
            return null;
        }
        com.alibaba.fastjson.JSONObject regeocode = addressMsg.getJSONObject("regeocode");
        String address = regeocode.getString("formatted_address");
        com.alibaba.fastjson.JSONObject addressComponent = regeocode.getJSONObject("addressComponent");
        String province = addressComponent.getString("province");
        String city = addressComponent.getString("city");
        String district = addressComponent.getString("district");
        String township = addressComponent.getString("township");
        JpaAtlasModel atlasModel = new JpaAtlasModel();
        atlasModel.setIngLat(ingLat);
        atlasModel.setAddress(address);
        if (StringUtil.isNotBlank(province) && !Objects.equals(province, "[]")) {
            atlasModel.setProvince(province);
        }
        if (StringUtil.isNotBlank(city) && !Objects.equals(city, "[]")) {
            atlasModel.setCity(city);
        }
        if (StringUtil.isNotBlank(district) && !Objects.equals(district, "[]")) {
            atlasModel.setArea(district);
        }
        if (StringUtil.isNotBlank(township) && !Objects.equals(township, "[]")) {
            atlasModel.setStreetName(township);
        }
        if (StringUtil.isBlank(atlasModel.getProvince()) && StringUtil.isBlank(atlasModel.getCity())
                && StringUtil.isBlank(atlasModel.getArea())) {
            return null;
        }
        if (Objects.equals(atlasModel.getProvince(), atlasModel.getCity()) &&
                Objects.equals(atlasModel.getCity(), atlasModel.getStreetName())) {
            return null;
        }
        return atlasModel;
    }

    //经纬度验证
    private static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]+.*[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static String getLngLatByAddress(String address) {
        String url = "http://api.map.baidu.com/geocoder/v2/?" + "&address=" + address + "&output=json&ak=" + BAIDU_MAP_AK + "&pois=1";
        String qr = null;
        try {
            qr = lng_lat_get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(qr);
        try {
            JSONObject result = jsonObject.getJSONObject("result");
            JSONObject locationObject = result.getJSONObject("location");
            Double lng = locationObject.getDouble("lng");
            String lat = locationObject.getString("lat");
            return lng + "," + lat;
        } catch (Exception e) {
            return null;
        }
    }

    private static String lng_lat_get(String url) throws IOException {
        String body = "{}";
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            body = EntityUtils.toString(entity);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
        return body;
    }

    /**
     * 通过ip获取所在城市
     *
     * @param ip
     * @return
     * @throws IOException
     */
    public static JpaAtlasModel getAddressByIp(String ip) {
        String url = "http://api.map.baidu.com/location/ip?ak=" + BAIDU_MAP_AK + "&ip=" + ip;
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            com.alibaba.fastjson.JSONObject json = com.alibaba.fastjson.JSONObject.parseObject(sb.toString());
            if (Objects.isNull(json)) {
                return null;
            }
            com.alibaba.fastjson.JSONObject content = json.getJSONObject("content");
            if (content == null) return null;
            if (content.containsKey("address_detail")) {
                com.alibaba.fastjson.JSONObject addressDetail = content.getJSONObject("address_detail");
                JpaAtlasModel atlasModel = new JpaAtlasModel();
                atlasModel.setIp(ip);
                String city = addressDetail.getString("city");
                String province = addressDetail.getString("province");
                String district = addressDetail.getString("district");
                if (StringUtil.isNotBlank(city)) atlasModel.setCity(city);
                if (StringUtil.isNotBlank(province)) atlasModel.setProvince(province);
                if (StringUtil.isNotBlank(district)) atlasModel.setArea(district);
                atlasModel.setAddress(province + city + district);
                return atlasModel;
            }
            return null;
        } catch (Exception e) {
            logger.error("获取地理位置失败=========={}", e.getMessage());
            return null;
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                logger.error("{}", e.getMessage());
            }
        }
    }

    /**
     * 通过经纬度和ip联合查询所在城市
     *
     * @param lngLat null
     * @param ip     null
     */
    public static JpaAtlasModel getAddressBylngLatOrIp(String lngLat, String ip) {
        if (StringUtil.isNotBlank(lngLat)) {
            JpaAtlasModel atlasModel = getAddress(lngLat);
            if (!Objects.isNull(atlasModel) && StringUtil.isNotBlank(atlasModel.getProvince())
                    && StringUtil.isNotBlank(atlasModel.getCity())) {
                atlasModel.setIp(ip);
                return atlasModel;
            }
        }
        if (StringUtil.isNotBlank(ip) && RegexUtils.checkIpAddress(ip)) {
            JpaAtlasModel atlasModel = getAddressByIp(ip);
            if (!Objects.isNull(atlasModel) && StringUtil.isNotBlank(atlasModel.getProvince())
                    && StringUtil.isNotBlank(atlasModel.getCity())) {
                atlasModel.setIngLat(lngLat);
                return atlasModel;
            }
        }
        return null;
    }

    /**
     * 生成随机国内IP
     *
     * @return
     */
    public static String getRandomIp() {
        String ips = "58.14.0.0,58.16.0.0,58.24.0.0,58.30.0.0,58.32.0.0,58.66.0.0,58.68.128.0,58.82.0.0,58.87.64.0,58.99.128.0,58.100.0.0,58.116.0.0,58.128.0.0,58.144.0.0,58.154.0.0,58.192.0.0,58.240.0.0,59.32.0.0,59.64.0.0,59.80.0.0,59.107.0.0,59.108.0.0,59.151.0.0,59.155.0.0,59.172.0.0,59.191.0.0,59.191.240.0,59.192.0.0,60.0.0.0,60.55.0.0,60.63.0.0,60.160.0.0,60.194.0.0,60.200.0.0,60.208.0.0,60.232.0.0,60.235.0.0,60.245.128.0,60.247.0.0,60.252.0.0,60.253.128.0,60.255.0.0,61.4.80.0,61.4.176.0,61.8.160.0,61.28.0.0,61.29.128.0,61.45.128.0,61.47.128.0,61.48.0.0,61.87.192.0,61.128.0.0,61.232.0.0,61.236.0.0,61.240.0.0,114.28.0.0,114.54.0.0,114.60.0.0,114.64.0.0,114.68.0.0,114.80.0.0,116.1.0.0,116.2.0.0,116.4.0.0,116.8.0.0,116.13.0.0,116.16.0.0,116.52.0.0,116.56.0.0,116.58.128.0,116.58.208.0,116.60.0.0,116.66.0.0,116.69.0.0,116.70.0.0,116.76.0.0,116.89.144.0,116.90.184.0,116.95.0.0,116.112.0.0,116.116.0.0,116.128.0.0,116.192.0.0,116.193.16.0,116.193.32.0,116.194.0.0,116.196.0.0,116.198.0.0,116.199.0.0,116.199.128.0,116.204.0.0,116.207.0.0,116.208.0.0,116.212.160.0,116.213.64.0,116.213.128.0,116.214.32.0,116.214.64.0,116.214.128.0,116.215.0.0,116.216.0.0,116.224.0.0,116.242.0.0,116.244.0.0,116.248.0.0,116.252.0.0,116.254.128.0,116.255.128.0,117.8.0.0,117.21.0.0,117.22.0.0,117.24.0.0,117.32.0.0,117.40.0.0,117.44.0.0,117.48.0.0,117.53.48.0,117.53.176.0,117.57.0.0,117.58.0.0,117.59.0.0,117.60.0.0,117.64.0.0,117.72.0.0,117.74.64.0,117.74.128.0,117.75.0.0,117.76.0.0,117.80.0.0,117.100.0.0,117.103.16.0,117.103.128.0,117.106.0.0,117.112.0.0,117.120.64.0,117.120.128.0,117.121.0.0,117.121.128.0,117.121.192.0,117.122.128.0,117.124.0.0,117.128.0.0,118.24.0.0,118.64.0.0,118.66.0.0,118.67.112.0,118.72.0.0,118.80.0.0,118.84.0.0,118.88.32.0,118.88.64.0,118.88.128.0,118.89.0.0,118.91.240.0,118.102.16.0,118.112.0.0,118.120.0.0,118.124.0.0,118.126.0.0,118.132.0.0,118.144.0.0,118.178.0.0,118.180.0.0,118.184.0.0,118.192.0.0,118.212.0.0,118.224.0.0,118.228.0.0,118.230.0.0,118.239.0.0,118.242.0.0,118.244.0.0,118.248.0.0,119.0.0.0,119.2.0.0,119.2.128.0,119.3.0.0,119.4.0.0,119.8.0.0,119.10.0.0,119.15.136.0,119.16.0.0,119.18.192.0,119.18.208.0,119.18.224.0,119.19.0.0,119.20.0.0,119.27.64.0,119.27.160.0,119.27.192.0,119.28.0.0,119.30.48.0,119.31.192.0,119.32.0.0,119.40.0.0,119.40.64.0,119.40.128.0,119.41.0.0,119.42.0.0,119.42.136.0,119.42.224.0,119.44.0.0,119.48.0.0,119.57.0.0,119.58.0.0,119.59.128.0,119.60.0.0,119.62.0.0,119.63.32.0,119.75.208.0,119.78.0.0,119.80.0.0,119.84.0.0,119.88.0.0,119.96.0.0,119.108.0.0,119.112.0.0,119.128.0.0,119.144.0.0,119.148.160.0,119.161.128.0,119.162.0.0,119.164.0.0,119.176.0.0,119.232.0.0,119.235.128.0,119.248.0.0,119.253.0.0,119.254.0.0,120.0.0.0,120.24.0.0,120.30.0.0,120.32.0.0,120.48.0.0,120.52.0.0,120.64.0.0,120.72.32.0,120.72.128.0,120.76.0.0,120.80.0.0,120.90.0.0,120.92.0.0,120.94.0.0,120.128.0.0,120.136.128.0,120.137.0.0,120.192.0.0,121.0.16.0,121.4.0.0,121.8.0.0,121.16.0.0,121.32.0.0,121.40.0.0,121.46.0.0,121.48.0.0,121.51.0.0,121.52.160.0,121.52.208.0,121.52.224.0,121.55.0.0,121.56.0.0,121.58.0.0,121.58.144.0,121.59.0.0,121.60.0.0,121.68.0.0,121.76.0.0,121.79.128.0,121.89.0.0,121.100.128.0,121.101.208.0,121.192.0.0,121.201.0.0,121.204.0.0,121.224.0.0,121.248.0.0,121.255.0.0,122.0.64.0,122.0.128.0,122.4.0.0,122.8.0.0,122.48.0.0,122.49.0.0,122.51.0.0,122.64.0.0,122.96.0.0,122.102.0.0,122.102.64.0,122.112.0.0,122.119.0.0,122.136.0.0,122.144.128.0,122.152.192.0,122.156.0.0,122.192.0.0,122.198.0.0,122.200.64.0,122.204.0.0,122.224.0.0,122.240.0.0,122.248.48.0,123.0.128.0,123.4.0.0,123.8.0.0,123.49.128.0,123.52.0.0,123.56.0.0,123.64.0.0,123.96.0.0,123.98.0.0,123.99.128.0,123.100.0.0,123.101.0.0,123.103.0.0,123.108.128.0,123.108.208.0,123.112.0.0,123.128.0.0,123.136.80.0,123.137.0.0,123.138.0.0,123.144.0.0,123.160.0.0,123.176.80.0,123.177.0.0,123.178.0.0,123.180.0.0,123.184.0.0,123.196.0.0,123.199.128.0,123.206.0.0,123.232.0.0,123.242.0.0,123.244.0.0,123.249.0.0,123.253.0.0,124.6.64.0,124.14.0.0,124.16.0.0,124.20.0.0,124.28.192.0,124.29.0.0,124.31.0.0,124.40.112.0,124.40.128.0,124.42.0.0,124.47.0.0,124.64.0.0,124.66.0.0,124.67.0.0,124.68.0.0,124.72.0.0,124.88.0.0,124.108.8.0,124.108.40.0,124.112.0.0,124.126.0.0,124.128.0.0,124.147.128.0,124.156.0.0,124.160.0.0,124.172.0.0,124.192.0.0,124.196.0.0,124.200.0.0,124.220.0.0,124.224.0.0,124.240.0.0,124.240.128.0,124.242.0.0,124.243.192.0,124.248.0.0,124.249.0.0,124.250.0.0,124.254.0.0,125.31.192.0,125.32.0.0,125.58.128.0,125.61.128.0,125.62.0.0,125.64.0.0,125.96.0.0,125.98.0.0,125.104.0.0,125.112.0.0,125.169.0.0,125.171.0.0,125.208.0.0,125.210.0.0,125.213.0.0,125.214.96.0,125.215.0.0,125.216.0.0,125.254.128.0,134.196.0.0,159.226.0.0,161.207.0.0,162.105.0.0,166.111.0.0,167.139.0.0,168.160.0.0,169.211.1.0,192.83.122.0,192.83.169.0,192.124.154.0,192.188.170.0,198.17.7.0,202.0.110.0,202.0.176.0,202.4.128.0,202.4.252.0,202.8.128.0,202.10.64.0,202.14.88.0,202.14.235.0,202.14.236.0,202.14.238.0,202.20.120.0,202.22.248.0,202.38.0.0,202.38.64.0,202.38.128.0,202.38.136.0,202.38.138.0,202.38.140.0,202.38.146.0,202.38.149.0,202.38.150.0,202.38.152.0,202.38.156.0,202.38.158.0,202.38.160.0,202.38.164.0,202.38.168.0,202.38.176.0,202.38.184.0,202.38.192.0,202.41.152.0,202.41.240.0,202.43.144.0,202.46.32.0,202.46.224.0,202.60.112.0,202.63.248.0,202.69.4.0,202.69.16.0,202.70.0.0,202.74.8.0,202.75.208.0,202.85.208.0,202.90.0.0,202.90.224.0,202.90.252.0,202.91.0.0,202.91.128.0,202.91.176.0,202.91.224.0,202.92.0.0,202.92.252.0,202.93.0.0,202.93.252.0,202.95.0.0,202.95.252.0,202.96.0.0,202.112.0.0,202.120.0.0,202.122.0.0,202.122.32.0,202.122.64.0,202.122.112.0,202.122.128.0,202.123.96.0,202.124.24.0,202.125.176.0,202.127.0.0,202.127.12.0,202.127.16.0,202.127.40.0,202.127.48.0,202.127.112.0,202.127.128.0,202.127.160.0,202.127.192.0,202.127.208.0,202.127.212.0,202.127.216.0,202.127.224.0,202.130.0.0,202.130.224.0,202.131.16.0,202.131.48.0,202.131.208.0,202.136.48.0,202.136.208.0,202.136.224.0,202.141.160.0,202.142.16.0,202.143.16.0,202.148.96.0,202.149.160.0,202.149.224.0,202.150.16.0,202.152.176.0,202.153.48.0,202.158.160.0,202.160.176.0,202.164.0.0,202.164.25.0,202.165.96.0,202.165.176.0,202.165.208.0,202.168.160.0,202.170.128.0,202.170.216.0,202.173.8.0,202.173.224.0,202.179.240.0,202.180.128.0,202.181.112.0,202.189.80.0,202.192.0.0,203.18.50.0,203.79.0.0,203.80.144.0,203.81.16.0,203.83.56.0,203.86.0.0,203.86.64.0,203.88.32.0,203.88.192.0,203.89.0.0,203.90.0.0,203.90.128.0,203.90.192.0,203.91.32.0,203.91.96.0,203.91.120.0,203.92.0.0,203.92.160.0,203.93.0.0,203.94.0.0,203.95.0.0,203.95.96.0,203.99.16.0,203.99.80.0,203.100.32.0,203.100.80.0,203.100.96.0,203.100.192.0,203.110.160.0,203.118.192.0,203.119.24.0,203.119.32.0,203.128.32.0,203.128.96.0,203.130.32.0,203.132.32.0,203.134.240.0,203.135.96.0,203.135.160.0,203.142.219.0,203.148.0.0,203.152.64.0,203.156.192.0,203.158.16.0,203.161.192.0,203.166.160.0,203.171.224.0,203.174.7.0,203.174.96.0,203.175.128.0,203.175.192.0,203.176.168.0,203.184.80.0,203.187.160.0,203.190.96.0,203.191.16.0,203.191.64.0,203.191.144.0,203.192.0.0,203.196.0.0,203.207.64.0,203.207.128.0,203.208.0.0,203.208.16.0,203.208.32.0,203.209.224.0,203.212.0.0,203.212.80.0,203.222.192.0,203.223.0.0,210.2.0.0,210.5.0.0,210.5.144.0,210.12.0.0,210.14.64.0,210.14.112.0,210.14.128.0,210.15.0.0,210.15.128.0,210.16.128.0,210.21.0.0,210.22.0.0,210.23.32.0,210.25.0.0,210.26.0.0,210.28.0.0,210.32.0.0,210.51.0.0,210.52.0.0,210.56.192.0,210.72.0.0,210.76.0.0,210.78.0.0,210.79.64.0,210.79.224.0,210.82.0.0,210.87.128.0,210.185.192.0,210.192.96.0,211.64.0.0,211.80.0.0,211.96.0.0,211.136.0.0,211.144.0.0,211.160.0.0,218.0.0.0,218.56.0.0,218.64.0.0,218.96.0.0,218.104.0.0,218.108.0.0,218.185.192.0,218.192.0.0,218.240.0.0,218.249.0.0,219.72.0.0,219.82.0.0,219.128.0.0,219.216.0.0,219.224.0.0,219.242.0.0,219.244.0.0,220.101.192.0,220.112.0.0,220.152.128.0,220.154.0.0,220.160.0.0,220.192.0.0,220.231.0.0,220.231.128.0,220.232.64.0,220.234.0.0,220.242.0.0,220.248.0.0,220.252.0.0,221.0.0.0,221.8.0.0,221.12.0.0,221.12.128.0,221.13.0.0,221.14.0.0,221.122.0.0,221.129.0.0,221.130.0.0,221.133.224.0,221.136.0.0,221.172.0.0,221.176.0.0,221.192.0.0,221.196.0.0,221.198.0.0,221.199.0.0,221.199.128.0,221.199.192.0,221.199.224.0,221.200.0.0,221.208.0.0,221.224.0.0,222.16.0.0,222.32.0.0,222.64.0.0,222.125.0.0,222.126.128.0,222.128.0.0,222.160.0.0,222.168.0.0,222.176.0.0,222.192.0.0,222.240.0.0,222.248.0.0";
        String ipArray[] = ips.split(",");
        Random random = new Random();
        Integer index = random.nextInt(ipArray.length);
        String ip = ipArray[index];
        ipArray = ip.split("./");
        ip = "";
        for (String p : ipArray) {
            if (Objects.equals("0", p)) p = random.nextInt(255) + "";
            ip = ip + p;
        }
        return ip;
    }

    /**
     * 获取指定范围的经纬度
     *
     * @param MinLon 最小经度
     * @param MaxLon 最大经度
     * @param MinLat 最小纬度
     * @param MaxLat 最大纬度
     * @return
     */
    public static String randomLonLat(double MinLon, double MaxLon, double MinLat, double MaxLat) {
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        return lon + "," + lat;
    }

    public static String randomLonLat() {
        double MinLon = 90;
        double MaxLon = 125;
        double MinLat = 20;
        double MaxLat = 40;
        BigDecimal db = new BigDecimal(Math.random() * (MaxLon - MinLon) + MinLon);
        String lon = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();// 小数后6位
        db = new BigDecimal(Math.random() * (MaxLat - MinLat) + MinLat);
        String lat = db.setScale(6, BigDecimal.ROUND_HALF_UP).toString();
        return lon + "," + lat;
    }

    /**
     * @param lngLat1 第一个经纬度
     * @param lngLat2 第一个经纬度
     * @return 两个经纬度的直线距离
     */
    public static double getDistance(String lngLat1, String lngLat2) {
        Double lat1 = getLat(lngLat1, true);
        Double lat2 = getLat(lngLat2, true);
        Double lng1 = getLat(lngLat1, false);
        Double lng2 = getLat(lngLat2, false);
        if (Objects.isNull(lat1) || Objects.isNull(lat2) || Objects.isNull(lng1) || Objects.isNull(lng2)) {
            return 0;
        }
        Double radLat1 = lat1 * Math.PI / 180.0;
        Double radLat2 = lat2 * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lng1 * Math.PI / 180.0 - lng2 * Math.PI / 180.0;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    /**
     * 获取double类型经纬度
     *
     * @param ingLat 经纬度字符串(经度,纬度)
     * @param isLat  是否是纬度
     * @return double类型经纬度
     */
    private static Double getLat(String ingLat, Boolean isLat) {
        if (StringUtil.isBlank(ingLat)) {
            return null;
        }
        String longitude = ingLat.split(",")[0].trim();
        String latitude = ingLat.split(",")[1].trim();
        if (!isNumeric(longitude) || !isNumeric(latitude)) {
            return null;
        }
        try {
            if (isLat) {
                return Double.valueOf(latitude);
            } else {
                return Double.valueOf(longitude);
            }
        } catch (Exception e) {
            return null;
        }
    }

    @Test
    public void ipTest() throws IOException {
        System.out.println(getAddressByIp("36.149.29.82").toString());
        System.out.println(getAddress("69.7851562500,31.9521622380").toString());
        // System.out.println(getAddressBylngLatOrIp("120.194281 , 30.190787", "123.125.71.38").toString());
    }

    @Test
    public void getLngLat() {
        String address = "安徽省芜湖市团结西路63-1号";
        String lnglat = getLngLatByAddress(address);
        System.out.println(lnglat);
        System.out.println(getAddress(lnglat));
    }

    @Test
    public void getLonLat() {
        String lonLat = "120.31374889049967,31.59282382193283";
        System.out.println(getAddressByGaoDe(lonLat));
    }

}