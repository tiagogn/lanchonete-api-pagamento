package br.com.fiap.lanchonete.pagamento.core.dto

import java.math.BigDecimal

data class PedidoInput(
    val pedidoId: String,
    val valor: BigDecimal
)
