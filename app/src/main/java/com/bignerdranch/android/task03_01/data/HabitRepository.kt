package com.bignerdranch.android.task03_01.data

import com.bignerdranch.android.task03_01.data.entity.Habit
import com.bignerdranch.android.task03_01.data.entity.HabitColor
import com.bignerdranch.android.task03_01.data.entity.HabitPriority
import com.bignerdranch.android.task03_01.data.entity.HabitType
import java.lang.RuntimeException
import java.util.*

object HabitRepository {
    public val habitList: MutableList<Habit> = LinkedList()

//    init {
//        for (i in 1..10) {
//            val habit = Habit()
//            habit.name = "habit$i"
//            habit.description = "desc$i"
//            habit.priority = HabitPriority.HIGH
//            habit.type = HabitType.Type1
//            habit.quantity = 1
//            habit.periodicity = 2
//            habit.color = HabitColor.GREEN
//            habitList.add(habit)
//        }
//    }

    public fun getHabit(habitId: UUID): Habit {
        for (habit in habitList) {
            if (habit.id == habitId){
                val copyHabit = Habit()
                copyHabit.id = habit.id
                copyHabit.name = habit.name
                copyHabit.description = habit.description
                copyHabit.priority = habit.priority
                copyHabit.type = habit.type
                copyHabit.quantity = habit.quantity
                copyHabit.periodicity = habit.periodicity
                copyHabit.color = habit.color

                return copyHabit
            }
        }
        throw RuntimeException("Habit not found")
    }

    fun saveHabit(habit: Habit) {
        val habitIndex = habitList.indexOfFirst { it.id == habit.id }
        if (habitIndex == -1) {
            habitList.add(habit)
        } else {
            habitList[habitIndex] = habit
        }
    }
}