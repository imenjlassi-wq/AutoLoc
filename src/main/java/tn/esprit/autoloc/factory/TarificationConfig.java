
package tn.esprit.autoloc.factory;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class TarificationConfig {

    @Bean
    public TarifStandardStrategy tarifStandardStrategy() {
        return new TarifStandardStrategy();
    }

    @Bean
    public TarifLongueDureeStrategy tarifLongueDureeStrategy() {
        return new TarifLongueDureeStrategy();
    }

    @Bean
    public TarifWeekendStrategy tarifWeekendStrategy() {
        return new TarifWeekendStrategy();
    }

    @Bean
    public TarificationStrategyFactory tarificationStrategyFactory(
            List<TarificationStrategy> strategies) {
        return new TarificationStrategyFactory(strategies);
    }
}
