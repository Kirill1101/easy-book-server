package com.easybook.schedulingservice.service.appointment;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.repository.AppointmentRepository;
import com.easybook.schedulingservice.repository.SlotRepository;
import com.easybook.schedulingservice.service.slot.SlotService;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

  private final SlotService slotService;

  private final SlotRepository slotRepository;

  private final AppointmentRepository appointmentRepository;

  public Appointment createAppointment(Appointment appointment) {
    appointment = appointmentRepository.save(appointment);
    slotService.setSlotsAsOccupiedByAppointment(appointment);
    appointment.setDuration(Duration.between(
        appointment.getStartTime(), appointment.getEndTime()).toSeconds());
    return appointment;
  }

  @Override
  public Optional<Appointment> getAppointmentById(Long id) {
    return appointmentRepository.findById(id);
  }

  @Override
  public List<Appointment> getAllAppointmentsByScheduleId(Long scheduleId) {
    return appointmentRepository.findAppointmentBySchedule_Id(scheduleId);
  }

  @Override
  public List<Appointment> getAllAppointmentsByUserLogin(String userLogin) {
    return appointmentRepository.findAppointmentsByUserLogin(userLogin);
  }

  @Override
  public Appointment updateAppointment(Appointment appointment) {
    Appointment appointmentFromBase
        = appointmentRepository.findById(appointment.getId()).orElseThrow();
    if (appointmentFromBase.getStartTime() != appointment.getStartTime() ||
        appointmentFromBase.getEndTime() != appointment.getEndTime() ||
        !appointmentFromBase.getDate().equals(appointment.getDate())) {
      slotRepository.getSlotsByAppointmentId(appointment.getId()).forEach(
          slot -> slot.setAppointmentId(null));

      appointmentFromBase.setDate(appointment.getDate());
      appointmentFromBase.setStartTime(appointment.getStartTime());
      appointmentFromBase.setEndTime(appointment.getEndTime());
      appointmentFromBase.setDuration(Duration.between(
          appointment.getStartTime(), appointment.getEndTime()).toSeconds());

      slotService.setSlotsAsOccupiedByAppointment(appointmentFromBase);
    }

    if (appointment.getServices() != null) {
      appointmentFromBase.setServices(appointment.getServices());
    }

    return appointmentRepository.save(appointmentFromBase);
  }

  @Override
  public void deleteAppointmentById(Long id) {
    slotRepository.getSlotsByAppointmentId(id).forEach(slot -> slot.setAppointmentId(null));
    appointmentRepository.deleteById(id);
  }
}
