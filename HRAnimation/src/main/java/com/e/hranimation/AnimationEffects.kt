package com.e.hranimation

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.*


class AnimationEffects {
    companion object{

       //This method is used to blink the view infinite
        fun animationBlink(view: View, setDuration: Any, setStartOffSet: Any){
            val anim: Animation = AlphaAnimation(0.0f, 1.0f)
            anim.duration = setDuration.toString().toLong()//You can manage the blinking time with this parameter
            anim.startOffset = setStartOffSet.toString().toLong()
            anim.repeatMode = Animation.REVERSE
            anim.repeatCount = Animation.INFINITE
            view.startAnimation(anim)
        }
        //This method is used to fadeIn/fadeOut with the help of axis
        fun animationFadeIn(view:View,setDuration: Any,setStartOffSet: Any) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = setDuration.toString().toLong()
            fadeIn.repeatMode = Animation.REVERSE
            view.animation = fadeIn
        }
        fun animationFadeOut(view:View,setDuration: Any,setStartOffSet: Any) {
            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator() //and this
            fadeOut.startOffset = setStartOffSet.toString().toLong()
            fadeOut.duration = setDuration.toString().toLong()
            fadeOut.repeatMode=Animation.REVERSE
            view.animation = fadeOut
        }
        fun animationFadeInOut(view:View,setDuration: Any,setStartOffSet: Any,status:Boolean) {
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = setDuration.toString().toLong()
            fadeIn.repeatMode = Animation.REVERSE
             fadeIn.repeatCount = Animation.INFINITE

            val fadeOut = AlphaAnimation(1f, 0f)
            fadeOut.interpolator = AccelerateInterpolator() //and this
            fadeOut.startOffset = setStartOffSet.toString().toLong()
            fadeOut.duration = setDuration.toString().toLong()
            fadeOut.repeatMode=Animation.REVERSE
            fadeOut.repeatCount=Animation.INFINITE
            val animation = AnimationSet(status) //change to false
            animation.addAnimation(fadeIn)
            animation.addAnimation(fadeOut)
            view.animation = animation
        }
        //This method is used to zoomIn/ZoomOut of any View
        fun animationZoom(view:View,fromXScale:Any,fromYScale:Any,pivotX:Any,pivotY:Any,toXScale:Any,toYScale:Any,setDuration:Any){
                val anim=ScaleAnimation(fromXScale.toString().toFloat(),toXScale.toString().toFloat(),fromYScale.toString().toFloat(),toYScale.toString().toFloat(),pivotX.toString().toFloat(),pivotY.toString().toFloat())
                    anim.duration=setDuration.toString().toLong()
                    view.animation=anim
        }
        //This method is used to bounce the any view
        fun animationBouncing(view:View,fromXScale: Any,fromYScale: Any,toXScale: Any,toYScale: Any,setDuration: Any){
            val anim= ScaleAnimation(fromXScale.toString().toFloat(),toXScale.toString().toFloat(),fromYScale.toString().toFloat(),toYScale.toString().toFloat())
                anim.duration=setDuration.toString().toLong()
                view.animation=anim
        }
        // This method is used to move the view in x-direction and y- direction
        fun animationMove(view:View,fromXDelta: Any,fromYDelta: Any,toXDelta: Any,toYDelta: Any,setDuration: Any){
            val anim= TranslateAnimation(fromXDelta.toString().toFloat(),toXDelta.toString().toFloat(),fromYDelta.toString().toFloat(),toYDelta.toString().toFloat())
            anim.duration=setDuration.toString().toLong()
            anim.fillAfter=true
            anim.interpolator=LinearInterpolator()
            view.animation=anim
        }
    }
}