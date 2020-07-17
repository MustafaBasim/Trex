package com.mustafa.trexdinogame

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.util.Log
import android.view.MotionEvent
import android.view.SurfaceView
import android.view.SurfaceHolder

class GameView(context: Context) : SurfaceView(context), SurfaceHolder.Callback {
    private val thread: MainThread
    private var characterDinoAndCactus: CharacterDinoAndCactus? = null
    private var floor: FloorAndClouds? = null

    init {

        holder.addCallback(this)

        thread = MainThread(holder, this)

        isFocusable = true

    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        characterDinoAndCactus!!.jump { isRunningFlag = it }
        return super.onTouchEvent(event)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        characterDinoAndCactus = CharacterDinoAndCactus(
            BitmapFactory.decodeResource(resources, R.drawable.player_dino),
            BitmapFactory.decodeResource(resources, R.drawable.small_cactus),
            BitmapFactory.decodeResource(resources, R.drawable.big_cactus))

        floor = FloorAndClouds(
            BitmapFactory.decodeResource(resources, R.drawable.floor),
            BitmapFactory.decodeResource(resources, R.drawable.cloud),
            BitmapFactory.decodeResource(resources, R.drawable.game_over),
            BitmapFactory.decodeResource(resources, R.drawable.numbers))


        thread.setRunning(true)
        thread.start()

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()

            } catch (e: InterruptedException) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    private var isRunningFlag = true
    fun update(frameNumber : Int ) {
        characterDinoAndCactus!!.update(frameNumber) { isRunningFlag = it }
        floor!!.update(isRunningFlag)
    }

    override fun draw(canvas: Canvas?) {

        super.draw(canvas)
        if (canvas != null) {
            canvas.drawColor(Color.parseColor("#f7f7f7"))
            floor!!.draw(canvas)
            characterDinoAndCactus!!.draw(canvas)
        }
    }


}