package com.ltthuc.feature.di

import com.ltthuc.feature.di.imp.FeatureSettingImp
import com.ltthuc.feature.domain.repository.ApiExternalService
import com.ltthuc.feature.domain.repository.ApiService
import com.ltthuc.feature.data.repository.ApiServiceImp
import com.ltthuc.feature.domain.repository.ApiDownloadFileService
import com.ltthuc.feature.domain.repository.ApiVnMobileService
import com.ltthuc.network.helper.ExceptionCallAdapterFactory
import com.ltthuc.ui.base.settings.IFeatureSetting
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
interface FeatureModule {

    companion object {
        @Provides
        @Singleton
        fun provideApiRetrofit(
            apiVnMobileService: ApiVnMobileService,
            apiExternalService: ApiExternalService
        ): ApiService = ApiServiceImp(apiVnMobileService, apiExternalService)

        @Provides
        @Singleton
        @Named("download")
        fun provideDownloadRetrofit(client: OkHttpClient): Retrofit =
            Retrofit.Builder()
                .baseUrl(ApiDownloadFileService.DOWNLOAD_URL)
                .client(client)
                .addCallAdapterFactory(ExceptionCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        @Provides
        @Singleton
        fun provideVnMobileService(@Named("VnMobile") retrofit: Retrofit): ApiVnMobileService =
            retrofit.create(ApiVnMobileService::class.java)

        @Provides
        @Singleton
        fun provideExternalService(@Named("External") retrofit: Retrofit): ApiExternalService =
            retrofit.create(ApiExternalService::class.java)

        @Provides
        @Singleton
        fun provideDownloadService(@Named("download") retrofit: Retrofit): ApiDownloadFileService =
            retrofit.create(ApiDownloadFileService::class.java)

        @Provides
        @Singleton
        fun provideFeatureSetting(): IFeatureSetting = FeatureSettingImp()
    }

}