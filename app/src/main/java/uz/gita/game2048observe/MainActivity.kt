package uz.gita.game2048observe

import android.annotation.SuppressLint
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import uz.gita.game2048observe.data.Movement
import uz.gita.game2048observe.utils.MovementDetector
import uz.gita.game2048observe.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var textView_time: Chronometer
    private val buttons: ArrayList<TextView> = ArrayList(16)
    private lateinit var movesText: TextView
    private lateinit var scoreText: TextView
    private lateinit var bestScoreText: TextView
    private lateinit var newGame: TextView
    private lateinit var undo: TextView
    private lateinit var viewModel:MainViewModel
    private var score=0
    private var move=0
    private var sound=true
    private var music_e=true
    private  var mp:MediaPlayer?=null
    private  var music:MediaPlayer?=null


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]


        viewModel.onStart()
        mp = MediaPlayer.create(this, R.raw.music)
        music = MediaPlayer.create(this, R.raw.music_app)
        viewModel.sound.observe(this){
            sound=it
        }
         viewModel.music.observe(this){
            music_e=it
        }

        loadViews()
        textView_time.start()
        viewModel.list.observe(this ){
            changeState(it)
        }
        viewModel.score.observe(this){
            scoreText.text="score\n$it"
            score=it
        }


        viewModel.bestScore.observe(this){
            bestScoreText.text="best\n$it"
        }
        viewModel.move.observe(this){
            movesText.text="$it moves"
            move=it
        }
        viewModel.pauseTime.observe(this){
            textView_time.base=SystemClock.elapsedRealtime()-it
        }
        viewModel.gameOver.observe(this){

            if (it==true){
                textView_time.stop()
                dialogGameOver()
            }
            else{
                textView_time.start()
            }
        }
    }

    private fun loadViews() {
        val mainContainer = findViewById<LinearLayout>(R.id.mainContainer)
        textView_time = findViewById(R.id.time_text)
        movesText = findViewById(R.id.moves_text)
        scoreText = findViewById(R.id.score_text)
        bestScoreText = findViewById(R.id.best_score)
        newGame = findViewById(R.id.new_game)
        undo = findViewById(R.id.undo_text)
        val back=findViewById<TextView>(R.id.text2048)
            back.setOnClickListener {
                YoYo.with(Techniques.Tada)
                    .duration(100)
                    .repeat(0)
                    .playOn(back);
            finish()
        }

        newGame.setOnClickListener {
            YoYo.with(Techniques.Tada)
            .duration(100)
            .repeat(0)
            .playOn(newGame);
            dialogNewGame()


        }
        undo.setOnClickListener {
            YoYo.with(Techniques.Tada)
            .duration(100)
            .repeat(0)
            .playOn(undo);
            viewModel.undo()

        }
        for (i in 0 until mainContainer.childCount) {
            val childContainer = mainContainer.getChildAt(i) as LinearLayout
            for (j in 0 until childContainer.childCount) {
                buttons.add(childContainer.getChildAt(j) as TextView)
            }
        }

        val movementDetector = MovementDetector(this)


        movementDetector.setOnMovementListener {
            var moves=move

            when (it) {
                Movement.LEFT -> viewModel.moveLeft()
                Movement.RIGHT -> viewModel.moveRight()
                Movement.DOWN -> viewModel.moveDown()
                Movement.UP -> viewModel.moveUp()

            }
      if (sound && move>moves ) {

          mp?.start()
      }
        }

        mainContainer.setOnTouchListener(movementDetector)
    }

     private fun changeState(list:List<Int>) {
        for (i in list.indices) {

                val button = buttons[i]

                val value = list[i]

                if (value == 0) button.text = ""
                else button.text = list[i].toString()

//                YoYo.with(Techniques.BounceInUp)
//                    .duration(100)
//                    .repeat(1)
//                    .playOn(button);

                button.setBackgroundResource(
                    when (value) {
                        2 -> R.drawable.background_1
                        4 -> R.drawable.background_2
                        8 -> R.drawable.background_3
                        16 -> R.drawable.background_4
                        32 -> R.drawable.background_5
                        64 -> R.drawable.background_6
                        128 -> R.drawable.background_7
                        256 -> R.drawable.background_8
                        512 -> R.drawable.background_9
                        1024 -> R.drawable.background_10
                        2048 -> R.drawable.background_11
                        else -> R.drawable.background_default
                    }
                )


            }
        }


    override fun onPause() {
        val pauseTime = SystemClock.elapsedRealtime() - textView_time.base
        super.onPause()
        textView_time.stop()
        viewModel.onPause(pauseTime)

        if (music_e) {
            music?.stop()
            music?.release()
        }
    }

    override fun onResume() {
        super.onResume()
        music = MediaPlayer.create(this, R.raw.music_app)
        viewModel.onResume()
        textView_time.start()

        if (music_e){
            music?.start()
            music?.isLooping=true
        }


    }


  private  fun dialogGameOver(){

        val builder=AlertDialog.Builder(this,R.style.CustomAlertDialog).create()
        val view:View= LayoutInflater.from(this).inflate(R.layout.game_over,null)
        val textView:TextView= view.findViewById(R.id.text_game_over)
        val playAgain:TextView= view.findViewById(R.id.play_again)
        val undo:TextView= view.findViewById(R.id.undo_bt)

        builder.setView(view)
        textView.text="You earned $score points with $move moves in ${textView_time.text}. "

        playAgain.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(100)
                .repeat(0)
                .playOn(playAgain);

            viewModel.restart()
            builder.cancel()
        }

      undo.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(100)
                .repeat(0)
                .playOn(undo);

            viewModel.undo()
            builder.cancel()
        }


        builder.setCancelable(false)
        builder.show()
    }
  private  fun dialogNewGame(){

        val builder=AlertDialog.Builder(this,R.style.CustomAlertDialog).create()
        val view:View= LayoutInflater.from(this).inflate(R.layout.new_game,null)
        val no:TextView= view.findViewById(R.id.no)
        val yes:TextView= view.findViewById(R.id.yes)

        builder.setView(view)

        yes.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(100)
                .repeat(0)
                .playOn(yes);

            viewModel.restart()
            builder.cancel()
        }
 no.setOnClickListener {

            builder.cancel()
        }


        builder.setCancelable(true)
        builder.show()
    }


}
