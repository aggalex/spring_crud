export const MOCK_CONFIG = {
    enabled: true,
    delay: 3000
};

const BASE_URL = "";

export function request<T>(
    info: string,
    init?: RequestInit & { mock?(): T }
): Promise<T> {
    if (MOCK_CONFIG.enabled) {
        return new Promise((resolve, reject) => {
            setTimeout(() => {
                if (init?.mock)
                    resolve(init.mock())
                else
                    reject("Mock object was not set up")
            },
                MOCK_CONFIG.delay)
        })
    }

    throw new Error("Not implemented");
}