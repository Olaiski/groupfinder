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
//private const val BASE_URL = "http://192.168.11.130:3000/"
private const val BASE_URL = "http://10.0.2.2:3000/"


/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 * Moshi is a modern JSON library for Android and Java. It makes it easy to parse JSON into Java objects
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * Use the Retrofit builder to build a retrofit object using a Moshi converter with our Moshi
 * object.
 *
 * Retrofit turns your HTTP API into a Java interface.
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
     *  @param email Gets all groups related to the student, query by email.
     */
    @GET("api/user/userGroups")
    fun getUserGroupsAsync(@Query("email") email: String) : Deferred<UserGroups>

    /**
     * @param email Gets information about the logged in student, query by email.
     *
     */
    @GET("api/user/home")
    fun getStudentAsync(@Query("email") email: String) : Deferred<ResponseStudent>

    /**
     * @param groupId  Gets all group members related to a group, query by groupId.
     */
    @GET("api/user/groupMembers")
    fun getGroupMembersAsync(@Query("gid") groupId: Int) : Deferred<GroupMembers>

    /**
     * @param email Gets all groups that the user created, query by email
     */
    @GET("api/user/groupLeaderGroups")
    fun getGroupLeaderGroupsAsync(@Query("email") email: String) : Deferred<GroupLeaderGroups>

    /**
     * Gets vacant rooms based on:
     * @param date Date for reservation
     * @param start Start time for reservation
     * @param end End time for reservation
     */
    @GET("api/user/vacantRooms")
    fun getVacantRoomsAsync(@Query("date") date: String, @Query("start") start: String, @Query("end") end: String) : Deferred<VacantRooms>


    /**
     *
     */
    @GET("api/user/userReservations")
    fun getUserReservationsAsync(@Query("email") email: String) : Deferred<UserReservations>



    // POST-endpoints
    /**
     * @param group as body, inserts a new group, returns a message
     */
    @POST("api/user/registerGroup")
    fun postRegisterGroupAsync(@Body group: PostGroup) : Deferred<PostMessage>

    /**
     * Inserts a new student based on @Field(), returns a message
     *
     */
    @POST("api/auth/registerStudent")
    @FormUrlEncoded
    fun postRegisterStudentAsync(@Field("firstname") fname: String,
                                 @Field("lastname") lname: String,
                                 @Field("email") email: String,
                                 @Field("phonenumber") phonenumber: Int,
                                 @Field("password") password: String) : Deferred<PostMessage>

    @POST("api/user/reserveRoom")
    fun postReserveRoomAsync(@Body reservation: Reservation) : Deferred<PostMessage>


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

