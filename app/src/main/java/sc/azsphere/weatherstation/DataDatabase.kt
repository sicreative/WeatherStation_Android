package sc.azsphere.weatherstation

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.*

@Database(entities = arrayOf(Data::class), version = 1)

abstract class DataDatabase : RoomDatabase() {
    abstract fun dataDao(): DataDao

    private class DataDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.dataDao())
                }
            }
        }

        suspend fun populateDatabase(dataDao: DataDao) {


        }
    }


    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DataDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DataDatabase {

            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DataDatabase::class.java,
                    "sensors_database"
                ).addCallback(DataDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}





