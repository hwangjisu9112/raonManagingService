package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.util.List;
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
		
		//Sort.Orderオブジェクトのリストを作成
		List<Sort.Order> sorts = new ArrayList<>();
		
		//降順に勤怠記録リストを並べ替え
		sorts.add(Sort.Order.desc("attCheckIn"));
		
		//pageableオブジェクトを作成、1ページあたりのアイテム数を25に設定
		Pageable pageable = PageRequest.of(page, 25, Sort.by(sorts));
		
		//Attendanceリストを取得
		return this.attendanceRepository.findAll(pageable);
	}

	//特定の日付の出勤記録を検索
	public List<Attendance> searchByDate(Long employeeCode, int year, int month, int day) {
		
		//検索結果を保存する空のAttendanceリストを作成
		List<Attendance> searchResults = new ArrayList<>();

		//特定のユーザーのすべての出勤記録を取得してリピート文
		for (Attendance attendance : getList(employeeCode, 0).getContent()) {
			
			//Attendanceオブジェクトの出勤時間を取得 (年,月,日)
			LocalDateTime attCheckIn = attendance.getAttCheckIn();
			int checkInYear = attCheckIn.getYear();
			int checkInMonth = attCheckIn.getMonthValue();
			int checkInDay = attCheckIn.getDayOfMonth();

			//検索したい年度、月および仕事と出勤時間の年度、月および日を比較. 
			//もし二つの日付が一致したら、当該出勤記録をsearchResultsリストに追加
			if (checkInYear == year && checkInMonth == month && checkInDay == day) {
				searchResults.add(attendance);
			}
		}
		
		//
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
