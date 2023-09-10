function callMyAlarms() {

    $('#alarm_list').empty();

    fetch('/api/alarms', {
        method: 'GET',
        headers: {
            'Authorization': Cookies.get('Authorization'),
            'Refresh-Token': Cookies.get('Refresh-Token')
        }
    })

    .then(async res => {
        checkTokenExpired(res)
        refreshToken(res)

        let alarmsResponse = await res.json();
        let alarms = alarmsResponse['alarms'];

        for (let alarmId in alarms) {
            if (alarms.hasOwnProperty(alarmId)) {
                let alarm = alarms[alarmId];
                $('#alarm-list').append(formAlarm(alarm));

                // 여기서 알림 객체마다 이벤트 리스너를 추가
                $('#read-alarm-btn-' + alarmId).click(function () {
                    readAlarm(alarmId);
                });
            }
        }
    })
}

function deleteAlarm(aId) {

    // 삭제 여부 확인
    const confirmation = confirm("알림을 삭제하시겠습니까?");

    if (confirmation) {

        // 사용자가 확인을 선택한 경우에만 알람 삭제
        fetch('/api/alarms/' + aId, {
            method: 'DELETE',
            headers: {
                'Authorization': Cookies.get('Authorization'),
                'Refresh-Token': Cookies.get('Refresh-Token')
            }
        })
        .then(async res => {
            checkTokenExpired(res);
            refreshToken(res);

            let alarms = await res.json();

            for (let alarm of alarms['alarms']) {
                alert('알림이 삭제되었습니다.');
            }
        })
        .catch(error => {
            console.error('알림 삭제 실패:', error);
        });
    }
}

function readAlarm(aId) {

    fetch('/api/alarms/' + aId, {
        method: 'PUT',
        headers: {
            'Authorization': Cookies.get('Authorization'),
            'Refresh-Token': Cookies.get('Refresh-Token')
        }
    })
    .then(async res => {
        checkTokenExpired(res)
        refreshToken(res)

        let alarms = await res.json();

        if (Array.isArray(alarms)) {
            for (let alarm of alarms) {
                if (alarm['id'] === aId) {
                    console.log('알림 읽음');
                    alert('알림 읽음');
                    break;
                }
            }
        }
    })
    .catch(error => {
        console.error('알림 읽기 실패 :', error);
    });
}

// function updateAlarmBadgeColor(alarms) {
//     const alarmBadge = document.getElementById('alarm-badge');
//     const isAnyUnread = alarms.some(alarm => !alarm.isRead);
//
//     if (isAnyUnread) {
//         alarmBadge.style.backgroundColor = '#37ff02'; // Fluorescent green
//     } else {
//         alarmBadge.style.backgroundColor = '#705c3b'; // Brown
//     }
// }

function formAlarm(alarm) {
    let aId = alarm['id']
    let title = alarm['title'];
    let content = alarm['content']
    let isRead = alarm['isRead'];

    let readClass = isRead ? 'read' : '';

    return `
       <li id="alarm-${aId}" class="${readClass}">
          <div>
            <div id="alarm-title">${title}</div>
            <div id="alarm-content">${content}</div>
          </div>
          <button id="read-alarm-btn-${aId}" class="alarm-panel-btn" onclick="readAlarm(${aId})">Read</button>
          <button id="delete-alarm-btn-${aId}" class="alarm-panel-btn" onclick="deleteAlarm(${aId})">Delete</button>
       </li>
        `
}