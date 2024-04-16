package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Slot;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlotRepository extends JpaRepository<Slot, Long> {
  List<Slot> findSlotsBySchedule_Id(Long scheduleId);

  List<Slot> findSlotsBySchedule_IdAndAppointmentIdIsNotNull(Long scheduleId);

  List<Slot> findSlotsBySchedule_IdAndAppointmentIdIsNull(Long scheduleId);

  @Query("select Slot from Slot where Slot.startTime >= startTime AND Slot.startTime < endTime")
  List<Slot> findSlotsThatOccurWithinSpecifiedTimeInterval(LocalTime startTime, LocalTime endTime);

  List<Slot> getSlotsByAppointmentId(Long appointmentId);
}
