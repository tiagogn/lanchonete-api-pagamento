package br.com.fiap.lanchonete.pagamento.adapters.input.rest.response

data class PagamentoPedidoResponse (
    val pagamentoId: String,
    val pedidoId: String,
    val valor: Double,
    val formaPagamento: String,
    val dataPagamento: String,
    val mensagem: String
)