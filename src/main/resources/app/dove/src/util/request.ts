const BASE_URL = "http://localhost:8080";
const CSRF_COOKIE_NAME = "XSRF-TOKEN";

export interface Pagination {
    page: number,
    size: number
}

export const Pagination = {
    default: { size: 10 },
    to_url(pagination?: Pagination) {
        if (!pagination)
            return ""
        return `?page=${pagination.page}&size=${pagination.size}`
    }
}

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
            'X-XSRF-TOKEN': csrf
        }
    }
}

export function request<T>(
    info: string,
    init?: RequestInit & {
        validator?(t: any): t is T,
        pagination?: Pagination
    }
): Promise<T> {
    const validator = init?.validator || ((_) => true)

    let fetch_init: RequestInit = {
        ...init,
        credentials: "include",
        // @ts-ignore
        headers: {
            ...HEADERS.csrf(),
            ...init?.headers
        }
    };

    let url = BASE_URL + info + Pagination.to_url(init?.pagination)

    console.log("Requesting", url, fetch_init)

    return fetch(url, fetch_init)
    .then(res => !res.ok? res.text()
        .then(text => Promise.reject(
            new StatusError(res.status, res.statusText, text)
        )): res.json() as Promise<T>)
    .then(t => !validator(t)? Promise.reject(
        new TypeError("Validation of received object failed")
    ): t);
}