
  Feature: Pagamento
    Como um usuário
    Eu quero realizar um pagamento
    Para que eu possa pagar um produto

    Scenario: Realizar pagamento com sucesso
      Given que eu tenho um pedido feito com sucesso
      When eu realizo o pagamento
      Then o pagamento é realizado com sucesso

    Scenario: Realizar um pagamento de um pedido já pago
      Given que eu tenho um pedido feito e pago
      When eu realizo o pagamento do pedido novamente
      Then o pagamento falha pois o pedido já foi pago

    Scenario: Realizar um pagamento com valor diferente do pedido
      Given que eu tenho um pedido feito com um valor
      When eu realizo o pagamento com um valor diferente
      Then o pagamento falha pois o valor é diferente

    Scenario: Realizar a consulta de um pagamento para um pedido
      Given que eu tenho um pedido feito e com pagamento realizado
      When eu quero consultar o pagamento desse pedido
      Then o pagamento desse pedido é exibido com sucesso

    Scenario: Realizar a consulta de um pagamento inexistente
      Given que eu tenho um pagamento inexistente de um pedido
      When eu consulto o pagamento desse pedido
      Then o pagamento do pedido não é encontrado

    Scenario: Realizar a consulta de uma lista de pagamentos
      Given que eu tenho uma lista de pagamentos
      When eu consulto a lista de pagamentos
      Then a lista de pagamentos é exibida com sucesso