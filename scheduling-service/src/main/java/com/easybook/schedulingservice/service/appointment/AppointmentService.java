package com.easybook.schedulingservice.service.appointment;

import com.easybook.schedulingservice.entity.Appointment;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {
  Appointment createAppointment(Appointment appointment);

  Optional<Appointment> getAppointmentById(UUID id);

  List<Appointment> getAllAppointmentsByScheduleId(UUID scheduleId);

  List<Appointment> getAllAppointmentsByUserLogin(String userLogin);

  Appointment updateAppointment(Appointment appointment);

  void deleteAppointmentById(UUID id);
}
