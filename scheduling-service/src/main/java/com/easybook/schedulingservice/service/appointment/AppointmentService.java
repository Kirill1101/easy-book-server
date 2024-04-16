package com.easybook.schedulingservice.service.appointment;

import com.easybook.schedulingservice.entity.Appointment;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
  Appointment createAppointment(Appointment appointment);

  Optional<Appointment> getAppointmentById(Long id);

  List<Appointment> getAllAppointmentsByScheduleId(Long scheduleId);

  List<Appointment> getAllAppointmentsByUserLogin(String userLogin);

  Appointment updateAppointment(Appointment appointment);

  void deleteAppointmentById(Long id);
}
