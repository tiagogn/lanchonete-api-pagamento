package br.com.fiap.lanchonete.pagamento.adapters.output.persistence

import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Repository
import java.util.*

@Profile("!test")
@Repository
class PagamentoRepositoryAdapter(
    private val mongoTemplate: MongoTemplate
): PagamentoRepository {
    override fun save(pagamento: Pagamento) {
        mongoTemplate.save(pagamento)
    }

    override fun findById(id: UUID): Pagamento? {
        return mongoTemplate.findById(id.toString(), Pagamento::class.java)
    }

    override fun findByPedidoId(pedidoId: String): Pagamento? {
        val query = Query()
        query.addCriteria(Criteria.where("pedidoId").`is`(pedidoId))
        return mongoTemplate.findOne(query, Pagamento::class.java)
    }

    override fun findAll(): List<Pagamento> {
        return mongoTemplate.findAll(Pagamento::class.java)
    }
}