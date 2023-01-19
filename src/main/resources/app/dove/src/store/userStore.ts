import {defineStore} from "pinia";
import type {UserInfoDto} from "@/api/user/dto/UserInfoDto";
import type {UserLoginDto} from "@/api/user/dto/UserLoginDto";
import userApi from "@/api/user/api";

export const useUserStore = defineStore('userData', {
    state: () => ({
        info: null as UserInfoDto | null
    }),
    actions: {
        async init() {
            this.info = await userApi.info()
        },
        login(data: UserLoginDto): Promise<UserInfoDto> {
            let promise = userApi.login(data)
            promise.then(data => this.info = data)
            return promise
        },
        logout() {
            if (this.info === null) {
                return
            }

            userApi.logout().then(() => this.info = null)
        },
    }
})