package com.example.tetrisprev.storage

import android.content.Context

class AppPreferences(ctx: Context) {

    var data = ctx.getSharedPreferences("APP_PREFERENCES", Context.MODE_PRIVATE)
    
    fun saveHighScore(highScore: Int) {
        data.edit().putInt("HIGH_SCORE", highScore).apply()
    }

    fun getHighScore(): Int {
        return data.getInt("HIGH_SCORE", 0)
    }

    fun clearHighScore() {
        data.edit().putInt("HIGH_SCORE", 0).apply()
    }
}