package br.com.fiap.lanchonete.pagamento.adapters.input.rest.request

import br.com.fiap.lanchonete.core.dto.PagamentoInput
import br.com.fiap.lanchonete.pagamento.core.domain.FormaPagamento
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotBlank
import java.math.BigDecimal

data class PagamentoPedidoRequest(

    @field:NotBlank
    val pedidoId: String,

    @field:DecimalMin(value = "0.0", inclusive = false)
    val valor: Double,

    @field:NotBlank
    val formaPagamento: String
){
    fun toModel() = PagamentoInput(
        pedidoId = pedidoId,
        valor = BigDecimal.valueOf(valor),
        formaPagamento = FormaPagamento.valueOf(formaPagamento)
    )
}