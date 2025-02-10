package br.com.fiap.lanchonete.pagamento.bdd

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals

class PagamentoSteps: SpringIntegrationTest() {

    var pagamentoPayload = ""
    lateinit var pagamentoResponse: HttpResponse<String>
    var pedidoId = ""
    var pagamentoId = ""

    @Given("que eu tenho um pedido feito")
    fun `que eu tenho um pedido feito`() {
        pagamentoPayload = """
            {
                "pedidoId": "b3e2f12a-2fcb-493b-a683-36678d95db72",
                "valor": 71.0,
                "formaPagamento": "PIX"
            }
        """.trimIndent()
        //Criar um pedido aqui
    }

    @When("eu realizo o pagamento")
    fun `eu realizo o pagamento`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(pagamentoPayload))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
        assertEquals(200, response.statusCode())
    }

    @Then("o pagamento é realizado com sucesso")
    fun `o pagamento é realizado com sucesso`() {
        val body = pagamentoResponse.body()
        assert(body.contains("Pagamento efetuado com sucesso"))
    }

    @Given("que eu tenho um pagamento inexistente de um pedido")
    fun `que eu tenho um pagamento inexistente`() {
        pedidoId = "b3e2f12a-2fcb-493b-a683-36678d95db73"
    }

    @When("eu consulto o pagamento desse pedido")
    fun `eu consulto o pagamento`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento/$pedidoId"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
    }

    @Then("o pagamento do pedido não é encontrado")
    fun `o pagamento não é encontrado`() {
        assertEquals(404, pagamentoResponse.statusCode())
    }
}