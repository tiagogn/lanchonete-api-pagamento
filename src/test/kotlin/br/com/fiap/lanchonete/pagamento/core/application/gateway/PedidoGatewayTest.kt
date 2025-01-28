package br.com.fiap.lanchonete.pagamento.core.application.gateway

import br.com.fiap.lanchonete.pagamento.PagamentoApplicationTests
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertNull

class PedidoGatewayTest: PagamentoApplicationTests() {

    @MockkBean
    lateinit var pedidoGateway: PedidoGateway

    @BeforeEach
    fun setup() {
        every { pedidoGateway.consultarPedido(any<String>()) }.returns(PedidoInput(
            pedidoId = UUID.randomUUID().toString(),
            valor = BigDecimal.valueOf(100.00)
        ))

        every { pedidoGateway.consultarPedido("1") }.returns(null)

        every { pedidoGateway.confirmarPagamento(any<PedidoOutput>()) }.returns(Unit)
    }

    @Test
    fun `should find a order`() {
        val pedido = pedidoGateway.consultarPedido(UUID.randomUUID().toString())

        assertNotNull(pedido)
    }

    @Test
    fun `should not find a order`() {
        val pedido = pedidoGateway.consultarPedido("1")

        assertNull(pedido)
    }

    @Test
    fun `should confirm a order`() {
        val pedidoOutput = PedidoOutput(
            pedidoId = UUID.randomUUID().toString(),
            valor = BigDecimal.valueOf(100.00),
            status = "APROVADO",
            formaPagamento = "CARTAO_CREDITO",
            dataPagamento = LocalDateTime.now().toString(),
            pagamentoId = UUID.randomUUID().toString(),
            mensagem = "Pagamento efetuado com sucesso"
        )

        assertDoesNotThrow { pedidoGateway.confirmarPagamento(pedidoOutput) }
    }
}

