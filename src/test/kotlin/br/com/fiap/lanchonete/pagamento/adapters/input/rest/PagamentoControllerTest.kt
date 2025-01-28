package br.com.fiap.lanchonete.pagamento.adapters.input.rest

import br.com.fiap.lanchonete.pagamento.PagamentoApplicationTests
import br.com.fiap.lanchonete.pagamento.adapters.input.rest.request.PagamentoPedidoRequest
import br.com.fiap.lanchonete.pagamento.core.application.ports.input.PagamentoService
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class PagamentoControllerTest(
    @Autowired private val mockMvc: MockMvc
): PagamentoApplicationTests() {

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
        val pagamentoPedidoRequest = PagamentoPedidoRequest(
            pedidoId = "1",
            valor = 100.0,
            formaPagamento = "PIX"
        )

        mockMvc.perform(
            post("/v1/pagamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(pagamentoPedidoRequest))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.pagamentoId").isNotEmpty)
            .andExpect(jsonPath("$.pedidoId").value(pagamentoPedidoRequest.pedidoId))
            .andExpect(jsonPath("$.valor").value(pagamentoPedidoRequest.valor))
            .andExpect(jsonPath("$.formaPagamento").value(pagamentoPedidoRequest.formaPagamento))
            .andExpect(jsonPath("$.dataPagamento").isNotEmpty)
            .andExpect(jsonPath("$.mensagem").isNotEmpty)
    }

    @Test
    fun `should not make a payment`() {
        val pagamentoPedidoRequest = PagamentoPedidoRequest(
            pedidoId = "2",
            valor = 100.0,
            formaPagamento = "PIX"
        )

        mockMvc.perform(
            post("/v1/pagamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(pagamentoPedidoRequest))
        )
            .andExpect { status().is4xxClientError }
            .andExpect { jsonPath("$.message").value("Pedido ${pagamentoPedidoRequest.pedidoId} não encontrado") }
    }
}