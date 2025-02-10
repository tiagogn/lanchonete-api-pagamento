package br.com.fiap.lanchonete.pagamento.adapters.output.gateway

import br.com.fiap.lanchonete.pagamento.adapters.output.client.PedidoClient
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput
import org.springframework.stereotype.Component

@Component
class PedidoGatewayAdapter(
    private val pedidoClient: PedidoClient
): PedidoGateway {
    override fun consultarPedido(pedidoId: String): PedidoInput {
        return pedidoClient.consultarPedido(pedidoId)
    }

    override fun confirmarPagamento(pedidoId: String, pedidoOutput: PedidoOutput) {
        pedidoClient.confirmarPagamento(pedidoId, pedidoOutput)
    }
}