package edu.ucne.composeTarea1.tareas.local.di

import android.content.Context
import androidx.room.Room
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import dagger.hilt.android.qualifiers.ApplicationContext
import edu.ucne.composeTarea1.data.remote.MovimientosApi
import edu.ucne.composeTarea1.tareas.local.Jugador.JugadorDao
import edu.ucne.composeTarea1.tareas.local.AppDataBase
import edu.ucne.composeTarea1.tareas.local.Logro.LogroDao
import edu.ucne.composeTarea1.tareas.local.Partida.PartidaDao
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

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

    @Provides
    @Singleton
    fun provideLogroDao(db : AppDataBase): LogroDao {
        return db.logroDao()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://gestionhuacalesapi.azurewebsites.net/")
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }

    @Provides
    @Singleton
    fun provideMovimientosApi(retrofit: Retrofit): MovimientosApi {
        return retrofit.create(MovimientosApi::class.java)
    }
}