package com.easybook.schedulingservice.repository;

import com.easybook.schedulingservice.entity.Slot;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SlotRepository extends JpaRepository<Slot, UUID> {
  List<Slot> findSlotsByScheduleDate_Id(UUID scheduleDateId);

  List<Slot> findSlotsByScheduleDate_IdAndAppointmentIdIsNotNull(UUID scheduleDateId);

  List<Slot> findSlotsByScheduleDate_IdAndAppointmentIdIsNull(UUID scheduleDateId);

  @Query(value = "SELECT * FROM slot WHERE slot.schedule_date_id = ?1 AND "
      + "slot.start_time >= ?2 AND slot.end_time < ?3",
      nativeQuery = true)
  List<Slot> findSlotsThatOccurWithinSpecifiedTimeInterval(UUID scheduleDateId,
      LocalTime startTime, LocalTime endTime);

  List<Slot> getSlotsByAppointmentId(UUID appointmentId);
}
