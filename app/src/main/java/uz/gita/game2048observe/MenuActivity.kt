package uz.gita.game2048observe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import com.daimajia.androidanimations.library.Techniques
import com.daimajia.androidanimations.library.YoYo
import uz.gita.game2048observe.repository.Repository

class MenuActivity : AppCompatActivity() {
    private var tinydb:TinyDB?=null
    private var sound=true
    private lateinit var bestScore:TextView
    private var music=true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        tinydb=TinyDB(this)
        bestScore=findViewById<TextView>(R.id.textView)
        val play=findViewById<LinearLayout>(R.id.linearLayout2)
        val about=findViewById<LinearLayout>(R.id.linearLayoutInfo)
        val soundL=findViewById<LinearLayout>(R.id.linearLayoutSound)
        val soundText:TextView = findViewById(R.id.soundText)
        val musicL=findViewById<LinearLayout>(R.id.linearLayoutusic)
        val musicText:TextView = findViewById(R.id.MusicText)


        sound =tinydb!!.getBoolean("sound")
       music =tinydb!!.getBoolean("music")
        play.setOnClickListener {

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        about.setOnClickListener {

            val intent=Intent(this,AboutActivity::class.java)
            startActivity(intent)
        }

        if (sound){

            soundText.setText("Sound on")
        }
        else{

            soundText.setText("Sound of")
        }
        if (music){

            musicText.setText("Music on")
        }
        else{

            musicText.setText("Music of")
        }

        soundL.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(100)
                .repeat(0)
                .playOn(soundL);
            if (sound){
                sound=false
                tinydb?.putBoolean("sound",sound)
                soundText.setText("Sound of")
            }
            else{
                sound=true
                tinydb?.putBoolean("sound",sound)
                soundText.setText("Sound on")
            }

        }
        musicL.setOnClickListener {
            YoYo.with(Techniques.Tada)
                .duration(100)
                .repeat(0)
                .playOn(musicL);
            if (music){
                music=false
                tinydb?.putBoolean("music",music)
                musicText.setText("Music of")
            }
            else{
                music=true
                tinydb?.putBoolean("music",music)
                musicText.setText("Music on")
            }

        }
    }

    override fun onResume() {
        super.onResume()
        bestScore.text="Best Score \n ${tinydb?.getInt("BestScore")}"
    }

}