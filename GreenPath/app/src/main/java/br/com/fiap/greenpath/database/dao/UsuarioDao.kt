package br.com.fiap.greenpath.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.greenpath.database.model.Usuario

@Dao
interface UsuarioDao {

    /**
     * Cadastra um novo usuário no banco de dados.
     * @param usuario O objeto Usuario a ser inserido.
     * @return O ID do usuário recém-cadastrado (útil se você precisar dele imediatamente).
     */
    @Insert
   fun cadastrar(usuario: Usuario): Long

    /**
     * Busca um usuário pelo seu nome de usuário.
     * Útil para verificar se um usuário existe ou para pegar seus dados durante o login.
     * @param nomeUsuario O nome de usuário a ser pesquisado.
     * @return O objeto Usuario correspondente ou null se não for encontrado.
     */
    @Query("SELECT * FROM usuarios WHERE user_name = :nomeUsuario LIMIT 1")
    fun buscarPorNomeUsuario(nomeUsuario: String): Usuario?

    /**
     * (Opcional, mas comum para login) Busca um usuário pelo e-mail.
     * @param email O e-mail a ser pesquisado.
     * @return O objeto Usuario correspondente ou null se não for encontrado.
     */
    @Query("SELECT * FROM usuarios WHERE email = :email LIMIT 1")
    fun buscarPorEmail(email: String): Usuario?

}