import {assertCreatePostDto} from "@/api/post/dto/CreatePostDto";

export interface UpdatePostDto {
    id: number,
    title?: string,
    body?: string
}

export function assertUpdatePostDto (v: any): v is UpdatePostDto {
    return assertCreatePostDto(v)
}

