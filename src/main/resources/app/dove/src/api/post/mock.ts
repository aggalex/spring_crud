import type {PostDto} from "@/api/post/dto/PostDto";
import type {CreatePostDto} from "@/api/post/dto/CreatePostDto";
import {faker} from "@faker-js/faker";
import {CONFIG} from "@/api/post/config";
import type {Pagination} from "@/util/request";


const repository: PostDto[] = new Array(CONFIG.pageLength)
    .fill(null)
    .map((_, index, array) => ({
        id: index,
        title: faker.hacker.phrase(),
        body: faker.lorem.paragraphs(5),
        poster: Math.floor(Math.random() * 100),
        parent: Math.ceil(Math.random() * 10),
        likes: Math.ceil(Math.random() * 10_000),
        dislikes: Math.ceil(Math.random() * 100),
        views: Math.ceil(Math.random() * 100_000),
        deleted: false
    }))
    .map((item, _index, array) => ({
        ...item,
        parent: array[item.parent] || null
    }))

export default {
    async get(pagination?: Pagination) {
        return repository
    },

    async getById(id: number): Promise<PostDto> {
        let posts = repository.filter(post => post.id === id)
        if (posts.length === 0)
            return Promise.reject("No post found")
        else
            return Promise.resolve(posts[0])
    },

    async getCommentsOf(id: number, pageable?: Pagination): Promise<PostDto[]> {
        let posts = repository.filter(post => post.parent?.id === id)
        if (posts.length === 0)
            return Promise.reject("No comments found")
        else
            return Promise.resolve(posts)
    },

    async post(dto: CreatePostDto): Promise<PostDto> {
        const parentList = repository
            .filter(p => p.id === dto.parent);

        const parent = parentList.length > 0?
            parentList[0]:
            null;

        const post = {
            ...dto,
            deleted: false,
            id: repository.length,
            parent: parent,
            dislikes: 0,
            likes: 0,
            poster: 0,
            views: 0
        }

        repository.push(post)
        return post
    }
}