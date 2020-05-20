package com.vonkez.utils;

import okhttp3.CacheControl;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;

import static java.util.concurrent.TimeUnit.MINUTES;

public final class Requests {
    private static CacheControl DEFAULT_CACHE_CONTROL = new CacheControl.Builder().maxAge(10, MINUTES).build();
    private static Headers DEFAULT_HEADERS = new Headers.Builder().build();
    private static FormBody DEFAULT_BODY = new FormBody.Builder().build();

    private Requests(){}

    public static class GET {
        CacheControl _cacheControl = DEFAULT_CACHE_CONTROL;
        Headers _headers = DEFAULT_HEADERS;
        String _url;
        public GET(String url) {
           this._url = url;
        }
        public GET cacheControl(CacheControl cacheControl) {
            this._cacheControl = cacheControl;
            return this;
        }
        public GET headers(Headers headers) {
            this._headers = headers;
            return this;
        }
        public Request build() {
            return new Request.Builder()
                    .url(this._url)
                    .headers(this._headers)
                    .cacheControl(this._cacheControl)
                    .build();
        }

    }

    public static class POST {
        CacheControl _cacheControl = DEFAULT_CACHE_CONTROL;
        Headers _headers = DEFAULT_HEADERS;
        FormBody _body = DEFAULT_BODY;
        String _url;
        public POST(String url) {
            this._url = url;
        }
        public POST cacheControl(CacheControl cacheControl) {
            this._cacheControl = cacheControl;
            return this;
        }
        public POST headers(Headers headers) {
            this._headers = headers;
            return this;
        }
        public POST body(FormBody body) {
            this._body = body;
            return this;
        }
        public Request build() {
            return new Request.Builder()
                    .url(this._url)
                    .post(this._body)
                    .headers(this._headers)
                    .cacheControl(this._cacheControl)
                    .build();
        }
    }
}
