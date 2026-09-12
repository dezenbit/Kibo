package com.dezenbit.habitos.data

import android.content.Context
import android.net.Uri
import org.json.JSONArray
import org.json.JSONObject

/**
 * Exporta/importa todos los hábitos e historial a un único archivo JSON legible,
 * para que el usuario pueda respaldar sus datos o pasarlos a otro teléfono.
 */
object BackupManager {

    private const val SCHEMA_VERSION = 1

    fun toJson(backup: BackupData): String {
        val root = JSONObject()
        root.put("schemaVersion", SCHEMA_VERSION)

        val habitsArray = JSONArray()
        backup.habits.forEach { habit ->
            habitsArray.put(
                JSONObject().apply {
                    put("id", habit.id)
                    put("name", habit.name)
                    put("emoji", habit.emoji)
                    put("colorHex", habit.colorHex)
                    put("frequencyDays", habit.frequencyDays)
                    put("reminderHour", habit.reminderHour)
                    put("reminderMinute", habit.reminderMinute)
                    put("reminderEnabled", habit.reminderEnabled)
                    put("createdAtEpochDay", habit.createdAtEpochDay)
                    put("archived", habit.archived)
                    put("type", habit.type.name)
                    put("targetValue", habit.targetValue)
                    put("unit", habit.unit)
                    put("category", habit.category)
                    put("vacationUntilEpochDay", habit.vacationUntilEpochDay)
                    put("sortOrder", habit.sortOrder)
                }
            )
        }
        root.put("habits", habitsArray)

        val completionsArray = JSONArray()
        backup.completions.forEach { completion ->
            completionsArray.put(
                JSONObject().apply {
                    put("habitId", completion.habitId)
                    put("epochDay", completion.epochDay)
                    put("note", completion.note ?: JSONObject.NULL)
                    put("value", completion.value ?: JSONObject.NULL)
                }
            )
        }
        root.put("completions", completionsArray)

        return root.toString(2)
    }

    fun fromJson(json: String): BackupData {
        val root = JSONObject(json)

        val habits = mutableListOf<Habit>()
        val habitsArray = root.optJSONArray("habits") ?: JSONArray()
        for (i in 0 until habitsArray.length()) {
            val obj = habitsArray.getJSONObject(i)
            habits.add(
                Habit(
                    id = obj.optLong("id", 0),
                    name = obj.optString("name", "Hábito"),
                    emoji = obj.optString("emoji", "\u2B50"),
                    colorHex = obj.optString("colorHex", "#6750A4"),
                    frequencyDays = obj.optString("frequencyDays", "1,2,3,4,5,6,7"),
                    reminderHour = obj.optInt("reminderHour", -1),
                    reminderMinute = obj.optInt("reminderMinute", -1),
                    reminderEnabled = obj.optBoolean("reminderEnabled", false),
                    createdAtEpochDay = obj.optLong("createdAtEpochDay"),
                    archived = obj.optBoolean("archived", false),
                    type = runCatching { HabitType.valueOf(obj.optString("type", "BOOLEAN")) }
                        .getOrDefault(HabitType.BOOLEAN),
                    targetValue = obj.optDouble("targetValue", 1.0),
                    unit = obj.optString("unit", ""),
                    category = obj.optString("category", ""),
                    vacationUntilEpochDay = obj.optLong("vacationUntilEpochDay", -1),
                    sortOrder = obj.optInt("sortOrder", 0)
                )
            )
        }

        val completions = mutableListOf<HabitCompletion>()
        val completionsArray = root.optJSONArray("completions") ?: JSONArray()
        for (i in 0 until completionsArray.length()) {
            val obj = completionsArray.getJSONObject(i)
            completions.add(
                HabitCompletion(
                    habitId = obj.optLong("habitId"),
                    epochDay = obj.optLong("epochDay"),
                    note = if (obj.isNull("note")) null else obj.optString("note"),
                    value = if (obj.isNull("value")) null else obj.optDouble("value")
                )
            )
        }

        return BackupData(habits = habits, completions = completions)
    }

    fun writeToUri(context: Context, uri: Uri, backup: BackupData) {
        context.contentResolver.openOutputStream(uri)?.use { output ->
            output.write(toJson(backup).toByteArray())
        }
    }

    fun readFromUri(context: Context, uri: Uri): BackupData {
        val text = context.contentResolver.openInputStream(uri)?.use { input ->
            input.readBytes().decodeToString()
        } ?: throw IllegalStateException("No se pudo leer el archivo")
        return fromJson(text)
    }
}
