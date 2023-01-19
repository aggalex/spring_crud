import type {UserLoginDto} from "@/api/user/dto/UserLoginDto";
import {Validate, String} from "@/util/Checks";
import {assertUserLoginDto} from "@/api/user/dto/UserLoginDto";
export interface UserRegisterDto extends UserLoginDto {
    username: string
}

export function assertUserRegisterDto(v: any): v is UserLoginDto {
    return assertUserLoginDto(v)
    && new Validate({
        username: String.max(255)
    }).check(v)
}