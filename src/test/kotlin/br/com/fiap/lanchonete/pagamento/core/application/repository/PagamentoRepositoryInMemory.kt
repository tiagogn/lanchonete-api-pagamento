package br.com.fiap.lanchonete.pagamento.core.application.repository

import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Repository
import java.util.UUID

@Profile("test")
@Repository
class PagamentoRepositoryInMemory: PagamentoRepository {

    private val pagamentos = mutableListOf<Pagamento>()

    override fun save(pagamento: Pagamento) {
        if (!pagamentos.contains(pagamento)) {
            pagamentos.add(pagamento)
        }
    }

    override fun findById(id: UUID): Pagamento? {
        return pagamentos
            .find { it.id == id.toString() }
    }

    override fun findByPedidoId(pedidoId: String): Pagamento? {
        return pagamentos
            .find { it.pedidoId == pedidoId }
    }

    override fun findAll(): List<Pagamento> {
        return pagamentos
    }
}