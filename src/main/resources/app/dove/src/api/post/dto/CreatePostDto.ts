import {String, Validate} from "../../../util/Checks"
import {checks} from "../../../util/Checks";
import {HEADERS, request} from "@/util/request";
import type {PostDto} from "@/api/post/dto/PostDto";

export interface CreatePostDto {
    title: string,
    body: string,
    parent?: number
}

export function assertCreatePostDto(v: any): v is CreatePostDto {
    return new Validate({
        title: String.max(255),
        body: String.max(10_000)
    }).check(v)
}

