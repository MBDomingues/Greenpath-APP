package br.com.fiap.greenpath.database.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.greenpath.database.model.Co2E
import br.com.fiap.greenpath.database.model.Usuario

@Database(entities = [Usuario::class, Co2E::class], version = 1)
abstract class GreenPathDb: RoomDatabase() {

    abstract  fun getUsuariodao(): UsuarioDao
    abstract fun getCo2E(): Co2EDao


    companion object {

        private lateinit var instance: GreenPathDb

        fun getDatabase(context: Context): GreenPathDb {
            if (!::instance.isInitialized) {
                instance = Room
                    .databaseBuilder(
                        context,
                        GreenPathDb::class.java,
                        "green_path_db"
                    )
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance
        }
    }
}


