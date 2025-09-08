package br.com.fiap.greenpath.database.repository

import android.content.Context
import br.com.fiap.greenpath.database.dao.GreenPathDb
import br.com.fiap.greenpath.database.model.Usuario

class UsuarioRepository(context: Context) {

    private val db = GreenPathDb.getDatabase(context).getUsuariodao()

    fun cadastrar(usuario: Usuario): Long{
        return db.cadastrar(usuario)
    }

    fun buscarPorNomeUsuario(nomeUsuario: String): Usuario{
        return db.buscarPorNomeUsuario(nomeUsuario)
    }
}