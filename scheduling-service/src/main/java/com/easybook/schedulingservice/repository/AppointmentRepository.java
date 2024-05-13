package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {
  List<Appointment> findAppointmentBySchedule_Id(UUID scheduleId);

  List<Appointment> findAppointmentsByUserLogin(String userLogin);
}
