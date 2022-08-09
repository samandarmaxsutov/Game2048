package uz.gita.game2048observe.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import uz.gita.game2048observe.TinyDB
import kotlin.math.sqrt

class Repository private constructor(context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private lateinit var tinyDB: TinyDB
        private var repository: Repository? = null
        fun instens(context: Context){
    if (repository == null) {
        repository = Repository(context)
        tinyDB = TinyDB(context)
    }

}
        fun getInstense(): Repository {
             return repository!!
        }

    }


    private val MIN_VALUE = 2

    private var listenerGameOver=false

    private  var sound=true
    private  var music=true
    private var coordinate=ArrayList<Pair<Int,Int>>()
    private var move = 0
    private var moveUndo = 0
    private var score = 0
    private var bestScore = 0
    private var scoreUndo = 0
    private var pauseTime:Long = 0
    private var matrixUndo: Array<Array<Int>> = arrayOf(
        arrayOf(0, 0, 0, 0), // 4, 8, 0, 0
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )
    private var matrixUndo1: Array<Array<Int>> = arrayOf(
        arrayOf(0, 0, 0, 0), // 4, 8, 0, 0
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )


    private var matrix: Array<Array<Int>> = arrayOf(
        arrayOf(0, 0, 0, 0), // 4, 8, 0, 0
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0),
        arrayOf(0, 0, 0, 0)
    )

    init {

        addElement()
        addElement()
    }

    private fun addElement() {
        val elements = ArrayList<Pair<Int, Int>>()


        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] == 0) elements.add(Pair(i, j))
            }
        }

       if( elements.size==0){
           listenerGameOver=gameOver()
       }
        else{
           SaveUndMatrix()
       }
        elements.shuffle()
        if (elements.size == 0) return


        val element = elements[0]
        matrix[element.first][element.second] = MIN_VALUE
    }

    fun getMatrix(): ArrayList<Int> {

        val list = ArrayList<Int>()

        list.clear()
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {

                list.add(matrix[i][j])
            }
        }

        return list
    }

    fun moveLeft() {

        SaveUndMatrix1()
        for (i in 0 until matrix.size) {

            var state = true
            val amounts: ArrayList<Int> = arrayListOf()
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] == 0) continue;

                if (amounts.isEmpty()) {
                    amounts.add(matrix[i][j])
                } else {

                    if (amounts.last() == matrix[i][j] && state) {
                        state = false
                        score += matrix[i][j] * 2
                        amounts[amounts.lastIndex] = matrix[i][j] * 2
                    } else {
                        state = true
                        amounts.add(matrix[i][j])
                    }
                }
                matrix[i][j] = 0
            }

            for (j in 0 until amounts.size) {
                matrix[i][j] = amounts[j]

            }
        }
        addElement()

    }

    fun moveRight() {
        SaveUndMatrix1()

        for (i in matrix.indices) {
            var state = true
            val amounts: ArrayList<Int> = arrayListOf()
            for (j in 0..matrix.size - 1) {
                if (matrix[matrix.size - 1 - i][matrix.size - 1 - j] == 0) continue;

                if (amounts.isEmpty()) {
                    amounts.add(matrix[matrix.size - 1 - i][matrix.size - 1 - j])
                } else {
                    if (amounts.last() == matrix[matrix.size - 1 - i][matrix.size - 1 - j] && state) {
                        state = false
                        score += matrix[matrix.size - 1 - i][matrix.size - 1 - j] * 2
                        amounts[amounts.lastIndex] =
                            matrix[matrix.size - 1 - i][matrix.size - 1 - j] * 2
                    } else {
                        state = true
                        amounts.add(matrix[matrix.size - 1 - i][matrix.size - 1 - j])
                    }
                }
                matrix[matrix.size - 1 - i][matrix.size - 1 - j] = 0
            }

            for (j in 0 until amounts.size) {
                matrix[matrix.size - 1 - i][matrix.size - 1 - j] = amounts[j]

            }
        }
        addElement()

    }

    fun moveUp() {

        SaveUndMatrix1()
        for (i in matrix.indices) {
            var state = true
            val amounts: ArrayList<Int> = arrayListOf()
            for (j in matrix.indices) {
                if (matrix[j][i] == 0) continue;

                if (amounts.isEmpty()) {
                    amounts.add(matrix[j][i])
                } else {
                    if (amounts.last() == matrix[j][i] && state) {
                        state = false
                        score += matrix[j][i] * 2
                        amounts[amounts.lastIndex] = matrix[j][i] * 2
                    } else {
                        state = true
                        amounts.add(matrix[j][i])
                    }
                }
                matrix[j][i] = 0
            }

            for (j in 0 until amounts.size) {
                matrix[j][i] = amounts[j]

            }
        }
        addElement()

    }


    fun moveDown() {

        SaveUndMatrix1()
        val list = ArrayList<Int>()
        list.clear()
        for (i in matrix.indices) {
            var state = true
            val amounts: ArrayList<Int> = arrayListOf()
            for (j in matrix.indices) {
                if (matrix[matrix.size - 1 - j][matrix.size - 1 - i] == 0) continue;

                if (amounts.isEmpty()) {
                    amounts.add(matrix[matrix.size - 1 - j][matrix.size - 1 - i])
                } else {
                    if (amounts.last() == matrix[matrix.size - 1 - j][matrix.size - 1 - i] && state) {
                        state = false
                        score += matrix[matrix.size - 1 - j][matrix.size - 1 - i] * 2
                        amounts[amounts.lastIndex] =
                            matrix[matrix.size - 1 - j][matrix.size - 1 - i] * 2
                    } else {
                        state = true
                        amounts.add(matrix[matrix.size - 1 - j][matrix.size - 1 - i])
                    }
                }
                matrix[matrix.size - 1 - j][matrix.size - 1 - i] = 0
            }

            for (j in 0 until amounts.size) {
                matrix[matrix.size - 1 - j][matrix.size - 1 - i] = amounts[j]
                list.add(amounts[j])
            }
        }
        addElement()

    }

    fun getScore(): Int {
        return score
    }
    fun getPauseTime(): Long {
        return pauseTime
    }

    fun getBestScore(): Int {
        bestScore = if (score <= bestScore) bestScore else score
        return bestScore
    }

    fun onMove() {
        for(i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] != matrixUndo1[i][j]) {
                    move++
                    return
                }
            }
        }
    }
    fun getMove(): Int {

        return move
    }

    fun getUndo() {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrix[i][j] != matrixUndo[i][j]) {
                    matrixUndo1[i]
                }
            }
        }
    }

    fun undo() {
        listenerGameOver=false
        score = scoreUndo
        move = moveUndo
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                matrix[i][j] = matrixUndo[i][j]
            }
        }
    }


    fun restart() {
        listenerGameOver=false

        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                matrix[i][j] = 0
            }
        }
        addElement()
        addElement()
        score = 0
        move = 0
        pauseTime=0
    }


    fun onStart(){
        sound= tinyDB.getBoolean("sound")
        music= tinyDB.getBoolean("music")
    }
    fun saved(pause:Long) {
        pauseTime=pause
        Log.i("pauseTime","$pause")
        Log.i("pauseTime","$pauseTime")

        tinyDB.putInt("score", score)
        tinyDB.putInt("BestScore", bestScore)
        tinyDB.putInt("moves", move)
        tinyDB.putLong("pauseTime",pauseTime)
        val list = ArrayList<Int>()
        list.clear()
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                list.add(matrix[i][j])
            }
        }

        val listUndo = ArrayList<Int>()
        listUndo.clear()
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                listUndo.add(matrixUndo[i][j])
            }
        }
        tinyDB.putListInt("list", list)
        tinyDB.putListInt("listUndo", listUndo)
        tinyDB.putInt("undoScore", scoreUndo)
        tinyDB.putInt("undoMoves", moveUndo)
    }

    fun getSaved() {

        val list = tinyDB.getListInt("list")
        if (list.size>0) {
            score = tinyDB.getInt("score")
            pauseTime = tinyDB.getLong("pauseTime")
            bestScore = tinyDB.getInt("BestScore")
            move = tinyDB.getInt("moves")


            var count1 = 0
            for (i in 0 until matrix.size) {
                for (j in 0 until matrix[i].size) {
                    matrix[i][j] = list[count1]
                    count1++
                }
            }
            var count2 = 0
            val listUndo = tinyDB.getListInt("listUndo")
            for (i in 0 until matrix.size) {
                for (j in 0 until matrix[i].size) {
                    matrixUndo[i][j] = listUndo[count2]
                    count2++
                }
            }


            scoreUndo = tinyDB.getInt("undoScore")
            moveUndo = tinyDB.getInt("undoMoves")
        }
    }


    private fun SaveUndMatrix() {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrixUndo[i][j] != matrixUndo1[i][j]) {
                    matrixUndo[i][j] = matrixUndo1[i][j]
                    scoreUndo = score
                    moveUndo = move
                }
            }
        }

    }

 private fun SaveUndMatrix1() {
        for (i in 0 until matrix.size) {
            for (j in 0 until matrix[i].size) {
                if (matrixUndo1[i][j] != matrix[i][j]) {
                    matrixUndo1[i][j] = matrix[i][j]

                }
            }
        }

    }


    private fun gameOver(): Boolean {
        for (i in matrix.indices) {
            val chekList: ArrayList<Int> = arrayListOf()
            for (j in 0 until matrix[i].size) {
                if (chekList.isEmpty()) {
                    chekList.add(matrix[i][j])
                } else {
                    if (chekList.last() == matrix[i][j]){
                        return false
                    }else{
                        chekList.add(matrix[i][j])
                    }
                }
            }
        }
        for (i in matrix.indices) {
            val chekList: ArrayList<Int> = arrayListOf()
            for (j in 0 until matrix[i].size) {
                if (chekList.isEmpty()) {
                    chekList.add(matrix[j][i])
                } else {
                    if (chekList.last() == matrix[j][i]){
                        return false
                    }else{
                        chekList.add(matrix[j][i])
                    }
                }
            }
        }
        return true

    }

    fun  getGameOver():Boolean{
        return  listenerGameOver
    }
 fun  getSound():Boolean{
        return  sound
    }
 fun  getMusic():Boolean{
        return  music
    }

}