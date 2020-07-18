package com.hao.easy.user.ui.activity

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import com.hao.easy.base.extensions.init
import com.hao.easy.base.extensions.snack
import com.hao.easy.base.ui.BaseActivity
import com.hao.easy.base.ui.WebActivity
import com.hao.easy.user.R
import com.hao.easy.user.model.Menu
import com.hao.easy.user.ui.adapter.AboutAdapter
import kotlinx.android.synthetic.main.user_activity_about.*


class AboutActivity : BaseActivity() {

    companion object {
        const val HOME = "https://haoshiy.github.io"
        const val ANDROID_PROJECT_LINK = "https://github.com/haoshiy/kotlin_wanandroid"
        const val WECHAT_PROJECT_LINK = "https://github.com/haoshiy/wechat_wanandroid"
        const val DOWNLOAD_LINK = "https://fir.im/wanAndroid"
        const val THANKS = "https://wanandroid.com/blog/show/2"
    }

    override fun getLayoutId() = R.layout.user_activity_about

    override fun initView() {
        title = "關於項目"
        tvVersion.text = "V" + getVersionName()
        val list = ArrayList<Menu>()
        list.add(Menu("個人主頁", HOME, "個人主頁", HOME))
        list.add(Menu("Android項目鏈接", ANDROID_PROJECT_LINK, "玩Android", ANDROID_PROJECT_LINK))
        list.add(Menu("小程序項目鏈接", WECHAT_PROJECT_LINK, "玩Android", WECHAT_PROJECT_LINK))
        list.add(Menu("Apk下載地址", DOWNLOAD_LINK, "", DOWNLOAD_LINK))
        list.add(Menu("Thanks", "鸿洋-玩Android开发Api", "鸿洋-玩Android开发Api", THANKS))

        val adapter = AboutAdapter(list)
        adapter.itemClickListener = { _, item, _ ->
            if (item.link == DOWNLOAD_LINK) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(DOWNLOAD_LINK)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            } else {
                WebActivity.start(this, item.webTitle, item.link)
            }
        }
        adapter.itemLongClickListener = { _, item, _ ->
            copy(item.link)
            true
        }
        recyclerView.init(adapter)
    }

    private fun copy(text: String) {
        val clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text))
        recyclerView.snack("已複製到剪粘板")
    }

    private fun getVersionName(): String {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, 0)
            packageInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            ""
        }
    }
}
