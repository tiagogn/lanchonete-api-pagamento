package br.com.fiap.lanchonete.pagamento.core.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import kotlin.test.assertNotNull

class PagamentoTest {

    @Test
    fun `test pagamento creation`() {
        val pagamento = Pagamento(
            valor = BigDecimal("100.00"),
            status = StatusPagamento.APROVADO,
            formaPagamento = FormaPagamento.CARTAO_CREDITO,
            dataPagamento = LocalDateTime.now(),
            pedidoId = "12345",
            mensagem = "Pagamento efetuado com sucesso"
        )

        assertNotNull(pagamento.id)
        assertEquals(BigDecimal("100.00"), pagamento.valor)
        assertEquals(StatusPagamento.APROVADO, pagamento.status)
        assertEquals(FormaPagamento.CARTAO_CREDITO, pagamento.formaPagamento)
        assertEquals("12345", pagamento.pedidoId)
        assertEquals("Pagamento efetuado com sucesso", pagamento.mensagem)
    }
}