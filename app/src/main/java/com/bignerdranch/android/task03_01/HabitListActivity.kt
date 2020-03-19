package com.bignerdranch.android.task03_01

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.task03_01.data.HabitRepository
import com.bignerdranch.android.task03_01.data.entity.Habit
import kotlinx.android.synthetic.main.activity_habit_list.*

class HabitListActivity : AppCompatActivity() {
    private var adapter: CrimeAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit_list)

        floating_action_button.setOnClickListener {
            val intent = Intent(this@HabitListActivity, HabitActivity::class.java)
            startActivity(intent)
        }
    }

    private inner class HabitHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val linearLayout: RelativeLayout = itemView.findViewById(R.id.habit_relative_layout)
        private val name: TextView = itemView.findViewById(R.id.habit_list_item_name)
        private val description: TextView = itemView.findViewById(R.id.habit_list_item_description)
        private val priority: TextView = itemView.findViewById(R.id.habit_list_item_priority)
        private val type: TextView = itemView.findViewById(R.id.habit_list_item_type)
        private val quantity: TextView = itemView.findViewById(R.id.habit_list_item_quantity)
        private val periodicity: TextView = itemView.findViewById(R.id.habit_list_item_periodicity)
        private var habit: Habit? = null

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            val intent = Intent(this@HabitListActivity, HabitActivity::class.java)
            intent.putExtra(HabitActivity.EXTRA_HABIT_ID_KEY, habit?.id)
            startActivity(intent)
        }

        fun bindCrime(habit: Habit) {
            this.habit = habit
            name.text = habit.name
            description.text = habit.description
            priority.text = habit.priority?.name
            type.text = habit.type?.name
            quantity.text = getString(R.string.quantity) + habit.quantity.toString()
            periodicity.text = getString(R.string.periodicity) + habit.periodicity.toString()
            linearLayout.setBackgroundColor(resources.getColor(habit.color.colorId))
        }
    }

    private inner class CrimeAdapter(var habits: List<Habit>) : RecyclerView.Adapter<HabitHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HabitHolder {
            val layoutInflater = LayoutInflater.from(this@HabitListActivity)
            val view = layoutInflater.inflate(R.layout.list_item_habit, parent, false)
            return HabitHolder(view)
        }

        override fun onBindViewHolder(holder: HabitHolder, position: Int) {
            val crime = habits[position]
            holder.bindCrime(crime)
        }

        override fun getItemCount(): Int = habits.size
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    private fun updateUI() {
        val habits = HabitRepository.habitList

        if (adapter == null) {
            adapter = CrimeAdapter(habits)
            crime_recycler_view.adapter = adapter
        } else {
            adapter?.habits = habits
            adapter?.notifyDataSetChanged()
        }
    }
}
