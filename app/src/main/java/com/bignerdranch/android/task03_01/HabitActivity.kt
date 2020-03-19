package com.bignerdranch.android.task03_01

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.ColorUtils
import androidx.core.view.marginTop
import com.bignerdranch.android.task03_01.data.HabitRepository
import com.bignerdranch.android.task03_01.data.entity.Habit
import com.bignerdranch.android.task03_01.data.entity.HabitColor
import com.bignerdranch.android.task03_01.data.entity.HabitPriority
import com.bignerdranch.android.task03_01.data.entity.HabitType
import kotlinx.android.synthetic.main.activity_habit.*
import java.util.*


class HabitActivity : AppCompatActivity() {
    private var habit: Habit? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_habit)

        habit = getHabit()

        initUI()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        habit!!.name = savedInstanceState.getString(EXTRA_HABIT_NAME_KEY)!!
        habit!!.description = savedInstanceState.getString(EXTRA_HABIT_DESCRIPTION_KEY)!!
        habit!!.type = savedInstanceState.getSerializable(EXTRA_HABIT_TYPE_KEY)!! as HabitType
        habit!!.priority = savedInstanceState.getSerializable(EXTRA_HABIT_PRIORITY_KEY)!! as HabitPriority
        habit!!.quantity = savedInstanceState.getInt(EXTRA_HABIT_QUANTITY_KEY)
        habit!!.periodicity = savedInstanceState.getInt(EXTRA_HABIT_PERIODICITY_KEY)
        habit!!.color = savedInstanceState.getSerializable(EXTRA_HABIT_COLOR_KEY)!! as HabitColor
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_HABIT_NAME_KEY, habit!!.name)
        outState.putString(EXTRA_HABIT_DESCRIPTION_KEY, habit!!.description)
        outState.putSerializable(EXTRA_HABIT_TYPE_KEY, habit!!.type)
        outState.putSerializable(EXTRA_HABIT_PRIORITY_KEY, habit!!.priority)
        outState.putInt(EXTRA_HABIT_PERIODICITY_KEY, habit!!.periodicity)
        outState.putInt(EXTRA_HABIT_QUANTITY_KEY, habit!!.quantity)
        outState.putSerializable(EXTRA_HABIT_COLOR_KEY, habit!!.color)
    }

    private fun initUI() {
        habit_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.name = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_description.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.description = s.toString()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_quantity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.quantity = s.toString().ifEmpty { "0" }.toInt()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })
        habit_periodicity.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                habit!!.periodicity =  s.toString().ifEmpty { "0" }.toInt()
            }

            override fun afterTextChanged(s: Editable) {
            }
        })

        initPrioritySpinner()
        initTypeRadioGroup()
        initColorPicker()
        initSaveButton()
    }

    private fun getHabit(): Habit {
        return if (intent.hasExtra(EXTRA_HABIT_ID_KEY)) {
            val habitId = intent.getSerializableExtra(EXTRA_HABIT_ID_KEY) as UUID
            HabitRepository.getHabit(habitId)
        } else {
            Habit()
        }
    }

    private fun initSaveButton() {
        save_habit_button.setOnClickListener {
            run {
                habit?.let {
                    HabitRepository.saveHabit(habit!!)
                }
                finish()
            }
        }
    }

    private fun initColorPicker() {
        HabitColor.values().forEachIndexed{ index, habitColor ->
            run {
                val previousColorId: Int = when (index) {
                    0 -> habitColor.colorId
                    else -> {
                        HabitColor.values()[index - 1].colorId
                    }
                }
                val nextColorId = when (index) {
                    HabitColor.values().size - 1 -> habitColor.colorId
                    else -> {
                        HabitColor.values()[index + 1].colorId
                    }
                }

                val previousColor: Int = resources.getColor(previousColorId)
                val nextColor: Int = resources.getColor(nextColorId)
                val currentColor: Int = resources.getColor(habitColor.colorId)

                val drawable = GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    intArrayOf(
                        ColorUtils.blendARGB(previousColor, currentColor, 0.5f),
                        currentColor,
                        ColorUtils.blendARGB(currentColor, nextColor, 0.5f)
                    )
                ).apply {
                    shape = GradientDrawable.RECTANGLE
                    gradientType = GradientDrawable.LINEAR_GRADIENT
                }

                val colorButton = Button(this).apply {
                    setBackgroundColor(currentColor)
                    layoutParams = LinearLayout.LayoutParams(200, 200)
                    setOnClickListener {
                        current_color_img.setBackgroundColor(currentColor)
                        habit!!.color = habitColor
                        current_color_rgb.text = "r: ${Color.red(currentColor)};\ng: ${Color.green(currentColor)};\nb: ${Color.blue(currentColor)};"

                        val hsvArray = floatArrayOf(0f, 0f,0f)
                        Color.colorToHSV(currentColor, hsvArray)
                        current_color_hsv.text = "h: ${hsvArray[0]};\ns: ${hsvArray[1]};\nv: ${hsvArray[2]};"
                    }
                }

                val linearLayout: LinearLayout = LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    gravity = Gravity.CENTER
                    layoutParams = LinearLayout.LayoutParams(300, 300)
                    background = drawable
                    addView(colorButton)
                }

                color_picker.addView(linearLayout)
            }
        }
    }

    private fun initPrioritySpinner() {
        val priorities = HabitPriority.values().map { it.name }
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, priorities)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        habit_priority.adapter = adapter
        habit_priority.setSelection(0)

        habit_priority.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                habit!!.priority = HabitPriority.values()[position]
            }
        }
    }

    private fun initTypeRadioGroup() {
        HabitType.values().forEach {
            run {
                habit_type.addView(
                    RadioButton(this).apply { text = it.name }
                )
            }
        }

        habit_type.setOnCheckedChangeListener { _, checkedId ->
            val selectedRadioButton = findViewById<RadioButton>(checkedId)
            val typeIndex = HabitType.values().indexOfFirst { it.name == selectedRadioButton.text }
            habit!!.type = HabitType.values()[typeIndex]
        }
    }

    private fun updateUI() {
        habit_name.setText(habit!!.name)
        habit_description.setText(habit!!.description)

        val priorityIndex = HabitPriority.values().indexOf(habit!!.priority)
        habit_priority.setSelection(priorityIndex)

        val typeIndex = HabitType.values().indexOf(habit!!.type)
        (habit_type.getChildAt(typeIndex) as RadioButton).isChecked = true

        habit_quantity.setText(habit!!.quantity.toString())
        habit_periodicity.setText(habit!!.periodicity.toString())

        current_color_img.setBackgroundColor(resources.getColor(habit!!.color.colorId))

        current_color_rgb.text = "r: ${Color.red(resources.getColor(habit!!.color.colorId))};\ng: ${Color.green(resources.getColor(habit!!.color.colorId))};\nb: ${Color.blue(resources.getColor(habit!!.color.colorId))};"

        val hsvArray = floatArrayOf(0f, 0f,0f)
        Color.colorToHSV(resources.getColor(habit!!.color.colorId), hsvArray)
        current_color_hsv.text = "h: ${hsvArray[0]};\ns: ${hsvArray[1]};\nv: ${hsvArray[2]};"
    }

    companion object {
        const val EXTRA_HABIT_ID_KEY = "extra_habit_id_key"
        const val EXTRA_HABIT_NAME_KEY = "extra_habit_name_key"
        const val EXTRA_HABIT_DESCRIPTION_KEY = "extra_habit_description_key"
        const val EXTRA_HABIT_TYPE_KEY = "extra_habit_type_key"
        const val EXTRA_HABIT_PRIORITY_KEY = "extra_habit_priority_key"
        const val EXTRA_HABIT_QUANTITY_KEY = "extra_habit_quantity_key"
        const val EXTRA_HABIT_PERIODICITY_KEY = "extra_habit_periodicity_key"
        const val EXTRA_HABIT_COLOR_KEY = "extra_habit_color_key"


    }
}
