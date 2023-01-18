export interface PostDto {
    id: number
    title: string
    body: string
    poster: number
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