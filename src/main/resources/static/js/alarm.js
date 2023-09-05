/* alarm */
// function getAlarms() {
//
//     $('#alarm_list').empty();
//
//     fetch('/api/alarms', {
//         method: 'GET',
//         headers: {
//             'Authorization': Cookies.get('Authorization'),
//             'Refresh-Token': Cookies.get('Refresh-Token')
//         }
//     })
//
//     .then(async res => {
//         checkTokenExpired(res)
//         refreshToken(res)
//
//         let alarms = await res.json()
//
//         for (let alarm of alarms['alarms']) {
//             let aId = alarm['alarmId']
//             $('#alarm-list')
//         }
//     }) {
//
//     }
// }
