package com.easybook.schedulingservice.service.slot;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Slot;
import com.easybook.schedulingservice.repository.SlotRepository;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class SlotServiceImpl implements SlotService{
  private final SlotRepository slotRepository;

  @Override
  public Slot createSlot(Slot slot) {
    return slotRepository.save(slot);
  }

  @Override
  public Optional<Slot> getSlotById(Long slotId) {
    return slotRepository.findById(slotId);
  }

  @Override
  public List<Slot> getAllSlotsByScheduleId(Long scheduleId) {
    return slotRepository.findSlotsBySchedule_Id(scheduleId);
  }

  @Override
  public List<Slot> getFreeSlots(Long scheduleId) {
    return slotRepository.findSlotsBySchedule_IdAndAppointmentIdIsNull(scheduleId);
  }

  @Override
  public List<Slot> getOccupiedSlots(Long scheduleId) {
    return slotRepository.findSlotsBySchedule_IdAndAppointmentIdIsNotNull(scheduleId);
  }

  @Override
  public Slot updateSlot(Slot slot) {
    return slotRepository.save(slot);
  }

  @Override
  public void deleteSlotById(Long slotId) {
    slotRepository.deleteById(slotId);
  }

  @Override
  public List<Slot> setSlotsAsOccupiedByAppointment(Appointment appointment) {
    List<Slot> slots = slotRepository.findSlotsThatOccurWithinSpecifiedTimeInterval(
        appointment.getStartTime(), appointment.getEndTime());
    slots.forEach(slot -> {
      if (slot.getAppointmentId() != null) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Slot is already occupied");
      } else {
        slot.setAppointmentId(appointment.getId());
      }
    });
    return slotRepository.saveAll(slots);
  }

  @Override
  public List<Slot> getAvailableSlotsForSpecifiedDuration(Long scheduleId, Duration duration) {
    List<Slot> freeSlots = getFreeSlots(scheduleId);

    List<Slot> slotsAvailableForAppointment = new ArrayList<>();
    for (int i = 0; i < freeSlots.size(); i++) {
      Duration sumOfTimeAfterCurrentSlot = Duration.ZERO;
      for (int j = i + 1; j < freeSlots.size(); j++) {
        LocalTime startTime = freeSlots.get(j - 1).getStartTime();
        LocalTime endTime = freeSlots.get(j - 1).getEndTime();
        sumOfTimeAfterCurrentSlot = sumOfTimeAfterCurrentSlot.plus(Duration.between(startTime, endTime));
        if (!freeSlots.get(j - 1).getEndTime().equals(freeSlots.get(j).getStartTime())) {
          break;
        }
      }

      if (sumOfTimeAfterCurrentSlot.compareTo(duration) <= 0) {
        slotsAvailableForAppointment.add(freeSlots.get(i));
      }
    }

    return slotsAvailableForAppointment;
  }

  @Override
  public List<Slot> getSlotsByAppointmentId(Long appointmentId) {
    return slotRepository.getSlotsByAppointmentId(appointmentId);
  }
}


