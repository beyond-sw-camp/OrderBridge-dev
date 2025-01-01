import salert from "sweetalert2";

/*
    icon의 종류
    warning, error, success, info, question
*/

async function sAlert(title, text, icon) {
    salert.fire({
        title: title,
        text: text,
        icon: icon
    });
}

async function sSuccess(message) {
    salert.fire({
        title: `성공!`,
        text: message,
        icon: `success`
    })
}

async function sError(error) {
    salert.fire({
        title: error.response.data.httpStatus || `Error`,
        text: error.response.data.message || `오류가 발생했습니다.`,
        icon: `error`
    });
}

async function sConfirm(message) {
    salert.fire({
        title: message,
        showDenyButton: true,
        confirmButtonText: `수락`,
        denyButtonText: `거절`,
        icon: `question`
    }).then((result) => {
        if (result.isConfirmed) { return true }
        else if (result.isDenied) { return false }
    });
}

export { sAlert, sSuccess, sError, sConfirm }
