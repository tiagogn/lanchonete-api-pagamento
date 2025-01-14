package br.com.fiap.lanchonete.pagamento.adapters.output.client

import br.com.fiap.lanchonete.pagamento.core.dto.PedidoInput
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

@FeignClient(name = "pedidoClient", url = "\${lanchonete.pedido.url}")
interface PedidoClient {
    @RequestMapping(method = [RequestMethod.GET], value = ["/v1/pedidos/{pedidoId}"], consumes = ["application/json"])
    fun consultarPedido(@PathVariable pedidoId: String): PedidoInput

    @RequestMapping(method = [RequestMethod.POST], value = ["/v1/pedidos/pagamento/{pedidoId}"], consumes = ["application/json"])
    fun confirmarPagamento(pedidoOutput: PedidoOutput): ResponseEntity<String>
}