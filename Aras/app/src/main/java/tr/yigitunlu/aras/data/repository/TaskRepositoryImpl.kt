package tr.yigitunlu.aras.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tr.yigitunlu.aras.data.TaskDao
import tr.yigitunlu.aras.data.mapper.toDomain
import tr.yigitunlu.aras.data.mapper.toEntity
import tr.yigitunlu.aras.domain.model.Task
import tr.yigitunlu.aras.domain.repository.TaskRepository

class TaskRepositoryImpl(
    private val taskDao: TaskDao
) : TaskRepository {

    override fun getAllTasks(): Flow<List<Task>> {
        return taskDao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTasksByCompletion(isCompleted: Boolean): Flow<List<Task>> {
        return taskDao.getTasksByCompletion(isCompleted).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insert(task: Task) {
        taskDao.insert(task.toEntity())
    }

    override suspend fun update(task: Task) {
        taskDao.update(task.toEntity())
    }

    override suspend fun delete(task: Task) {
        taskDao.delete(task.toEntity())
    }
}
