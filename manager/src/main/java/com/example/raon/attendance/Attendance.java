package com.example.raon.attendance;

import java.time.LocalDateTime;
import java.time.LocalTime;

import java.time.Duration;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceId;

	private String nameofEmployee;
	
	private LocalDateTime attCheckIn;
	
	private LocalDateTime attCheckOut;
	
	private Boolean isRest ; 

	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;

	public void setAttCheckIn(LocalDateTime attCheckIn) {
		this.attCheckIn = attCheckIn;
		this.status = AttendanceStatus(attCheckIn);
	}
	
	public String getFormattedWorkDuration() {
	    if (attCheckIn != null && attCheckOut != null) {
	        Duration duration = Duration.between(attCheckIn, attCheckOut);

	       
	        if (isRest != null && isRest) {
	            duration = duration.minusHours(1);
	        }

	        long hours = duration.toHours();
	        long minutes = duration.toMinutesPart();
	        return String.format("%02d:%02d", hours, minutes);
	    } else {
	        return "";
	    }
	}
    
    
	private AttendanceStatus AttendanceStatus(LocalDateTime checkInTime) {
		LocalTime lateTime = LocalTime.of(10, 0); // Define the late threshold here
		if (checkInTime.toLocalTime().isAfter(lateTime)) {
			return AttendanceStatus.LATE;
			
		} else {
			
			return AttendanceStatus.ONTIME;
		}
	}
	
	  
	


}
