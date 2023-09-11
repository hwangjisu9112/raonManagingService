package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	public Page<Attendance> getList(Long code, int page) {
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("attCheckIn"));
		Pageable pageable = PageRequest.of(page, 25, Sort.by(sorts));
		return this.attendanceRepository.findAll(pageable);
	}

	public List<Attendance> searchByDate(Long employeeCode, int year, int month, int day) {
		List<Attendance> searchResults = new ArrayList<>();

		for (Attendance attendance : getList(employeeCode, 0).getContent()) {
			LocalDateTime attCheckIn = attendance.getAttCheckIn();
			int checkInYear = attCheckIn.getYear();
			int checkInMonth = attCheckIn.getMonthValue();
			int checkInDay = attCheckIn.getDayOfMonth();

			if (checkInYear == year && checkInMonth == month && checkInDay == day) {
				searchResults.add(attendance);
			}
		}

		return searchResults;
	}

	// 勤怠記録を残った社員
	public List<Attendance> getAttendanceByemployeeCode(Long code) {
		return attendanceRepository.findByemployeeCode(code);
	}

	public boolean existsByCode(Long code) {
		return attendanceRepository.existsByemployeeCode(code);
	}

	// 勤怠記録を残った社員
	public List<Attendance> getAttendanceByEmployeeName(Long code) {
		return attendanceRepository.findByemployeeCode(code);
	}

	// 勤務開始時間を記録
	public void checkIn(Long code) {
		Employee employee = employeeRepository.findByEmployeeId(code);

		if (employee != null) {
			Attendance a = new Attendance();
			a.setEmployeeCode(code);
			a.setAttCheckIn(LocalDateTime.now());
			attendanceRepository.save(a);
		}
	}

	// 勤務終了時間を記録
	public void checkOut(Long code) {
		List<Attendance> attendanceList = attendanceRepository.findByemployeeCodeOrderByAttCheckInDesc(code);

		if (!attendanceList.isEmpty()) {
			Attendance latestAttendance = attendanceList.get(0);
			latestAttendance.setAttCheckOut(LocalDateTime.now());
			attendanceRepository.save(latestAttendance);
		}
	}

	public void save(Attendance attendance) {
		attendanceRepository.save(attendance);
	}
	
	
	public void updateLateReason(Long attendanceId, String lateReason) {
	    // Retrieve the attendance record by ID
	    Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceId);

	    if (optionalAttendance.isPresent()) {
	        Attendance attendance = optionalAttendance.get();
	        attendance.setLateReason(lateReason);
	        // Save the updated attendance record
	        attendanceRepository.save(attendance);
	    }
	}

	// まだ未実装。。。社員の休み時間可否
	public void updateRestStatus(Long attendanceId, Boolean isRest) {
		Attendance attendance = attendanceRepository.findById(attendanceId).orElse(null);
		if (attendance != null) {
			attendance.setIsRest(isRest);
			attendanceRepository.save(attendance);
		}
	}

}
