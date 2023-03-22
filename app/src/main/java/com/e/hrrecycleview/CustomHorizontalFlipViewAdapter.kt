package com.e.hrrecycleview

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.e.animationlibrary.R
import com.e.hranimation.AnimationEffects
import com.e.hranimation.ImageFlip

class CustomHorizontalFlipViewAdapter: RecyclerView.Adapter<CustomHorizontalFlipViewAdapter.ViewHolder> {

    lateinit var context: Context
    lateinit var list: List<TestModelIcon>
    constructor(context: Context, list: List<TestModelIcon>){
        this.context=context
        this.list=list
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview2, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder.flipView.currentFlipState === ImageFlip.FlipState.FRONT_SIDE && list[position].isFlipped) {
            holder.flipView.setFlipDuration(0)
            holder.flipView.setToHorizontalType()
            holder.flipView.flipTheView()
        } else if (holder.flipView.currentFlipState === ImageFlip.FlipState.BACK_SIDE
            && !list[position].isFlipped
        ) {
            holder.flipView.setFlipDuration(0)
            holder.flipView.setToHorizontalType()
            holder.flipView.flipTheView()
        }
        holder.frontIcon.setImageResource(list[position].frontIcon)
        holder.backIcon.setImageResource(list[position].backIcon)


        holder.flipView.setOnClickListener {
            if (list[position].isFlipped) {
                list[position].isFlipped = false

            } else {
                list[position].isFlipped = true
            }
            holder.flipView.setFlipDuration(700)
            holder.flipView.setToHorizontalType()
            AnimationEffects.animationZoom(holder.flipView,0,0,40,40,1,1,2000)
            holder.flipView.flipTheView()
        }
    }
    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var flipView: ImageFlip
        val frontIcon:ImageView
        var backIcon: ImageView

        init {
            flipView = view.findViewById(R.id.flipView) as ImageFlip
            frontIcon=view.findViewById(R.id.user_image)
            backIcon=view.findViewById(R.id.user_image2)
        }
    }

}