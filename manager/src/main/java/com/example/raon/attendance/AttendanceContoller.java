package com.example.raon.attendance;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.raon.employee.Employee;
import com.example.raon.employee.EmployeeRepository;
import com.example.raon.user.RaonUser;
import com.example.raon.user.RaonUserRepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attendance")

//勤怠管理ページのコントローラー
public class AttendanceContoller {

	private final AttendanceService attendanceService;
	private final EmployeeRepository employeeRepository;

	// 勤怠記録する画面に移動
	@GetMapping("/attend")
	@PreAuthorize("isAuthenticated()")
	public String Attend(Model model) {
		List<Employee> employees = employeeRepository.findAll();
		model.addAttribute("employees", employees);
		return "attendance_attend";
	}

	// 勤怠開始時間を記録

	@PostMapping("/attend/checkin")
	public String checkIn(@RequestParam Long code) {
		Employee employee = employeeRepository.findByEmployeeId(code);
		if (employee != null) {
			attendanceService.checkIn(code);
			return "redirect:/attendance/attend";
		} else {
			return "redirect:/error";
		}
	}

	// 勤怠終了時間を記録
	@PostMapping("/attend/checkout")
	public String checkOut(@RequestParam Long code) {
		Employee employee = employeeRepository.findByEmployeeId(code);

		if (employee != null) {
			attendanceService.checkOut(code);
		}

		return "redirect:/attendance/attend";
	}

	@GetMapping("/list/{code}/{page}")
	@PreAuthorize("isAuthenticated()")
	public String getAttendanceList(@PathVariable Long code,
	                                @PathVariable int page,
	                                Model model) {
	    if (page < 0) {page = 0;}

	    boolean employeeExists = attendanceService.existsByCode(code);

	    if (!employeeExists) {
	        return "_errorhandler_TPE"; 
	    }
	    
	    Page<Attendance> attendancePage = attendanceService.getList(code, page);
	    model.addAttribute("attendancePage", attendancePage);
	    return "attendance_list";
	}
	
	//	
	@PostMapping("/list/{code}/{page}")
	public String updateLateReason(
	    @PathVariable Long code,
	    @PathVariable int page,
	    @RequestParam("attendanceId") Long attendanceId,
	    @RequestParam("lateReason") String lateReason
	) {
	    attendanceService.updateLateReason(attendanceId, lateReason);
	    
	    return "redirect:/attendance/list/" + code + "/" + page;
	}






}