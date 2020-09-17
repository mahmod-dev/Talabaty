package com.android.talabaty.retrofit

import com.android.talabaty.model.*
import retrofit2.http.*

interface ApiService {

    @GET("settings")
    suspend fun getSettings(): MainSettings

    @GET("getCountries")
    suspend fun getCountries(): Countries

    @GET("getCities")
    suspend fun getCities(@Query("country_id") countryId: String): Cities

    @GET("getAds")
    suspend fun getAds(): Ads

    @GET("getOffers")
    suspend fun getOffers(): GetOffers

    @GET("getOtherServices")
    suspend fun getOtherServices(): OtherServices

    @GET("getFaq")
    suspend fun getFaq(): FaqX

    @GET("getCategories")
    suspend fun getCategories(@Query("store_id") storeId: Int): Categories

    @GET("changeNotifiStatus")
    suspend fun changeNotifiStatus(
        @Query("status") status: Int,
        @Query("notification") notification: String
    ): GeneralResponse

    @GET("profile")
    suspend fun getProfile(): EditProfile

    @GET("getUserDetails")
    suspend fun getUserDetails(): GetUserDetails

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

    @GET("productDetails")
    suspend fun getProductDetails(@Query("product_id") productId: Int): ProductDetails

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

    @GET("myOrders")
    suspend fun getMyOrders(): MyOrders

    @GET("storesFreeDelivery")
    suspend fun getStoresFreeDelivery(): StoresFreeDelivery

    @GET("getDigitals")
    suspend fun getDigitals(): getDigitals

    @GET("getHomePageCategories")
    suspend fun getHomePageCategories(): HomePageCategories

    @GET("getCars")
    suspend fun getCars(): GetCars

    @GET("getPaymentMethods")
    suspend fun getPaymentMethods(): GetPaymentMethod

    @GET("getMyPaymentCards")
    suspend fun getMyPaymentCards(): GetMyPaymentCard

    @GET("nearbyStores")
    suspend fun nearbyStores(
        @Query("latitude") latitude: Long,
        @Query("longitude") longitude: Long
    ): GetNearbyStores

    @GET("viewTatbeqakumProducts")
    suspend fun viewTatbeqakumProducts(@Query("activity_id") activityId: Int): GetViewTatbeqakumProducts

    @GET("deleteCoupon")
    suspend fun deleteCoupon(@Query("coupon_id") coupon_id: Int): GeneralResponse

    /////////////////////////////////////////////


    @POST("signUp")
    suspend fun signUp(@Body user: SignUpPost): SignUp

    @POST("requestDigitalService")
    suspend fun requestDigitalService(@Body user: DigitalServiceBody): DigitalService


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
        @Field("quantity") quantity: Int,
        @Field("color_id")color_id : Int ,
        @Field("size_id")size_id : Int

    ): AddProductToCart

    @FormUrlEncoded
    @POST("changeQuantity")
    suspend fun changeQuantity(
        @Field("product_id") productId: Int,
        @Field("type") type: String
    ): ChangeQuantity


    @POST("createNewOrder")
    suspend fun createNewOrder(@Body order: NewOrderPost): CreateNewOrder

    @POST("requestCar")
    suspend fun requestCar(@Body car: RequestCarPost): RequestCar

    @POST("requestOtherService")
    suspend fun requestOtherService(@Body request: RequestOtherServicePost): RequestOtherService

    @POST("requestService")
    suspend fun requestService(@Body request: RequestServicePost): RequestOtherService

    @POST("addPaymentCard")
    suspend fun addPaymentCard(@Body request: AddPaymentCardPost): AddPaymentCard

    @POST("editPaymentCard")
    suspend fun editPaymentCard(@Body request: EditPaymentCardPost): AddPaymentCard

    @FormUrlEncoded
    @POST("deletePaymentCard")
    suspend fun deletePaymentCard(
        @Field("card_id") cardId: Int
    ): GeneralResponse

    @POST("getMyCoupons")
    suspend fun getMyCoupons(): GetMyCoupons

    @FormUrlEncoded
    @POST("chargeWallet")
    suspend fun chargeWallet(   @Field("amount") amount: Int): ChargeWallet

    @FormUrlEncoded
    @POST("addNewCoupon")
    suspend fun addNewCoupon(   @Field("coupon") coupon: String): AddNewCoupon
}