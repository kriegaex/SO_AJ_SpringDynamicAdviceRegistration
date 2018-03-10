package de.scrum_master.performancemonitor;

import java.time.LocalDate;
import java.time.Period;

public class PersonService {
  public String getFullName(Person person) {
    return person.getFirstName() + " " + person.getLastName();
  }

  public int getAge(Person person) {
    Period p = Period.between(person.getDateOfBirth(), LocalDate.now());
    return p.getYears();
  }
}
