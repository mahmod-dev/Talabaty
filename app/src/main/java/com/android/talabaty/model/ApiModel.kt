package com.android.talabaty.model




data class Users(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User
)

data class ForgotPassword(
    val code: Int,
    val message: String,
    val status: Boolean
)


data class Login(
    val code: Int,
    val status: Boolean,
    val user: User
)

data class User(
    val access_token: String,
    val activity: Any,
    val activity_id: Any,
    val address: Any,
    val available: Int,
    val bio: Any,
    val car_license: String,
    val cars_count: Any,
    val city: Any,
    val city_id: Any,
    val country: Any,
    val country_id: Any,
    val created_at: String,
    val discount_percent: Any,
    val email: String,
    val id: Int,
    val image_profile: String,
    val images: List<Any>,
    val latitude: Any,
    val longitude: Any,
    val mobile: String,
    val name: String,
    val `open`: Int,
    val owner_id_card: String,
    val provider_id_card: String,
    val provider_license: String,
    val rate: Int,
    val remember_token: Any,
    val reviews: List<Any>,
    val status: String,
    val time_from: Any,
    val time_to: Any,
    val type: Int,
    val user_categories: List<Any>
)

data class Countries(
    val code: Int,
    val countries: List<Country>,
    val message: String,
    val status: Boolean
)

data class ContactUsMsg(
    val code: Int,
    val contact_msg: Msg,
    val message: String,
    val status: Boolean
)

data class Msg(
    val created_at: String,
    val id: Int,
    val message: String,
    val read: Int,
    val title: String,
    val user_id: Int
)

data class GetUserDetails(
    val `as`: String,
    val city: String,
    val country: String,
    val countryCode: String,
    val isp: String,
    val lat: Double,
    val lon: Double,
    val org: String,
    val query: String,
    val region: String,
    val regionName: String,
    val status: String,
    val timezone: String,
    val zip: String
)

data class GeneralResponse(
    val code: Int,
    val message: String,
    val status: Boolean
)

data class UserPost(
    val name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val fcm_token: String,
    val device_type: String,
    val latitude: Double,
    val longitude: Double
)

data class SignUpPost(
    var name: String,
    val email: String,
    val mobile: String,
    val password: String,
    val fcm_token: String,
    val device_type: String = "android"
)

data class LoginPost(
    val email: String,
    val password: String,
    val fcm_token: String,
    val device_type: String = "android"
)

data class EditProfile(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User,
    val validator: String
)

data class CheckCode(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User
)

data class StoresFreeDelivery(
    val code: Int,
    val message: String,
    val status: Boolean,
    val storesFreeDelivery: List<FreeDelivery>
)

data class FreeDelivery(
    val activity: Activity,
    val activity_id: Int,
    val address: String,
    val available: Int,
    val bio: String,
    val car_license: String,
    val cars_count: Int,
    val city: City,
    val city_id: Int,
    val country: Country,
    val country_id: Int,
    val created_at: String,
    val delivery_cost: Int,
    val discount_percent: Int,
    val email: String,
    val id: Int,
    val image_profile: String,
    val images: List<Image>,
    val latitude: Double,
    val longitude: Double,
    val mobile: String,
    val name: String,
    val `open`: Int,
    val owner_id_card: String,
    val provider_id_card: String,
    val provider_license: String,
    val rate: Int,
    val remember_token: Any,
    val reviews: List<Any>,
    val status: String,
    val time_from: String,
    val time_to: String,
    val type: Int,
    val user_categories: List<UserCategory>
)

data class Activity(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val status: String
)


data class SignUp(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User
)

data class ChangeUserStatus(
    val code: Int,
    val message: String,
    val status: Boolean,
    val user: User
)

data class Faq(
    val code: Int,
    val faq: List<FaqX>,
    val message: String,
    val status: Boolean
)

data class FaqX(
    val answer: String,
    val created_at: String,
    val id: Int,
    val question: String,
    val status: String
)

data class Categories(
    val categories: ArrayList<Category>,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class Cities(
    val cities: List<City>,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class City(
    val country_id: Int,
    val created_at: String,
    val id: Int,
    val name: String,
    val status: String
)

data class CreateNewOrder(
    val code: Int,
    val message: String,
    val order: Order,
    val status: Boolean
)

data class SpinnerObj(
    val id: Int,
    val name: String



){
    override fun toString(): String {
        return name
    }
}


data class NewOrderPost(
    val category_id: Int,
    val details: String,
    val ordered_date: String,
    val from_latitude: Long,
    val from_longitude: Long,
    val to_latitude: Long,
    val to_longitude: Long,
    val orders_delivery_cost: Double
)

data class RequestCarPost(
    val car_id: Int,
    val details: String,
    val from_latitude: Long,
    val from_longitude: Long,
    val to_latitude: Long,
    val to_longitude: Long,
    val request_car_cost: Double
)

data class Order(
    val category_id: String,
    val cost: String,
    val created_at: String,
    val details: String,
    val from_latitude: String,
    val from_longitude: String,
    val id: Int,
    val ordered_date: String,
    val to_latitude: String,
    val to_longitude: String,
    val user_id: Int,
    val car_id: Int
)

data class FavProducts(
    val products: ArrayList<Product>,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class Ads(
    val ads: List<Ad>,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class ViewStores(
    val code: Int,
    val message: String,
    val offers: Offer,
    val status: Boolean,
    val stores: ArrayList<Store>
)

data class OtherServices(
    val code: Int,
    val digitals: List<Digital>,
    val message: String,
    val status: Boolean
)

data class Digital(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val status: String,
    val type: String
)
data class GetOffers(
    val code: Int,
    val message: String,
    val offers: List<Offer>,
    val status: Boolean
)

data class Offer(
    val category: Category,
    val category_id: Int,
    val description: String,
    val id: Int,
    val image: String,
    val is_favorite: Int,
    val name: String,
    val offer_price: Int,
    val price: Int,
    val rate: Int,
    val status: String,
    val store: Store,
    val store_id: Int,
    val delivery_cost: Int
)


data class Store(
    val myActivity: MyActivity,
    val activity_id: Int,
    val address: String,
    val available: Int,
    val bio: String,
    val car_license: String,
    val cars_count: Int,
    val city: City,
    val city_id: Int,
    val country: Country,
    val country_id: Int,
    val created_at: String,
    val discount_percent: Int,
    val email: String,
    val id: Int,
    val image_profile: String,
    val images: List<Image>,
    val latitude: Double,
    val longitude: Double,
    val mobile: String,
    val name: String,
    val `open`: Int,
    val owner_id_card: String,
    val provider_id_card: String,
    val provider_license: String,
    val rate: Int,
    val remember_token: Any,
    val reviews: List<Review>,
    val status: String,
    val time_from: String,
    val time_to: String,
    val type: Int,
    val delivery_cost: Int,
    val user_categories: List<UserCategory>
)

data class StoreProducts(
    val code: Int,
    val message: String,
    val products: ArrayList<Product>,
    val status: Boolean
)

data class ProductEx(
    val id: Int,
    val description: String,
    val image: String,
    val is_favorite: Int,
    val name: String,
    val price: Int
)


data class Product(
    val category: Category,
    val category_id: Int,
    val description: String,
    val id: Int,
    val image: String,
    val is_favorite: Int,
    val name: String,
    val offer_price: Int,
    val price: Int,
    val rate: Int,
    val in_cart: Int,
    val status: String,
    val store: Store,
    val store_id: Int
)

data class MyOrders(
    val code: Int,
    val message: String,
    val myOrders: List<MyOrder>,
    val status: Boolean
)

data class MyOrder(
    val car: Any,
    val car_id: Int,
    val category: Category,
    val category_id: Int,
    val cost: Int,
    val created_at: String,
    val details: String,
    val from_latitude: String,
    val from_longitude: String,
    val id: Int,
    val offers: List<Offer>,
    val ordered_date: String,
    val provider: Any,
    val status: Int,
    val to_latitude: String,
    val to_longitude: String,
    val user: User,
    val user_id: Int
)

data class HomePageCategories(
    val code: Int,
    val home_page_categories: List<HomePageCategory>,
    val message: String,
    val status: Boolean,
    val settings: Settings

)

data class HomePageCategory(
    val created_at: Any,
    val id: Int,
    val image: String,
    val name: String,
    val status: String,
    val type: String
)

data class MyCart(
    val cart: ArrayList<Cart>,
    val code: Int,
    val message: String,
    val status: Boolean
)


data class ChangeQuantity(
    val cart: Cart,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class ProductToFav(
    val code: Int,
    val message: String,
    val status: Boolean
)

data class AddProductToCart(
    val cart: Cart,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class Cart(
    val created_at: String,
    val id: Int,
    val product_id: String,
    val quantity: String,
    val store_id: Int,
    val user_id: Int,
    val store: Store,
    val product: Product
)

data class StoreDetails(
    val code: Int,
    val message: String,
    val status: Boolean,
    val store: Store
)


data class Activities(
    val activities: ArrayList<MyActivity>,
    val code: Int,
    val message: String,
    val status: Boolean
)


data class Image(
    val created_at: Any,
    val details: String,
    val id: Int,
    val image: String,
    val status: String,
    val user_id: Int
)

data class Review(
    val created_at: String,
    val details: String,
    val id: Int,
    val provider_id: Int,
    val rate: Int,
    val status: String,
    val user: User,
    val user_id: Int
)


data class UserCategory(
    val category: Category,
    val category_id: Int,
    val created_at: String,
    val deleted_at: Any,
    val id: Int,
    val status: String,
    val updated_at: String,
    val user_id: Int
)

data class MainSettings(
    val code: Int,
    val message: String,
    val status: Boolean,
    val items: Settings
)


data class Settings(
    val aboutUs: AboutUs,
    val myActivities: List<MyActivity>,
    val address: String,
    val ads: List<Ad>,
    val app_store_url: String,
    val categories: List<Category>,
    val countries: List<Country>,
    val description: String,
    val facebook: String,
    val id: Int,
    val image: String,
    val info_email: String,
    val instagram: String,
    val latitude: String,
    val linked_in: String,
    val logo: String,
    val longitude: String,
    val mobile: String,
    val phone: String,
    val play_store_url: String,
    val privacy: Privacy,
    val terms: Terms,
    val title: String,
    val twitter: String,
    val url: String,
    val request_car_cost: Double,
    val orders_delivery_cost: Double
)

data class RequestCar(
    val code: Int,
    val message: String,
    val order: Order,
    val status: Boolean
)

data class GetCars(
    val cars: List<Car>,
    val code: Int,
    val message: String,
    val status: Boolean
)

data class Car(
    val created_at: String,
    val id: Int,
    val name: String,
    val status: String
)



data class AboutUs(
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val key_words: String,
    val title: String,
    val views: Int
)

data class MyActivity(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val status: String
)

data class Ad(
    val created_at: String,
    val details: String,
    val id: Int,
    val image: String,
    val link: String,
    val status: String,
    val title: String
)

data class Category(
    val created_at: String,
    val id: Int,
    val image: String,
    val name: String,
    val status: String
)

data class Country(
    val created_at: String,
    val id: Int,
    val name: String,
    val status: String
)

data class Privacy(
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val key_words: String,
    val title: String,
    val views: Int
)

data class Terms(
    val created_at: String,
    val description: String,
    val id: Int,
    val image: String,
    val key_words: String,
    val title: String,
    val views: Int
)