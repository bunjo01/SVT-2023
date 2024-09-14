package uns.ftn.projekat.svt2023.configration;

import org.apache.tika.language.detect.LanguageDetector;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uns.ftn.projekat.svt2023.exceptionhandling.exception.NotFoundException;

import java.io.IOException;

@Configuration
public class BeanConfiguration {

    @Bean
    public LanguageDetector languageDetector() {
        LanguageDetector languageDetector;
        try {
            languageDetector = LanguageDetector.getDefaultLanguageDetector().loadModels();
        } catch (IOException e) {
            throw new NotFoundException("Error while loading language models.");
        }
        return languageDetector;
    }
}
