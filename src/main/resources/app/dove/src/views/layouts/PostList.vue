<template>
  <Transition v-for="(post, index) in state.posts" name="fade">
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

onActivated(() => console.log("Activated"));
onDeactivated(() => console.log("Deactivated"));

</script>