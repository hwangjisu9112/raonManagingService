package com.example.raon.attendance;

import java.util.List;
import java.util.Optional;

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
public class AttendanceContoller {

	private final AttendanceService attendanceService ;
	private final AttendanceRepository attendanceRepository ;
	private final EmployeeRepository employeeRepository ;

	@GetMapping("/attend")
	public String Attend(Model model) {
		List<Employee> employees = employeeRepository.findAll();
		model.addAttribute("employees", employees);
		return "attendance_attend";
	}

	@PostMapping("/attend/checkin")
	public String checkIn(@RequestParam String name) {
		Employee employee = employeeRepository.findByEmployeeName(name);
		if (employee != null) {
			attendanceService.checkIn(name);
			return "redirect:/attendance/list/" + name;
		} else {
			return "redirect:/error";
		}
	}

	@PostMapping("/attend/checkout")
	public String checkOut(@RequestParam String name) {
		Employee employee = employeeRepository.findByEmployeeName(name);

		if (employee != null) {
			attendanceService.checkOut(name);
		}

		return "redirect:/attendance/list/" + name;
	}

//	@PostMapping("/attend/checkin")
//	public String checkIn(@RequestParam String name) {
//		attendanceService.checkIn(name);
//		return "redirect:/attendance/list/" + name;
//	}

//	@PostMapping("/attend/checkout")
//	public String checkOut(@RequestParam String name) {
//		attendanceService.checkOut(name);
//		return "redirect:/attendance/list/" + name;
//	}

	@PostMapping("/list/{name}")
	public String updateRest(@RequestParam Long attendanceId, 
							@RequestParam(required = false) Boolean isRest,
							@PathVariable String name) {
		attendanceService.updateRestStatus(attendanceId, isRest);
		return "redirect:/attendance/list/" + name;
	}

	@GetMapping("/list/{name}")
	public String getAttendanceList(@PathVariable String name, Model model) {
		List<Attendance> attendanceList = attendanceService.getAttendanceByEmployeeName(name);
		model.addAttribute("attendanceList", attendanceList);
		return "attendance_list";
	}

}
