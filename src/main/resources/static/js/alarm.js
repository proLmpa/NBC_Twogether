// DOM 요소 가져오기
const alarmButton = document.querySelector('.open-alarm-btn');
const alarmBadge = document.getElementById('alarm-badge');

// 초기 알림 개수 (예: 0개)
let alarmCount = 0;

// 댓글 생성에 대한 알림이 발생할 때마다 호출되는 함수
function handleNewAlarm() {
    // 알림 개수 증가
    alarmCount++;

    // 알림 아이콘에 형광 초록색 배지 표시 및 개수 업데이트
    alarmBadge.style.display = 'block';
    alarmBadge.textContent = alarmCount;
}

// 알림 창을 띄우는 동그란 버튼을 클릭할 때 알림 조회 기능을 수행
alarmButton.addEventListener('click', () => {
    // 알림 아이콘 클릭 시 알림 조회 기능 추가
    // 여기에 알림 목록을 표시하거나 관련 동작을 수행하는 코드 추가
    // 이 예제에서는 알림 개수를 초기화합니다.
    alarmCount = 0;
    alarmBadge.style.display = 'none'; // 배지 숨기기
});





