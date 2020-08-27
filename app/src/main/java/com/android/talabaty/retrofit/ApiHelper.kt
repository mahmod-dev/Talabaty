package com.android.talabaty.retrofit

import com.android.talabaty.model.*
import retrofit2.http.Body


interface ApiHelper {

    suspend fun getSettings(): Settings

    suspend fun getCountries(): Countries

    suspend fun getCities(countryId: String): Cities

    suspend fun getAds(): Ads

    suspend fun getFaq(): FaqX

    suspend fun getCategories(storeId: Int): Categories

    suspend fun getProfile(): EditProfile

    suspend fun getChangeUserStatus(): ChangeUserStatus

    suspend fun getLogout(): GeneralResponse

    suspend fun getViewStores(activityId: Int): ViewStores

    suspend fun getViewStoreProducts(storeId: Int, categoryId: Int): StoreProducts

    suspend fun getActivities(): Activities

    suspend fun getMyCart(): MyCart

    suspend fun getViewStoreDetails(storeId: Int): StoreDetails

    suspend fun getDeleteFromCart(productId: Int): GeneralResponse

    suspend fun getAddProductToFav(productId: Int): ProductToFav

    suspend fun getDeleteProductFromFav(productId: Int): GeneralResponse

    suspend fun getMyFavProducts(): FavProducts

    suspend fun signUp(
        signUp: SignUpPost
    ): SignUp

    suspend fun checkCode(code: Int, mobile: String): CheckCode

    suspend fun login(user: LoginPost): Login

    suspend fun forgotPassword(email: String): GeneralResponse

    suspend fun editProfile(user: UserPost): EditProfile

    suspend fun changePassword(
        oldPassword: String,
        password: String,
        confirmPassword: String
    ): GeneralResponse

    suspend fun sendContactUsMsg(title: String, message: String): ContactUsMsg

    suspend fun addProductToCart(productId: Int, quantity: Int): AddProductToCart

    suspend fun changeQuantity(productId: Int, type: String): ChangeQuantity

    suspend fun requestNewCode(mobile: String): GeneralResponse


    suspend fun getHomePageCategories(): HomePageCategories

    suspend fun getMyOrders(): MyOrders

    suspend fun getStoresFreeDelivery(): StoresFreeDelivery

    suspend fun createNewOrder(@Body order: NewOrderPost): CreateNewOrder

}