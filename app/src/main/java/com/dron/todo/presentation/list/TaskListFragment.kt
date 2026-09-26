package com.dron.todo.presentation.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dron.todo.databinding.FragmentTaskListBinding
import dagger.hilt.android.AndroidEntryPoint
import com.dron.todo.R

@AndroidEntryPoint
class TaskListFragment : Fragment() {

    private var _binding: FragmentTaskListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TaskListViewModel by viewModels()

    private lateinit var adapter: TaskListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupFab()
        setupSearch()
        setupSortButton()      // ← добавить
        observeTasks()
    }

    private fun setupSortButton() {
        binding.sortButton.setOnClickListener {
            val popup = PopupMenu(requireContext(), binding.sortButton)
            popup.inflate(R.menu.menu_sort)

            popup.setOnMenuItemClickListener { menuItem ->
                val mode = when (menuItem.itemId) {
                    R.id.sort_by_date -> SortMode.BY_DATE
                    R.id.sort_by_priority -> SortMode.BY_PRIORITY
                    else -> SortMode.BY_DATE
                }
                viewModel.setSortMode(mode)
                true
            }
            popup.show()
        }
    }

    private fun setupSearch() {
        binding.searchInput.doAfterTextChanged { text ->
            viewModel.search(text?.toString().orEmpty())
        }
    }

    private fun setupRecyclerView() {
        adapter = TaskListAdapter(
            onTaskClick = { task ->
                val action = TaskListFragmentDirections
                    .actionListToDetails(task.id)
                findNavController().navigate(action)
            },
            onCompletedToggle = { task, isChecked ->
                viewModel.toggleCompleted(task.id, isChecked)
            }
        )

        binding.tasksRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.tasksRecyclerView.adapter = adapter

        attachSwipeToDelete()
    }

    private fun attachSwipeToDelete() {
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.bindingAdapterPosition
                if (position == RecyclerView.NO_POSITION) return

                val task = adapter.currentList[position]
                viewModel.delete(task)
                Toast.makeText(requireContext(), "Удалено: ${task.title}", Toast.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(swipeCallback).attachToRecyclerView(binding.tasksRecyclerView)
    }

    private fun setupFab() {
        binding.addTaskFab.setOnClickListener {
            val action = TaskListFragmentDirections
                .actionListToEdit(-1L)   // -1 = создание
            findNavController().navigate(action)
        }
    }

    private fun observeTasks() {
        viewModel.tasks.observe(viewLifecycleOwner) { tasks ->
            adapter.submitList(tasks)

            val isEmpty = tasks.isEmpty()
            val isSearching = binding.searchInput.text?.isNotBlank() == true

            binding.emptyText.visibility = if (isEmpty) View.VISIBLE else View.GONE
            binding.tasksRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE

            binding.emptyText.text = if (isSearching) {
                "Ничего не найдено"
            } else {
                "Нет задач. Добавьте первую!"
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.tasksRecyclerView.adapter = null
        _binding = null
    }
}