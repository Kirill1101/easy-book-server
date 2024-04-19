package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Slot;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlotRepository extends JpaRepository<Slot, Long> {
  List<Slot> findSlotsByScheduleDate_Id(Long scheduleDateId);

  List<Slot> findSlotsByScheduleDate_IdAndAppointmentIdIsNotNull(Long scheduleDateId);

  List<Slot> findSlotsByScheduleDate_IdAndAppointmentIdIsNull(Long scheduleDateId);

  @Query(value = "SELECT * FROM slot WHERE slot.schedule_date_id = ?1 AND "
      + "slot.start_time >= ?2 AND slot.end_time < ?3",
      nativeQuery = true)
  List<Slot> findSlotsThatOccurWithinSpecifiedTimeInterval(Long scheduleDateId,
      LocalTime startTime, LocalTime endTime);

  List<Slot> getSlotsByAppointmentId(Long appointmentId);
}
