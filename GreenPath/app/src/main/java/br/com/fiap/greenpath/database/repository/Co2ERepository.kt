package br.com.fiap.greenpath.database.repository

import android.content.Context
import br.com.fiap.greenpath.database.dao.GreenPathDb
import br.com.fiap.greenpath.database.model.Co2E

class Co2ERepository(context: Context) {

    private val db = GreenPathDb.getDatabase(context).getCo2E()

    suspend fun cadastrar(co2E: Co2E): Long{
        return db.cadastrar(co2E)
    }

    fun buscarSomaGasto(id: Long?): Float{
        return db.buscarTotalCo2PorUsuario(id)
    }

}