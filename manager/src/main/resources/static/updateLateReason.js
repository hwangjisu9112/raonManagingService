/**
 * 
 */
    function submitLateReason() {
        var lateReason = document.getElementsByName("lateReason")[0].value;
        var attendanceId = /* 여기에 해당 열의 attendanceId 값을 가져오는 코드 추가 */
        
        // AJAX 요청을 사용하여 서버로 선택한 이유를 전송
        $.ajax({
            type: "POST",
            url: "/attendance/updateLateReason",
            data: JSON.stringify({ attendanceId: attendanceId, lateReason: lateReason }),
            contentType: "application/json",
            success: function(response) {
                // 성공적으로 업데이트된 경우 처리
            },
            error: function(error) {
                // 오류 처리
            }
        });
    }
