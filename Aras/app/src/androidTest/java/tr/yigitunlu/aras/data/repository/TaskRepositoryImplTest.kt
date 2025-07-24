package tr.yigitunlu.aras.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import tr.yigitunlu.aras.data.AppDatabase
import tr.yigitunlu.aras.data.TaskDao
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository

@RunWith(AndroidJUnit4::class)
class TaskRepositoryImplTest {

    private lateinit var database: AppDatabase
    private lateinit var dao: TaskDao
    private lateinit var repository: TaskRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.taskDao()
        repository = TaskRepositoryImpl(dao)
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTask_retrievesSameTask() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Desc", isCompleted = false)
        repository.insert(task)

        val allTasks = repository.getAllTasks().first()
        assertThat(allTasks).contains(task)
    }

    @Test
    fun deleteTask_removesTaskFromList() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Desc", isCompleted = false)
        repository.insert(task)
        repository.delete(task)

        val allTasks = repository.getAllTasks().first()
        assertThat(allTasks).doesNotContain(task)
    }

    @Test
    fun updateTask_reflectsChanges() = runTest {
        val task = Task(id = 1, title = "Test Task", description = "Test Desc", isCompleted = false)
        repository.insert(task)

        val updatedTask = task.copy(title = "Updated Title")
        repository.update(updatedTask)

        val allTasks = repository.getAllTasks().first()
        assertThat(allTasks).contains(updatedTask)
    }

    @Test
    fun getTasksByCompletion_returnsCorrectTasks() = runTest {
        val completedTask = Task(id = 1, title = "Completed Task", description = "", isCompleted = true)
        val incompleteTask = Task(id = 2, title = "Incomplete Task", description = "", isCompleted = false)
        repository.insert(completedTask)
        repository.insert(incompleteTask)

        val completedTasks = repository.getTasksByCompletion(true).first()
        assertThat(completedTasks).contains(completedTask)
        assertThat(completedTasks).doesNotContain(incompleteTask)

        val incompleteTasks = repository.getTasksByCompletion(false).first()
        assertThat(incompleteTasks).contains(incompleteTask)
        assertThat(incompleteTasks).doesNotContain(completedTask)
    }
}
