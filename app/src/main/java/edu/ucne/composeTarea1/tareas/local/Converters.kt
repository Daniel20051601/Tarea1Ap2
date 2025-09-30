package edu.ucne.composeTarea1.tareas.local

import androidx.room.TypeConverter
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromBoardToString(board: List<Player?>): String {
        return board.joinToString("") {
            when (it) {
                Player.X -> "X"
                Player.O -> "O"
                else -> "."
            }
        }
    }

    @TypeConverter
    fun fromStringToBoard(data: String): List<Player?> {
        return data.map {
            when (it) {
                'X' -> Player.X
                'O' -> Player.O
                else -> null
            }
        }
    }
}