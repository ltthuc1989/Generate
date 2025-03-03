package com.ltthuc.network.di

import android.content.Context
import com.ltthuc.network.helper.ExceptionCallAdapterFactory
import com.ltthuc.network.helper.NetworkConnectionInterceptor
import com.ltthuc.utils.ISecretNetworkKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {


    @Provides
    @Singleton
    @Named("VnMobile")
    fun provideVnMobileRetrofit(client: OkHttpClient, secretKey: ISecretNetworkKey): Retrofit =
        Retrofit.Builder()
            .baseUrl(secretKey.getServiceUrl())
            .client(client)
            .addCallAdapterFactory(ExceptionCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    @Named("External")
    fun provideExternalRetrofit(client: OkHttpClient, secretKey: ISecretNetworkKey): Retrofit =
        Retrofit.Builder()
            .baseUrl(secretKey.getExternalServiceUrl())
            .client(client)
            .addCallAdapterFactory(ExceptionCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    @Singleton
    @Provides
    fun provideOkHttpClient(@ApplicationContext context: Context): OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addInterceptor(
                HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY)
            )
            .addInterceptor(NetworkConnectionInterceptor(context))
            .build()


}
