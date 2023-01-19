import {HEADERS, Pagination, request} from "@/util/request";
import type {CreatePostDto} from "@/api/post/dto/CreatePostDto";
import type {PostDto} from "@/api/post/dto/PostDto";
import {api} from "@/util/api";
import type {UpdatePostDto} from "@/api/post/dto/UpdatePostDto";
import mock from "@/api/post/mock";

const postApi = {
    async get(pagination?: Pagination): Promise<PostDto[]> {
        return request("/api/post", {
            method: 'GET',
            pagination,
            headers: {
                ...HEADERS.application.json
            },
        })
            .then((posts) => {
                console.log(posts)
                return posts as PostDto[]
            })
    },

    async like(id: number): Promise<PostDto> {
        return request("/api/post/likes", {
            method: 'POST',
        })
    },

    async getById(id: number): Promise<PostDto> {
        return request(`/api/post/${id}`, {
            method: 'GET',
            headers: {
                ...HEADERS.application.json
            }
        })
    },

    async getCommentsOf(id: number, pagination?: Pagination): Promise<PostDto[]> {
        return request(`/api/post/${id}/comments`, {
            method: 'GET',
            pagination,
            headers: {
                ...HEADERS.application.json
            }
        })
    },

    async post(dto: CreatePostDto): Promise<PostDto> {
        return request('/api/post', {
            method: 'POST',
            headers: {
                ...HEADERS.application.json
            },
            body: JSON.stringify(dto),
        })
    },

    async update(dto: UpdatePostDto): Promise<PostDto> {
        return request('/api/post', {
            method: 'POST',
            headers: {
                ...HEADERS.application.json
            },
            body: JSON.stringify(dto)
        })
    },

    async delete(id: number) {
        return request('/api/post/' + id, {
            method: 'DELETE'
        })
    }
}

export default api(
    postApi,
    mock
)