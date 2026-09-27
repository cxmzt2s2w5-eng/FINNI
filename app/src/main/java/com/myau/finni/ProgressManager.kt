package com.myau.finni

import android.content.Context


class ProgressManager(
    context: Context
) {

    private val prefs =
        context.getSharedPreferences(
            "finni_progress",
            Context.MODE_PRIVATE
        )


    fun getCompletedTasks(): Set<Int> {

        return prefs
            .getStringSet(
                "completed_tasks",
                emptySet()
            )
            ?.map {
                it.toInt()
            }
            ?.toSet()
            ?: emptySet()
    }


    fun completeTask(id: Int) {

        val current =
            getCompletedTasks()
                .toMutableSet()

        current.add(id)

        prefs.edit()
            .putStringSet(
                "completed_tasks",
                current.map {
                    it.toString()
                }.toSet()
            )
            .apply()
    }


    fun isCompleted(id: Int): Boolean {

        return getCompletedTasks()
            .contains(id)
    }


    fun clearProgress() {

        prefs.edit()
            .clear()
            .apply()
    }
}