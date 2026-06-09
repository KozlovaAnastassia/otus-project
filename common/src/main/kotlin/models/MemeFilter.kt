package ru.otus.otuskotlin.common.models

data class MemeFilter(
    var searchString: String = "",
    var ownerId: MemeUserId = MemeUserId.NONE,
    val tags: List<String>  = emptyList()
) {
    fun deepCopy(): MemeFilter = copy()

    fun isEmpty() = this == NONE

    companion object {
        private val NONE = MemeFilter()
    }
}
