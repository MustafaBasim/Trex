package com.mustafa.trexdinogame

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kotlin.random.Random

class CharacterDinoAndCactus(imageDino: Bitmap, imageSmallCactus: Bitmap, imageBigCactus: Bitmap) {
    private var xVelocity = 30
    private var yVelocity = 50
    private val dinoColumns = 4
    private val cactusColumns = 6
    private var maxJump = 700
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    private var stateReadyAndJumping = 0
    private var stateRunningOne = 1
    private var stateRunningTwo = 2
    private var stateCrash = 3

    private var currentState = stateReadyAndJumping

    private var dinoImageReadyAndJumping: Bitmap? = null
    private var dinoImageRunningOne: Bitmap? = null
    private var dinoImageRunningTwo: Bitmap? = null
    private var dinoImageCrash: Bitmap? = null

    private var dino : Object
    private var smallCactus : Object
    private var bigCactus : Object

    private val cactusList = ArrayList<Bitmap>()
    init {
        dino = Object(imageDino,0,0,dinoColumns)
        dino.x =  screenWidth / 4 - dino.width / 2
        dino.y = screenHeight / 2 - dino.height / 2

        // dino images
        dinoImageReadyAndJumping = Bitmap.createBitmap(dino.image, stateReadyAndJumping * dino.width, 0 ,dino.width,dino.height)
        dinoImageRunningOne = Bitmap.createBitmap(dino.image, stateRunningOne * dino.width, 0 ,dino.width,dino.height)
        dinoImageRunningTwo = Bitmap.createBitmap(dino.image, stateRunningTwo * dino.width, 0 ,dino.width,dino.height)
        dinoImageCrash = Bitmap.createBitmap(dino.image, stateCrash * dino.width, 0 ,dino.width,dino.height)

        smallCactus = Object(imageSmallCactus,0,0,cactusColumns)
        bigCactus = Object(imageBigCactus,0,0,cactusColumns)

        smallCactus.y = screenHeight / 2 - 100
        bigCactus.y = screenHeight / 2 - 180

        cactusList.add(Bitmap.createBitmap(smallCactus.image, 0 * smallCactus.width, 0 , ( 0 + 1 ) * smallCactus.width,smallCactus.height))
        cactusList.add(Bitmap.createBitmap(smallCactus.image, 1 * smallCactus.width, 0 , ( 1 + 1 ) * smallCactus.width,smallCactus.height))
        cactusList.add(Bitmap.createBitmap(smallCactus.image, 3 * smallCactus.width, 0 , ( 2 + 1 ) * smallCactus.width,smallCactus.height))
        cactusList.add(Bitmap.createBitmap(bigCactus.image, 0 * bigCactus.width, 0 , ( 0 + 1 ) * bigCactus.width,bigCactus.height))
        cactusList.add(Bitmap.createBitmap(bigCactus.image, 1 * bigCactus.width, 0 , ( 1 + 1 ) * bigCactus.width,bigCactus.height))
        cactusList.add(Bitmap.createBitmap(bigCactus.image, 3 * bigCactus.width, 0 , ( 2 + 1 ) * bigCactus.width ,bigCactus.height))
    }



    fun draw(canvas: Canvas) {

//        val paint = Paint()
//        paint.color = Color.RED
//        paint.strokeWidth = 3F
//        paint.style = Paint.Style.STROKE
//        canvas.drawRect(dino.x.toFloat(), dino.y.toFloat(), dino.x.toFloat() + dino.width  ,  dino.y.toFloat() + dino.height, paint)
//        paint.color = Color.GREEN
//        canvas.drawRect(getCactusX().toFloat(), getCactusY().toFloat(), getCactusX().toFloat() + getCactus().width  ,  getCactusY().toFloat() + getCactus().height, paint)

        canvas.drawBitmap(getPlayer(), dino.x.toFloat(), dino.y.toFloat(), null)
        canvas.drawBitmap(getCactus(), getCactusX().toFloat(), getCactusY().toFloat(), null)

    }

    fun update(frameNumber : Int , isRunning : (Boolean) -> Unit ) {
        performJump()
        performRunning(frameNumber)
        showCactus()
        checkIfCrashed { isRunning(it) }
    }

    // Crashed
    var isRunningFlag = true
    private fun checkIfCrashed( isRunning : (Boolean) -> Unit ) {
        val x = getCactusX()
        val y = getCactusY()

        if ( dino.x > x + getCactus().width ) {

        }
        else if ( dino.x + dino.width - 100 > x && dino.y + dino.height - 100 > y ) {
            currentState = stateCrash
            isRunningFlag = false
            isRunning(false)
        }

    }


    // dino selector
    private fun getPlayer() : Bitmap {  
        return when ( currentState ) {
            stateReadyAndJumping -> dinoImageReadyAndJumping!!
            stateRunningOne -> dinoImageRunningOne!!
            stateRunningTwo -> dinoImageRunningTwo!!
            else -> dinoImageCrash!! // stateCrash
        }
    }


    // cactus selector
    var index = 0
    private fun getCactus() : Bitmap {
        return cactusList[index]
    }

    private fun getCactusX() : Int {
        return if ( index in 0..2) smallCactus.x
        else bigCactus.x
    }

    private fun getCactusY() : Int {
        return if ( index in 0..2) smallCactus.y
        else bigCactus.y
    }

    private var nextCactusFlag = true
    private fun showCactus() {
        if ( isRunningFlag ) {
            if ( nextCactusFlag )
                index = Random.nextInt(0, 5) // 6

            nextCactusFlag = false
            var x: Int
            x = if ( index in 0..2) {
                smallCactus.x
            } else {
                bigCactus.x
            }

            if ( x * -1 > getCactus().width ) {
                x = screenWidth
                nextCactusFlag = true
            }
            x -= xVelocity

            if ( index in 0..2) {
                smallCactus.x = x
            } else {
                bigCactus.x = x
            }
        }
    }


    // Running
    private fun performRunning(frameNumber: Int) {
        if (isRunningFlag && !isJumping) {
            currentState = if (
                frameNumber in 1..5 ||
                frameNumber in 11..15 ||
                frameNumber in 21..25 ||
                frameNumber in 31..35 ||
                frameNumber in 41..45 ||
                frameNumber in 51..55 ) {
                stateRunningOne
            } else {
                stateRunningTwo
            }
        }
    }



    // Jumping
    private var isJumping = false
    private var reversJump = false
    fun jump(isRunning : (Boolean) -> Unit) {
        if ( !isRunningFlag ) {
            isRunningFlag = true
            currentState = stateRunningOne
            smallCactus.x = screenWidth + 700
            bigCactus.x = screenWidth + 700
            dino.x =  screenWidth / 4 - dino.width / 2
            dino.y = screenHeight / 2 - dino.height / 2
            index = Random.nextInt(0, 5)
            isJumping = false
            reversJump = false
            isRunning(true)
        } else {
            isJumping = true
            currentState = stateReadyAndJumping
        }
    }

    private var currentTime = 0L
    private var lastTime = 0L
    private var timeStamp = 150L
    private var timeFlag = true

    private fun performJump() {
        if ( isJumping && isRunningFlag ) {
            if ( dino.y > getDinoCenterY() - maxJump && !reversJump ) {
                dino.y -= yVelocity
                lastTime = System.currentTimeMillis()
                timeFlag = true
            } else if ( timeFlag ) {
                currentTime = System.currentTimeMillis()
                if ( currentTime - lastTime >= timeStamp ) {
                    timeFlag = false
                }
            }
            else if ( dino.y < getDinoCenterY() && reversJump ) {
                dino.y += yVelocity
            } else if ( !reversJump ){
                reversJump = true
            } else {
                isJumping = false
                reversJump = false
                currentState = stateRunningOne
            }
        }
    }

    private fun getDinoCenterY(): Int {
        return screenHeight / 2 - dino.image.height / 2
    }



}

