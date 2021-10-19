package com.example.fdjtest.network

import com.example.fdjtest.Utils.Constants
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitInit {

    fun builder(): Retrofit {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BASIC)

            val okHttpClient = OkHttpClient.Builder()
            okHttpClient.addInterceptor(logging)

            val moshi = Moshi.Builder().build()

            return Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
                    .baseUrl(Constants.BASE_URL)
                    .client(okHttpClient.build())
                    .build()
        }
}