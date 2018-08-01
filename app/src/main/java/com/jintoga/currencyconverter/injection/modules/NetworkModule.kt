package com.jintoga.currencyconverter.injection.modules

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.jintoga.currencyconverter.BuildConfig
import com.jintoga.currencyconverter.network.ClientApi
import com.jintoga.currencyconverter.network.XTokenInterceptor
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideXTokenInterceptor(): XTokenInterceptor = XTokenInterceptor()

    @Provides
    @Singleton
    fun provideClientApi(retrofit: Retrofit): ClientApi =
            retrofit.create(ClientApi::class.java)

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        return logging
    }

    @Provides
    @Singleton
    fun provideHttpClient(xTokenInterceptor: XTokenInterceptor,
                          httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient =
            OkHttpClient()
                    .newBuilder()
                    .addInterceptor(xTokenInterceptor)
                    .addInterceptor(httpLoggingInterceptor)
                    .build()

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideRetrofitClientApi(okHttpClient: OkHttpClient,
                                 gson: Gson): Retrofit =
            Retrofit.Builder()
                    .baseUrl(BuildConfig.ENDPOINT)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

}