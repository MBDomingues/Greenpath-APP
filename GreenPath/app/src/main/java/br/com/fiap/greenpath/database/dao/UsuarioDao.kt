package br.com.fiap.greenpath.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.greenpath.database.model.Usuario

@Dao
interface UsuarioDao {

    @Insert
   fun cadastrar(usuario: Usuario): Long


    @Query("SELECT * FROM usuarios WHERE user_name = :nomeUsuario LIMIT 1")
    fun buscarPorNomeUsuario(nomeUsuario: String): Usuario

    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    fun buscarPorEmail(email: String): Usuario?

}