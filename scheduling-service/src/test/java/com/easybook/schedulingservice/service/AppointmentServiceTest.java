package com.easybook.schedulingservice.service;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.repository.AppointmentRepository;
import com.easybook.schedulingservice.repository.SlotRepository;
import com.easybook.schedulingservice.service.appointment.AppointmentServiceImpl;
import com.easybook.schedulingservice.service.slot.SlotService;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AppointmentServiceTest {

  @Mock
  private SlotService slotService;

  @Mock
  private SlotRepository slotRepository;

  @Mock
  private AppointmentRepository appointmentRepository;

  @InjectMocks
  private AppointmentServiceImpl appointmentService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  void createAppointment_ValidAppointment_ShouldReturnCreatedAppointment() {
    Appointment appointment = new Appointment();
    appointment.setStartTime(LocalTime.now());
    appointment.setEndTime(LocalTime.now().plusHours(1));

    when(appointmentRepository.save(appointment)).thenReturn(appointment);

    Appointment result = appointmentService.createAppointment(appointment);

    assertEquals(appointment, result);
    verify(slotService, times(1)).setSlotsAsOccupiedByAppointment(appointment);
  }

  @Test
  void getAppointmentById_ExistingId_ShouldReturnAppointment() {
    UUID appointmentId = UUID.randomUUID();
    Appointment appointment = new Appointment();
    when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(appointment));

    Optional<Appointment> result = appointmentService.getAppointmentById(appointmentId);

    assertEquals(Optional.of(appointment), result);
  }

  @Test
  void getAllAppointmentsByScheduleId_ExistingScheduleId_ShouldReturnAppointments() {
    UUID scheduleId = UUID.randomUUID();
    List<Appointment> appointments = Collections.singletonList(new Appointment());
    when(appointmentRepository.findAppointmentBySchedule_Id(scheduleId)).thenReturn(appointments);

    List<Appointment> result = appointmentService.getAllAppointmentsByScheduleId(scheduleId);

    assertEquals(appointments, result);
  }

  @Test
  void getAllAppointmentsByUserLogin_ExistingUserLogin_ShouldReturnAppointments() {
    String userLogin = "user";
    List<Appointment> appointments = Collections.singletonList(new Appointment());
    when(appointmentRepository.findAppointmentsByUserLogin(userLogin)).thenReturn(appointments);

    List<Appointment> result = appointmentService.getAllAppointmentsByUserLogin(userLogin);

    assertEquals(appointments, result);
  }

  @Test
  void updateAppointment_ValidAppointment_ShouldReturnUpdatedAppointment() {
    Appointment appointment = new Appointment();
    appointment.setId(UUID.randomUUID());
    LocalTime startTime = LocalTime.now();
    LocalTime endTime = LocalTime.now().plusHours(2);
    appointment.setStartTime(startTime);
    appointment.setEndTime(endTime);

    Appointment appointmentFromBase = new Appointment();
    appointmentFromBase.setId(appointment.getId());
    appointmentFromBase.setStartTime(startTime.minusHours(1));
    appointmentFromBase.setEndTime(endTime.minusHours(1));

    when(appointmentRepository.findById(appointment.getId())).thenReturn(Optional.of(appointmentFromBase));
    when(appointmentRepository.save(appointmentFromBase)).thenReturn(appointmentFromBase);

    Appointment result = appointmentService.updateAppointment(appointment);

    assertEquals(appointmentFromBase, result);
    verify(slotRepository, times(1)).getSlotsByAppointmentId(appointment.getId());
    verify(slotService, times(1)).setSlotsAsOccupiedByAppointment(appointmentFromBase);
  }

  @Test
  void deleteAppointmentById_ValidId_ShouldDeleteAppointment() {
    UUID appointmentId = UUID.randomUUID();

    appointmentService.deleteAppointmentById(appointmentId);

    verify(slotRepository, times(1)).getSlotsByAppointmentId(appointmentId);
    verify(appointmentRepository, times(1)).deleteById(appointmentId);
  }
}
