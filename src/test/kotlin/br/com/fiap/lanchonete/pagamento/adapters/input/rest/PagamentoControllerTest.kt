package br.com.fiap.lanchonete.pagamento.adapters.input.rest

import br.com.fiap.lanchonete.pagamento.PagamentoApplicationTests
import br.com.fiap.lanchonete.pagamento.adapters.input.rest.request.PagamentoPedidoRequest
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.domain.FormaPagamento
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import br.com.fiap.lanchonete.pagamento.core.domain.StatusPagamento
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDateTime

class PagamentoControllerTest(
    @Autowired private val mockMvc: MockMvc
): PagamentoApplicationTests() {

    @MockkBean
    lateinit var pedidoGateway: PedidoGateway

    @Autowired
    lateinit var pagamentoRepository: PagamentoRepository

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

    @Test
    fun `should not make a payment due to different values`() {
        val pagamentoPedidoRequest = PagamentoPedidoRequest(
            pedidoId = "1",
            valor = 200.0,
            formaPagamento = "PIX"
        )

        mockMvc.perform(
            post("/v1/pagamento")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(pagamentoPedidoRequest))
        )
            .andExpect { status().is4xxClientError }
            .andExpect { jsonPath("$.message").value("Valor do pagamento não corresponde ao valor do pedido") }
    }

    @Test
    fun `should get a payment`() {

        val pagamento = Pagamento(
            pedidoId = "1",
            valor = BigDecimal.valueOf(100.0),
            formaPagamento = FormaPagamento.PIX,
            dataPagamento = LocalDateTime.now(),
            status = StatusPagamento.APROVADO,
            mensagem = "Pagamento efetuado com sucesso"
        )

        pagamentoRepository.save(pagamento)

        mockMvc.perform(
            get("/v1/pagamento/${pagamento.pedidoId}")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.pagamentoId").isNotEmpty)
            .andExpect(jsonPath("$.pedidoId").value("1"))
            .andExpect(jsonPath("$.valor").value(100.0))
            .andExpect(jsonPath("$.formaPagamento").value("PIX"))
            .andExpect(jsonPath("$.dataPagamento").isNotEmpty)
            .andExpect(jsonPath("$.mensagem").isNotEmpty)
    }

    @Test
    fun `should not get a payment`() {
        mockMvc.perform(
            get("/v1/pagamento/2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `should get a list of payments`() {

        val pagamento = Pagamento(
            pedidoId = "1",
            valor = BigDecimal.valueOf(100.0),
            formaPagamento = FormaPagamento.PIX,
            dataPagamento = LocalDateTime.now(),
            status = StatusPagamento.APROVADO,
            mensagem = "Pagamento efetuado com sucesso"
        )

        pagamentoRepository.save(pagamento)

        mockMvc.perform(
            get("/v1/pagamento")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].pagamentoId").isNotEmpty)
            .andExpect(jsonPath("$[0].pedidoId").value("1"))
            .andExpect(jsonPath("$[0].valor").value(100.0))
            .andExpect(jsonPath("$[0].formaPagamento").value("PIX"))
            .andExpect(jsonPath("$[0].dataPagamento").isNotEmpty)
            .andExpect(jsonPath("$[0].mensagem").isNotEmpty)
    }
}