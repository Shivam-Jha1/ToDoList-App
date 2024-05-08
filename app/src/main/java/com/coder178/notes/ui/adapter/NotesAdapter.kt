package com.coder178.notes.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.coder178.notes.Model.Notes
import com.coder178.notes.R
import com.coder178.notes.databinding.ItemNotesBinding
import com.coder178.notes.ui.fragments.HomeFragmentDirections

class NotesAdapter(val requireContext: Context, var notesList: List<Notes>) :
    RecyclerView.Adapter<NotesAdapter.notesViewHolder>() {

    fun filtering(newFilterList: ArrayList<Notes>) {
        notesList = newFilterList
        notifyDataSetChanged()
    }

    class notesViewHolder(val binding: ItemNotesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): notesViewHolder {

        return notesViewHolder(
            ItemNotesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: notesViewHolder, position: Int) {

        val data = notesList[position]
        holder.binding.notesTitle.text = data.title
        holder.binding.notesSubtitle.text = data.subTitile
        holder.binding.notesDate.text = "Created on- "+data.date
        holder.binding.dueNotesDate.text = "Due date- "+data.dueDate

        when (data.priority) {
            "1" -> {
                holder.binding.viewPriority.setBackgroundResource(R.drawable.green_dot)
            }

            "2" -> {
                holder.binding.viewPriority.setBackgroundResource(R.drawable.yellow_dot)
            }

            "3" -> {
                holder.binding.viewPriority.setBackgroundResource(R.drawable.red_dot)
            }
        }
holder.binding.root.setOnClickListener {
    val action = HomeFragmentDirections.actionHomeFragment2ToEditNotesFragment2(data)
    Navigation.findNavController(it).navigate(action)
}
    }
    override fun getItemCount() = notesList.size


}