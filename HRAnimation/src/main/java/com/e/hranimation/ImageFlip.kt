package com.e.hranimation

import android.animation.Animator
import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.os.Handler
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.view.GestureDetectorCompat
    class ImageFlip : FrameLayout {

        private val animFlipHorizontalOutId: Int = R.animator.animation_horizontal_flip_out
        private val animFlipHorizontalInId: Int = R.animator.animation_horizontal_flip_in
        private val animFlipHorizontalRightOutId: Int = R.animator.animation_horizontal_right_out
        private val animFlipHorizontalRightInId: Int = R.animator.animation_horizontal_right_in
        private val animFlipVerticalOutId: Int = R.animator.animation_vertical_flip_out
        private val animFlipVerticalInId: Int = R.animator.animation_vertical_flip_in
        private val animFlipVerticalFrontOutId: Int = R.animator.animation_vertical_front_out
        private val animFlipVerticalFrontInId: Int = R.animator.animation_vertical_flip_front_in

        enum class FlipState {
            FRONT_SIDE, BACK_SIDE
        }

        private var mSetRightOut: AnimatorSet? = null
        private var mSetLeftIn: AnimatorSet? = null
        private var mSetTopOut: AnimatorSet? = null
        private var mSetBottomIn: AnimatorSet? = null
        private var mIsBackVisible = false
        private var mCardFrontLayout: View? = null
        private var mCardBackLayout: View? = null
        private var flipType: String? = "vertical"
        //horizontal return flip
        var flipTypeFrom: String? = "right"
            private set
        var isFlipOnTouch = false
        private var flipDuration = 0
        var isFlipEnabled = false
        var isFlipOnceEnabled = false
        var isAutoFlipBack = false
        var autoFlipBackTime = 0
        var currentFlipState = FlipState.FRONT_SIDE
            private set
        var onFlipListener: OnFlipAnimationListener? = null
        private var gestureDetector: GestureDetectorCompat? = null

        constructor(context: Context) : super(context) {
            init(context, null)
        }
        constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
            init(context, attrs)
        }

        @SuppressLint("CustomViewStyleable")
        private fun init(context: Context, attrs: AttributeSet?) {
            // Setting Default Values
            isFlipOnTouch = true
            flipDuration = DEFAULT_FLIP_DURATION
            isFlipEnabled = true
            isFlipOnceEnabled = false
            isAutoFlipBack = false
            autoFlipBackTime = DEFAULT_AUTO_FLIP_BACK_TIME

            // Check for the attributes
            if (attrs != null) {
                // Attribute initialization
                val attrArray: TypedArray =
                    context.obtainStyledAttributes(attrs, R.styleable.easy_flip_view, 0, 0)
                try {
                    isFlipOnTouch = attrArray.getBoolean(R.styleable.easy_flip_view_flipOnTouch, true)
                    flipDuration = attrArray.getInt(R.styleable.easy_flip_view_flipDuration,DEFAULT_FLIP_DURATION)
                    isFlipEnabled =    attrArray.getBoolean(R.styleable.easy_flip_view_flipEnabled, true)
                    isFlipOnceEnabled = attrArray.getBoolean(R.styleable.easy_flip_view_flipOnceEnabled, false)
                    isAutoFlipBack =attrArray.getBoolean(R.styleable.easy_flip_view_autoFlipBack, false)
                    autoFlipBackTime = attrArray.getInt(
                        R.styleable.easy_flip_view_autoFlipBackTime,
                        DEFAULT_AUTO_FLIP_BACK_TIME
                    )
                    flipType = attrArray.getString(R.styleable.easy_flip_view_flipType)
                    flipTypeFrom = attrArray.getString(R.styleable.easy_flip_view_flipFrom)
                    if (TextUtils.isEmpty(flipType)) {
                        flipType = "vertical"
                    }
                    if (TextUtils.isEmpty(flipTypeFrom)) {
                        flipTypeFrom = "left"
                    }
                } finally {
                    attrArray.recycle()
                }
            }
            loadAnimations()
        }

        override fun onFinishInflate() {
            super.onFinishInflate()
            check(childCount <= 2) { "ImageFlip can host only two direct children!" }
            findViews()
            changeCameraDistance()
            setupInitializations()
            initGestureDetector()
        }

        override fun addView(v: View?, pos: Int, params: ViewGroup.LayoutParams?) {
            check(childCount != 2) { "ImageFlip can host only two direct children!" }
            super.addView(v, pos, params)
            findViews()
            changeCameraDistance()
        }

        override fun removeView(v: View?) {
            super.removeView(v)
            findViews()
        }

        override fun removeAllViewsInLayout() {
            super.removeAllViewsInLayout()

            // Reset the state
            currentFlipState = FlipState.FRONT_SIDE
            findViews()
        }

        private fun findViews() {
            // Invalidation since we use this also on removeView
            mCardBackLayout = null
            mCardFrontLayout = null
            val childs = childCount
            if (childs < 1) {
                return
            }
            if (childs < 2) {
                // Only invalidate flip state if we have a single child
                currentFlipState = FlipState.FRONT_SIDE
                mCardFrontLayout = getChildAt(0)
            } else if (childs == 2) {
                mCardFrontLayout = getChildAt(1)
                mCardBackLayout = getChildAt(0)
            }
            if (!isFlipOnTouch) {
                mCardFrontLayout?.visibility = VISIBLE
                if (mCardBackLayout != null) {
                    mCardBackLayout?.visibility = GONE
                }
            }
        }

        private fun setupInitializations() {
            mCardBackLayout?.visibility = GONE
        }

        private fun initGestureDetector() {
            gestureDetector = GestureDetectorCompat(context, SwipeDetector())
        }

        private fun loadAnimations() {
            if (flipType.equals("horizontal", ignoreCase = true)) {
                if (flipTypeFrom.equals("left", ignoreCase = true)) {
                    mSetRightOut = AnimatorInflater.loadAnimator(context, animFlipHorizontalOutId) as AnimatorSet
                    mSetLeftIn = AnimatorInflater.loadAnimator(context,animFlipHorizontalInId) as AnimatorSet
                } else {
                    mSetRightOut = AnimatorInflater.loadAnimator(context, animFlipHorizontalRightOutId) as AnimatorSet
                    mSetLeftIn = AnimatorInflater.loadAnimator(context,animFlipHorizontalRightInId) as AnimatorSet
                }
                if (mSetRightOut == null || mSetLeftIn == null) {
                    throw RuntimeException("No Animations Found! Please set Flip in and Flip out animation Ids.")
                }
                mSetRightOut!!.removeAllListeners()
                mSetRightOut!!.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        if (currentFlipState == FlipState.FRONT_SIDE) {
                            mCardBackLayout?.visibility = GONE
                            mCardFrontLayout?.visibility = VISIBLE
                            if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@ImageFlip, FlipState.FRONT_SIDE)
                        } else {
                            mCardBackLayout?.visibility = VISIBLE
                            mCardFrontLayout?.visibility = GONE
                            if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@ImageFlip, FlipState.BACK_SIDE)

                            // Auto Flip Back
                            if (isAutoFlipBack == true) {
                                Handler().postDelayed(Runnable {
                                    flipTheView() },
                                    autoFlipBackTime.toString().toLong())
                            }
                        }
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                setFlipDuration(flipDuration)
            } else {
                if (!TextUtils.isEmpty(flipTypeFrom) && flipTypeFrom.equals("front", ignoreCase = true)) {
                    mSetTopOut = AnimatorInflater.loadAnimator(context, animFlipVerticalFrontOutId) as AnimatorSet
                    mSetBottomIn = AnimatorInflater.loadAnimator(context, animFlipVerticalFrontInId) as AnimatorSet
                } else {
                    mSetTopOut =
                        AnimatorInflater.loadAnimator(context, animFlipVerticalOutId) as AnimatorSet
                    mSetBottomIn =
                        AnimatorInflater.loadAnimator(context, animFlipVerticalInId) as AnimatorSet
                }
                if (mSetTopOut == null || mSetBottomIn == null) {
                    throw RuntimeException("No Animations Found! Please set Flip in and Flip out animation Ids.")
                }
                mSetTopOut!!.removeAllListeners()
                mSetTopOut!!.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animator: Animator) {}
                    override fun onAnimationEnd(animator: Animator) {
                        if (currentFlipState == FlipState.FRONT_SIDE) {
                            mCardBackLayout?.visibility = GONE
                            mCardFrontLayout?.visibility = VISIBLE
                            if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(this@ImageFlip, FlipState.FRONT_SIDE)
                        } else {
                            mCardBackLayout?.visibility = VISIBLE
                            mCardFrontLayout?.visibility = GONE
                            if (onFlipListener != null) onFlipListener!!.onViewFlipCompleted(
                                this@ImageFlip,
                                FlipState.BACK_SIDE
                            )

                            // Auto Flip Back
                            if (isAutoFlipBack == true) {
                                Handler().postDelayed(Runnable { flipTheView() },
                                    autoFlipBackTime.toString().toLong())
                            }
                        }
                    }

                    override fun onAnimationCancel(animator: Animator) {}
                    override fun onAnimationRepeat(animator: Animator) {}
                })
                setFlipDuration(flipDuration)
            }
        }

        private fun changeCameraDistance() {
            val distance = 8000
            val scale = resources.displayMetrics.density * distance
            mCardFrontLayout?.cameraDistance = scale
            mCardBackLayout?.cameraDistance = scale
        }

        fun flipTheView() {
            if (!isFlipEnabled || childCount < 2) return
            if (isFlipOnceEnabled && currentFlipState == FlipState.BACK_SIDE) return
            if (flipType.equals("horizontal", ignoreCase = true)) {
                if (mSetRightOut!!.isRunning || mSetLeftIn!!.isRunning) return
                mCardBackLayout?.visibility = VISIBLE
                mCardFrontLayout?.visibility = VISIBLE
                if (currentFlipState == FlipState.FRONT_SIDE) {
                    // From front to back
                    mSetRightOut!!.setTarget(mCardFrontLayout)
                    mSetLeftIn!!.setTarget(mCardBackLayout)
                    mSetRightOut!!.start()
                    mSetLeftIn!!.start()
                    mIsBackVisible = true
                    currentFlipState = FlipState.BACK_SIDE
                } else {
                    // from back to front
                    mSetRightOut!!.setTarget(mCardBackLayout)
                    mSetLeftIn!!.setTarget(mCardFrontLayout)
                    mSetRightOut!!.start()
                    mSetLeftIn!!.start()
                    mIsBackVisible = false
                    currentFlipState = FlipState.FRONT_SIDE
                }
            } else {
                if (mSetTopOut!!.isRunning || mSetBottomIn!!.isRunning) return
                mCardBackLayout?.visibility = VISIBLE
                mCardFrontLayout?.visibility = VISIBLE
                if (currentFlipState == FlipState.FRONT_SIDE) {
                    // From front to back
                    mSetTopOut!!.setTarget(mCardFrontLayout)
                    mSetBottomIn!!.setTarget(mCardBackLayout)
                    mSetTopOut!!.start()
                    mSetBottomIn!!.start()
                    mIsBackVisible = true
                    currentFlipState = FlipState.BACK_SIDE
                } else {
                    // from back to front
                    mSetTopOut!!.setTarget(mCardBackLayout)
                    mSetBottomIn!!.setTarget(mCardFrontLayout)
                    mSetTopOut!!.start()
                    mSetBottomIn!!.start()
                    mIsBackVisible = false
                    currentFlipState = FlipState.FRONT_SIDE
                }
            }
        }

        fun flipTheView(withAnimation: Boolean) {
            if (childCount < 2) return
            if (flipType.equals("horizontal", ignoreCase = true)) {
                if (!withAnimation) {
                    mSetLeftIn!!.duration = 0
                    mSetRightOut!!.duration = 0
                    val oldFlipEnabled = isFlipEnabled
                    isFlipEnabled = true
                    flipTheView()
                    mSetLeftIn!!.duration = flipDuration.toLong()
                    mSetRightOut!!.duration = flipDuration.toLong()
                    isFlipEnabled = oldFlipEnabled
                } else {
                    flipTheView()
                }
            } else {
                if (!withAnimation) {
                    mSetBottomIn!!.duration = 0
                    mSetTopOut!!.duration = 0
                    val oldFlipEnabled = isFlipEnabled
                    isFlipEnabled = true
                    flipTheView()
                    mSetBottomIn!!.duration = flipDuration.toLong()
                    mSetTopOut!!.duration = flipDuration.toLong()
                    isFlipEnabled = oldFlipEnabled
                } else {
                    flipTheView()
                }
            }
        }

        override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
            return try {
                gestureDetector!!.onTouchEvent(ev) || super.dispatchTouchEvent(ev)
            } catch (throwable: Throwable) {
                throw IllegalStateException("Error in dispatchTouchEvent: ", throwable)
            }
        }

        override fun onTouchEvent(event: MotionEvent): Boolean {
            return if (isEnabled && isFlipOnTouch) {
                gestureDetector!!.onTouchEvent(event)
            } else {
                super.onTouchEvent(event)
            }
        }

        fun getFlipDuration(): Int {
            return flipDuration
        }

        fun setFlipDuration(flipDuration: Int) {
            this.flipDuration = flipDuration
            if (flipType.equals("horizontal", ignoreCase = true)) {
                mSetRightOut!!.childAnimations[0].duration = flipDuration.toLong()
                mSetRightOut!!.childAnimations[1].startDelay = (flipDuration / 2).toLong()
                mSetLeftIn!!.childAnimations[1].duration = flipDuration.toLong()
                mSetLeftIn!!.childAnimations[2].startDelay = (flipDuration / 2).toLong()
            } else {
                mSetTopOut!!.childAnimations[0].duration = flipDuration.toLong()
                mSetTopOut!!.childAnimations[1].startDelay = (flipDuration / 2).toLong()
                mSetBottomIn!!.childAnimations[1].duration = flipDuration.toLong()
                mSetBottomIn!!.childAnimations[2].startDelay = (flipDuration / 2).toLong()
            }
        }
        val isFrontSide: Boolean
            get() = currentFlipState == FlipState.FRONT_SIDE


        val isBackSide: Boolean
            get() = currentFlipState == FlipState.BACK_SIDE

        val isHorizontalType: Boolean
            get() = flipType == "horizontal"

        val isVerticalType: Boolean
            get() = flipType == "vertical"


        fun setToHorizontalType() {
            flipType = "horizontal"
            loadAnimations()
        }


        fun setToVerticalType() {
            flipType = "vertical"
            loadAnimations()
        }

        fun setFlipTypeFromRight() {
            flipTypeFrom = if (flipType == "horizontal") "right" else "front"
            loadAnimations()
        }

        fun setFlipTypeFromLeft() {
            flipTypeFrom = if (flipType == "horizontal") "left" else "back"
            loadAnimations()
        }

        fun setFlipTypeFromFront() {
            flipTypeFrom = if (flipType == "vertical") "front" else "right"
            loadAnimations()
        }

        fun setFlipTypeFromBack() {
            flipTypeFrom = if (flipType == "vertical") "back" else "left"
            loadAnimations()
        }

        interface OnFlipAnimationListener {
            fun onViewFlipCompleted(imageFlipView: ImageFlip?, newCurrentSide: FlipState?)
        }

        private inner class SwipeDetector : GestureDetector.SimpleOnGestureListener() {
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return false
            }

            override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                if (isEnabled && isFlipOnTouch) {
                    flipTheView()
                }
                return super.onSingleTapConfirmed(e)
            }

            override fun onDown(e: MotionEvent): Boolean {
                return if (isEnabled && isFlipOnTouch) {
                    true
                } else super.onDown(e)
            }
        }

        companion object {
            val TAG = ImageFlip::class.java.simpleName
            const val DEFAULT_FLIP_DURATION = 400
            const val DEFAULT_AUTO_FLIP_BACK_TIME = 1000
        }
    }

