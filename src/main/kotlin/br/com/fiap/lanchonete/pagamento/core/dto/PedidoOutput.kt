package br.com.fiap.lanchonete.pagamento.core.dto

import java.math.BigDecimal

data class PedidoOutput(
    val pedidoId: String,
    val valor: BigDecimal,
    val formaPagamento: String,
    val status: String,
    val pagamentoId: String,
    val dataPagamento: String,
    val mensagem: String
)
