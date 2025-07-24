package tr.yigitunlu.aras.data.mapper

import tr.yigitunlu.aras.data.TaskEntity
import tr.yigitunlu.aras.domain.model.Task

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        title = title,
        description = description,
        isCompleted = isCompleted
    )
}
