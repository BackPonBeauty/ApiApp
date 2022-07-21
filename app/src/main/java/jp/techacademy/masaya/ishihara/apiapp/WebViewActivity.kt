package jp.techacademy.masaya.ishihara.apiapp


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_web_view.*

var public_id: String = ""
var public_imgUrl: String = ""
var public_name: String = ""
var public_url: String = ""
var public_address: String = ""
var public_which: Int = 1
class WebViewActivity: AppCompatActivity(),FragmentCallback{

    private val apiAdapter by lazy { ApiAdapter(this) }
    private val favoriteAdapter by lazy { FavoriteAdapter(this) }
    private val itemsa = mutableListOf<Shop>()
    private val items = mutableListOf<FavoriteShop>()
    // 一覧画面から登録するときのコールバック（FavoriteFragmentへ通知するメソッド)
    var onClickAddFavorite: ((FavoriteShop) -> Unit)? = null
    // 一覧画面から削除するときのコールバック（ApiFragmentへ通知するメソッド)
    var onClickDeleteFavorite: ((FavoriteShop) -> Unit)? = null

    private val isFavorite = FavoriteShop.findBy(public_id) != null

    fun refresh(list: List<FavoriteShop>) {
        update(list, false)
        startActivity(Intent(this, WebViewActivity::class.java).putExtra(KEY_URL, public_url))
        favoriteAdapter.notifyDataSetChanged()
        finish();
    }

    fun update(list: List<FavoriteShop>, isAdd: Boolean) {
        items.apply {
            if(!isAdd){ // 追加のときは、Clearしない
                clear() // items を 空にする
            }
            addAll(list) // itemsにlistを全て追加する
        }
    }
 //   override fun onBackPressed() {
 //       favoriteAdapter.notifyDataSetChanged()
 //       finish();
 //   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)
        webView.loadUrl(intent.getStringExtra(KEY_URL).toString())
      //  val idd = intent.getStringExtra("VA")
     //   textView.text = idd

        val data = items
        Log.d("zzzzzzzzzzzzzz", public_id.toString())

        favoriteImageView.setImageResource(if (isFavorite) R.drawable.ic_star else R.drawable.ic_star_border)
        if (isFavorite){
            FavoriteShop.findAll()
        }
        favoriteImageView.setOnClickListener {
            if (isFavorite) {
                onDeleteFavorite(public_id)
            } else {
                addFavorite()
            }
        }
    }


    companion object {
        private const val KEY_URL = "key_url"
        fun start(activity: Activity, url: String , id: String , imageUrl: String ,name: String ,address: String, which: Int) {
            activity.startActivity(Intent(activity, WebViewActivity::class.java).putExtra(KEY_URL, url))
     //       Log.d("zzzzzzzzzzzzzz", id)
            public_id = id
            public_imgUrl = imageUrl
            public_name = name
            public_url = url
            public_address = address
            public_which = which
        }
        private const val VIEW_PAGER_POSITION_API = 0
        private const val VIEW_PAGER_POSITION_FAVORITE = 1
    }


    override fun onClickItem(shop: Shop) {
        TODO("Not yet implemented")
    }

    override fun onClickItem1(shop: FavoriteShop) {
        TODO("Not yet implemented")
    }


    override fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        TODO("Not yet implemented")
    }
    private fun addFavorite(){
        FavoriteShop.insert(FavoriteShop().apply {
            id = public_id
            name = public_name
            imageUrl = public_imgUrl
            address = public_address
            url = public_url
        })
        favoriteImageView.setImageResource(R.drawable.ic_star)
  //      if(public_which == 1){
    //        refresh1(itemsa)
    //    }else{
            refresh(FavoriteShop.findAll())
    //    }
    }
    override fun onDeleteFavorite(id: String) { // Favoriteから削除するときのメソッド(Fragment -> Activity へ通知する)
      showConfirmDeleteFavoriteDialog(id)
    }


    private fun showConfirmDeleteFavoriteDialog(id: String) {
        AlertDialog.Builder(this)
            .setTitle(R.string.delete_favorite_dialog_title)
            .setMessage(R.string.delete_favorite_dialog_message)
            .setPositiveButton(android.R.string.ok) { _, _ ->
                deleteFavorite(id)
            }
            .setNegativeButton(android.R.string.cancel) { _, _ ->}
            .create()
            .show()
    }

    private fun deleteFavorite(id: String) {
      FavoriteShop.delete(id)
      favoriteImageView.setImageResource(R.drawable.ic_star_border)
   //   ApiFragment.updateData()
  //    FavoriteFragment.updateData()
   //     if(public_which == 2){
    //        refresh1(itemsa)
   //     }else{
            refresh(FavoriteShop.findAll())
   //     }
    }
}