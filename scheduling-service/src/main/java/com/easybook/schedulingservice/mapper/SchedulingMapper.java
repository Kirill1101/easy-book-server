package com.easybook.schedulingservice.mapper;

import com.easybook.schedulingservice.dto.createdto.AppointmentCreateDto;
import com.easybook.schedulingservice.dto.createdto.OrganizationCreateDto;
import com.easybook.schedulingservice.dto.createdto.ScheduleCreateDto;
import com.easybook.schedulingservice.dto.createdto.ServiceCreateDto;
import com.easybook.schedulingservice.dto.createdto.SlotCreateDto;
import com.easybook.schedulingservice.dto.regulardto.AppointmentDto;
import com.easybook.schedulingservice.dto.regulardto.OrganizationDto;
import com.easybook.schedulingservice.dto.regulardto.ScheduleDto;
import com.easybook.schedulingservice.dto.regulardto.ServiceDto;
import com.easybook.schedulingservice.dto.regulardto.SlotDto;
import com.easybook.schedulingservice.entity.Appointment;
import com.easybook.schedulingservice.entity.Organization;
import com.easybook.schedulingservice.entity.Schedule;
import com.easybook.schedulingservice.entity.Service;
import com.easybook.schedulingservice.entity.Slot;

@org.mapstruct.Mapper(componentModel = "spring")
public interface SchedulingMapper {
  Organization organizationCreateDtoToOrganization(OrganizationCreateDto organizationCreateDto);

  OrganizationDto organizationToOrganizationDto(Organization organization);

  Organization organizationDtoToOrganization(OrganizationDto organizationDto);

  Appointment appointmentCreateDtoToAppointment(AppointmentCreateDto appointmentCreateDto);

  AppointmentDto appointmentToAppointmentDto(Appointment appointment);

  Appointment appointmentDtoToAppointment(AppointmentDto appointment);

  Schedule scheduleCreateDtoToSchedule(ScheduleCreateDto scheduleCreateDto);

  ScheduleDto scheduleToScheduleDto(Schedule schedule);

  Schedule scheduleDtoToSchedule(ScheduleDto scheduleDto);

  Service serviceCreateDtoToService(ServiceCreateDto serviceCreateDto);

  ServiceDto serviceToServiceDto(Service service);

  Service serviceDtoToService(ServiceDto serviceDto);

  Slot slotCreateDtoToSlot(SlotCreateDto slotCreateDto);

  SlotDto slotToSlotDto(Slot slot);

  Slot slotDtoToSlot(SlotDto slotDto);
}
