package com.android.talabaty.retrofit

import com.android.talabaty.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Query


interface ApiHelper {

    suspend fun getSettings(): MainSettings

    suspend fun getCountries(): Countries

    suspend fun getCities(countryId: String): Cities

    suspend fun getAds(): Ads

    suspend fun getOffers(): GetOffers

    suspend fun getFaq(): FaqX

    suspend fun getCategories(storeId: Int): Categories

    suspend fun getProfile(): EditProfile

    suspend fun getChangeUserStatus(): ChangeUserStatus

    suspend fun getLogout(): GeneralResponse

    suspend fun getViewStores(activityId: Int): ViewStores

    suspend fun getViewStoreProducts(storeId: Int, categoryId: Int): StoreProducts

    suspend fun getActivities(): Activities

    suspend fun getMyCart(
        coupon: String?,
        payment_method: String?,
        delivery_method: Int,
        user_address_id: Int
    ): MyCart

    suspend fun checkout(
        coupon: String?,
        payment_method: String?,
        delivery_method: Int,
        user_address_id: Int
    ): Checkout

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

    //  suspend fun editProfile(user: UserPost): EditProfile
    suspend fun editProfile(
        name: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        password: RequestBody,
        device_type: RequestBody,
        fcm_token: RequestBody,
        image_profile: MultipartBody.Part
    ): EditProfile

    suspend fun changePassword(
        oldPassword: String,
        password: String,
        confirmPassword: String
    ): GeneralResponse

    suspend fun sendContactUsMsg(title: String, message: String): ContactUsMsg

    suspend fun addProductToCart(
        productId: Int,
        quantity: Int,
        color_id: Int,
        size_id: Int
    ): AddProductToCart

    suspend fun changeQuantity(productId: Int, type: String): ChangeQuantity

    suspend fun requestNewCode(mobile: String): GeneralResponse

    suspend fun getHomePageCategories(): HomePageCategories


    suspend fun getStoresFreeDelivery(): StoresFreeDelivery

    suspend fun createNewOrder(order: NewOrderPost): CreateNewOrder

    suspend fun getCars(): GetCars

    suspend fun requestCar(car: RequestCarPost): RequestCar

    suspend fun getOtherServices(): OtherServices

    suspend fun changeNotifiStatus(status: Int, notification: String): GeneralResponse

    suspend fun getUserDetails(): GetUserDetails

    suspend fun requestDigitalService(
        digital_id: RequestBody,
        name: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        priority: RequestBody,
        details: RequestBody,
        date_from: RequestBody,
        date_to: RequestBody,
        order_images: MultipartBody.Part?,
        order_files: MultipartBody.Part?
    ): DigitalService

    suspend fun getDigitals(): getDigitals

    suspend fun requestOtherService(request: RequestOtherServicePost): RequestOtherService

    suspend fun requestService(request: RequestServicePost): RequestOtherService

    suspend fun getProductDetails(productId: Int): ProductDetails

    suspend fun addPaymentCard(request: AddPaymentCardPost): AddPaymentCard

    suspend fun getPaymentMethods(): GetPaymentMethod

    suspend fun getMyPaymentCards(): GetMyPaymentCard

    suspend fun editPaymentCard(request: EditPaymentCardPost): AddPaymentCard

    suspend fun deletePaymentCard(cardId: Int): GeneralResponse

    suspend fun nearbyStores(
        latitude: Long,
        longitude: Long
    ): GetNearbyStores

    suspend fun viewTatbeqakumProducts(activityId: Int): GetViewTatbeqakumProducts

    suspend fun getMyCoupons(): GetMyCoupons

    suspend fun chargeWallet(amount: Int): ChargeWallet

    suspend fun addNewCoupon(coupon: String): AddNewCoupon

    suspend fun deleteCoupon(coupon_id: Int): GeneralResponse

    suspend fun searchProducts(text: String): SearchProduct

    suspend fun addNewAddress(
        latitude: Double,
        longitude: Double,
        address: String
    ): AddNewAddress

    suspend fun editMyAddress(
        address_id: Int,
        latitude: Double,
        longitude: Double,
        address: String
    ): GeneralResponse

    suspend fun deleteMyAddress(address_id: Int): GeneralResponse

    suspend fun getMyAddresses(): GetAllBookAddress

    suspend fun getClientOrders(): GetClientOrders

    suspend fun getClientOrderDetails(order_id: Int): GetClientOrderDetails

    suspend fun clientCancelOrder(
        order_id: Int, notes: String
    ): GeneralResponse

    suspend fun searchOffers(text: String): SearchOffer


}