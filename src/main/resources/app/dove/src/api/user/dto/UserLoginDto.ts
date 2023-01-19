import {checks, Validate, String} from "@/util/Checks";

export interface UserLoginDto {
    password: string
    email: string
}

export function assertUserLoginDto (v: any): v is UserLoginDto {
    return new Validate({
        password: checks(
            String.min(8),
            String.max(500)
        ),
        email: String.max(255)
    }).check(v)
}