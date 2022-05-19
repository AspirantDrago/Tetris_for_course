package com.example.tetrisprev

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.example.tetrisprev.storage.AppPreferences
import com.example.tetrisprev.models.AppModel
import com.example.tetrisprev.view.TetrisView
import android.view.MotionEvent
import android.view.View

class GameActivity : AppCompatActivity() {
    var tvHighScore: TextView? = null
    var tvCurrentScore: TextView? = null
    var appPreferences: AppPreferences? = null
    private lateinit var tetrisView: TetrisView
    private val appModel = AppModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        appPreferences = AppPreferences(this)
        appModel.setPreferences(appPreferences)

        var btnRestart = findViewById<Button>(R.id.btn_restart)
        tvHighScore = findViewById(R.id.tv_high_score_game)
        tvCurrentScore = findViewById(R.id.tv_current_score)
        tetrisView = findViewById(R.id.view_tetris)
        tetrisView.setActivity(this)
        tetrisView.setModel(appModel)
        tetrisView.setOnTouchListener(this::onTetrisViewTouch)
        btnRestart.setOnClickListener(this::btnRestartClick)

        updateHighScore()
        updateCurrentScore()
    }

    private fun btnRestartClick(view: View) {
        appModel.restartGame()
    }

    private fun onTetrisViewTouch(view: View, event: MotionEvent) : Boolean {
        if (appModel.isGameOver() || appModel.isGameAwaitingStart()) {
            appModel.startGame()
            tetrisView.setGameCommandWithDelay(AppModel.Motions.DOWN)
        } else if (appModel.isGameActive()) {
            when (resolveTouchDirection(view, event)) {
                0 -> moveTetramino(AppModel.Motions.LEFT)
                1 -> moveTetramino(AppModel.Motions.ROTATE)
                2 -> moveTetramino(AppModel.Motions.DOWN)
                3 -> moveTetramino(AppModel.Motions.RIGHT)
            }
        }
        return true
    }

    private fun resolveTouchDirection(view: View, event: MotionEvent) : Int {
        val x = event.x / view.width
        val y = event.y / view.height
        return if (y > x) {
            if (x > 1 - y) 2 else 0
        } else {
            if (x > 1 - y) 3 else 1
        }
    }

    private fun moveTetramino(motion: AppModel.Motions) {
        if (appModel.isGameActive()) {
            tetrisView.setGameCommand(motion)
        }
    }

    private fun updateHighScore() {
        tvHighScore?.text = appPreferences?.getHighScore().toString()
    }

    private fun updateCurrentScore() {
        tvCurrentScore?.text = "0"
    }
}