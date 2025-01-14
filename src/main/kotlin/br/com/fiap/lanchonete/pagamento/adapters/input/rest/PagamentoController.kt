package br.com.fiap.lanchonete.pagamento.adapters.input.rest

import br.com.fiap.lanchonete.pagamento.adapters.input.rest.request.PagamentoPedidoRequest
import br.com.fiap.lanchonete.pagamento.adapters.input.rest.response.PagamentoPedidoResponse
import br.com.fiap.lanchonete.pagamento.core.application.ports.input.PagamentoService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/pagamento")
class PagamentoController(
    private val pagamentoService: PagamentoService
) {

    @PostMapping()
    fun efetuarPagamento(@RequestBody @Valid pagamentoPedidoRequest: PagamentoPedidoRequest): ResponseEntity<PagamentoPedidoResponse> {

        val pagamento = pagamentoService.efetuarPagamento(pagamentoPedidoRequest.toModel())

        return ResponseEntity.ok(PagamentoPedidoResponse(
            pagamentoId = pagamento.id!!,
            pedidoId = pagamento.pedidoId.toString(),
            valor = pagamento.valor.toDouble(),
            formaPagamento = pagamento.formaPagamento.name,
            dataPagamento = pagamento.dataPagamento.toString(),
            mensagem = "Pagamento efetuado com sucesso"
        ))
    }

    @GetMapping("/{pedidoId}")
    fun consultarPagamento(@PathVariable(required = true) pedidoId: String): ResponseEntity<PagamentoPedidoResponse> {

        val pagamento = pagamentoService.consultarPagamento(pedidoId)

        return if (pagamento != null) {
            ResponseEntity.ok(PagamentoPedidoResponse(
                pagamentoId = pagamento.id!!,
                pedidoId = pagamento.pedidoId,
                valor = pagamento.valor.toDouble(),
                formaPagamento = pagamento.formaPagamento.name,
                dataPagamento = pagamento.dataPagamento.toString(),
                mensagem = pagamento.mensagem
            ))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping()
    fun listarPagamentos(): ResponseEntity<List<PagamentoPedidoResponse>> {

        val pagamentos = pagamentoService.listarPagamentos()

        return ResponseEntity.ok(pagamentos.map {
            PagamentoPedidoResponse(
                pagamentoId = it.id!!,
                pedidoId = it.pedidoId,
                valor = it.valor.toDouble(),
                formaPagamento = it.formaPagamento.name,
                dataPagamento = it.dataPagamento.toString(),
                mensagem = it.mensagem
            )
        })
    }
}