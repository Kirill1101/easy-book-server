package com.easybook.schedulingservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Appointment {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id;

  private String userLogin;

  @Temporal(TemporalType.DATE)
  private LocalDate date;

  @Temporal(TemporalType.TIME)
  private LocalTime startTime;

  @Temporal(TemporalType.TIME)
  private LocalTime endTime;

  @ManyToMany
  private List<Service> services;

  private Long duration;

  @ManyToOne
  private Schedule schedule;
}
