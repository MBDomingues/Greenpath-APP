package br.com.fiap.greenpath.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(
    tableName = "emissoes_co2",
    foreignKeys = [ForeignKey(
        entity = Usuario::class,
        parentColumns = ["id"],
        childColumns = ["usuario_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.NO_ACTION
    )],
    indices = [Index(value = ["usuario_id"])]
)
data class Co2E(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "usuario_id")
    val usuarioId: Long?,

    @ColumnInfo(name = "valor_co2e")
    val valorCo2e: Float,

    @ColumnInfo(name = "unidade_co2e", defaultValue = "kg")
    val unidadeCo2e: String = "kg",

    @ColumnInfo(name = "data_registro", defaultValue = "CURRENT_TIMESTAMP")
    val dataRegistro: String
)