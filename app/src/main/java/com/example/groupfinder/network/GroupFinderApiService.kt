package com.example.groupfinder.network

import com.example.groupfinder.network.models.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 *  http://LOCALIP:PORT/
 */
private const val BASE_URL = ""


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 */
private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

/**
 * A public interface that exposes the POST/GET request methods
 */
interface GroupFinderApiService {


    // GET-endpoints

    /**
     *  Gets all groups related to the student, query by email.
     */
    @GET("api/user/userGroups")
    fun getUserGroupsAsync(@Query("email") type: String) : Deferred<UserGroups>

    /**
     *  Gets information about the logged in student, query by email.
     */
    @GET("api/user/home")
    fun getStudentAsync(@Query("email") type: String) : Deferred<ResponseStudent>

    /**
     *  Gets all group members related to a group, query by groupId.
     */
    @GET("api/user/groupMembers")
    fun getGroupMembersAsync(@Query("gid") type: Int) : Deferred<GroupMembers>


    // POST-endpoints

    /**
     * @param group as body, inserts a new group, returns a message
     */
    @POST("api/user/registerGroup")
    fun postRegisterGroupAsync(@Body group: PostGroup) : Deferred<PostMessage>

    /**
     * @param student as body, inserts a new student, returns a message
     */
    @POST("api/auth/registerStudent")
    @FormUrlEncoded
    fun postRegisterStudentAsync(@Field("firstname") fname: String,
                                 @Field("lastname") lname: String,
                                 @Field("email") email: String,
                                 @Field("phonenumber") phonenumber: Int,
                                 @Field("password") password: String) : Deferred<PostMessage>


    /**
     * @param email
     * @param password
     */
    @POST("api/auth/loginStudent")
    @FormUrlEncoded
    fun postLoginStudentAsync(@Field("email") email: String,
                              @Field("password") password: String) : Deferred<ResponseStudent>

}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object GroupFinderApi {
    val retrofitService : GroupFinderApiService by lazy { retrofit.create(GroupFinderApiService::class.java) }
}

