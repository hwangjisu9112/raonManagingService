package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService {

	private final AttendanceRepository attendanceRepository;
	private final EmployeeRepository employeeRepository;

	public List<Attendance> getList() {
		return this.attendanceRepository.findAll();
	}

	public List<Attendance> getAttendanceByEmployeeName(String name) {
		return attendanceRepository.findBynameofEmployee(name);
	}

//	public void checkIn(String name) {
//		Attendance a = new Attendance();
//		a.setNameofEmployee(name);
//		LocalDateTime checkIn = LocalDateTime.now();
//		a.setAttCheckIn(checkIn);
//		attendanceRepository.save(a);
//	}
//
//	public void checkOut(String name) {
//		Optional<Attendance> a = attendanceRepository.findByNameofEmployee(name);
//		if (a.isPresent()) {
//			Attendance attendance = a.get();
//			LocalDateTime checkOut = LocalDateTime.now();
//			attendance.setAttCheckOut(checkOut);
//			attendanceRepository.save(attendance);
//
//		}

	public void checkIn(String employeeName) {
		Employee employee = employeeRepository.findByEmployeeName(employeeName);

		if (employee != null) {
			Attendance a = new Attendance();
			a.setNameofEmployee(employeeName);
			a.setAttCheckIn(LocalDateTime.now());
			attendanceRepository.save(a);
		}
	}

	public void checkOut(String employeeName) {
		List<Attendance> attendanceList = attendanceRepository.findBynameofEmployeeOrderByAttCheckInDesc(employeeName);

		if (!attendanceList.isEmpty()) {
			Attendance latestAttendance = attendanceList.get(0);
			latestAttendance.setAttCheckOut(LocalDateTime.now());
			attendanceRepository.save(latestAttendance);
		}
	}


	public void save(Attendance attendance) {
		attendanceRepository.save(attendance);
	}
	
	 public void updateRestStatus(Long attendanceId, Boolean isRest) {
	        Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);
	        if (attendance != null) {
	            attendance.setIsRest(isRest);
	            attendanceRepository.save(attendance);
	        }
	    }
	

//	public void checkOut(String employeeName) {
//		List<Attendance> attendanceList = attendanceRepository.findBynameofEmployee(employeeName);
//		LocalDateTime checkOut = LocalDateTime.now();
//
//		for (Attendance attendance : attendanceList) {
//			attendance.setAttCheckOut(checkOut);
//			attendanceRepository.save(attendance);
//		}
//
//	}

}
