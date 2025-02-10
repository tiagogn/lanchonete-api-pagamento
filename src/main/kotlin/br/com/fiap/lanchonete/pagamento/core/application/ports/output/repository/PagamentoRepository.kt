package br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository

import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import java.util.*

interface PagamentoRepository {
    fun save(pagamento: Pagamento)
    fun findById(id: UUID): Pagamento?
    fun findByPedidoId(pedidoId: String): Pagamento?
    fun findAll(): List<Pagamento>
}