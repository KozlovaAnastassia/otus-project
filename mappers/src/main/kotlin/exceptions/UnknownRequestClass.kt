package ru.otus.otuskotlin.mappers.v1.exceptions

class UnknownRequestClass(requestClass: Class<*>) :
    RuntimeException("Unknown request class: ${requestClass.simpleName}")