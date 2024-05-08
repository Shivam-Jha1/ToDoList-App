package com.coder178.notes.Repositiry

import androidx.lifecycle.LiveData
import com.coder178.notes.Dao.NotesDao
import com.coder178.notes.Model.Notes

class NotesRepository(val dao: NotesDao) {

    fun getAllNotes(): LiveData<List<Notes>> = dao.getNotes()

    fun getLowNotes(): LiveData<List<Notes>> = dao.getLowNotes()

    fun getMediumNotes(): LiveData<List<Notes>> = dao.getMediumNotes()

    fun getHighNotes(): LiveData<List<Notes>> = dao.getHighNotes()

    fun getNotesByDueDateAsc():LiveData<List<Notes>> = dao.getNotesByDueDateAsc()

    fun getNotesByDateAsc():LiveData<List<Notes>> = dao.getNotesByDateAsc()



    fun insertNotes(notes: Notes) {
        dao.insertNotes(notes)
    }

    fun deleteNotes(id: Int) {
        dao.deleteNotes(id)
    }

    fun updateNotes(notes: Notes) {
        dao.updatetNotes(notes)
    }

}