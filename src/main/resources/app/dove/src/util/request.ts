const BASE_URL = "/";
const CSRF_COOKIE_NAME = "";

export class StatusError extends Error {
    constructor(
        public status: number,

        public statusText: string,
        public response: string
    ) {
        super(`${status} ${statusText}: ${response}`);
    }
}

function getCsrfCookie(name: string) {
    if (!document.cookie) {
        return null;
    }

    const xsrfCookies = document.cookie.split(';')
        .map(c => c.trim())
        .filter(c => c.startsWith(name + '='));

    if (xsrfCookies.length === 0) {
        return null;
    }
    return decodeURIComponent(xsrfCookies[0].split('=')[1]);
}

export const HEADERS = {
    application: {
        json: {
            'content-type': 'application/json;charset=UTF-8'
        }
    },
    csrf(name: string = CSRF_COOKIE_NAME) {
        const csrf = getCsrfCookie(CSRF_COOKIE_NAME)

        if (csrf === null) {
            return {}
        }

        return {
            'X-CSRF-TOKEN': csrf
        }
    }
}

export function request<T>(
    info: string,
    init?: RequestInit & { validator?(t: any): t is T }
): Promise<T> {
    const validator = init?.validator || ((_) => true)
    return fetch(BASE_URL + info, {
        ...init,
        headers: {
            ...HEADERS.csrf,
            ...init?.headers
        }
    })
    .then(res => !res.ok? res.text()
        .then(text => Promise.reject(
            new StatusError(res.status, res.statusText, text)
        )): res.json() as Promise<T>)
    .then(t => !validator(t)? Promise.reject(
        new TypeError("Validation of received object failed")
    ): t);
}