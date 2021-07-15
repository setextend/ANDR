package ru.netology.dao

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import androidx.lifecycle.LiveData
import ru.netology.dto.Post

class PostDaoImpl(private val db: SQLiteDatabase) : PostDao {

    companion object {
        val DDL = """
            CREATE TABLE ${PostColumns.TABLE} (
            ${PostColumns.COLUMN_ID} INTEGER PRIMARY KEY AUTOINCREMENT,
            ${PostColumns.COLUMN_AUTHOR} TEXT NOT NULL,
            ${PostColumns.COLUMN_CONTENT} TEXT NOT NULL,
            ${PostColumns.COLUMN_PUBLISHED} TEXT NOT NULL,
            ${PostColumns.COLUMN_LIKES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWS} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARES} INTEGER NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_LIKED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_SHARED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_VIEWED_BY_ME} BOOLEAN NOT NULL DEFAULT 0,
            ${PostColumns.COLUMN_ATT_VIDEO} TEXT NOT NULL
            );            
            """.trimIndent()
    }

    object PostColumns {
        const val TABLE = "posts"
        const val COLUMN_ID = "id"
        const val COLUMN_AUTHOR = "author"
        const val COLUMN_CONTENT = "content"
        const val COLUMN_PUBLISHED = "published"
        const val COLUMN_LIKES = "likes"
        const val COLUMN_VIEWS = "views"
        const val COLUMN_SHARES = "shares"
        const val COLUMN_LIKED_BY_ME = "likedByMe"
        const val COLUMN_SHARED_BY_ME = "sharedByMe"
        const val COLUMN_VIEWED_BY_ME = "viewedByMe"
        const val COLUMN_ATT_VIDEO = "attVideo"
        val ALL_COLUMNS = arrayOf(
            COLUMN_ID,
            COLUMN_AUTHOR,
            COLUMN_CONTENT,
            COLUMN_PUBLISHED,
            COLUMN_LIKES,
            COLUMN_VIEWS,
            COLUMN_SHARES,
            COLUMN_LIKED_BY_ME,
            COLUMN_SHARED_BY_ME,
            COLUMN_VIEWED_BY_ME,
            COLUMN_ATT_VIDEO
        )
    }

//    data class Post(
//        val id: Long,
//        val author: String,
//        val content: String,
//        val published: String,
//        val likes: Long = 0,
//        val views: Long = 0,
//        val shares: Long = 0,
//        val likedByMe: Boolean = false,
//        val sharedByMe: Boolean = false,
//        val viewedByMe: Boolean = false,
//        val attVideo: String
//    )

    override fun getAll(): List<Post> {
        val posts = mutableListOf<Post>()
        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            null,
            null,
            null,
            null,
            "${PostColumns.COLUMN_ID} DESC"
        ).use {
            while (it.moveToNext()) {
                posts.add(map(it))
            }
        }

        return posts
    }

    override fun likeById(id: Long) {
        db.execSQL(
            """
            UPDATE posts SET 
                likes = likes + CASE WHEN likedByMe THEN -1 ELSE 1 END,
                likedByMe = CASE WHEN likedByMe THEN 0 ELSE 1 END
            WHERE id = ?
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun shareById(id: Long) {
        db.execSQL(
            """
            UPDATE posts SET 
                shares = shares + 1,
                sharedByMe = 1
            WHERE id = ?
            """.trimIndent(), arrayOf(id)
        )
    }

    override fun removeById(id: Long) {
        db.delete(
            PostColumns.TABLE,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString())
        )
    }

    override fun save(post: Post): Post {
        val values = ContentValues().apply {
            if (post.id != 0L) {
                put(PostColumns.COLUMN_ID, post.id)
            }
            put(PostColumns.COLUMN_PUBLISHED, "Now")
            put(PostColumns.COLUMN_AUTHOR, "Me")
            put(PostColumns.COLUMN_CONTENT, post.content)
            put(PostColumns.COLUMN_VIEWS, post.views)
            put(PostColumns.COLUMN_LIKES, post.likes)
            put(PostColumns.COLUMN_SHARES, post.shares)
            put(PostColumns.COLUMN_LIKED_BY_ME, post.likedByMe)
            put(PostColumns.COLUMN_SHARED_BY_ME, post.sharedByMe)
            put(PostColumns.COLUMN_VIEWED_BY_ME, post.viewedByMe)
            put(PostColumns.COLUMN_ATT_VIDEO, "")
        }

        // replace сам понимает, добавить или обновить
        val id = db.replace(PostColumns.TABLE, null, values)

        db.query(
            PostColumns.TABLE,
            PostColumns.ALL_COLUMNS,
            "${PostColumns.COLUMN_ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null,
        ).use {
            it.moveToNext()
            return map(it)
        }
    }

    override fun edit(post: Post) {
        TODO("Not yet implemented")
    }

    override fun video() {
        TODO("Not yet implemented")
    }

    private fun map(cursor: Cursor): Post {
        with(cursor) {
            return Post(
                id = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_ID)),
                author = getString(getColumnIndexOrThrow(PostColumns.COLUMN_AUTHOR)),
                content = getString(getColumnIndexOrThrow(PostColumns.COLUMN_CONTENT)),
                published = getString(getColumnIndexOrThrow(PostColumns.COLUMN_PUBLISHED)),
                views = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_VIEWS)),
                likes = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                shares = getLong(getColumnIndexOrThrow(PostColumns.COLUMN_LIKES)),
                likedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                sharedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                viewedByMe = getInt(getColumnIndexOrThrow(PostColumns.COLUMN_LIKED_BY_ME)) != 0,
                attVideo = getString(getColumnIndexOrThrow(PostColumns.COLUMN_ATT_VIDEO))
            )
        }
    }
}