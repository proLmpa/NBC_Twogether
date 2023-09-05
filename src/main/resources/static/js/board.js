const BASE_URL = 'http://localhost:8080'

// html 로딩 시 바로 실행되는 로직
$(document).ready(function () {
    let auth = Cookies.get('Authorization') ? Cookies.get('Authorization') : ''
    let refresh = Cookies.get('Refresh-Token') ? Cookies.get('Refresh-Token') : ''

    // access 토큰과 refresh 토큰이 모두 존재하지 않을 때 -- 로그아웃
    if (auth === '' && refresh === '') {
        window.location.href = BASE_URL + '/views/login'
    }

    // 본인 정보 불러오기
    getUserInfo()
})


// fetch API 로직
async function getUserInfo() {
    // when
    await fetch('/api/users/info', {
        method: 'GET',
        headers: {
            'Authorization': Cookies.get('Authorization'),
            'Refresh-Token': Cookies.get('Refresh-Token')
        }
    })

    // then
    .then(async res => {
        checkTokenExpired(res)
        refreshToken(res)

        let user = await res.json()
        $('#nickname').text(user['nickname'])
        $('#email').text(user.email)
        $('#role').text(user.role)
    })
}


// 순수 javascript 동작
function logout() {
    resetToken()
    window.location.href = BASE_URL + '/views/login'
}

// token 관련 재생성, 삭제, 만료 로직
function refreshToken(response) {
    let token = response.headers.get('Authorization')
    if (token !== null) {
        resetToken()
        Cookies.set('Authorization', token, {path: '/'})
        Cookies.set('Refresh-Token', response.headers.get('Refresh-Token'),
            {path: '/'})
    }
}

function resetToken() {
    Cookies.remove('Authorization', {path: '/'})
    Cookies.remove('Refresh-Token', {path: '/'})
}

function checkTokenExpired(res) {
    if (res.status === 412) {
        alert('토큰이 만료되었습니다. 다시 로그인해주세요!')
        resetToken()
        window.location.href = BASE_URL + '/views/login'
    }
}