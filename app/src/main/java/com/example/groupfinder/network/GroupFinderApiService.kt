package com.example.groupfinder.network

import com.example.groupfinder.network.models.Group
import com.example.groupfinder.network.models.ResponseStudent
import com.example.groupfinder.network.models.Student
import com.example.groupfinder.network.models.UserGroups
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


private const val BASE_URL = ""



///**
// * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
// * full Kotlin compatibility.
// */
//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()
//
//
///**
// * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
// * object.
// */

//object ServiceBuilder {
//
//    private val client = OkHttpClient.Builder().build()
//
//    private val retrofit = Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .client(client)
//        .build()
//
//    fun<T> buildService(service: Class<T>): T {
//        return retrofit.create(service)
//    }
//
//    /**
//     * A public Api object that exposes the lazy-initialized Retrofit service
//     */
//    object GroupFinderApi {
//        val retrofitService : GroupFinderApiService by lazy { retrofit.create(GroupFinderApiService::class.java) }
//    }
//}


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the [getProperties] method
 */
interface GroupFinderApiService {
    /**
     *
     * The @GET annotation indicates that the "groups" endpoint will be requested with the GET
     * HTTP method
     */
//    @GET("api/user/userGroups")
//    fun getUserGroups(@Query("email") type: String) : Call<UserGroups>


    @GET("api/user/userGroups")
    fun getUserGroupsAsync(@Query("email") type: String) : Deferred<UserGroups>

    @GET("api/user/home")
    fun getStudentAsync(@Query("email") type: String) : Deferred<ResponseStudent>

    @GET("api/user/groupMembers")
    fun getGroupMembersAsync(@Query("gid") type: Int) : Deferred<Student>
}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object GroupFinderApi {
    val retrofitService : GroupFinderApiService by lazy { retrofit.create(GroupFinderApiService::class.java) }
}

