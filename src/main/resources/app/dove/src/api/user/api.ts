import {api} from "@/util/api";
import type {UserLoginDto} from "@/api/user/dto/UserLoginDto";
import type {UserInfoDto} from "@/api/user/dto/UserInfoDto";
import {HEADERS, request} from "@/util/request";
import type {UserRegisterDto} from "@/api/user/dto/UserRegisterDto";
import mock from "@/api/user/mock";

const userApi = {
    async login (dto: UserLoginDto): Promise<UserInfoDto> {
        return request("/api/user/login", {
            method: 'POST'
        })
    },
    async logout () {
        return request("/api/user/logout", {
            method: 'POST'
        })
    },
    async register (dto: UserRegisterDto) {
        return request("/api/user/register", {
            method: 'POST',
            headers: {
                ...HEADERS.application.json
            },
            body: JSON.stringify(dto)
        })
    },
}

export default api(
    userApi,
    mock
)