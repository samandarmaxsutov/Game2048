package uz.gita.game2048observe.viewmodel

import androidx.lifecycle.LiveData

interface GameViewModel {

    val score:LiveData<Int>
    val bestScore:LiveData<Int>
    val list:LiveData<List<Int>>
    val move:LiveData<Int>
    val pauseTime:LiveData<Long>
    val gameOver:LiveData<Boolean>
    val sound:LiveData<Boolean>
    val music:LiveData<Boolean>




    fun onPause(pauseTime:Long)
    fun onResume()
    fun onStart()
    fun moveLeft()
    fun moveRight()
    fun moveUp()
    fun moveDown()
    fun restart()
    fun undo()


}