package ru.otus.otuskotlin.repo.postgres

import org.jetbrains.exposed.sql.Table
import org.postgresql.util.PGobject

fun Table.visibilityEnumeration(
    columnName: String
) = customEnumeration(
    name = columnName,
    sql = SqlFields.VISIBILITY_TYPE,
    fromDb = { value ->
        when (value.toString()) {
            SqlFields.VISIBILITY_OWNER -> MemeVisibility.VISIBLE_TO_OWNER
            SqlFields.VISIBILITY_GROUP -> MemeVisibility.VISIBLE_TO_GROUP
            SqlFields.VISIBILITY_PUBLIC -> MemeVisibility.VISIBLE_PUBLIC
            else -> MemeVisibility.NONE
        }
    },
    toDb = { value ->
        when (value) {
            MemeVisibility.VISIBLE_TO_OWNER -> PgVisibilityOwner
            MemeVisibility.VISIBLE_TO_GROUP -> PgVisibilityGroup
            MemeVisibility.VISIBLE_PUBLIC -> PgVisibilityPublic
            MemeVisibility.NONE -> throw Exception("Wrong value of Visibility. NONE is unsupported")
        }
    }
)

sealed class PgVisibilityValue(eValue: String) : PGobject() {
    init {
        type = SqlFields.VISIBILITY_TYPE
        value = eValue
    }
}

object PgVisibilityPublic : PgVisibilityValue(SqlFields.VISIBILITY_PUBLIC) {
    private fun readResolve(): Any = PgVisibilityPublic
}

object PgVisibilityOwner : PgVisibilityValue(SqlFields.VISIBILITY_OWNER) {
    private fun readResolve(): Any = PgVisibilityOwner
}

object PgVisibilityGroup : PgVisibilityValue(SqlFields.VISIBILITY_GROUP) {
    private fun readResolve(): Any = PgVisibilityGroup
}