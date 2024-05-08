package com.coder178.notes.ui.fragments

import NotesViewModel
import android.app.DatePickerDialog
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.coder178.notes.Model.Notes
import com.coder178.notes.R
import com.coder178.notes.databinding.FragmentCreateNotesBinding
import java.text.SimpleDateFormat
import java.util.*

class CreateNotesFragment : Fragment() {

    lateinit var binding: FragmentCreateNotesBinding
    var priority:String = "1"
    val viewModel: NotesViewModel by viewModels()
    private lateinit var dueDate:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateNotesBinding.inflate(layoutInflater, container, false)
        dueDate = ""

        binding.PickDate.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->
                    dueDate = ("$year-${monthOfYear + 1}-$dayOfMonth") // Format dueDate
                    val formattedDueDate = formatDate(dueDate) // Format dueDate to display
                    binding.SelectedDate.text = formattedDueDate // Set dueDate in TextView
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        if (dueDate.isNotEmpty()) {
            val formattedDueDate = formatDate(dueDate)
            binding.SelectedDate.text = formattedDueDate
        } else {
            binding.SelectedDate.text = "Select Due Date"
        }

        binding.pGreen.setImageResource(R.drawable.ic_baseline_done_24)//Default green p-1
        binding.pGreen.setOnClickListener {
            priority = "1"
            binding.pGreen.setImageResource(R.drawable.ic_baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pYellow.setImageResource(0)
        }
        binding.pYellow.setOnClickListener {
            priority = "2"
            binding.pYellow.setImageResource(R.drawable.ic_baseline_done_24)
            binding.pRed.setImageResource(0)
            binding.pGreen.setImageResource(0)
        }
        binding.pRed.setOnClickListener {
            priority = "3"
            binding.pRed.setImageResource(R.drawable.ic_baseline_done_24)
            binding.pGreen.setImageResource(0)
            binding.pYellow.setImageResource(0)
        }

        binding.btnSaveNotes.setOnClickListener {
            createNotes(it)
        }

        return binding.root
    }

    private fun createNotes(it: View?) {
        val title = binding.edtTitle.text.toString()
        val subtitle = binding.edtSubtitle.text.toString()
        val notes = binding.edtNotes.text.toString()

        // Format current date to yyyy-MM-dd
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        val data = Notes(
            null,
            title = title,
            subTitile = subtitle,
            notes = notes,
            date = currentDate, // Use formatted current date
            priority = priority,
            dueDate = formatDate(dueDate) // No need to convert to String as it's already in the desired format
        )
        if (title.isBlank() || subtitle.isBlank() || notes.isBlank() || !isValidDate(dueDate, currentDate)) {
            Toast.makeText(requireContext(), "Fill all the details", Toast.LENGTH_LONG).show()
        } else {
            viewModel.addNotes(data)
            Toast.makeText(requireContext(), "Note Created Successfully", Toast.LENGTH_SHORT).show()
            Navigation.findNavController(it!!).navigate(R.id.action_createNotesFragment3_to_homeFragment2)
        }
    }

    private fun isValidDate(dueDate: String, currentDate: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            // Parse dueDate and currentDate to Date objects
            val dueDateObj = dateFormat.parse(dueDate)
            val currentDateObj = dateFormat.parse(currentDate)

            // Check if dueDate is after currentDate
            return dueDateObj.after(currentDateObj)
        } catch (e: Exception) {
            // Handle parsing errors
            e.printStackTrace()
            return false
        }
    }

    private fun formatDate(dateString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = inputFormat.parse(dateString)
        return outputFormat.format(date)
    }

}
