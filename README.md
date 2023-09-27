# SimpleCarouselSample
**無限ループするカルーセルです**<br>
・viewPager2  
・TabLayout  

を使用して作成しています。

##1.Adapterの作成

###caroucelAdapter.kt
・ポイント１　実際のアイテム数＋２した値の準備

'''kotlin
 //実際のアイテム数+2した数を準備しておく
    private val addCount = imageList.size+2
'''

ポイント2　onBindViewHolderでの実際のポジションの取得方法
'''kotlin
  val dataPosition = when(position){
            //0の時→一番最後のアイテムを指定
            0 -> getRealCount() - 1
            //実際のアイテムの個数を超えた時→最初のアイテムを指定
            getRealCount() + 1 -> 0
            //それ以外は実際のポジションから-1したアイテムを指定（アダプターのポジションが1を基準点にしているため）
            else  -> position -1
        }
'''

##2.viewPagerへの実装

###MainActivity.kt

・ポイント1.アダプターの選択位置は初期で1にしておく
・ポイント2.選択されているポジションの位置を保持しておく変数を用意する

'''kotlin
 //最初に表示するアイテムはpositionが1のアイテム
        pager.currentItem = 1
  //ViewPagerの現在のポジションを保持しておく変数（初期の値は1にしておく）
    private var nowPosition = 1
'''

・ポイント3　カルーセルのインジケータとして使用しているタブの選択位置の処理

'''kotlin
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
  '''

  ・ポイント4　ポジションが端にいった時の処理（画面移動が完了した後にアニメーションなしで正しい位置に変更し直すことで、無限にループしているように見える）

  '''kotlin
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
  '''

  ##おまけ
  ###・tabのアイテムのクリック無効化
  ・各タブアイテムごとに無効化処理を行う必要があることに注意する

    '''kotlin
 //オプション：タブのアイテムのクリックを無効にする
        for (i in 0 until tab.tabCount){
            val tab = tab.getTabAt(i)
            tab?.view?.isClickable = false
        }
  '''

  ###・左右に次のアイテムを表示させてよりカルーセルのような見た目に近づける

  1.res/valuesにdimenファイル作成、all_marginを定義しておく

  2.カルーセルに使用するレイアウトファイルのmarginStart,marginEndにそれぞれall_marginをつける


  3.カルーセルのカスタムをコードで記述する

 ・MainActivity.kt

 '''kotlin
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
'''


   





