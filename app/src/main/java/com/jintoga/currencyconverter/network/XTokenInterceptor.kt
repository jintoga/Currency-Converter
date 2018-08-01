package com.jintoga.currencyconverter.network

import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class XTokenInterceptor @Inject constructor() : Interceptor {
  
    override fun intercept(chain: Interceptor.Chain?): Response {
        val request = chain?.request()
        val builder = request?.newBuilder()
        builder?.addHeader("Content-Type", "application/x-www-form-urlencoded")
        //Add headers here if required
        return chain?.proceed(builder!!.build())!!
    }
}