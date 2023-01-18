<template>
  <Editor :class="(state.loaded || state.failed)? '': 'flattened template'" @submit="onSubmit" />
  <Transition v-if="!state.failed" v-for="(post, index) in state.userPosts" name="fade">
    <Post
        :post="post"
        :key="index"
        @open="(open) => foreground(index, open)"
        clickable
    />
  </Transition>
  <Transition v-if="!state.failed" v-for="(post, index) in state.posts" name="fade">
    <Post
          :post="post"
          :key="index"
          @open="(open) => foreground(index, open)"
          clickable
    />
  </Transition>
</template>

<script setup lang="ts">
import Post from "@/views/layouts/Post.vue";
import {onActivated, onDeactivated, onMounted, reactive} from "vue";
import PostApi from "@/api/post/api";
import type {PostDto} from "@/api/post/dto/PostDto";
import {CONFIG} from "@/api/post/config";
import Editor from "@/views/layouts/Editor.vue";
import type {CreatePostDto} from "@/api/post/dto/CreatePostDto";

interface Props {
  loader?(): Promise<PostDto[]>,
  baseCratePostDto: Partial<CreatePostDto> | null
}

const props = withDefaults(defineProps<Props>(), {
  baseCratePostDto: null,
  loader: PostApi.get,
})

interface State {
  promise: Promise<PostDto[]>,
  failed: Boolean,
  loaded: Boolean,
  posts: Promise<PostDto>[],
  userPosts: Promise<PostDto>[]
}

function onSubmit(dto: CreatePostDto) {
  let post = PostApi.post({
    ...props.baseCratePostDto || {},
    ...dto,
  });

  state.userPosts = [
      ...state.userPosts,
      post
  ]
}

const state = reactive(load())

function load(): State {
  console.log("Loading")
  let promise = props.loader()
  promise.catch(_ => state.failed = true)
  promise.then(_ => state.loaded = true)
  return {
    promise,
    failed: false,
    loaded: false,
    userPosts: [],
    posts: new Array(CONFIG.pageLength)
        .fill(null)
        .map((_, index) =>
            promise.then(async data => {
              if (data.length > index)
                return data[index]
              else throw new Error("No post")
            })
        )
  }
}

onActivated(() => console.log("Activated"));
onDeactivated(() => console.log("Deactivated"));

</script>