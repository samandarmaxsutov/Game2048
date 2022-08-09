package uz.gita.game2048observe.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import uz.gita.game2048observe.MainActivity
import uz.gita.game2048observe.app.App
import uz.gita.game2048observe.repository.Repository

class MainViewModel :ViewModel(),GameViewModel {
    private val repository= Repository.getInstense()
    override val score: MutableLiveData<Int> = MutableLiveData(0)
    override val bestScore: MutableLiveData<Int> = MutableLiveData(0)
    override val list: MutableLiveData<List<Int>> = MutableLiveData()
    override val move: MutableLiveData<Int> = MutableLiveData(0)
    override val pauseTime: MutableLiveData<Long> =MutableLiveData(0)
    override val gameOver: MutableLiveData<Boolean> =MutableLiveData()
    override val sound: MutableLiveData<Boolean> = MutableLiveData(true)
    override val music: MutableLiveData<Boolean> = MutableLiveData(true)

    override fun moveLeft() {
        repository.moveLeft()
        repository.onMove()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()

        gameOver.value =repository.getGameOver()
    }

    override fun moveRight() {
        repository.moveRight()
        repository.onMove()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()

        gameOver.value =repository.getGameOver()
    }

    override fun moveUp() {
        repository.moveUp()
        repository.onMove()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()

        gameOver.value =repository.getGameOver()
    }

    override fun moveDown() {
        repository.moveDown()
        repository.onMove()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()

        gameOver.value =repository.getGameOver()
    }

    override fun restart() {
        bestScore.value=repository.getBestScore()
        repository.restart()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        move.value=repository.getMove()

        gameOver.value =repository.getGameOver()
        pauseTime.value=repository.getPauseTime()
    }

    override fun undo() {


        repository.undo()
        list.value=repository.getMatrix()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()
        gameOver.value =repository.getGameOver()
    }



    init {
        gameOver.value =repository.getGameOver()
        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()
        list.value=repository.getMatrix()
        pauseTime.value=repository.getPauseTime()
    }

    override fun onPause(pauseTime:Long) {
        repository.saved(pauseTime)

    }

    override fun onResume() {

        repository.getSaved()

        score.value=repository.getScore()
        bestScore.value=repository.getBestScore()
        move.value=repository.getMove()
        list.value=repository.getMatrix()
        pauseTime.value=repository.getPauseTime()
    }

    override fun onStart() {
        repository.onStart()
        sound.value=repository.getSound()
        music.value=repository.getMusic()
    }
}