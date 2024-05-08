import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.coder178.notes.Database.NotesDatabase
import com.coder178.notes.Model.Notes
import com.coder178.notes.Repositiry.NotesRepository



class NotesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: NotesRepository
    private val allNotes: LiveData<List<Notes>>

    init {
        val dao = NotesDatabase.getDatabaseInstance(application).MyNotesDao()
        repository = NotesRepository(dao)
        allNotes = repository.getAllNotes()
    }

    fun addNotes(notes: Notes) {
        repository.insertNotes(notes)
    }

    fun getLowNotes(): LiveData<List<Notes>> = repository.getLowNotes()

    fun getMediumNotes(): LiveData<List<Notes>> = repository.getMediumNotes()

    fun getHighNotes(): LiveData<List<Notes>> = repository.getHighNotes()

    fun getNotes(): LiveData<List<Notes>> = allNotes

    fun deleteNotes(id: Int) {
        repository.deleteNotes(id)
    }

    fun updateNotes(notes: Notes) {
        repository.updateNotes(notes)
    }

    fun getNotesByDueDateAsc(): LiveData<List<Notes>> = repository.getNotesByDueDateAsc()

    fun getNotesByDateAsc(): LiveData<List<Notes>> = repository.getNotesByDateAsc()


}
