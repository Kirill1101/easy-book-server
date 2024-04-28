package com.easybook.schedulingservice.service.slot;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Slot;
import java.time.Duration;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SlotService {

  Slot createSlot(Slot slot);

  Optional<Slot> getSlotById(UUID slotId);

  List<Slot> getAllSlotsByScheduleDateId(UUID scheduleDateId);

  List<Slot> getOccupiedSlots(UUID scheduleId);

  List<Slot> getFreeSlots(UUID scheduleId);

  List<Slot> getAvailableSlotsForSpecifiedDuration(UUID scheduleId, Duration duration);

  List<Slot> getSlotsByAppointmentId(UUID appointmentId);

  Slot updateSlot(Slot slot);

  void deleteSlotById(UUID slotId);

  List<Slot> setSlotsAsOccupiedByAppointment(Appointment appointment);
}
