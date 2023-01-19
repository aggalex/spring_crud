export interface PostDto {
    id: number
    title: string
    body: string
    poster: {
        id: number,
        username: number
    }
    parent?: {
        id: number,
        title: string,
        body: string
    } | null
    likes: number
    dislikes: number
    views: number
    deleted: boolean
}