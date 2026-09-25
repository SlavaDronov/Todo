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
import com.dron.todo.databinding.FragmentTaskEditBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TaskEditFragment : Fragment() {

    private var _binding: FragmentTaskEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskEditViewModel by viewModels()

    private val args: TaskEditFragmentArgs by navArgs()

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

        // В режиме редактирования — заполняем поля
        viewModel.observeTask()?.observe(viewLifecycleOwner) { task ->
            viewModel.setExistingTask(task)
            fillFields(task)
        }

        binding.saveButton.setOnClickListener {
            saveTask()
        }
    }

    private fun fillFields(task: com.dron.todo.data.local.entity.TaskEntity) {
        binding.titleInput.setText(task.title)
        binding.descriptionInput.setText(task.description ?: "")
        binding.dueDateInput.setText(task.dueDate.toString())
        binding.completedCheckbox.isChecked = task.completed

        when (task.priority) {
            0 -> binding.priorityLow.isChecked = true
            1 -> binding.priorityMedium.isChecked = true
            2 -> binding.priorityHigh.isChecked = true
        }
    }

    private fun saveTask() {
        val title = binding.titleInput.text?.toString().orEmpty()
        val description = binding.descriptionInput.text?.toString()
        val dueDate = binding.dueDateInput.text?.toString()?.toLongOrNull() ?: 0L
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

        viewModel.save(
            title = title,
            description = description,
            priority = priority,
            dueDate = dueDate,
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