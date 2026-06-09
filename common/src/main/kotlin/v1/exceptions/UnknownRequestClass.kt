package ru.otus.otuskotlin.common.exceptions

import ru.otus.otuskotlin.common.models.MemeCommand

class UnknownMemeCommand(command: MemeCommand) : Throwable("Wrong command $command at mapping toTransport stage")