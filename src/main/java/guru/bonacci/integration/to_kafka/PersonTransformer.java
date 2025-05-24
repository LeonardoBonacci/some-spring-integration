package guru.bonacci.integration.to_kafka;

import org.springframework.stereotype.Component;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class PersonTransformer {

  private final CSVParser parser = new CSVParserBuilder()
      .withSeparator(',')
      .withIgnoreQuotations(false)
      .build();
  
  public Person transform(String line) {
  	log.info(line);
  	
  	try {
        String[] fields = parser.parseLine(line);
        if (fields.length < 3) return null;

        Person person = new Person();
        person.setId(fields[0].trim());
        person.setName(fields[1].trim());
        person.setAge(Integer.parseInt(fields[2].trim()));
        return person;
    } catch (Exception e) {
        // optionally log or route to error handling
        return null;
    }
}
}
