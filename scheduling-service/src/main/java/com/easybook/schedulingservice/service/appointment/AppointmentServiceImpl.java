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
    slotService.setSlotsAsOccupiedByAppointment(appointment);
    appointment.setDuration(Duration.between(
        appointment.getStartTime(), appointment.getEndTime()));
    return appointmentRepository.save(appointment);
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
        appointmentFromBase.getEndTime() != appointment.getEndTime()) {
      slotRepository.getSlotsByAppointmentId(appointment.getId()).forEach(
          slot -> slot.setAppointmentId(null));
      slotService.setSlotsAsOccupiedByAppointment(appointment);
      appointment.setDuration(Duration.between(
          appointment.getStartTime(), appointment.getEndTime()));
    }
    return appointmentRepository.save(appointment);
  }

  @Override
  public void deleteAppointmentById(Long id) {
    slotRepository.getSlotsByAppointmentId(id).forEach(slot -> slot.setAppointmentId(null));
    appointmentRepository.deleteById(id);
  }
}
