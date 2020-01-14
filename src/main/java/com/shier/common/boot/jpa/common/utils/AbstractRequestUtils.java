package com.shier.common.boot.jpa.common.utils;

import com.google.common.collect.Lists;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * @Author: liyunbiao
 * @Date: 2019/7/24 12:37 PM
 * @description
 */
public abstract class AbstractRequestUtils {
    private static org.slf4j.Logger logger = LoggerFactory.getLogger(AbstractRequestUtils.class);

    private AbstractRequestUtils() {
    }

    public static String getRequestBody(HttpServletRequest request) {
        StringBuilder sb = new StringBuilder();
        if (Lists.newArrayList("POST", "PUT").contains(request.getMethod())) {
            try (BufferedReader bufferedReader = request.getReader()) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
        return sb.toString();
    }
}
