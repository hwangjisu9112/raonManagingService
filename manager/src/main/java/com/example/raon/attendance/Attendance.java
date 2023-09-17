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

//勤怠DBに保存するfieldを定義
public class Attendance {
	
	/*
	 *attendanceId : 勤怠記録のID
	 *employeeCode : 出勤記録を残した職員のID
	 *attCheckIn : 出勤時間
	 *attCheckOut : 出勤時間
	 *isRest : 休憩時間1時間
	 *lateReason : もし遅刻をした場合、入力する遅刻事由
	 *status : 遅刻あるいは定時出勤
	 */

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long attendanceId;
	
	private Long employeeCode;

	private LocalDateTime attCheckIn;

	private LocalDateTime attCheckOut;

	private Boolean isRest;
	
	private String lateReason;

	// 社員の勤怠状態を表示する列挙Class
	@Enumerated(EnumType.STRING)
	private AttendanceStatus status;
	
	
	

	// 社員の勤怠開始時間を記録
	public void setAttCheckIn(LocalDateTime attCheckIn) {
		this.attCheckIn = attCheckIn;
		this.status = AttendanceStatus(attCheckIn);
	}

	// 社員の勤怠時間を記録
	public String getFormattedWorkDuration() {
		
		//attCheckIn と attCheckOut が null でない場合、つまり社員の出勤と退勤が記録されている場合にコードが実行す。
		if (attCheckIn != null && attCheckOut != null) {
			
			// Duration.between メソッドを使用, attCheckIn~attCheckOutの時間差を計算
			Duration duration = Duration.between(attCheckIn, attCheckOut);

			// 休み時間が本当なら、勤務時間から1時間を除く。

			if (isRest != null && isRest) {
				duration = duration.minusHours(1);
			}

			//勤務時間は時間と分で表現
			long hours = duration.toHours();
			long minutes = duration.toMinutesPart();
			return String.format("%02d:%02d", hours, minutes);
			
			
		} else {
			return "";
		}
	}

	// 社員が午前10時前に出勤 ＝ OnTime, それより後なら LATE
	private AttendanceStatus AttendanceStatus(LocalDateTime checkInTime) {
		
		//遅刻を基準とする時間設定, 午前10
		LocalTime lateTime = LocalTime.of(10, 0); 
		
		//チェックインをした時間が10時前なら定時、10時後なら遅刻
		if (checkInTime.toLocalTime().isAfter(lateTime)) {
			return AttendanceStatus.LATE;

		} else {

			return AttendanceStatus.ONTIME;
		}
	}

}
