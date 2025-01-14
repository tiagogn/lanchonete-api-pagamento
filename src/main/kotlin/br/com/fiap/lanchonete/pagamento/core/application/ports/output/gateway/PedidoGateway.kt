package br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway

import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput

interface PedidoGateway {
    fun consultarPedido(pedidoId: String): PedidoInput?
    fun confirmarPagamento(pedidoOutput: PedidoOutput)
}