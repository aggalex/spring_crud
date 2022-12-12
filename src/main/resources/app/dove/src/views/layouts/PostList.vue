<template>
  <Transition v-for="(post, index) in state.posts" name="fade">
    <Post
          v-if="state.foreground === null || state.foreground === index"
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
import type {PostDto} from "@/api/Post";
import PostApi, {CONFIG} from "@/api/Post";

interface Props {
  loader?(): Promise<PostDto[]>
}

const props = withDefaults(defineProps<Props>(), {
  loader: PostApi.get
})

const state = reactive({
  posts: load(),
  foreground: null as number | null,
  boundary: null
})

function load(): Promise<PostDto>[] {
  let promise = props.loader()
  return new Array(CONFIG.pageLength)
      .fill(null)
      .map((_, index) =>
        promise.then(async data => {
          if (data.length > index)
            return data[index]
          else throw new Error("No post")
        })
      )
}

function foreground(index: number, open: boolean) {
  if (open) {
    state.foreground = index
  } else {
    state.foreground = null
  }
}

onActivated(() => console.log("Activated"));
onDeactivated(() => console.log("Deactivated"));

</script>