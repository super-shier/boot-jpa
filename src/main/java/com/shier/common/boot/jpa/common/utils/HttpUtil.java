package com.shier.common.boot.jpa.common.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class HttpUtil {
    /**
     * get请求，无header
     *
     * @param url
     * @return response
     * @throws IOException
     */
    public static String get(String url) throws IOException {
        return get(url, null);
    }

    /**
     * get请求，有header
     *
     * @param url
     * @param headers
     * @return String
     * @throws IOException
     */
    public static String get(String url, Map<String, String> headers) throws IOException {
        return fetch("GET", url, null, headers);
    }

    /**
     * post字符串请求，有header
     *
     * @param url
     * @param body
     * @param headers
     * @return String
     * @throws IOException
     */
    public static String post(String url, String body, Map<String, String> headers) throws IOException {
        return fetch("POST", url, body, headers);
    }

    /**
     * post字符串请求，无header
     *
     * @param url
     * @param body
     * @return String
     * @throws IOException
     */
    public static String post(String url, String body) throws IOException {
        return post(url, body, null);
    }

    /**
     * post map请求，无header
     *
     * @param url
     * @param params
     * @return String
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params) throws IOException {
        return postForm(url, params, null);
    }

    /**
     * post map串请求，有header
     *
     * @param url
     * @param params
     * @param headers
     * @return String
     * @throws IOException
     */
    public static String postForm(String url, Map<String, String> params, Map<String, String> headers) throws IOException {
        if (Objects.isNull(headers)) headers = new HashMap<>();
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        String body = "";
        if (params != null) {
            boolean first = true;
            for (String param : params.keySet()) {
                if (first) {
                    first = false;
                } else {
                    body += "&";
                }
                String value = params.get(param);
                body += URLEncoder.encode(param, "UTF-8") + "=";
                body += URLEncoder.encode(value, "UTF-8");
            }
        }

        return post(url, body, headers);
    }

    /**
     * put 字符串请求，有header
     *
     * @param url
     * @param body
     * @param headers
     * @return String
     * @throws IOException
     */
    public static String put(String url, String body, Map<String, String> headers) throws IOException {
        return fetch("PUT", url, body, headers);
    }

    /**
     * put 字符串请求，无header
     *
     * @param url
     * @return String
     * @throws IOException
     */
    public static String put(String url, String body) throws IOException {
        return put(url, body, null);
    }

    /**
     * @param url
     * @param headers
     * @return String
     * @throws IOException
     */
    public static String delete(String url, Map<String, String> headers) throws IOException {
        return fetch("DELETE", url, null, headers);
    }

    /**
     * delete请求
     *
     * @param url
     * @return String
     * @throws IOException
     */
    public static String delete(String url) throws IOException {
        return delete(url, null);
    }

    /**
     * @param url
     * @param params
     * @return url
     * @throws IOException
     */
    public static String appendQueryParams(String url, Map<String, String> params) throws IOException {
        String fullUrl = new String(url);
        if (Objects.nonNull(params)) {
            boolean first = (fullUrl.indexOf('?') == -1);
            for (String param : params.keySet()) {
                if (first) {
                    fullUrl += '?';
                    first = false;
                } else {
                    fullUrl += '&';
                }
                String value = params.get(param);
                fullUrl += URLEncoder.encode(param, "GBK") + '=';
                fullUrl += URLEncoder.encode(value, "GBK");
            }
        }
        return fullUrl;
    }

    /**
     * @param url
     * @return params
     * @throws IOException
     */
    public static Map<String, String> getQueryParams(String url) throws IOException {
        Map<String, String> params = new HashMap<>();
        int start = url.indexOf('?');
        while (start != -1) {
            int equals = url.indexOf('=', start);
            String param = "";
            if (equals != -1) {
                param = url.substring(start + 1, equals);
            } else {
                param = url.substring(start + 1);
            }
            String value = "";
            if (equals != -1) {
                start = url.indexOf('&', equals);
                if (start != -1) {
                    value = url.substring(equals + 1, start);
                } else {
                    value = url.substring(equals + 1);
                }
            }
            params.put(URLDecoder.decode(param, "GBK"), URLDecoder.decode(value, "GBK"));
        }
        return params;
    }

    /**
     * @param url
     * @return url
     * @throws IOException
     */
    public static String removeQueryParams(String url)
            throws IOException {
        int q = url.indexOf('?');
        if (q != -1) return url.substring(0, q);
        return url;

    }

    /**
     * @param method
     * @param url
     * @param body
     * @param headers
     * @return response
     * @throws IOException
     */
    private static String fetch(String method, String url, String body,
                                Map<String, String> headers) throws IOException {
        URL u = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        conn.setConnectTimeout(90000);
        conn.setReadTimeout(90000);
        if (Objects.nonNull(method)) conn.setRequestMethod(method);
        if (Objects.nonNull(headers)) {
            for (String key : headers.keySet()) {
                conn.addRequestProperty(key, headers.get(key));
            }
        }
        if (Objects.nonNull(body)) {
            conn.setDoOutput(true);
            OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();
        }
        InputStream is = conn.getInputStream();
        String response = streamToString(is);
        is.close();
        // handle redirects
        if (conn.getResponseCode() == 301) {
            String location = conn.getHeaderField("Location");
            return fetch(method, location, body, headers);
        }
        return response;
    }

    /**
     * @param in
     * @return
     * @throws IOException
     */
    private static String streamToString(InputStream in) throws IOException {
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        for (int n; (n = in.read(b)) != -1; ) {
            out.append(new String(b, 0, n));
        }
        return out.toString();
    }

    public static boolean isAjax(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }

    public static void ajaxStatus(HttpServletResponse response, int status, String tip) {
        try {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(status);
            PrintWriter out = response.getWriter();
            out.print(tip);
            out.flush();
        } catch (IOException var4) {
            System.out.println(var4.toString());
        }

    }
}