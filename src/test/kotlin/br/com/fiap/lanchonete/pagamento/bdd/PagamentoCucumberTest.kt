package br.com.fiap.lanchonete.pagamento.bdd

import io.cucumber.junit.CucumberOptions
import org.junit.runner.RunWith

@RunWith(io.cucumber.junit.Cucumber::class)
@CucumberOptions(
    features = ["src/test/resources/features/pagamento.feature"],
    glue = ["br.com.fiap.lanchonete.pagamento.bdd"],
    dryRun = true,
    plugin = ["pretty", "html:target/cucumber"]
)
class PagamentoCucumberTest {
}