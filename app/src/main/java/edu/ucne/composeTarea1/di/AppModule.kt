package edu.ucne.composeTarea1.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.ucne.composeTarea1.domain.repository.JugadorRepository
import edu.ucne.composeTarea1.domain.repository.LogroRepository
import edu.ucne.composeTarea1.domain.repository.PartidaRepository
import edu.ucne.composeTarea1.tareas.local.AppDataBase
import edu.ucne.composeTarea1.tareas.local.Logro.LogroDao
import edu.ucne.composeTarea1.tareas.repository.JugadorRepositoryImpl
import edu.ucne.composeTarea1.tareas.repository.LogroRepositoryImpl
import edu.ucne.composeTarea1.tareas.repository.PartidaRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {
    @Binds
    @Singleton
    abstract fun bindJugadorRepository(
        jugadorRepositoryImpl: JugadorRepositoryImpl
    ): JugadorRepository

    @Binds
    @Singleton
    abstract fun bindPartidaRepository(
        partidaRepositoryImpl: PartidaRepositoryImpl
    ): PartidaRepository

    @Binds
    @Singleton
    abstract fun bindLogroRepository(
        logroRepositoryImpl: LogroRepositoryImpl
    ): LogroRepository
}