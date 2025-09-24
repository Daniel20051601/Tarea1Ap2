package edu.ucne.composeTarea1.tareas.local

import androidx.room.TypeConverter
import edu.ucne.composeTarea1.tareas.Presentation.Game.Partida.Player
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun fromBoardToString(board: List<Player?>): String {
        val serializableList = board.map { it?.name }
        return Json.encodeToString(serializableList)
    }

    @TypeConverter
    fun fromStringToBoard(jsonString: String): List<Player?> {
        val serializableList = Json.decodeFromString<List<String?>>(jsonString)
        return serializableList.map { name ->
            when (name) {
                "X" -> Player.X
                "O" -> Player.O
                else -> null
            }
        }
    }
}