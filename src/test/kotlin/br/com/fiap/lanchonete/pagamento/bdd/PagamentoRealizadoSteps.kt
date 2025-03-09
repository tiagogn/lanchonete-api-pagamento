package br.com.fiap.lanchonete.pagamento.bdd

import com.fasterxml.jackson.databind.ObjectMapper
import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import kotlin.test.assertEquals

class PagamentoRealizadoSteps() {

    var pagamentoPayload = ""
    lateinit var pagamentoResponse: HttpResponse<String>
    var pedidoId = ""
    var pagamentoId = ""

    @Given("que eu tenho um pedido feito com sucesso")
    fun `que eu tenho um pedido feito com sucesso`() {
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

    @Given("que eu tenho um pedido feito e pago")
    fun `que eu tenho um pedido feito e pago`() {
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

    @When("eu realizo o pagamento do pedido novamente")
    fun `eu realizo o pagamento do pedido novamente`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(pagamentoPayload))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
        assertEquals(400, response.statusCode())
    }

    @Then("o pagamento falha pois o pedido já foi pago")
    fun `o pagamento não é realizado`() {
        val body = pagamentoResponse.body()
        assert(body.contains("Pagamento do pedido ${pedidoId} já realizado"))
    }

    @Given("que eu tenho um pedido feito com um valor")
    fun `que eu tenho um pedido feito`() {
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
                "valor": "${Math.random() * 100}",
                "formaPagamento": "PIX"
            }
        """.trimIndent()
    }

    @When("eu realizo o pagamento com um valor diferente")
    fun `eu realizo o pagamento com um valor diferente`() {
        val client = HttpClient.newHttpClient()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:8080/pagamento/v1/pagamento"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(pagamentoPayload))
            .build()
        val response = client.send(request, HttpResponse.BodyHandlers.ofString())
        pagamentoResponse = response
        assertEquals(400, response.statusCode())
    }

    @Then("o pagamento falha pois o valor é diferente")
    fun `o pagamento falha pois o valor é diferente`() {
        val body = pagamentoResponse.body()
        assert(body.contains("Valor do pagamento diferente do valor do pedido"))
    }

}