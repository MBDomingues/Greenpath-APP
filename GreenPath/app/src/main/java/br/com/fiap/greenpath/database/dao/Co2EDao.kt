package br.com.fiap.greenpath.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import br.com.fiap.greenpath.database.model.Co2E

@Dao
interface Co2EDao {

    @Insert
    suspend fun cadastrar(co2E: Co2E): Long



    @Query("SELECT SUM(valor_co2e) FROM emissoes_co2 WHERE usuario_id = :idUsuario")
    fun buscarTotalCo2PorUsuario(idUsuario: Long?): Float
}