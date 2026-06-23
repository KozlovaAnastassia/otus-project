package ru.otus.otuskotlin.repo.postgres

object SqlFields {
    const val ID = "id"
    const val TITLE = "title"
    const val TAGS = "tags"
    const val IMAGE = "image"
    const val IMAGE_URL = "image_url"
    const val CREATED_AT = "created_at"
    const val AUTHOR_ID = "author_id"
    const val LOCK = "lock"
    const val VISIBILITY = "visibility"

    const val VISIBILITY_TYPE = "visibility_type"
    const val VISIBILITY_PUBLIC = "public"
    const val VISIBILITY_OWNER = "owner"
    const val VISIBILITY_GROUP = "group"

    const val FILTER_TITLE = TITLE
    const val FILTER_TAGS = TAGS
    const val FILTER_AUTHOR_ID = AUTHOR_ID
}