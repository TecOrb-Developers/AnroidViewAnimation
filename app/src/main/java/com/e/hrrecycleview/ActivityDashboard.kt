package com.e.hrrecycleview

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.e.animationlibrary.FullScreenView
import com.e.animationlibrary.R
import com.e.animationlibrary.databinding.ActivityDashboardBinding

class ActivityDashboard : AppCompatActivity(), View.OnClickListener {
    private lateinit var context: Context
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var list:List<TestModel>
    private lateinit var list2:List<TestModelIcon>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=DataBindingUtil.setContentView(this,R.layout.activity_dashboard)
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
        initView()
    }
    fun initView(){

        binding.viewBack.setOnClickListener(this)
        binding.recyclerView.layoutManager= LinearLayoutManager(this, LinearLayoutManager.VERTICAL ,false)
            list=ArrayList()
        list2=ArrayList()
        for(i in 1..2){
               (list as ArrayList<TestModel>).add(TestModel(false,"Honda",1010,R.drawable.icon))
            (list as ArrayList<TestModel>).add(TestModel(false,"Ford",3460,R.drawable.icon2))

        }
        (list2 as ArrayList<TestModelIcon>).add(TestModelIcon(false,R.drawable.mobbackicon,R.drawable.mobfronticon))
        (list2 as ArrayList<TestModelIcon>).add(TestModelIcon(false,R.drawable.cardback,R.drawable.cardfront))
        binding.recyclerHorizontal.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        binding.recyclerHorizontal.adapter=CustomHorizontalFlipViewAdapter(context,list2)
        binding.recyclerView.adapter=CustomRecycleViewAdapter(context,list)
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.viewBack -> {
                finish()
            }
        }
    }
}