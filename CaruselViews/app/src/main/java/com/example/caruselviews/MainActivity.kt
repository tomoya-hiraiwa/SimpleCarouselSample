package com.example.caruselviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.caruselviews.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    private val imageList = listOf<Int>(R.drawable.a,R.drawable.b,R.drawable.c,R.drawable.d,R.drawable.e)
    private lateinit var binding: ActivityMainBinding
    //ViewPagerの現在のポジションを保持しておく変数（初期の値は1にしておく）
    private var nowPosition = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("dataSize", "${imageList.size.toString()}")
        val tab = binding.indicator
        //tabのアイテムを作成
        createTab(binding.indicator,imageList.size)
        //オプション：タブのアイテムのクリックを無効にする
        for (i in 0 until tab.tabCount){
            val tab = tab.getTabAt(i)
            tab?.view?.isClickable = false
        }
        //viewPagerにアダプターを設定
        val adapter = caroucelAdapter(imageList)
        val pager = binding.caroucelpager
        pager.adapter = adapter
        //最初に表示するアイテムはpositionが1のアイテム
        pager.currentItem = 1
        //サイドに表示できるアイテム数を設定
        pager.offscreenPageLimit = 2
        //オプション：ViewPagerのページ遷移動作をカスタマイズする
        pager.setPageTransformer { page, position ->
            //サイドのアイテムのサイズ倍率
            val sizeRate = 1.0f
            //サイドのアイテムの表示幅とアイテム同士のマージンの幅を読み込み
            val margin = resources.getDimensionPixelOffset(R.dimen.margin)
            val off = resources.getDimensionPixelOffset(R.dimen.off)
            //ページ幅の計算(全体-マージンと見せ幅の合計 *2)
            val itemWidth = pager.width - (margin + off) * 2
            //アニメーションを定義
            page.translationX = -position * (2 * off + margin + (1 - sizeRate) * itemWidth / 2)
            //サイズを定義
            val scale = 1 - (kotlin.math.abs(position) * (1 - sizeRate))
            page.scaleX = scale
            page.scaleY = scale
        }
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            //ページが切り替わった時にインジケータの選択位置を変更する
            override fun onPageSelected(position: Int) {
                Log.d("Position", "$position")
                //通常時はposition-1の位置のアイテムを選択
                tab.getTabAt(position - 1)?.select()
                //現在のポジションを保持する変数も更新
                nowPosition = position
                Log.d("nowPosition", "$nowPosition")
                //もしpositionが0の時→つまり一番最後のアイテムに戻る必要がある時
                if (position == 0){
                    //表示データの数-1の位置を指定
                    tab.getTabAt(imageList.size - 1)?.select()
                    //nowPositionは一旦0にする
                    nowPosition = 0
                }
                //もしポジションがアダプターのアイテムの個数-1の時→最初のアイテムに戻る必要がある時
                else if (position == adapter.itemCount -1){
                    //一つ目なのでposition0を指定
                    tab.getTabAt(0)?.select()
                    //現在のポジションは一旦一番後ろを指定
                    nowPosition = adapter.itemCount -1
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //スワイプが終了した時に動作させる
                if (state == ViewPager2.SCROLL_STATE_IDLE){
                    //もしポジションが0→一番最後のアイテムに戻す
                    if (nowPosition == 0){
                        //一番最後のアイテムはadapterのアイテム数-2なことに注意
                        pager.setCurrentItem(adapter.itemCount-2 ,false )
                        //現在のポジションもそれに合わせる
                        nowPosition = adapter.itemCount -2
                    }
                    //もしポジションが最後→一番最初に戻る
                    else if (nowPosition == adapter.itemCount -1){
                        //最初の状態→ポジション1のアイテムを表示
                        pager.setCurrentItem(1,false)
                        //現在のポジションも1にする
                        nowPosition = 1
                    }
                }
            }
        })
    }
    //タブのアイテムをデータの個数分作成する
    private fun createTab(tab: TabLayout,count: Int){
        for(i in 0 until count){
        val tabItem = tab.newTab().apply {
                setIcon(R.drawable.tabselctor)
            }
            tab.addTab(tabItem)
        }
    }
}