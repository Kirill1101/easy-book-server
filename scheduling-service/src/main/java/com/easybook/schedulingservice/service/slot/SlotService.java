package com.easybook.schedulingservice.service.slot;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Slot;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SlotService {

  Slot createSlot(Slot slot);

  Optional<Slot> getSlotById(Long slotId);

  List<Slot> getAllSlotsByScheduleId(Long scheduleId);

  List<Slot> getOccupiedSlots(Long scheduleId);

  List<Slot> getFreeSlots(Long scheduleId);

  List<Slot> getAvailableSlotsForSpecifiedDuration(Long scheduleId, Duration duration);

  List<Slot> getSlotsByAppointmentId(Long appointmentId);

  Slot updateSlot(Slot slot);

  void deleteSlotById(Long slotId);

  List<Slot> setSlotsAsOccupiedByAppointment(Appointment appointment);
}
