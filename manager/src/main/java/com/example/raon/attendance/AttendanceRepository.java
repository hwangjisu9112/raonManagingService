package com.example.raon.attendance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
   

   List<Attendance> findBynameofEmployee(String nameofEmployee);
   List<Attendance> findBynameofEmployeeOrderByAttCheckInDesc(String employeeName);
   Optional<Attendance> findById(Long attendanceId);
 }
