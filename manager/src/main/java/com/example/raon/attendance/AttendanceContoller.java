package com.example.raon.attendance;

import java.util.List;

import org.springframework.data.domain.Page;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")

//勤怠管理ページのコントローラー
public class AttendanceContoller {

	private final AttendanceService attendanceService;
	private final EmployeeRepository employeeRepository;

	// 勤怠記録する画面に移動, ログインしたユーザーのみ入場
	@GetMapping("/attend")
	@PreAuthorize("isAuthenticated()")
	public String Attend(Model model) {
		
		//全職員情報をemployee Repositoryから取得
		List<Employee> employees = employeeRepository.findAll();
		
		//employeesオブジェクトをmodelに追加
		model.addAttribute("employees", employees);
		
		//attendance_attend.htmlでレンダリング
		return "attendance_attend";
	}

	// 勤怠開始時間を記録
	@PostMapping("/attend/checkin")
	public String checkIn(@RequestParam Long code) {
		
		//与えられたコードに該当する職員IDを探します
		Employee employee = employeeRepository.findByEmployeeId(code);
		
		//employeeがnullでない場合は、以下のコードブロックを実行
		if (employee != null) {
			
			//checkInメソッドを実行
			attendanceService.checkIn(code);
			
			//リダイレクト -> /attendance/attend
			return "redirect:/attendance/attend";
		} else {
			//リダイレクト -> error 
			return "redirect:/error";
		}
	}

	// 勤怠終了時間を記録
	@PostMapping("/attend/checkout")
	public String checkOut(@RequestParam Long code) {
		
		//与えられたコードに該当する職員IDを探します
		Employee employee = employeeRepository.findByEmployeeId(code);

		//employeeがnullでない場合は、以下のコードブロックを実行
		if (employee != null) {
			
			//checkOutメソッドを実行
			attendanceService.checkOut(code);
		}

		// リダイレクト ->  attendance/attend
		return "redirect:/attendance/attend";
	}

	
	//ログインしたユーザー自身の勤怠記録を閲覧
	@GetMapping("/list/{code}/{page}")
	@PreAuthorize("isAuthenticated()")
	public String getAttendanceList(@PathVariable Long code,
	                                @PathVariable int page,
	                                Model model) {
		
		//ページングが0以下にならないように制限
	    if (page < 0) {page = 0;}

	    //入力した社員番号が存在するかどうかを確認
	    boolean employeeExists = attendanceService.existsByCode(code);

	    //出席データが存在しない場合、"_errorhandler_TPE"ビュー(エラーページ)へのリダイレクト
	    if (!employeeExists) {
	        return "_errorhandler_TPE"; 
	    }
	    
	    //コードとページに該当する出席データを取得。 このデータはattendancePage変数に割り当てます
	    Page<Attendance> attendancePage = attendanceService.getList(code, page);
	    
	    //attendancePageオブジェクトをmodelに追加
	    model.addAttribute("attendancePage", attendancePage);
	    
		//attendance_list.htmlでレンダリング
	    return "attendance_list";
	}
	
	//遅刻した出勤記録に遅刻事由を更新
	@PostMapping("/list/{code}/{page}")
	public String updateLateReason(
	    @PathVariable Long code,
	    @PathVariable int page,
	    @RequestParam("attendanceId") Long attendanceId,
	    @RequestParam("lateReason") String lateReason
	) {
		
		//updateLateReasonを使用してattendanceIdとlateReasonを使用して出席情報を更新
	    attendanceService.updateLateReason(attendanceId, lateReason);
	    
	    //リダイレクト -> /list/{code}/{page}
	    return "redirect:/attendance/list/" + code + "/" + page;
	}






}