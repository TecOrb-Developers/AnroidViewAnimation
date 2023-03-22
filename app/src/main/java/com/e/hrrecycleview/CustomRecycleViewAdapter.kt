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


class CustomRecycleViewAdapter: RecyclerView.Adapter<CustomRecycleViewAdapter.ViewHolder> {

    lateinit var context: Context
    lateinit var list: List<TestModel>
    constructor(context: Context,list: List<TestModel>){
        this.context=context
        this.list=list
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recyclerview, parent, false)
        return ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if (holder.flipView.currentFlipState === ImageFlip.FlipState.FRONT_SIDE && list[position].isFlipped) {
            holder.flipView.setFlipDuration(0)
            holder.flipView.flipTheView()
        } else if (holder.flipView.currentFlipState === ImageFlip.FlipState.BACK_SIDE
            && !list[position].isFlipped
        ) {
            holder.flipView.setFlipDuration(0)
            holder.flipView.flipTheView()
        }
        holder.name.text=list[position].firstName
        holder.age.text=list[position].age.toString()
        holder.user_image.setImageResource(list[position].icon)

        holder.flipView.setOnClickListener {
            if (list[position].isFlipped) {
                list[position].isFlipped = false

            } else {
                list[position].isFlipped = true
            }
            AnimationEffects.animationZoom(holder.flipView,0,0,400,100,1,1,2000)
            holder.flipView.setFlipDuration(700)
            holder.flipView.flipTheView()
        }
    }
    override fun getItemCount(): Int {
       return list.size
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var flipView: ImageFlip
        var name: TextView
        var age: TextView
        var user_image: ImageView
        init {
            flipView = view.findViewById(R.id.flipView) as ImageFlip
            name=view.findViewById(R.id.text_name)
            age=view.findViewById(R.id.text_age)
            user_image=view.findViewById(R.id.user_image)



        }
    }
}