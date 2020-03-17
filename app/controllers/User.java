package controllers;

import java.time.LocalDate;

import play.data.validation.Constraints;

public class User {
  @Constraints.Required
  public Reason reason;

  @Constraints.Required
  @Constraints.MinLength(2)
  @Constraints.MaxLength(40)
  public String lastName;

  @Constraints.Required
  @Constraints.MinLength(2)
  @Constraints.MaxLength(40)
  public String firstName;

  @Constraints.Required
  public LocalDate birthDate;

  @Constraints.Required
  @Constraints.MinLength(2)
  @Constraints.MaxLength(40)
  public String address;

  @Constraints.MinLength(2)
  @Constraints.MaxLength(40)
  public String address2;

  @Constraints.Required
  @Constraints.Pattern("[0-9]{5}")
  public String zip;

  @Constraints.MinLength(2)
  @Constraints.MaxLength(40)
  @Constraints.Required
  public String city;

  @Constraints.Required
  public String signature;
}
