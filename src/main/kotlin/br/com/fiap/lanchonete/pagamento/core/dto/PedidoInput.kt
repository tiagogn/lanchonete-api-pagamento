package br.com.fiap.lanchonete.pagamento.core.dto

import java.math.BigDecimal

data class PedidoInput(
    val id: String,
    var cliente: String? = null,
    val total: BigDecimal,
    val status: String,
    val codigo: String,
    val pagamento: String
)
