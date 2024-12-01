package computerdatabase;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;
import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class HeavyLoadSimulation extends Simulation {

    String apiKey = "8a60b2de14f7a17c7a11706b2cfcd87c";
    String baseUrl = "https://api.openweathermap.org/data/2.5/weather?q=Recife&appid=" + apiKey + "&units=metric&lang=pt_br";

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("https://pedrortm04.github.io/meu-site")
        .acceptHeader("application/json");

    ChainBuilder getWeatherRequest = exec(http("Get Weather API")
        .get(baseUrl)
        .check(status().is(200))
        .check(jsonPath("$.main.temp").exists()));

    ScenarioBuilder heavyLoad = scenario("Heavy Load Scenario")
        .exec(getWeatherRequest);

    {
        setUp(
            heavyLoad.injectOpen(constantUsersPerSec(100).during(30))  // 100 req/s por 30 segundos
        ).protocols(httpProtocol);
    }
}
