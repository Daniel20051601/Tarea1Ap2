package edu.ucne.composeTarea1.tareas.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.composeTarea1.tareas.local.JugadorDao
import edu.ucne.composeTarea1.tareas.local.JugadorDataBase

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDb(@ApplicationContext appContext: Context) : JugadorDataBase {
        return Room.databaseBuilder(
            appContext,
            JugadorDataBase::class.java,
            "Jugador.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideTaskDao(db: JugadorDataBase): JugadorDao {
        return db.jugadorDao()
    }
}