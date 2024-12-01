package computerdatabase;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class ComputerDatabaseSimulation extends Simulation {

    // Configuração do protocolo HTTP
    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://computer-database.gatling.io") // URL da aplicação
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3") // Cabeçalho HTTP
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36");

    // Definindo as ações de navegação
    ChainBuilder browseComputers = exec(http("Browse Computers")
        .get("/computers")  // Requisição GET para listar computadores
        .check(status().is(200)) // Verificando se o status da resposta é 200 OK
    );

    // Adicionar computador
    ChainBuilder addComputer = exec(http("Add Computer")
        .post("/computers")
        .formParam("name", "MacBook Pro 2021")
        .formParam("introduced", "2021-01-01")
        .formParam("discontinued", "2023-01-01")
        .formParam("company", "Apple")
        .check(status().is(200))  // Confirmar sucesso na operação
    );

    // Editar computador
    ChainBuilder editComputer = exec(http("Edit Computer")
        .put("/computers/1")
        .formParam("name", "MacBook Pro 2022")
        .formParam("introduced", "2022-01-01")
        .formParam("discontinued", "2024-01-01")
        .formParam("company", "Apple")
        .check(status().is(200))
    );

    // Excluir computador
    ChainBuilder deleteComputer = exec(http("Delete Computer")
        .delete("/computers/1")
        .check(status().is(200))
    );

    // Definir o cenário com a sequência de ações
    ScenarioBuilder scn = scenario("Computer Database Scenario")
        .exec(browseComputers)
        .pause(1)
        .exec(addComputer)
        .pause(1)
        .exec(editComputer)
        .pause(1)
        .exec(deleteComputer);

    {
        // Configuração da simulação: como o cenário será executado
        setUp(
            scn.injectOpen(rampUsers(10).during(5))  // Simulação de 10 usuários rampando durante 5 segundos
        ).protocols(httpProtocol); // Usando o protocolo HTTP configurado
    }
}
