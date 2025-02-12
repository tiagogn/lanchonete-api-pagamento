package br.com.fiap.lanchonete.pagamento.bdd

class TemplatePedido {

    companion object {
        val PEDIDO_PAYLOAD = """
            {
                "clienteId":"",
                "itens": [
                    {
                        "produtoId": "272a6b0f-6681-44f5-9c4f-c433d8ad2620",
                        "nomeProduto": "X-Egg",
                        "quantidade": 1,
                        "precoUnitario": 31.00,
                        "categoria": "LANCHE"
                    },
                    {
                        "produtoId": "e240f195-05fb-4a90-b94b-e2da8bd53918",
                        "nomeProduto": "Coca-Cola",
                        "quantidade": 1,
                        "precoUnitario": 10.00,
                        "categoria": "BEBIDA"
                    },
                    {
                        "produtoId": "bad3046b-6929-41fa-8a2c-d7ec748fde4a",
                        "nomeProduto": "Batata Frita",
                        "quantidade": 2,
                        "precoUnitario": 15.00,
                        "categoria": "ACOMPANHAMENTO"
                    }
                ]
            }
        """.trimIndent()

    }
}