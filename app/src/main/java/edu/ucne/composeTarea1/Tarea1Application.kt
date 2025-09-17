package edu.ucne.composeTarea1

import android.app.Application
import android.content.Context
import androidx.activity.result.launch
import dagger.hilt.android.HiltAndroidApp
import edu.ucne.composeTarea1.domain.model.Jugador
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class Tarea1Application : Application(){
    @Inject
    lateinit var jugadorRepository: JugadorRepository

    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onCreate() {
        super.onCreate()
        setupDefaultPlayer()
    }
    private fun setupDefaultPlayer() {
        applicationScope.launch {
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val isFirstLaunch = prefs.getBoolean("is_first_launch", true)

            if (isFirstLaunch) {

                val yoPlayer = jugadorRepository.getJugadorByName("Yo")
                if (yoPlayer == null) {
                    jugadorRepository.save(Jugador(nombres = "Yo", partidas = 0))
                }
                prefs.edit().putBoolean("is_first_launch", false).apply()
            }
        }
    }
}