package com.example.sslclient.support;

import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.protocol.HttpContext;

/**
 * @author qihongfei
 * @date 2019/5/7
 */
public class CustomConnectionKeepAliveStrategy implements ConnectionKeepAliveStrategy {
    @Override
    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
        return 30;
    }
}
