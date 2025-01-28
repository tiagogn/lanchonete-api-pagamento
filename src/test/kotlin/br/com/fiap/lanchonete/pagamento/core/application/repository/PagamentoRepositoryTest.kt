package br.com.fiap.lanchonete.pagamento.core.application.repository

import br.com.fiap.lanchonete.pagamento.PagamentoApplicationTests
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.domain.FormaPagamento
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import br.com.fiap.lanchonete.pagamento.core.domain.StatusPagamento
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID
import kotlin.test.Test

class PagamentoRepositoryTest: PagamentoApplicationTests() {

    var pagamentoRepository: PagamentoRepository = PagamentoRepositoryInMemory()

    @Test
    fun `should save a payment`() {

        val pagamento = Pagamento(
            valor = BigDecimal("10.00"),
            status = StatusPagamento.APROVADO,
            formaPagamento = FormaPagamento.DINHEIRO,
            dataPagamento = LocalDateTime.now(),
            pedidoId = RandomStringUtils.insecure().nextAscii(10),
            mensagem = RandomStringUtils.insecure().nextAscii(10)
        )

        pagamentoRepository.save(pagamento)

        assertNotNull(pagamento)
    }

    @Test
    fun `should find a payment by id`() {

        val id = UUID.randomUUID().toString()

        val pagamento = Pagamento(
            id,
            valor = BigDecimal("10.00"),
            status = StatusPagamento.APROVADO,
            formaPagamento = FormaPagamento.DINHEIRO,
            dataPagamento = LocalDateTime.now(),
            pedidoId = RandomStringUtils.insecure().nextAscii(10),
            mensagem = RandomStringUtils.insecure().nextAscii(10)
        )

        pagamentoRepository.save(pagamento)

        val pagamentoEncontrado = pagamentoRepository.findById(id)

        assertNotNull(pagamentoEncontrado)
        assertEquals(pagamento, pagamentoEncontrado)
    }

    @Test
    fun `should find all payments`() {

        val pagamento1 = Pagamento(
            id = UUID.randomUUID().toString(),
            valor = BigDecimal("10.00"),
            status = StatusPagamento.APROVADO,
            formaPagamento = FormaPagamento.DINHEIRO,
            dataPagamento = LocalDateTime.now(),
            pedidoId = RandomStringUtils.insecure().nextAscii(10),
            mensagem = RandomStringUtils.insecure().nextAscii(10)
        )

        val pagamento2 = Pagamento(
            id = UUID.randomUUID().toString(),
            valor = BigDecimal("10.00"),
            status = StatusPagamento.APROVADO,
            formaPagamento = FormaPagamento.DINHEIRO,
            dataPagamento = LocalDateTime.now(),
            pedidoId = RandomStringUtils.insecure().nextAscii(10),
            mensagem = RandomStringUtils.insecure().nextAscii(10)
        )

        pagamentoRepository.save(pagamento1)
        pagamentoRepository.save(pagamento2)

        val pagamentos = pagamentoRepository.findAll()

        assertEquals(2, pagamentos.size)
    }
}