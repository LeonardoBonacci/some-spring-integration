package guru.bonacci.integration.to_kafka;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageChannel;

@SpringBootApplication
public class SomeIntegrationApp {

	public static void main(String[] args) {
		SpringApplication.run(SomeIntegrationApp.class, args);
	}

	
	@Bean
  MessageChannel csvInputChannel() {
    return new DirectChannel();
  }


  @Bean
  IntegrationFlow printFile(PersonTransformer transformer) {
      return IntegrationFlow.from("csvInputChannel")
      	  .splitWith(s -> s.applySequence(false).delimiters("\n"))
          .filter(line -> !((String) line).startsWith("id")) // skip header
          .transform(String.class, transformer::transform)
          .filter(p -> p != null)
          .handle(System.out::println, e -> e.requiresReply(false))
          .get();
  }
  
  @EventListener(ApplicationReadyEvent.class)
  void sendCsvToFlow() throws Exception {
      ClassPathResource resource = new ClassPathResource("people.csv"); 

      try (InputStream is = resource.getInputStream()) {
          String content = new String(is.readAllBytes(), StandardCharsets.UTF_8);

          csvInputChannel().send(MessageBuilder.withPayload(content).build());
      }
  }
}
