package br.com.fiap.lanchonete.pagamento.adapters.output.gateway

import br.com.fiap.lanchonete.pagamento.adapters.output.client.PedidoClient
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput
import org.springframework.retry.annotation.Backoff
import org.springframework.retry.annotation.EnableRetry
import org.springframework.retry.annotation.Retryable
import org.springframework.stereotype.Component

@Component
@EnableRetry
class PedidoGatewayAdapter(
    private val pedidoClient: PedidoClient
): PedidoGateway {

    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 1000, multiplier = 1.5))
    override fun consultarPedido(pedidoId: String): PedidoInput {
        return pedidoClient.consultarPedido(pedidoId)
    }

    @Retryable(maxAttempts = 3, backoff = Backoff(delay = 1000, multiplier = 1.5))
    override fun confirmarPagamento(pedidoId: String, pedidoOutput: PedidoOutput) {
        pedidoClient.confirmarPagamento(pedidoId, pedidoOutput)
    }
}