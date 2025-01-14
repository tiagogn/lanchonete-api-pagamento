package br.com.fiap.lanchonete.pagamento.core.application.ports.input

import br.com.fiap.lanchonete.core.dto.PagamentoInput
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento

interface PagamentoService {
    fun efetuarPagamento(pagamentoInput: PagamentoInput): Pagamento
    fun consultarPagamento(pedidoId: String): Pagamento?
    fun listarPagamentos(): List<Pagamento>
}