package ru.otus.otuskotlin.biz.exceptions

import ru.otus.otuskotlin.common.models.MemeWorkMode

class MemeDbNotConfiguredException(val workMode: MemeWorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)