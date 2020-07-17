package com.mustafa.trexdinogame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint


class FloorAndClouds(
    private val imageFloor: Bitmap,
    private val imageCloud: Bitmap,
    private val imageGameOver: Bitmap,
    imageScore: Bitmap) {
    private var xFloor: Int = 0
    private var yFloor: Int = 0
    private var xCloud: Int = 0
    private var yCloud: Int = 0
    private var xVelocity = 30
    private val floorOffset = 90
    private val cloudOffset = 400
    private var widthFloor = 0
    private var heightFloor = 0
    private var widthCloud = 0
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels
    private val numbers : Object
    private val scoreColumns = 12
    private val numbersList = ArrayList<Bitmap>()
    init {
        widthFloor = imageFloor.width
        heightFloor = imageFloor.height
        xFloor = 0
        yFloor = screenHeight / 2 + floorOffset

        widthCloud = imageCloud.width
        xCloud = screenWidth
        yCloud = screenHeight / 2 - cloudOffset

        numbers = Object(imageScore,0,0,scoreColumns)

        numbersList.add(Bitmap.createBitmap(numbers.image, 0 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 1 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 2 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 3 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 4 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 5 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 6 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 7 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 8 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 9 * numbers.width, 0 , numbers.width,numbers.height))
        numbersList.add(Bitmap.createBitmap(numbers.image, 10 * numbers.width, 0 , 2 * numbers.width,numbers.height))
    }

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(imageFloor, xFloor.toFloat(), yFloor.toFloat(), null)
        canvas.drawBitmap(imageCloud, xCloud.toFloat(), yCloud.toFloat(), null)

        drawScore(canvas)

        if ( !isRunningFlag )
        canvas.drawBitmap(imageGameOver, screenWidth / 2.toFloat() - imageGameOver.width / 2 , screenHeight / 4.toFloat()  , null)
    }

    private var cloudFlag = true
    private var isRunningFlag = true

    fun update( isRunning : Boolean ) {
        isRunningFlag = isRunning
        if ( isRunning ) {
            if ( xFloor * -1 > widthFloor - screenWidth ) {
                xFloor = 0
            }
            xFloor -= xVelocity


            if ( xCloud + widthCloud < -300 ) {
                xCloud = screenWidth
                if ( cloudFlag ) {
                    yCloud =  screenHeight / 2 - cloudOffset - 200
                    cloudFlag = false
                } else {
                    cloudFlag = true
                    yCloud =  screenHeight / 2 - cloudOffset
                }
            }
            xCloud -= xVelocity / 5
        }
    }


    private var initScore = 0L
    private var score = 0L
    private var highestScore = 0L

    private var scoreFlag = true

    private val paint = Paint().apply {
        color = Color.BLACK
        isFakeBoldText = true
        textSize = 70F
    }

    private fun getScore() : ArrayList<String> {
        if ( scoreFlag ) {
            initScore = System.currentTimeMillis()
            scoreFlag = false
            if ( isRunningFlag ) score = 0
        }
        if ( isRunningFlag ) {
            score = ( System.currentTimeMillis() - initScore ) / 100
        } else {
            if ( highestScore < score) {
                highestScore = score
            }
            scoreFlag = true
        }
        val scoreTxt = "00000".substring(score.toString().length) + score.toString()
        val highestScoreTxt = "00000".substring(highestScore.toString().length) + highestScore.toString()
        return ArrayList<String>().apply { add(scoreTxt) ; add(highestScoreTxt) }
    }

    private fun getScoreList() : ArrayList<Int> {
        return ArrayList<Int>().apply {
            val score = getScore()

            add(score[0][4].toString().toInt())
            add(score[0][3].toString().toInt())
            add(score[0][2].toString().toInt())
            add(score[0][1].toString().toInt())
            add(score[0][0].toString().toInt())

            add(score[1][4].toString().toInt())
            add(score[1][3].toString().toInt())
            add(score[1][2].toString().toInt())
            add(score[1][1].toString().toInt())
            add(score[1][0].toString().toInt())
        }
    }

    private fun drawScore(canvas: Canvas) {
        val list = getScoreList()
        canvas.drawBitmap(numbersList[list[0]], screenWidth - ( numbers.width * 0 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[1]], screenWidth - ( numbers.width * 1 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[2]], screenWidth - ( numbers.width * 2 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[3]], screenWidth - ( numbers.width * 3 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[4]], screenWidth - ( numbers.width * 4 )  - 150F , 150F, null)

        canvas.drawBitmap(numbersList[list[5]], screenWidth - ( numbers.width * 6 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[6]], screenWidth - ( numbers.width * 7 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[7]], screenWidth - ( numbers.width * 8 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[8]], screenWidth - ( numbers.width * 9 )  - 150F , 150F, null)
        canvas.drawBitmap(numbersList[list[9]], screenWidth - ( numbers.width * 10 )  - 150F , 150F, null)

        canvas.drawBitmap(numbersList[10], screenWidth - ( numbersList[10].width / 2 * 13 )  - 150F , 150F, null)
    }

}

