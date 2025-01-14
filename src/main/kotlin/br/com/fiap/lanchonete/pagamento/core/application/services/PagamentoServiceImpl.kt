package br.com.fiap.lanchonete.pagamento.core.application.services

import br.com.fiap.lanchonete.core.dto.PagamentoInput
import br.com.fiap.lanchonete.pagamento.core.application.ports.input.PagamentoService
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.gateway.PedidoGateway
import br.com.fiap.lanchonete.pagamento.core.application.ports.output.repository.PagamentoRepository
import br.com.fiap.lanchonete.pagamento.core.domain.Pagamento
import br.com.fiap.lanchonete.pagamento.core.domain.StatusPagamento
import br.com.fiap.lanchonete.pagamento.core.dto.PedidoOutput
import br.com.fiap.lanchonete.pagamento.core.exceptions.PagamentoException
import java.time.LocalDateTime

class PagamentoServiceImpl(
    private val pagamentoRepository: PagamentoRepository,
    private val pedidoGateway: PedidoGateway
) : PagamentoService {
    override fun efetuarPagamento(pagamentoInput: PagamentoInput): Pagamento {

        val pedido = pedidoGateway.consultarPedido(pagamentoInput.pedidoId)
            ?: throw PagamentoException("Pedido ${pagamentoInput.pedidoId} não encontrado")

        var pagamento: Pagamento? = null

        if (pedido.valor.compareTo(pagamentoInput.valor) != 0) {
             pagamento = Pagamento(
                pedidoId = pagamentoInput.pedidoId,
                valor = pagamentoInput.valor,
                formaPagamento = pagamentoInput.formaPagamento,
                dataPagamento = LocalDateTime.now(),
                status = StatusPagamento.RECUSADO,
                mensagem = "Valor do pagamento não corresponde ao valor do pedido"
            )
        }
        else {
            pagamento = Pagamento(
                pedidoId = pagamentoInput.pedidoId,
                valor = pagamentoInput.valor,
                formaPagamento = pagamentoInput.formaPagamento,
                dataPagamento = LocalDateTime.now(),
                status = StatusPagamento.APROVADO,
                mensagem = "Pagamento efetuado com sucesso"
            )
        }

        pagamentoRepository.save(pagamento)

        pedidoGateway.confirmarPagamento(PedidoOutput(
            pedidoId = pagamento.pedidoId,
            valor = pagamento.valor,
            formaPagamento = pagamento.formaPagamento.name,
            status = pagamento.status.name,
            pagamentoId = pagamento.id.toString(),
            dataPagamento = pagamento.dataPagamento.toString(),
            mensagem = pagamento.mensagem
        ))

        return pagamento
    }

    override fun consultarPagamento(pedidoId: String): Pagamento? {
        return pagamentoRepository.findByPedidoId(pedidoId)
    }

    override fun listarPagamentos(): List<Pagamento> {
        return pagamentoRepository.findAll()
    }

}