package com.android.talabaty.retrofit

import com.android.talabaty.model.*
import retrofit2.http.*

interface ApiService {

    @GET("settings")
    suspend fun getSettings(): Settings

    @GET("getCountries")
    suspend fun getCountries(): Countries

    @GET("getCities")
    suspend fun getCities(@Query("country_id") countryId: String): Cities

    @GET("getAds")
    suspend fun getAds(): Ads

    @GET("getFaq")
    suspend fun getFaq(): FaqX

    @GET("getCategories")
    suspend fun getCategories(@Query("store_id") storeId: Int): Categories

    @GET("profile")
    suspend fun getProfile(): EditProfile

    @GET("changeUserStatus")
    suspend fun getChangeUserStatus(): ChangeUserStatus

    @GET("logout")
    suspend fun getLogout(): GeneralResponse

    @GET("viewStores")
    suspend fun getViewStores(@Query("activity_id") activityId: Int): ViewStores

    @GET("viewStoreProducts")
    suspend fun getViewStoreProducts(
        @Query("store_id") storeId: Int,
        @Query("category_id") categoryId: Int
    ): StoreProducts

    @GET("getActivities")
    suspend fun getActivities(): Activities

    @GET("viewStoreDetails")
    suspend fun getViewStoreDetails(@Query("store_id") storeId: Int): StoreDetails

    @GET("deleteFromCart")
    suspend fun getDeleteFromCart(@Query("product_id") productId: Int): GeneralResponse

    @GET("addProductToFav")
    suspend fun getAddProductToFav(@Query("product_id") productId: Int): ProductToFav

    @GET("deleteProductFromFav")
    suspend fun getDeleteProductFromFav(@Query("product_id") productId: Int): GeneralResponse

    @GET("myFavProducts")
    suspend fun getMyFavProducts(): FavProducts

    @GET("myCart")
    suspend fun getMyCart(): MyCart

    /////////////////////////////////////////////

    @POST("signUp")
    suspend fun signUp(@Body user: SignUpPost): SignUp


    @FormUrlEncoded
    @POST("checkCode")
    suspend fun checkCode(@Field("code") code: Int, @Field("mobile") mobile: String): CheckCode

    @FormUrlEncoded
    @POST("requestNewCode")
    suspend fun requestNewCode(@Field("mobile") mobile: String): GeneralResponse

    @POST("login")
    suspend fun login(@Body user: LoginPost): Login

    @FormUrlEncoded
    @POST("forgotPassword")
    suspend fun forgotPassword(@Field("email") email: String): GeneralResponse

    @POST("editProfile")
    suspend fun editProfile(@Body user: UserPost): EditProfile

    //   @FormUrlEncoded
    //    @POST("changeUserImage")
    //   suspend fun changeUserImage(@Field("image_profile") img: ByteArray): ApiUser

    @FormUrlEncoded
    @POST("changePassword")
    suspend fun changePassword(
        @Field("old_password") oldPassword: String,
        @Field("password") password: String,
        @Field("confirm_password") confirmPassword: String
    ): GeneralResponse

    @FormUrlEncoded
    @POST("sendContactUsMsg")
    suspend fun sendContactUsMsg(
        @Field("title") title: String,
        @Field("message") message: String
    ): ContactUsMsg

    @FormUrlEncoded
    @POST("addProductToCart")
    suspend fun addProductToCart(
        @Field("product_id") productId: Int,
        @Field("quantity") quantity: Int
    ): AddProductToCart

    @FormUrlEncoded
    @POST("changeQuantity")
    suspend fun changeQuantity(
        @Field("product_id") productId: Int,
        @Field("type") type: String
    ): ChangeQuantity
}