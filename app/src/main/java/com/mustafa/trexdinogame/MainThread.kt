package com.mustafa.trexdinogame


import android.graphics.Canvas
import android.view.SurfaceHolder

class MainThread(private val surfaceHolder: SurfaceHolder, private val gameView: GameView) : Thread() {
    private var running: Boolean = false
    private val targetFPS = 60
    private var averageFPS: Double = 0.0

    override fun run() {

        var startTime: Long
        var timeMillis: Long
        var waitTime: Long
        var totalTime: Long = 0
        var frameCount = 0
        val targetTime = (1000 / targetFPS).toLong()


        while (running) {
            startTime = System.nanoTime()
            canvas = null

            try {
                canvas = this.surfaceHolder.lockCanvas()
                synchronized(surfaceHolder) {
                    this.gameView.update(frameCount)
                    this.gameView.draw(canvas)
                }
            } catch (e: Exception) {
            } finally {
                if (canvas != null) {
                    try {
                        surfaceHolder.unlockCanvasAndPost(canvas)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            timeMillis = (System.nanoTime() - startTime) / 1000000
            waitTime = targetTime - timeMillis

            try {
                sleep(waitTime)
            } catch (e: Exception) {
            }

            totalTime += System.nanoTime() - startTime
            frameCount++
            if (frameCount == targetFPS) {
                averageFPS = (1000 / (totalTime / frameCount / 1000000)).toDouble()
                frameCount = 0
                totalTime = 0
                println(averageFPS)
            }
        }

    }

    fun setRunning(isRunning: Boolean) {
        running = isRunning
    }

    companion object {
        var canvas: Canvas? = null
    }
}