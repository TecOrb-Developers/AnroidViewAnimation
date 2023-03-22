package com.e.animationlibrary

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.e.animationlibrary.databinding.ActivityMainBinding
import com.e.hranimation.AnimationEffects
import com.e.hranimation.ImageFlip


class MainActivity : AppCompatActivity(),View.OnClickListener{
    private lateinit var binding: ActivityMainBinding
    private lateinit var context: Context
    lateinit var imageFlip:ImageFlip
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this,R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            FullScreenView.setWindowFlag(this,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true
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
        imageFlip= ImageFlip(context)
        initView()
      //  AnimationEffects.animationBlink(binding.textView,100,90)
        AnimationEffects.animationBlink(binding.icon,1000,500)
    }
   private fun initView(){
        binding.btnZoom.setOnClickListener(this)
       binding.btnBouncing.setOnClickListener(this)
       binding.icon4.setOnClickListener(this)
       binding.btnmove.setOnClickListener(this)
       binding.btnmove1.setOnClickListener(this)
       binding.nextActivity.setOnClickListener(this)
       binding.fadeIn.setOnClickListener(this)
       binding.fadeOut.setOnClickListener(this)

   }
    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btnZoom->{
                AnimationEffects.animationZoom(binding.icon2,1,1,35,50,2,2,3000)
            }
            R.id.btnBouncing->{
                AnimationEffects.animationBouncing(binding.icon3,-2,0,1,1,1500)
            }R.id.btnmove->{
            AnimationEffects.animationMove(binding.icon4,0,0,230,0,1500)
            }R.id.btnmove1->{
            AnimationEffects.animationMove(binding.icon4,0,0,-230,0,1500)
        }R.id.nextActivity->{
            var intent= Intent(context,FlipAnimation::class.java)
            startActivity(intent)
        }R.id.fadeIn->{
            AnimationEffects.animationFadeIn(binding.icon1,2000,1000)
        }R.id.fadeOut->{
            AnimationEffects.animationFadeOut(binding.icon1,2000,20)
        }
        }
    }
}