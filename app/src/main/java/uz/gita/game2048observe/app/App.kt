package uz.gita.game2048observe.app

import android.app.Application
import uz.gita.game2048observe.repository.Repository

class App: Application() {


    public var instance=this
    override fun onCreate() {
        super.onCreate()

        Repository.instens(this)
    }
}