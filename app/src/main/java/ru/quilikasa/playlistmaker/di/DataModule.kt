package ru.quilikasa.playlistmaker.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.quilikasa.playlistmaker.App.Companion.PLAYLIST_PREFERENCES
import ru.quilikasa.playlistmaker.BuildConfig
import ru.quilikasa.playlistmaker.data.NetworkClient
import ru.quilikasa.playlistmaker.data.StorageClient
import ru.quilikasa.playlistmaker.data.dto.TrackDbDto
import ru.quilikasa.playlistmaker.data.network.ItunesApiService
import ru.quilikasa.playlistmaker.data.network.RetrofitNetworkClient
import ru.quilikasa.playlistmaker.data.storage.PrefStorageClient

val dataModule = module {

    single {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
            })
            .build()
    }

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl(ItunesApiService.BASE_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences(PLAYLIST_PREFERENCES, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<StorageClient<Boolean>>(named("StorageClientBoolean")) {
        val type = object : TypeToken<Boolean>() {}.type
        PrefStorageClient(get(), get(), type)
    }

    single<StorageClient<TrackDbDto>>(named("StorageClientTrackDbDto")) {
        val type = object : TypeToken<List<TrackDbDto>>() {}.type
        PrefStorageClient(get(), get(), type)
    }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }
}