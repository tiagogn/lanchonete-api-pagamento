package br.com.fiap.lanchonete.pagamento.core.application

import br.com.fiap.lanchonete.core.dto.PagamentoInput
import br.com.fiap.lanchonete.pagamento.PagamentoApplicationTests
import br.com.fiap.lanchonete.pagamento.core.application.ports.input.PagamentoService
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.domain.FormaPagamento
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.exceptions.PagamentoException
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.annotation.DirtiesContext
import java.math.BigDecimal
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PagamentoServiceTest: PagamentoApplicationTests() {

    @MockkBean
    lateinit var pedidoGateway: PedidoGateway

    @Autowired
    lateinit var pagamentoService: PagamentoService

    @BeforeEach
    fun setup() {
        every { pedidoGateway.consultarPedido("1") }.answers {
            PedidoInput(
                pedidoId = "1",
                valor = BigDecimal.valueOf(100.00)
            )
        }

        every { pedidoGateway.consultarPedido("2") }.answers { null }

        every { pedidoGateway.confirmarPagamento(any()) }.answers { Unit }
    }

    @Test
    fun `should make a payment`() {

        val pagamentoInput = PagamentoInput(
            pedidoId = "1",
            valor = BigDecimal(100.0),
            formaPagamento = FormaPagamento.PIX
        )

        val pagamento = pagamentoService.efetuarPagamento(pagamentoInput)

        verify(exactly = 1) { pedidoGateway.consultarPedido("1") }
        verify(exactly = 1) { pedidoGateway.confirmarPagamento(any()) }

        assertNotNull(pagamento)
        assertNotNull(pagamento.id)
        assertNotNull(pagamento.dataPagamento)
        assertNotNull(pagamento.mensagem)
        assertNotNull(pagamento.status)
        assertEquals(pagamentoInput.valor, pagamento.valor)
    }

    @Test
    fun `should not make a payment`() {

        val pagamentoInput = PagamentoInput(
            pedidoId = "2",
            valor = BigDecimal(100.0),
            formaPagamento = FormaPagamento.PIX
        )

        assertThrows<PagamentoException> {
            pagamentoService.efetuarPagamento(pagamentoInput)
        }
    }

    @Test
    fun `should list payments`() {

        val pagamentoInput = PagamentoInput(
            pedidoId = "1",
            valor = BigDecimal(100.0),
            formaPagamento = FormaPagamento.PIX
        )

        val pagamento = pagamentoService.efetuarPagamento(pagamentoInput)

        val pagamentos = pagamentoService.listarPagamentos()

        assertEquals(1, pagamentos.size)
        assertContains(pagamentos, pagamento)
    }

    @Test
    fun `should find a payment`() {

        val pagamentoInput = PagamentoInput(
            pedidoId = "1",
            valor = BigDecimal(100.0),
            formaPagamento = FormaPagamento.PIX
        )

        val pagamento = pagamentoService.efetuarPagamento(pagamentoInput)

        val pagamentoFound = pagamentoService.consultarPagamento(pagamento.pedidoId)

        assertNotNull(pagamentoFound)
        assertEquals(pagamento, pagamentoFound)
    }
}