export const MOCK_CONFIG = {
    enabled: true,
    delay: 3000
};

export interface AsyncFunction {
    (...props: any[]): Promise<any>
}

export function api<T extends { [key: string]: AsyncFunction }>(web: T, mock: Partial<T>): T {
    if (!MOCK_CONFIG.enabled) {
        return web
    }


    let newMock: T = {} as T

    Object.keys(web).map(key => {
        const method: AsyncFunction | undefined | null = mock[key];
        newMock = {
            ...newMock,
            async [key](...props: unknown[]) {
                return new Promise((resolve, reject) => setTimeout(() => {
                    if (method)
                        method(...props)
                            .then(data => resolve(data))
                            .catch(err => reject(err))
                    else reject("Mock object was not set up")
                },
                MOCK_CONFIG.delay))
            }
        }
    })

    return newMock
}