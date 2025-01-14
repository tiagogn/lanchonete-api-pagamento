package br.com.fiap.lanchonete.core.dto

import br.com.fiap.lanchonete.pagamento.core.domain.FormaPagamento
import java.math.BigDecimal
import java.util.*

data class PagamentoInput(
    val pedidoId: String,
    val valor: BigDecimal,
    val formaPagamento: FormaPagamento,
)