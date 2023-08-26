package com.example.raon.attendance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

//勤怠管理のレポジトリ
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	 Page<Attendance> findAll(Pageable pageable);
	
	// 社員をIDで検索
	List<Attendance> findByemployeeCode(Long employeeCode);

//	// 社員か記録した出席時間で検索
//	List<Attendance> findBynameofEmployeeOrderByAttCheckInDesc(String employeeName);

	// 社員か記録した出席時間で検索
	List<Attendance> findByemployeeCodeOrderByAttCheckInDesc(Long code);

	// 勤怠管理記録をIDで検索
	Optional<Attendance> findById(Long attendanceId);
}
