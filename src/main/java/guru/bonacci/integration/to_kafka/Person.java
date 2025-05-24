package guru.bonacci.integration.to_kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person {
	
  private String id;
  private String name;
  private int age;
}
