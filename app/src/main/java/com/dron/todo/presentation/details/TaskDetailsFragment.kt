package com.dron.todo.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dron.todo.R
import com.dron.todo.databinding.FragmentTaskDetailsBinding
import com.dron.todo.util.DateTimeUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlin.getValue

@AndroidEntryPoint
class TaskDetailsFragment : Fragment() {

    private var _binding: FragmentTaskDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskDetailsViewModel by viewModels()

    private val args: TaskDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setTaskId(args.taskId)

        observeTask()

        binding.editButton.setOnClickListener {
            val action = TaskDetailsFragmentDirections
                .actionDetailsToEdit(args.taskId)
            findNavController().navigate(action)
        }
    }

    private fun observeTask() {
        viewModel.observeTask().observe(viewLifecycleOwner) { task ->
            binding.titleText.text = task.title
            binding.descriptionText.text = task.description ?: "Без описания"
            binding.priorityText.text = when (task.priority) {
                0 -> "Низкий"
                1 -> "Средний"
                2 -> "Высокий"
                else -> "Неизвестно"
            }
            binding.dueDateText.text = if (task.dueDate > 0) {
                DateTimeUtils.format(task.dueDate)
            } else {
                "Не задано"
            }
            binding.statusText.text = if (task.completed) "Выполнена" else "Не выполнена"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}