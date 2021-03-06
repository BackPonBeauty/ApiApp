package jp.techacademy.masaya.ishihara.apiapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), FragmentCallback {

    private val viewPagerAdapter by lazy { ViewPagerAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // ViewPager2の初期化
        viewPager2.apply {
            adapter = viewPagerAdapter
            orientation = ViewPager2.ORIENTATION_HORIZONTAL // スワイプの向き横（ORIENTATION_VERTICAL を指定すれば縦スワイプで実装可能です）
            offscreenPageLimit = viewPagerAdapter.itemCount // ViewPager2で保持する画面数
        }

        TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
            tab.setText(viewPagerAdapter.titleIds[position])
        }.attach()
    }
    override fun onClickItem(shop: Shop) {
        val url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        val id : String = shop.id
        val imageUrl: String = shop.logoImage
        val name : String = shop.name
        val address : String = shop.address
        val which : Int = 1
        val intent = Intent(this, WebViewActivity::class.java)
//        Log.d("xxxxxxxx1", id)
        intent.putExtra("VA", id)
         WebViewActivity.start(this, url,id,imageUrl,name,address,which)
        finish()
    }

    override fun onClickItem1(favoriteShop: FavoriteShop) {
        val url =favoriteShop.url
        val id : String = favoriteShop.id
        val imageUrl: String = favoriteShop.imageUrl
        val name : String = favoriteShop.name
        val address : String = favoriteShop.address
        val which : Int = 2
        val intent = Intent(this, WebViewActivity::class.java)
        Log.d("xxxxxxxx2", id)
        intent.putExtra("VA", id)
      //  startActivity(intent)
        WebViewActivity.start(this, url,id,imageUrl,name,address,which)
        finish()
    }

    override fun onAddFavorite(shop: Shop) { // Favoriteに追加するときのメソッド(Fragment -> Activity へ通知する)
        FavoriteShop.insert(FavoriteShop().apply {
            id = shop.id
            name = shop.name
            imageUrl = shop.logoImage
            address = shop.address
            url = if (shop.couponUrls.sp.isNotEmpty()) shop.couponUrls.sp else shop.couponUrls.pc
        })
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
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
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_API] as ApiFragment).updateView()
        (viewPagerAdapter.fragments[VIEW_PAGER_POSITION_FAVORITE] as FavoriteFragment).updateData()
    }

    companion object {
        fun start(activity: Activity,key: String) {

            activity.startActivity(Intent(activity, MainActivity::class.java).putExtra(key,key))
        }

        private const val VIEW_PAGER_POSITION_API = 0
        private const val VIEW_PAGER_POSITION_FAVORITE = 1
    }
}