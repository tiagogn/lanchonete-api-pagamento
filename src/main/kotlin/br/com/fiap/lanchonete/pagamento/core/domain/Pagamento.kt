package br.com.fiap.lanchonete.pagamento.core.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Document("pagamento")
data class Pagamento(

    @Id
    var id: String? = null,

    val valor: BigDecimal,

    val status: StatusPagamento,

    val formaPagamento: FormaPagamento,

    val dataPagamento: LocalDateTime,

    val pedidoId: String,

    val mensagem: String
)

enum class StatusPagamento {
    PENDENTE, APROVADO, RECUSADO
}

enum class FormaPagamento {
    CARTAO_CREDITO, CARTAO_DEBITO, DINHEIRO, PIX, VR
}
