package edu.ucne.composeTarea1.tareas.local.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorDao
import edu.ucne.composeTarea1.tareas.local.AppDataBase
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao

@InstallIn(SingletonComponent::class)
@Module
object AppModule {
    @Provides
    @Singleton
    fun provideJugadorDb(@ApplicationContext appContext: Context) : AppDataBase {
        return Room.databaseBuilder(
            appContext,
            AppDataBase::class.java,
            "Jugador.db")
            .fallbackToDestructiveMigration()
            .build()
    }
    @Provides
    @Singleton
    fun provideTaskDao(db: AppDataBase): JugadorDao {
        return db.jugadorDao()
    }

    @Provides
    @Singleton
    fun providePartidaDao(db: AppDataBase): PartidaDao {
        return db.PartidaDao()
    }
}