
    $(document).ready(function() {

       // 출발 날짜 선택
       $('#departureDate').daterangepicker({
           "singleDatePicker": true,  // 단일 날짜 선택
           "showDropdowns": false,     // 월/연도 드롭다운 보이기
           "locale": {
               "format": "YYYY-MM-DD",

               "weekLabel": "W",
               "daysOfWeek": ["S", "M", "T", "W", "T", "F", "S"],
               "monthNames": ["1 /", "2 /", "3 /", "4 /", "5 /", "6 /", "7 /", "8 /", "9 /", "10 /", "11 /", "12 /"],
               "firstDay": 0
           }
       });
 });

$(document).ready(function() {
       // 도착 날짜 선택
       $('#arrivalDate').daterangepicker({
           "singleDatePicker": true,  // 단일 날짜 선택
           "showDropdowns": false,     // 월/연도 드롭다운 보이기
           "locale": {
               "format": "YYYY-MM-DD",

               "weekLabel": "W",
               "daysOfWeek": ["S", "M", "T", "W", "T", "F", "S"],
               "monthNames": ["1 /", "2 /", "3 /", "4 /", "5 /", "6 /", "7 /", "8 /", "9 /", "10 /", "11 /", "12 /"],
               "firstDay": 0
           }
       });
 });