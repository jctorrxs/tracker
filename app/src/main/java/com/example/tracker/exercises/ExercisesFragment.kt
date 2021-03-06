package com.example.tracker.exercises

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tracker.R
import com.example.tracker.common.InjectorUtils
import com.example.tracker.common.entities.Exercise
import com.example.tracker.databinding.ExercisesFragmentBinding
import com.example.tracker.workout.WorkoutFragment.Companion.SELECTED_EXERCISE
import kotlinx.android.synthetic.main.exercises_fragment.*


class ExercisesFragment : Fragment(), ExerciseListListener {

    private val exercisesViewModel: ExercisesViewModel by viewModels {
        InjectorUtils.provideExercisesViewModelFactory(requireContext())
    }

    private inline val Fragment.appCompatActivity: AppCompatActivity get() = (activity as AppCompatActivity)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val adapter = ExercisesAdapter( this)
        var binding = DataBindingUtil.inflate<ExercisesFragmentBinding>(
            inflater, R.layout.exercises_fragment, container, false
        ).apply {
            appCompatActivity.setSupportActionBar(toolbar)
            viewModel = exercisesViewModel
            lifecycleOwner = viewLifecycleOwner

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

            exercisesViewModel.exercises.observe(lifecycleOwner!!, Observer { exercises ->
                adapter.setExercises(exercises)
            })

        }
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateExercise(name: String) {
        exercisesViewModel.createExercise(name);
    }

    override fun onDeleteExercise(exercise: Exercise) {
        exercisesViewModel.deleteExercise(exercise.id)
    }

    override fun onExerciseSelected(exercise: Exercise) {
        Log.d(this.javaClass.canonicalName, "onExerciseSelected ${exercise.id}");
        findNavController().previousBackStackEntry?.savedStateHandle?.set(
            SELECTED_EXERCISE, exercise.id
        )
        findNavController().popBackStack()
    }

    private fun displayCreateExerciseDialog() {
        CreateExerciseDialog.newInstance(this).show(this.requireFragmentManager(), CreateExerciseDialog.TAG)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.getItemId() === R.id.action_add) {
            displayCreateExerciseDialog();
        }
        return super.onOptionsItemSelected(menuItem)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exercises_menu, menu)
    }
}
