interface Predicate<T> {
    (val: T): boolean
}

interface Check<T> {
    (...args: any[]): Predicate<T>
}

interface CheckNamespace<T> {
    [key: string]: Check<T>
}

export function checks<T>(...checks: Predicate<T>[]): Predicate<T> {
    return checks.reduce((acc, next) => (val: T) => acc(val) && next(val))
}

export const String: CheckNamespace<string> = {
    is: () => (val) => typeof val === "string",
    max: (length: number) => (val) => val.length <= length,
    min: (length: number) => (val) => val.length <= length,
}

export class Validate<T extends { [key: string]: Predicate<any> }> {
    constructor(private checks: T) {

    }

    check(v: any): boolean {
        return Object.keys(this.checks)
            .reduce((ok, key) => ok &&
                this.checks[key](v?.[key]),
                true)
    }
}