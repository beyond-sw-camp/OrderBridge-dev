import salert from "sweetalert2";

/*
    icon의 종류
    warning, error, success, info, question
*/

async function sAlert(title, text, icon) {
    return await salert.fire({
        title: title,
        text: text,
        icon: icon
    });
}

async function sSuccess(message) {
    return await salert.fire({
        text: message,
        icon: `success`
    })
}

async function sError(message) {
    return await salert.fire({
        text: message,
        icon: `error`
    });
}

async function sServerError(error) {
    return await salert.fire({
        text: error.response.data.message || `오류가 발생했습니다.`,
        icon: `error`
    });
}

async function sWarning(message) {
    return await salert.fire({
        text: message,
        icon: `warning`
    });
}

async function sInfo(message) {
    return await salert.fire({
        text: message,
        icon: `info`
    });
}

async function sConfirm(message) {
    return await salert.fire({
        title: message,
        showDenyButton: true,
        confirmButtonText: `확인`,
        denyButtonText: `취소`,
        icon: `question`
    }).then((result) => {
        if (result.isConfirmed) { return true; }
        else if (result.isDenied) { return false; }
    });
}

export { sAlert, sSuccess, sError, sServerError, sWarning, sInfo, sConfirm }
