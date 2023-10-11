package uz.mlsoft.newmypuzzle15.screens

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import uz.mlsoft.newmypuzzle15.R
import uz.mlsoft.newmypuzzle15.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val orientation = this.resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            Glide.with(this).load(R.drawable.start_back).into(object : SimpleTarget<Drawable?>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable?>?
                ) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        binding.layoutStart.setBackgroundDrawable(resource);
                    }
                }

            })
        } else {
            Glide.with(this).load(R.drawable.start_back_land)
                .into(object : SimpleTarget<Drawable?>() {
                    override fun onResourceReady(
                        resource: Drawable,
                        transition: Transition<in Drawable?>?
                    ) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            binding.layoutStart.setBackgroundDrawable(resource);
                        }
                    }
                })
        }

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        binding.playBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
            finish()
        }

        binding.buttonShare.setOnClickListener {
            shareProject(this)
        }

        binding.rateBtn.setOnClickListener {
            rateApp()
        }

        binding.infoBtn.setOnClickListener {
            startActivity(Intent(this, InfoActivity::class.java))
            finish()
        }


    }

    private fun shareProject(context: Context) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(
            Intent.EXTRA_TEXT,
            "Dowloand now!: " + "https://play.google.com/store/apps/details?id=${context.applicationContext?.packageName}"
        )
        sendIntent.type = "text/plain"
        context.startActivity(sendIntent)
    }

    private fun rateApp() {
        val i = Intent(Intent.ACTION_VIEW)
        i.data =
            Uri.parse("https://play.google.com/store/apps/details?id={context?applicationContext?.packageName}")
        startActivity(i)
    }
}