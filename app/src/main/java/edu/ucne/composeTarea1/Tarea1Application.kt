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
}