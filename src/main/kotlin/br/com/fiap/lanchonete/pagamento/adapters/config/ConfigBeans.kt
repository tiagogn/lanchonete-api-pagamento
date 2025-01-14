package br.com.fiap.lanchonete.pagamento.adapters.config

import br.com.fiap.lanchonete.pagamento.core.application.ports.input.PagamentoService
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.application.services.PagamentoServiceImpl
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConfigBeans(
    private val pagamentoRepository: PagamentoRepository,
    private val pedidoGateway: PedidoGateway
) {

    @Bean
    fun pagamentoService(): PagamentoService {
        return PagamentoServiceImpl(pagamentoRepository, pedidoGateway)
    }
}