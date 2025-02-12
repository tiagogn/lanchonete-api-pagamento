package br.com.fiap.lanchonete.pagamento.bdd

import com.fasterxml.jackson.databind.ObjectMapper
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.junit.jupiter.api.Assertions.assertTrue
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals

class PagamentoConsultaSteps: SpringIntegrationTest() {

    var pagamentoPayload = ""
    lateinit var pagamentoResponse: HttpResponse<String>
    var pedidoId = ""
    var pagamentoId = ""

    @Given("que eu tenho um pedido feito e com pagamento realizado")
    fun `que eu tenho um pagamento realizado`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8081/pedido/v1/pedidos"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(TemplatePedido.PEDIDO_PAYLOAD))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(201, response.statusCode())
        val body = response.body()
        val json = ObjectMapper().readTree(body)
        pedidoId = json.get("id").asText()
        pagamentoPayload = """
            {
                "pedidoId": "${pedidoId}",
                "valor": "${json.get("total").asDouble()}",
                "formaPagamento": "PIX"
            }
        """.trimIndent()

        val pagamentoClient = HttpClient.newHttpClient()
        val pagamentoRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(pagamentoPayload))
            .build()
        val pagamentoResponse = pagamentoClient.send(pagamentoRequest, HttpResponse.BodyHandlers.ofString())
        assertEquals(200, pagamentoResponse.statusCode())
    }

    @When("eu quero consultar o pagamento desse pedido")
    fun `eu quero consultar o pagamento`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento/$pedidoId"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
    }

    @Then("o pagamento desse pedido é exibido com sucesso")
    fun `o pagamento é exibido com sucesso`() {
        assertEquals(200, pagamentoResponse.statusCode())
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

    @Given("que eu tenho uma lista de pagamentos")
    fun `que eu tenho uma lista de pagamentos`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8081/pedido/v1/pedidos"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(TemplatePedido.PEDIDO_PAYLOAD))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        assertEquals(201, response.statusCode())
        val body = response.body()
        val json = ObjectMapper().readTree(body)
        pedidoId = json.get("id").asText()
        pagamentoPayload = """
            {
                "pedidoId": "${pedidoId}",
                "valor": "${json.get("total").asDouble()}",
                "formaPagamento": "PIX"
            }
        """.trimIndent()

        val pagamentoClient = HttpClient.newHttpClient()
        val pagamentoRequest = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(pagamentoPayload))
            .build()
        val pagamentoResponse = pagamentoClient.send(pagamentoRequest, HttpResponse.BodyHandlers.ofString())
        assertEquals(200, pagamentoResponse.statusCode())
    }

    @When("eu consulto a lista de pagamentos")
    fun `eu consulto a lista de pagamentos`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .GET()
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
    }

    @Then("a lista de pagamentos é exibida com sucesso")
    fun `eu recebo a lista de pagamentos`() {
        assertEquals(200, pagamentoResponse.statusCode())
        val body = pagamentoResponse.body()
        val json = ObjectMapper().readTree(body)
        assertTrue(json.isArray)
        assertTrue(json.size() > 0)
    }
}