package tr.yigitunlu.aras.domain.repository

import kotlinx.coroutines.flow.Flow
import tr.yigitunlu.aras.domain.model.Task

interface TaskRepository {

    fun getAllTasks(): Flow<List<Task>>

    fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>>

    fun getTaskById(id: Int): Flow<Task?>

    suspend fun insert(task: Task)

    suspend fun update(task: Task)

    suspend fun delete(task: Task)
}
