package com.android.talabaty.retrofit

import com.android.talabaty.model.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getSettings(): MainSettings {
        return apiService.getSettings()
    }

    override suspend fun getCountries(): Countries {
        return apiService.getCountries()
    }

    override suspend fun getCities(countryId: String): Cities {
        return apiService.getCities(countryId)
    }

    override suspend fun getAds(): Ads {
        return apiService.getAds()
    }

    override suspend fun getOffers(): GetOffers {
        return apiService.getOffers()
    }

    override suspend fun getFaq(): FaqX {
        return apiService.getFaq()
    }

    override suspend fun getCategories(storeId: Int): Categories {
        return apiService.getCategories(storeId)
    }

    override suspend fun getProfile(): EditProfile {
        return apiService.getProfile()
    }

    override suspend fun getChangeUserStatus(): ChangeUserStatus {
        return apiService.getChangeUserStatus()
    }

    override suspend fun getLogout(): GeneralResponse {
        return apiService.getLogout()
    }

    override suspend fun getViewStores(activityId: Int): ViewStores {
        return apiService.getViewStores(activityId)
    }

    override suspend fun getViewStoreProducts(storeId: Int, categoryId: Int): StoreProducts {
        return apiService.getViewStoreProducts(storeId, categoryId)
    }

    override suspend fun getActivities(): Activities {
        return apiService.getActivities()
    }

    override suspend fun getMyCart(
        coupon: String?,
        payment_method: String?,
        delivery_method: Int,
        user_address_id: Int
    ): MyCart {
        return apiService.getMyCart(coupon, payment_method, delivery_method, user_address_id)
    }

    override suspend fun getViewStoreDetails(storeId: Int): StoreDetails {
        return apiService.getViewStoreDetails(storeId)
    }

    override suspend fun getDeleteFromCart(productId: Int): GeneralResponse {
        return apiService.getDeleteFromCart(productId)
    }

    override suspend fun getAddProductToFav(productId: Int): ProductToFav {
        return apiService.getAddProductToFav(productId)
    }

    override suspend fun getDeleteProductFromFav(productId: Int): GeneralResponse {
        return apiService.getDeleteProductFromFav(productId)
    }

    override suspend fun getMyFavProducts(): FavProducts {
        return apiService.getMyFavProducts()
    }

    override suspend fun signUp(signUp: SignUpPost): SignUp {
        return apiService.signUp(signUp)
    }

    override suspend fun checkCode(code: Int, mobile: String): CheckCode {
        return apiService.checkCode(code, mobile)
    }

    override suspend fun login(user: LoginPost): Login {
        return apiService.login(user)
    }

    override suspend fun forgotPassword(email: String): GeneralResponse {
        return apiService.forgotPassword(email)
    }

//    override suspend fun editProfile(user: UserPost): EditProfile {
//        return apiService.editProfile(user)
//    }

    override suspend fun editProfile(
        name: RequestBody,
        email: RequestBody,
        mobile: RequestBody,
        latitude: RequestBody,
        longitude: RequestBody,
        password: RequestBody,
        device_type: RequestBody,
        fcm_token: RequestBody,
        image_profile: MultipartBody.Part
    ): EditProfile {
        return apiService.editProfile(
            name,
            email,
            mobile,
            latitude,
            longitude,
            password,
            device_type,
            fcm_token,
            image_profile
        )
    }


    override suspend fun changePassword(
        oldPassword: String,
        password: String,
        confirmPassword: String
    ): GeneralResponse {
        return apiService.changePassword(oldPassword, password, confirmPassword)
    }

    override suspend fun sendContactUsMsg(title: String, message: String): ContactUsMsg {
        return apiService.sendContactUsMsg(title, message)
    }

    override suspend fun addProductToCart(
        productId: Int,
        quantity: Int,
        color_id: Int,
        size_id: Int
    ): AddProductToCart {
        return apiService.addProductToCart(productId, quantity, color_id, size_id)
    }

    override suspend fun changeQuantity(productId: Int, type: String): ChangeQuantity {
        return apiService.changeQuantity(productId, type)
    }

    override suspend fun requestNewCode(mobile: String): GeneralResponse {
        return apiService.requestNewCode(mobile)
    }

    override suspend fun getHomePageCategories(): HomePageCategories {
        return apiService.getHomePageCategories()
    }


    override suspend fun getStoresFreeDelivery(): StoresFreeDelivery {
        return apiService.getStoresFreeDelivery()
    }

    override suspend fun createNewOrder(order: NewOrderPost): CreateNewOrder {
        return apiService.createNewOrder(order)
    }

    override suspend fun getCars(): GetCars {
        return apiService.getCars()
    }

    override suspend fun requestCar(car: RequestCarPost): RequestCar {
        return apiService.requestCar(car)
    }

    override suspend fun getOtherServices(): OtherServices {
        return apiService.getOtherServices()

    }

    override suspend fun changeNotifiStatus(status: Int, notification: String): GeneralResponse {
        return apiService.changeNotifiStatus(status, notification)
    }

    override suspend fun getUserDetails(): GetUserDetails {
        return apiService.getUserDetails()
    }


    override suspend fun requestDigitalService(
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
    ): DigitalService {
        return apiService.requestDigitalService(
            digital_id,
            name,
            email,
            mobile,
            priority,
            details,
            date_from,
            date_to,
            order_images,
            order_files
        )
    }

    override suspend fun getDigitals(): getDigitals {
        return apiService.getDigitals()
    }

    override suspend fun requestOtherService(request: RequestOtherServicePost): RequestOtherService {
        return apiService.requestOtherService(request)
    }

    override suspend fun requestService(request: RequestServicePost): RequestOtherService {
        return apiService.requestService(request)
    }

    override suspend fun getProductDetails(productId: Int): ProductDetails {
        return apiService.getProductDetails(productId)
    }

    override suspend fun addPaymentCard(request: AddPaymentCardPost): AddPaymentCard {
        return apiService.addPaymentCard(request)
    }

    override suspend fun getPaymentMethods(): GetPaymentMethod {
        return apiService.getPaymentMethods()
    }

    override suspend fun getMyPaymentCards(): GetMyPaymentCard {
        return apiService.getMyPaymentCards()
    }

    override suspend fun editPaymentCard(request: EditPaymentCardPost): AddPaymentCard {
        return apiService.editPaymentCard(request)
    }

    override suspend fun deletePaymentCard(cardId: Int): GeneralResponse {
        return apiService.deletePaymentCard(cardId)
    }

    override suspend fun nearbyStores(latitude: Long, longitude: Long): GetNearbyStores {
        return apiService.nearbyStores(latitude, longitude)
    }

    override suspend fun viewTatbeqakumProducts(activityId: Int): GetViewTatbeqakumProducts {
        return apiService.viewTatbeqakumProducts(activityId)
    }

    override suspend fun getMyCoupons(): GetMyCoupons {
        return apiService.getMyCoupons()
    }

    override suspend fun chargeWallet(amount: Int): ChargeWallet {
        return apiService.chargeWallet(amount)
    }

    override suspend fun addNewCoupon(coupon: String): AddNewCoupon {
        return apiService.addNewCoupon(coupon)
    }

    override suspend fun deleteCoupon(coupon_id: Int): GeneralResponse {
        return apiService.deleteCoupon(coupon_id)
    }

    override suspend fun searchProducts(text: String): SearchProduct {
        return apiService.searchProducts(text)
    }

    override suspend fun addNewAddress(
        latitude: Double,
        longitude: Double,
        address: String
    ): AddNewAddress {
        return apiService.addNewAddress(latitude, longitude, address)
    }

    override suspend fun editMyAddress(
        address_id: Int,
        latitude: Double,
        longitude: Double,
        address: String
    ): GeneralResponse {
        return apiService.editMyAddress(address_id, latitude, longitude, address)
    }

    override suspend fun deleteMyAddress(address_id: Int): GeneralResponse {
        return apiService.deleteMyAddress(address_id)
    }

    override suspend fun getMyAddresses(): GetAllBookAddress {
        return apiService.getMyAddresses()
    }

    override suspend fun checkout(
        coupon: String?,
        payment_method: String?,
        delivery_method: Int,
        user_address_id: Int
    ): Checkout {
        return apiService.checkout(coupon, payment_method, delivery_method, user_address_id)
    }

    override suspend fun getClientOrders(): GetClientOrders {
        return apiService.getClientOrders()
    }

    override suspend fun getClientOrderDetails(order_id: Int): GetClientOrderDetails {
        return apiService.getClientOrderDetails(order_id)
    }

    override suspend fun clientCancelOrder(order_id: Int, notes: String): GeneralResponse {
        return apiService.clientCancelOrder(order_id,notes)
    }

    override suspend fun searchOffers(text: String): SearchOffer {
        return apiService.searchOffers(text)
    }
}