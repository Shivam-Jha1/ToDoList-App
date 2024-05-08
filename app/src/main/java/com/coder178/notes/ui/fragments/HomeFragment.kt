package com.coder178.notes.ui.fragments

import NotesViewModel
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.coder178.notes.R
import com.coder178.notes.databinding.FragmentHomeBinding
import com.coder178.notes.ui.adapter.NotesAdapter
import com.coder178.notes.Model.Notes
//import androidx.appcompat.widget.SearchView;
//import androidx.appcompat.widget.SearchView;
import android.widget.SearchView;
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.coder178.notes.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    val viewModel: NotesViewModel by viewModels()
    var oldNotesLists = arrayListOf<Notes>()
    lateinit var adapter: NotesAdapter

    private val CHANNEL_ID = "com.coder178.notes"
    private val notificationId = 101

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setHasOptionsMenu(true)

        val sv = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        binding.rcvAllNotes.layoutManager = sv

        viewModel.getNotes().observe(viewLifecycleOwner, { notesList ->
            oldNotesLists = notesList as ArrayList<Notes>
            binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
            adapter = NotesAdapter(requireContext(), notesList)
            binding.rcvAllNotes.adapter = adapter

            // Check due dates and trigger notifications if necessary
            for (note in notesList) {
                if (calculateDateDifference(note.dueDate) <= 1L) {
                    // Trigger notification
                    triggerNotification(note.title)
                }
            }
        })

        binding.filterHigh.setOnClickListener {
            viewModel.getHighNotes().observe(viewLifecycleOwner, { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter            })
//            binding.rcvAllNotes.adapter = NotesAdapter(requireContext(), notesList)})
        }

        binding.filterMedium.setOnClickListener {
            viewModel.getMediumNotes().observe(viewLifecycleOwner, { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter            })
        }

        binding.filterLow.setOnClickListener {
            viewModel.getLowNotes().observe(viewLifecycleOwner, { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter            })
        }

        binding.allNotes.setOnClickListener {

            viewModel.getNotes().observe(viewLifecycleOwner, { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter            })

        }

        binding.btnAdd.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_homeFragment2_to_createNotesFragment3)
        }

        binding.filterDueDate.setOnClickListener {
            viewModel.getNotesByDueDateAsc().observe(viewLifecycleOwner) { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter
            }
        }
        binding.filterCreateDate.setOnClickListener {
            viewModel.getNotesByDateAsc().observe(viewLifecycleOwner) { notesList ->
                oldNotesLists = notesList as ArrayList<Notes>
                binding.rcvAllNotes.layoutManager = LinearLayoutManager(requireContext())
                adapter = NotesAdapter(requireContext(), notesList)
                binding.rcvAllNotes.adapter = adapter
            }
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Enter Notes Here..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                NotesFiltering(newText)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun NotesFiltering(newText: String?) {

        val newFilterList = arrayListOf<Notes>()

        for(i in oldNotesLists){
            if(i.title.contains(newText!!) || i.subTitile.contains(newText!!)){
                newFilterList.add(i)
            }
        }
        adapter.filtering(newFilterList)
    }
    private fun calculateDateDifference(dueDate: String): Long {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = Calendar.getInstance().time
        val dueDateObj = dateFormat.parse(dueDate)
        val diff = dueDateObj.time - currentDate.time
        return diff / (1000 * 60 * 60 * 24) // Convert milliseconds to days
    }

    private fun triggerNotification(noteTitle: String) {
        // Create an intent for the notification
        val intent = Intent(requireContext(), MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            requireContext(),
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        // Build the notification
        val builder = NotificationCompat.Builder(requireContext(), CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Note Due Tomorrow")
            .setContentText("Your note '$noteTitle' is due tomorrow.")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Show the notification
        with(NotificationManagerCompat.from(requireContext())) {
            notify(notificationId, builder.build())
        }
    }
}