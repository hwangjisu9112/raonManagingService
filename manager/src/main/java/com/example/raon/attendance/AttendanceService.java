package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional

//勤怠管理のビジネスロージク
public class AttendanceService {

	// 生成子
	private final AttendanceRepository attendanceRepository;
	private final EmployeeRepository employeeRepository;

	// 勤務開始時間を記録
	public List<Attendance> getList() {
		return this.attendanceRepository.findAll();
	}

	// 勤怠記録を残った社員
	public List<Attendance> getAttendanceByEmployeeName(String name) {
		return attendanceRepository.findBynameofEmployee(name);
	}

	// 勤務開始時間を記録
	public void checkIn(String employeeName) {
		Employee employee = employeeRepository.findByEmployeeName(employeeName);

		if (employee != null) {
			Attendance a = new Attendance();
			a.setNameofEmployee(employeeName);
			a.setAttCheckIn(LocalDateTime.now());
			attendanceRepository.save(a);
		}
	}

	//勤務終了時間を記録
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

	//まだ未実装。。。
	public void updateRestStatus(Long attendanceId, Boolean isRest) {
		Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);
		if (attendance != null) {
			attendance.setIsRest(isRest);
			attendanceRepository.save(attendance);
		}
	}

}
