import type {UserRegisterDto} from "@/api/user/dto/UserRegisterDto";
import type {UserLoginDto} from "@/api/user/dto/UserLoginDto";
import type {UserInfoDto} from "@/api/user/dto/UserInfoDto";

const repository: UserRegisterDto[] = []
let loggedIn: UserRegisterDto | null = null;

export default {
    async login(dto: UserLoginDto): Promise<UserInfoDto> {
        let [user = null] = repository
            .filter(user =>
                user.email === dto.email
            &&  user.password === dto.password)

        if (user === null) {
            return Promise.reject("Wrong email or password")
        }

        loggedIn = user;
        return user;
    },

    async logout() {
        loggedIn = null;
    },

    async register(dto: UserRegisterDto) {
        repository.push(dto)
    }
}