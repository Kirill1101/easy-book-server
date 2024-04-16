package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
  List<Appointment> findAppointmentBySchedule_Id(Long scheduleId);

  List<Appointment> findAppointmentsByUserLogin(String userLogin);
}
