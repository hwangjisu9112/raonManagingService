package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.domain.Specification;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

//勤怠管理のレポジトリ
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

	List<Attendance> findAll(Specification<Attendance> spec);

	default List<Attendance> searchAttendanceByAttCheckIn(LocalDateTime attCheckIn) {
		Specification<Attendance> spec = (root, query, criteriaBuilder) -> {
			if (attCheckIn == null) {
				return criteriaBuilder.conjunction();
			}
			return criteriaBuilder.equal(root.get("attCheckIn"), attCheckIn);
		};
		return findAll(spec);
	}

	boolean existsByemployeeCode(Long employeeCode);

	// 社員をIDで検索
	List<Attendance> findByemployeeCode(Long employeeCode);

	// 社員か記録した出席時間で検索
	List<Attendance> findByemployeeCodeOrderByAttCheckInDesc(Long code);

	// 勤怠管理記録をIDで検索
	Optional<Attendance> findById(Long attendanceId);
	
	

    @Query("SELECT a FROM Attendance a WHERE YEAR(a.attCheckIn) = :year AND MONTH(a.attCheckIn) = :month")
    List<Attendance> findByYearAndMonth(Long employeeCode, int year, int month);

	
}
