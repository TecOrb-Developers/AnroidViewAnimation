package com.e.animationlibrary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.e.animationlibrary.databinding.ActivityFlipAnimationBinding
import com.e.hrrecycleview.ActivityDashboard

class FlipAnimation : AppCompatActivity(), View.OnClickListener {

    private lateinit var context:Context
    private lateinit var binding:ActivityFlipAnimationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_flip_animation)
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            FullScreenView.setWindowFlag(this,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true
            )
        }
        if (Build.VERSION.SDK_INT >= 19) {
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        }
        if (Build.VERSION.SDK_INT >= 21) {
            FullScreenView.setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false)
            window.statusBarColor = Color.TRANSPARENT
        }
        context=this
        binding.flipVertical.setFlipDuration(400)
        binding.flipVertical.isFlipEnabled=true
        binding.flipHorizontal.setFlipDuration(400)
        binding.flipHorizontal.isFlipEnabled=true
        binding.flipCircular.setFlipDuration(400)
        binding.flipCircular.isFlipEnabled=true
        initView()
    }
    fun initView(){
        binding.imgBackCard.setOnClickListener(this)
        binding.imgFrontCard.setOnClickListener(this)
        binding.imgBackCard1.setOnClickListener(this)
        binding.imgFrontCard1.setOnClickListener(this)
        binding.imgFrontCard2.setOnClickListener(this)
        binding.imgBackCard2.setOnClickListener(this)
        binding.nextClick.setOnClickListener(this)
        binding.viewBack.setOnClickListener(this)
        binding.verticalFlip.setOnClickListener(this)
        binding.horizontalFlip.setOnClickListener(this)
        binding.textFlip.setOnClickListener(this)
        binding.flipVertical.setToVerticalType()
        binding.flipVertical.setFlipTypeFromFront()
        binding.flipHorizontal.setToHorizontalType()
        binding.flipHorizontal.setFlipTypeFromLeft()

    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.imgFrontCard,R.id.verticalFlip->{
                Toast.makeText(context, "Front Card", Toast.LENGTH_SHORT).show();
                binding.flipVertical.flipTheView()
            }R.id.imgBackCard,R.id.verticalFlip-> {
            Toast.makeText(context, "Back Card", Toast.LENGTH_SHORT).show();
            binding.flipVertical.flipTheView()
        }R.id.imgFrontCard1,R.id.horizontalFlip->{
                Toast.makeText(context, "Front Card", Toast.LENGTH_SHORT).show();
                binding.flipHorizontal.flipTheView()
            }R.id.imgBackCard1,R.id.horizontalFlip->{
            Toast.makeText(context, "Back Card", Toast.LENGTH_SHORT).show();
            binding.flipHorizontal.flipTheView()
        }R.id.imgFrontCard2,R.id.textFlip->{
            binding.flipCircular.flipTheView()
        }R.id.imgBackCard2,R.id.textFlip->{
            binding.flipCircular.flipTheView()
        }R.id.nextClick->{
            val intent =Intent(context,ActivityDashboard::class.java)
            startActivity(intent)
        }R.id.viewBack->{
            finish()
        }
        }

    }
}