package uz.mlsoft.newmypuzzle15.screens

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import uz.mlsoft.newmypuzzle15.databinding.ActivityInfoBinding
import uz.mlsoft.newmypuzzle15.utils.underLine

class InfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInfoBinding
    var content = "<div>\n" +
            "        <h1>\n" +
            "            \tZoo puzzle 15\n" +
            "        </h1>\n" +
            "        \n" +
            "        <ul>\n" +
            "            <li>\n" +
            "                \tHow can you play this Zoo puzzle 15 game?\n" +
            "            </li> \n" +
            "            <li>\n" +
            "               \tInitially, you see a splash screen, after that, there is a start screen and there you can touch play button. After that, you can start a game. By touching buttons, you can change positions of them" +
            ". If you can put all buttons by order, you will win and restart a game. \n" +
            "            </li> \n" +
            "            <li>\n" +
            "                \tBy touching share and rate button, you can absolutely rate my app and share absolutely! \n" +
            "            </li>\n" +
            "            <li>\n" +
            "               \tIn this app, I used so many web sites to dowloand icons and lottie animtions \n" +
            "            </li>\n" +
            "            <li>\n" +
            "               \tThey are:lottiefiles.com, flaticon.com\n" +
            "            </li>\n" +
            "        </ul>\n" +
            "        <div>\n" +
            "            <h3>\n" +
            "                Framework:\n" +
            "            </h3>\n" +
            "            <ul>\n" +
            "                <li>\n" +
            "                    <b>\n" +
            "                        Android Studio\n" +
            "                    </b>\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                    <b>\n" +
            "                        Kotlin and Java\n" +
            "                    </b>\n" +
            "                </li>\n" +
            "                <li>\n" +
            "                    <div>\n" +
            "                        <h3>\n" +
            "                            Used technologies:\n" +
            "                        </h3>\n" +
            "                        <ul>\n" +
            "                            <li>\n" +
            "                                Media Player\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                Chronometer\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                Lottie animation\n" +
            "                            </li>\n" +
            "                            <li>\n" +
            "                                Shared Prefernce\n" +
            "                            <li>\n" +
            "                                Saved Instance\n" +
            "                            </li>\n" +
            "                        </ul>\n" +
            "                    </div>\n" +
            "                </li>\n" +
            "            </ul>\n" +
            "        </div>\n" +
            "    </div>"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

//        val orientation = this.resources.configuration.orientation
//        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Glide.with(this).load(R.drawable.info_back).into(object : SimpleTarget<Drawable?>() {
//                override fun onResourceReady(
//                    resource: Drawable,
//                    transition: Transition<in Drawable?>?
//                ) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        binding.layoutInfo.setBackgroundDrawable(resource);
//                    }
//                }
//
//            })
//        } else {
//            Glide.with(this).load(R.drawable.info_back_land)
//                .into(object : SimpleTarget<Drawable?>() {
//                    override fun onResourceReady(
//                        resource: Drawable,
//                        transition: Transition<in Drawable?>?
//                    ) {
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                            binding.layoutInfo.setBackgroundDrawable(resource);
//                        }
//                    }
//                })
//        }
        val text = binding.descriptionText

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY))
        } else text.setText(Html.fromHtml(content))

        binding.backImage.setOnClickListener {
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }

        binding.contactDev.underLine()
        binding.contactDev.setOnClickListener {
            gotoLink("https://t.me/astrogirll06")
        }

    }

    private fun gotoLink(s: String) {
        val uri = Uri.parse(s)
        startActivity(Intent(Intent.ACTION_VIEW, uri))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, StartActivity::class.java))
        finish()

    }
}