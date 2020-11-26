package com.example.groupfinder.network

import com.example.groupfinder.network.models.*
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

/**
 *  [GroupFinderApiService] grensesnitt for API kall.
 * @author Anders Olai Pedersen - 225280
 */

// http://LOCALIP:PORT/
//private const val BASE_URL = "http://192.168.11.130:3000/"
//private const val BASE_URL = "http://10.0.2.2:3000/"
private const val BASE_URL = "https://cryptic-castle-54022.herokuapp.com/"


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
 * Et public grensesnitt som eksponerer POST / GET-forespørselsmetodene
 */
interface GroupFinderApiService {


    // GET-endpoints

    /**
     *  @param email Får alle grupper relatert til studenten, spørring via e-post.
     *  @return [UserGroups] objekt
     */
    @GET("api/user/userGroups")
    fun getUserGroupsAsync(@Query("email") email: String) : Deferred<UserGroups>

    /**
     * @param email Får informasjon om den påloggede studenten, søk via e-post.
     * @return [ResponseStudent] objekt
     */
    @GET("api/user/home")
    fun getStudentAsync(@Query("email") email: String) : Deferred<ResponseStudent>

    /**
     * @param groupId  Får alle gruppemedlemmer relatert til en gruppe, søk etter groupId.
     * @return [GroupMembers] objekt
     */
    @GET("api/user/groupMembers")
    fun getGroupMembersAsync(@Query("gid") groupId: Int) : Deferred<GroupMembers>

    /**
     * @param email Får alle gruppene som brukeren opprettet, søk på e-post
     * @return [GroupLeaderGroups] objekt
     */
    @GET("api/user/groupLeaderGroups")
    fun getGroupLeaderGroupsAsync(@Query("email") email: String) : Deferred<GroupLeaderGroups>

    /**
     * Henter ledige rom
     * @param date Dato for reservasjon
     * @param start Start-tid for reservasjon
     * @param end slutt-time for reservasjon
     * @return [VacantRooms] objekt
     */
    @GET("api/user/vacantRooms")
    fun getVacantRoomsAsync(@Query("date") date: String, @Query("start") start: String, @Query("end") end: String) : Deferred<VacantRooms>

    /**
     * Henter alle reservasjoner basert på email
     * @param email
     * @return [UserReservations] objekt
     */
    @GET("api/user/userReservations")
    fun getUserReservationsAsync(@Query("email") email: String) : Deferred<UserReservations>


    @GET("api/user/allGroups")
    fun getAllGroupsAsync() : Deferred<UserGroups>



    // POST-endpoints
    /**
     * @param group som "body", sett inn ny gruppe
     * @return [PostMessage] objekt, melding
     */
    @POST("api/user/registerGroup")
    fun postRegisterGroupAsync(@Body group: PostGroup) : Deferred<PostMessage>

    /**
     * Setter inn ny student basert på @Field()
     * @return [PostMessage] objekt, melding
     */
    @POST("api/auth/registerStudent")
    @FormUrlEncoded
    fun postRegisterStudentAsync(@Field("firstname") fname: String,
                                 @Field("lastname") lname: String,
                                 @Field("email") email: String,
                                 @Field("phonenumber") phonenumber: Int,
                                 @Field("password") password: String) : Deferred<PostMessage>


    /**
     * @param reservation som "body", sett inn ny reservasjon
     * @return [PostMessage] objekt, melding
     */
    @POST("api/user/reserveRoom")
    fun postReserveRoomAsync(@Body reservation: Reservation) : Deferred<PostMessage>


    /**
     * Login, henter student info basert på:
     * @param email
     * @param password
     * @return [ResponseStudent] objekt
     */
    @POST("api/auth/loginStudent")
    @FormUrlEncoded
    fun postLoginStudentAsync(@Field("email") email: String,
                              @Field("password") password: String) : Deferred<ResponseStudent>


    /**
     * Blir med i gruppe basert på: (Koblingstabell)
     * @param sId StudentId
     * @param gId GruppeId
     * @return [PostMessage] meldings objekt
     */
    @POST("api/user/joinGroup")
    @FormUrlEncoded
    fun postGroupRequestAsync(@Field("StudentId") sId: Int, @Field("GroupId") gId: Int) : Deferred<PostMessage>

}


/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */
object GroupFinderApi {
    val retrofitService : GroupFinderApiService by lazy { retrofit.create(GroupFinderApiService::class.java) }
}

