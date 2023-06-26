package com.example.raon.attendance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

//勤怠管理のレポジトリ
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
	// 社員を名前で検索
	List<Attendance> findBynameofEmployee(String nameofEmployee);

	// 社員か記録した出席時間で検索
	List<Attendance> findBynameofEmployeeOrderByAttCheckInDesc(String employeeName);

	// 勤怠管理記録をIDで検索
	Optional<Attendance> findById(Long attendanceId);
}
