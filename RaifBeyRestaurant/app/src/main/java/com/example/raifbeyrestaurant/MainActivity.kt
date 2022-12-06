package com.example.raifbeyrestaurant

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.raifbeyrestaurant.API.TimeApi
import com.example.raifbeyrestaurant.API.TimeTurkey
import com.example.raifbeyrestaurant.API.Usd
import com.example.raifbeyrestaurant.API.UsdAPI
import com.example.raifbeyrestaurant.adapter.CategoryAdapter
import com.example.raifbeyrestaurant.adapter.ProductAdapter
import com.example.raifbeyrestaurant.models.Category
import com.example.raifbeyrestaurant.models.Product
import com.example.raifbeyrestaurant.services.ProductServices
import com.example.raifbeyrestaurant.services.UserServices
import com.example.raifbeyrestaurant.view.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_add.*
import kotlinx.android.synthetic.main.item_image.*
import kotlinx.android.synthetic.main.item_info.*
import kotlinx.android.synthetic.main.products_design.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

var categorys: ArrayList<Category>? = null
var searchProduct = ArrayList<Product>()
var products = ArrayList<Product>()
var favorites = ArrayList<String>()
var favoritesProducts = ArrayList<Product>()
var cartProducts = ArrayList<Product>()
var cartPrice = 0.0
var kurDurum = true
var kurDegeri = 1.0

class MainActivity : AppCompatActivity(), ProductAdapter.ClickListener,
    CategoryAdapter.ClickListener {
    private val auth = FirebaseAuth.getInstance()
    private var categoryLayoutManager: RecyclerView.LayoutManager? = null

    private var productLayoutManager: RecyclerView.LayoutManager? = null

    private var retrofitTime: Retrofit? = null
    private var timeApi: TimeApi? = null
    private val baseUrlTime = "http://worldtimeapi.org/api/timezone/"
    private var timeTurkeyCall: Call<TimeTurkey>? = null
    private var timeTurkey: TimeTurkey? = null
    private var time = "-1"

    private val baseUrlUsd = "https://api.genelpara.com/"
    private var usdApi: UsdAPI? = null
    private var retrofitUSD: Retrofit? = null
    private var usdCall: Call<Usd>? = null
    private var usd: Usd? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        loadData()
        worldTimeAPI()
        usdAPI()
        kurKontrol()
        search_view.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            View.OnFocusChangeListener {
            override fun onQueryTextSubmit(search: String?): Boolean {

                return false
            }

            override fun onQueryTextChange(search: String?): Boolean {
                if (search?.trim() == null) {
                    toastMessage(search!!)
                }
                searchProduct.clear()
                search.trim()
                for (product in products) {
                    if (product.get_name().toLowerCase().contains(search.toLowerCase())) {
                        searchProduct.add(product)
                    }
                }
                searchList()
                return true
            }

            override fun onFocusChange(p0: View?, p1: Boolean) {
                TODO("Not yet implemented")
            }


        })
        cvKur.setOnClickListener {
            if (kurDurum) {
                tvKur.text = "$"
                kurDurum = false
                productLayoutManager = GridLayoutManager(this, 2)
                val recyclerViewProduct: RecyclerView? =
                    findViewById<RecyclerView>(R.id.main_product)
                val productAdapter = ProductAdapter(products!!, this, this)
                recyclerViewProduct?.adapter = productAdapter
                recyclerViewProduct?.layoutManager = productLayoutManager
                // cvKur.setCardBackgroundColor(R.color.red)
            } else {
                tvKur.text = "₺"
                kurDurum = true
                productLayoutManager = GridLayoutManager(this, 2)
                val recyclerViewProduct: RecyclerView? =
                    findViewById<RecyclerView>(R.id.main_product)
                val productAdapter = ProductAdapter(products!!, this, this)
                recyclerViewProduct?.adapter = productAdapter
                recyclerViewProduct?.layoutManager = productLayoutManager
                // cvKur.setCardBackgroundColor(R.color.green)
            }
        }
        //loadDataProducts()
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun toastMessage(search: String) {
        Toast.makeText(this, search, Toast.LENGTH_LONG).show()
    }

    private fun searchList() {
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById<RecyclerView>(R.id.main_product)
        val productAdapter = ProductAdapter(searchProduct, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager
    }


    private fun kurKontrol() {
        if (kurDurum) {
            tvKur.text = "₺"
            // cvKur.setCardBackgroundColor(R.color.green)
        } else {
            tvKur.text = "$"
            // cvKur.setCardBackgroundColor(R.color.red)
        }
    }


    private fun loadData() {

        System.out.println(categorys!!.size)
        categoryLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val recyclerViewCategory: RecyclerView? = findViewById<RecyclerView>(R.id.main_category)
        val categoryAdapter = CategoryAdapter(categorys!!, this)
        recyclerViewCategory?.adapter = categoryAdapter
        recyclerViewCategory?.layoutManager = LinearLayoutManager(this)
        recyclerViewCategory?.layoutManager = categoryLayoutManager



        System.out.println(products!!.size)
        productLayoutManager = GridLayoutManager(this, 2)
        val recyclerViewProduct: RecyclerView? = findViewById<RecyclerView>(R.id.main_product)
        val productAdapter = ProductAdapter(products!!, this, this)
        recyclerViewProduct?.adapter = productAdapter
        recyclerViewProduct?.layoutManager = productLayoutManager

    }


    private fun usdAPI() {
        retrofitUSD = Retrofit.Builder()
            .baseUrl(baseUrlUsd)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        usdApi = retrofitUSD?.create(UsdAPI::class.java)
        usdCall = usdApi?.getDolar()

        usdCall!!.enqueue(object : Callback<Usd?> {

            override fun onResponse(call: Call<Usd?>, response: Response<Usd?>) {
                if (response.isSuccessful) {
                    usd = response.body()
                    kurDegeri = usd?.dolar!!.toDouble()
                    println(kurDegeri)
                }
            }

            override fun onFailure(call: Call<Usd?>, t: Throwable) {
                println(t.toString())
            }


        })

    }

    fun worldTimeAPI() {

        retrofitTime = Retrofit.Builder()
            .baseUrl(baseUrlTime)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        timeApi = retrofitTime?.create(TimeApi::class.java)
        timeTurkeyCall = timeApi?.getTime()
        timeTurkeyCall!!.enqueue(object : Callback<TimeTurkey?> {
            override fun onResponse(call: Call<TimeTurkey?>, response: Response<TimeTurkey?>) {
                if (response.isSuccessful) {
                    timeTurkey = response.body()
                    var timeIndex: Int = timeTurkey?.getDateTime()?.indexOf("T")!!
                    time = timeTurkey?.getDateTime()?.substring(timeIndex + 1, timeIndex + 3)
                        .toString()
                    changeTitle()
                }
            }

            override fun onFailure(call: Call<TimeTurkey?>, t: Throwable) {
                println(t.toString())
            }
        })

    }

    fun changeTitle() {
        when (time) {

            "01", "02", "03", "04", "05", "06" -> tvTitle.text =
                auth.currentUser!!.displayName + ", İyi Geceler"

            "07", "08", "09", "10", "11", "12" -> tvTitle.text = auth.currentUser!!.displayName + ", Günaydın."

            "13", "14", "15", "16", "17", "18" -> tvTitle.text =
                auth.currentUser!!.displayName + ", İyi Günler."
            "19", "20", "21", "22", "23", "24", "00" -> tvTitle.text =
                auth.currentUser!!.displayName +", İyi Geceler."

            else -> tvTitle.text =
                auth.currentUser!!.displayName + ", İyi Günler(time)"

        }

    }

    fun goToProfile(view: View) {
        val intent = Intent(this@MainActivity, ProfileActivity::class.java)
        startActivity(intent)
    }
    fun goToCart(view: View) {
        val intent = Intent(this@MainActivity, CartActivity::class.java)
        startActivity(intent)
    }

    fun goToFavorites(view: View) {
        val intent = Intent(this@MainActivity, FavoritesActivity::class.java)
        startActivity(intent)
    }

    override fun onClick(product: Product) {
        val intent = Intent(this, ProductDetails::class.java)
        intent.putExtra("productId", product.get_id())
        intent.putExtra("productName", product.get_name())
        intent.putExtra("productDescription", product.get_description())
        intent.putExtra("productPrice", product.get_price())
        intent.putExtra("productCategoryId", product.categoryId)
        intent.putExtra("productStorageName", product.get_storageName())
        startActivity(intent)
    }

    override fun addCart(product: Product) {
        println(cartProducts.size)
    }

    override fun addFavorites(product: Product) {
        println(favoritesProducts.size)
    }

    override fun onClick(category: Category) {
        Toast.makeText(this, "categoryId ${category.get_id()}", Toast.LENGTH_LONG).show()
        val intent = Intent(this, CategoryByIdProducts::class.java)
        intent.putExtra("categoryId", category.get_id())
        intent.putExtra("categoryName", category.get_name())
        startActivity(intent)
    }


    /* private fun loadDataProducts() {
       var addProducts: ArrayList<Product>? = null
       //Çorba 4
       ProductServices.addProduct(
           Product(
               "Ezogelin Çorbası",
               "domates, pirinç ve kırmızı mercimek ile yapılan bir çorba",
               "20",
               "1"
           )
       )

       ProductServices.addProduct(
           Product(
               "Mercimek Çorbası",
               "ana malzemesi mercimek olan bir çorbadır",
               "20",
               "1"
           )
       )

       ProductServices.addProduct(
           Product(
               "Tavuk Suyu Çorbası",
               "haşlanmış tavuğun suyundan yapılan,şehriye ya da pirinç içeren,yağlı,kötü kokulu,gereksiz sıvı gıda.",
               "25",
               "1"
           )
       )

       ProductServices.addProduct(
           Product(
               "Bamya Çorbası",
               "orta Anadolu'da yapılan bamya çorbası Konya'da klasik menülerde ara yemeği olarak önem arz eder.",
               "25",
               "1"
           )
       )


       //Döner  4
       ProductServices.addProduct(
           Product(
               "Et Döner Dürüm",
               "100gr et döner",
               "60",
               "2"
           )
       )
       ProductServices.addProduct(

           Product(
               "Pilav Üstü Et Döner",
               "120gr et döner",
               "70",
               "2"
           )
       )
       ProductServices.addProduct(

           Product(
               "Tombik Ekmek Arası Döner",
               "90gr et döner",
               "55",
               "2"
           )
       )
       ProductServices.addProduct(
           Product(
               "İskender",
               "100gr et döner",
               "80",
               "2"
           )
       )


       //Burger 7

       ProductServices.addProduct(
           Product(
               "Chessburger",
               "chedar peynirli burger",
               "75",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Double Burger",
               "double köfteli burger",
               "80",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Falafel Burger",
               "falafelli burger",
               "85",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Lokum Burger",
               "120gr et lokum burger",
               "120",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Special Burger",
               "special burger",
               "90",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Steak Burger",
               "et burger",
               "120",
               "3"
           )
       )
       ProductServices.addProduct(
           Product(
               "Şnitzel Burger",
               "şnitzelli burger",
               "60",
               "3"
           )
       )



       //Kebap 4
       ProductServices.addProduct(
           Product(
               "Adana Kebap",
               "adana usulü kebap",
               "85",
               "4"
           )
       )
       ProductServices.addProduct(
           Product(
               "Urfa Kebap",
               "urfa usulü kebap",
               "85",
               "4"
           )
       )
       ProductServices.addProduct(
           Product(
               "Karışık Kebap",
               "tüm kebap çeşitleri bir arada",
               "95",
               "4"
           )
       )
       ProductServices.addProduct(
           Product(
               "Köşk Kebap",
               "kebap",
               "85",
               "4"
           )
       )


       //Pizza 5
       ProductServices.addProduct(
           Product(
               "Büyük Boy Pizza",
               "büyük boy pizza",
               "85",
               "5"
           )
       )
       ProductServices.addProduct(
           Product(
               "Küçük Boy Pizza",
               "küçük boy pizza",
               "65",
               "5"
           )
       )
       ProductServices.addProduct(
           Product(
               "Orta Boy Pizza",
               "orta boy pizza",
               "75",
               "5"
           )
       )
       ProductServices.addProduct(
           Product(
               "Karışık Pizza",
               "sucuk mantar  kaşar zeytinli pizza",
               "85",
               "5"
           )
       )
       ProductServices.addProduct(
           Product(
               "Sucuklu Pizza",
               "sadece sucuklu",
               "85",
               "5"
           )
       )




       //Tost&Sandviç  7
       ProductServices.addProduct(
           Product(
               "Ayvalık Tost",
               "ayvalık usulü tost",
               "45",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Bol Peynirli Tost",
               "bol peynirli sucuklu tost",
               "45",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Jambonlu Sandviç",
               "4 dilim jambonlu sandviç",
               "55",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Karışık Tost",
               "sucuk ve kaşarlı tost",
               "45",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Soğuk Sandviç",
               "domates peynir salatalıklı soğuk sandviç",
               "40",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Sucuklu Tost",
               "bol sucuklu tost",
               "45",
               "6"
           )
       )
       ProductServices.addProduct(
           Product(
               "Ton Balıklı Sandviç",
               "hazır ton balıklı tost",
               "60",
               "6"
           )
       )



       //salata 4
       ProductServices.addProduct(
           Product(
               "Beyaz Peynirli Akdeniz Salata",
               "akdeniz yöresine ait beyaz peynirli salata",
               "35",
               "7"
           )
       )
       ProductServices.addProduct(
           Product(
               "Gavurdağ Salata",
               "soslu salata",
               "30",
               "7"
           )
       )
       ProductServices.addProduct(
           Product(
               "Mevsim Salata",
               "mevsim sebzeleriyle yapılan salata",
               "35",
               "7"
           )
       )
       ProductServices.addProduct(
           Product(
               "Çoban Salata",
               "salatalık domates ve soğanlı çoban salata",
               "35",
               "7"
           )
       )


       //Tatlı  7
       ProductServices.addProduct(
           Product(
               "Cevizli Baklava",
               "4 dilimli cevizli baklava",
               "30",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "Fıstıklı Baklava",
               "4 dilimli fıstıklı baklava",
               "30",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "Katmer",
               "katmer",
               "35",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "KaymaklI Fıstıklı Kadayıf",
               "ekstra kaymaklı fıstıklı kadayıf",
               "45",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "Künefe",
               "1 porsiyon künefe",
               "60",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "Soğuk Baklava",
               "4 dilimli soğuk sütlü baklava",
               "30",
               "8"
           )
       )
       ProductServices.addProduct(
           Product(
               "Çikolatalı Baklava",
               "4 dilimli çikolatalı baklava",
               "35",
               "8"
           )
       )



       //İçecekler 6
       ProductServices.addProduct(
           Product(
               "Fanta",
               "330mL kutu fanta",
               "15",
               "9"
           )
       )
       ProductServices.addProduct(
           Product(
               "Kola",
               "330mL kutu kola",
               "15",
               "9"
           )
       )
       ProductServices.addProduct(
           Product(
               "Maden Suyu",
               "mineralli maden suyu",
               "10",
               "9"
           )
       )
       ProductServices.addProduct(
           Product(
               "Sprite",
               "330mL kutu gazoz",
               "15",
               "9"
           )
       )
       ProductServices.addProduct(
           Product(
               "Su",
               "0.5mL soğuk su",
               "7",
               "9"
           )
       )
       ProductServices.addProduct(
           Product(
               "Çay",
               "çay bardağında çay",
               "4",
               "9"
           )
       )
   }*/
}


