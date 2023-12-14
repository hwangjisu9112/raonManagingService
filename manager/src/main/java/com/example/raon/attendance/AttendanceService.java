package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;
import org.springframework.data.domain.Sort;
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
	public List<Attendance> getList(Long code) {
	    Sort sort = Sort.by(Sort.Order.desc("attCheckIn"));
	    return this.attendanceRepository.findAll(sort);
	}
	
	
	//特定の日付の出勤記録を検索
	public List<Attendance> findByYearAndMonth(Long employeeCode, int year, int month) {
	    return this.attendanceRepository.findByYearAndMonth(employeeCode, year, month);
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
		
		//与えられたcodeに該当するユーザー(Employee)を探します
		Employee employee = employeeRepository.findByEmployeeId(code);

		//ユーザーが存在する場合は、次のブロックを実行
		if (employee != null) {
			
			//新しいAtendanceオブジェクトを作成
			Attendance a = new Attendance();
			
			//setEmployeeCodeメソッドを使用して、当該出勤レコードのユーザーコードを設定
			//setAttCheckInメソッドを使用して出勤時間を現在の時間(LocalDateTime.now())に設定
			a.setEmployeeCode(code);
			a.setAttCheckIn(LocalDateTime.now());
			
			//新たに生成した出勤レコード(a)を保存
			attendanceRepository.save(a);
		}
	}

	// 勤務終了時間を記録
	public void checkOut(Long code) {
		
		//attendanceRepositoryを使用して与えられたcodeに該当するユーザーの出勤レコード(Attendance)リスト
		//リストは出勤時間(AttCheckIn)を降順に整列
		List<Attendance> attendanceList = attendanceRepository.findByemployeeCodeOrderByAttCheckInDesc(code);
		
		//出勤レコードリストが空でない場合は、次のブロックを実行
		if (!attendanceList.isEmpty()) {
			
			//出勤レコードリストから最初の(最新)出勤レコードを取得し、latestAttendance変数に割り当て
			Attendance latestAttendance = attendanceList.get(0);
			
			//latestAtendanceオブジェクトのsetAttCheckOutメソッドを使用して、勤務終了時間を現在の時間(LocalDateTime.now())に設定
			latestAttendance.setAttCheckOut(LocalDateTime.now());
			
			//出勤レコード(latestAttendance)を保存合
			attendanceRepository.save(latestAttendance);
		}
	}

	//出勤レコードの遅刻事由を更新
	public void updateLateReason(Long attendanceId, String lateReason) {
	    
		// attendance Repositoryを使用して与えられたattendance Idに該当する出勤レコードを検索
		//結果をOptionalで包み込んで返却
	    Optional<Attendance> optionalAttendance = attendanceRepository.findById(attendanceId);

	    //出勤レコードが存在する場合にのみ、次のブロックを実行
	    if (optionalAttendance.isPresent()) {
	    	
	    	//optional Attendanceから出勤レコードを取得し、attendance変数に割り当て
	        Attendance attendance = optionalAttendance.get();
	        
	        //出勤レコードの遅刻事由をlate Reason値に設定
	        attendance.setLateReason(lateReason);
	        
	        //更新された出勤レコードを保存
	        attendanceRepository.save(attendance);
	    }
	}
	

	
	

}
