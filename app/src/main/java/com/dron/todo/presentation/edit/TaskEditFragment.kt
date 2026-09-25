package com.dron.todo.presentation.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dron.todo.R
import com.dron.todo.data.local.entity.TaskEntity
import com.dron.todo.databinding.FragmentTaskEditBinding
import com.dron.todo.util.DateTimeUtils
import com.dron.todo.util.PermissionHelper
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskEditViewModel by viewModels()

    private val args: TaskEditFragmentArgs by navArgs()

    /** Выбранный timestamp в миллисекундах */
    private var selectedDueDate: Long = 0L

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTaskId(args.taskId)

        viewModel.observeTask()?.observe(viewLifecycleOwner) { task ->
            viewModel.setExistingTask(task)
            fillFields(task)
        }

        binding.dueDateInput.setOnClickListener {
            showDatePicker()
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun fillFields(task: TaskEntity) {
        binding.titleInput.setText(task.title)
        binding.descriptionInput.setText(task.description ?: "")

        selectedDueDate = task.dueDate
        binding.dueDateInput.setText(DateTimeUtils.format(task.dueDate))

        binding.completedCheckbox.isChecked = task.completed

        when (task.priority) {
            0 -> binding.priorityLow.isChecked = true
            1 -> binding.priorityMedium.isChecked = true
            2 -> binding.priorityHigh.isChecked = true
        }
    }

    private fun showDatePicker() {
        val calendar = DateTimeUtils.toCalendar(selectedDueDate)

        val picker = MaterialDatePicker.Builder.datePicker()
            .setTitleText("Выберите дату")
            .setSelection(calendar.timeInMillis)
            .build()

        picker.addOnPositiveButtonClickListener { selection ->
            selectedDueDate = selection
            showTimePicker()
        }

        picker.show(parentFragmentManager, "date_picker")
    }

    private fun showTimePicker() {
        val calendar = DateTimeUtils.toCalendar(selectedDueDate)

        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(calendar.get(java.util.Calendar.HOUR_OF_DAY))
            .setMinute(calendar.get(java.util.Calendar.MINUTE))
            .setTitleText("Выберите время")
            .build()

        picker.addOnPositiveButtonClickListener {
            val newCal = DateTimeUtils.toCalendar(selectedDueDate).apply {
                set(java.util.Calendar.HOUR_OF_DAY, picker.hour)
                set(java.util.Calendar.MINUTE, picker.minute)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }
            selectedDueDate = newCal.timeInMillis
            binding.dueDateInput.setText(DateTimeUtils.format(selectedDueDate))
        }

        picker.show(parentFragmentManager, "time_picker")
    }

    private fun saveTask() {
        val title = binding.titleInput.text?.toString().orEmpty()
        val description = binding.descriptionInput.text?.toString()
        val completed = binding.completedCheckbox.isChecked

        val priority = when (binding.priorityGroup.checkedRadioButtonId) {
            R.id.priorityLow -> 0
            R.id.priorityHigh -> 2
            else -> 1
        }

        if (title.isBlank()) {
            Toast.makeText(requireContext(), "Введите название", Toast.LENGTH_SHORT).show()
            return
        }

        if (selectedDueDate > 0 && !PermissionHelper.canScheduleExactAlarms(requireContext())) {
            Toast.makeText(
                requireContext(),
                "Разрешите точные напоминания для своевременных уведомлений",
                Toast.LENGTH_LONG
            ).show()
            PermissionHelper.requestExactAlarmPermission(requireContext())
        }

        viewModel.save(
            title = title,
            description = description,
            priority = priority,
            dueDate = selectedDueDate,
            completed = completed,
            onSuccess = {
                Toast.makeText(requireContext(), "Сохранено", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}