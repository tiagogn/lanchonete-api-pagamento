package br.com.fiap.lanchonete.pagamento.core.exceptions

class ResourceNotFoundException(
    override val message: String
): RuntimeException() {
}