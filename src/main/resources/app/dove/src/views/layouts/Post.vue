<template>
<Card
    v-if="!state.rejected"
    class="post"
    :class="[
        state.post? '': 'flattened template',
        props.clickable? 'clickable': ''
    ].join(' ')"
    @click="openPost"
>
  <div class="heading">
    <h1 class="title text">{{ state.post?.title }}</h1>
    <div class="horizontal-spacer"/>
    <button class="fab-style centered-icon"
            v-if="dismissible"
            @click="closePost">
      <Icon>close</Icon>
    </button>
  </div>
  <p class="contents text">
    {{ state.post?.body || "Title" }}
  </p>
  <div class="button-box small">
    <button>
      <Icon>thumb_up</Icon>
      {{ state.post?.likes || "Content" }}
    </button>
    <button>
      <Icon>chat</Icon>
      Comment
    </button>
    <div class="spacer"/>
    <span class="last">
        <Icon>visibility</Icon>
        {{ state.post?.views || "0" }}
      </span>
  </div>
  <slot/>
</Card>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive} from "vue";
import type { PostDto } from "@/api/Post";
import Card from "@/views/components/containers/Card.vue";
import Icon from "@/views/components/Icon.vue";
import PostList from "@/views/layouts/PostList.vue";
import PostApi from "@/api/Post";
import {useRouter} from "vue-router";

interface Props {
  post: Promise<PostDto>,
  clickable?: boolean,
  dismissible?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  clickable: false,
  dismissible: false
})

const router = useRouter()

const state = reactive({
  post: null as PostDto | null,
  rejected: false
})

function openPost() {
  if (!state.post || !props.clickable)
    return
  router.push(`/posts/${state.post.id}`)
}

function closePost() {
  router.back()
}

onMounted(() => props.post
    .then((post) => {
      state.post = post
    })
    .catch(_ => state.rejected = true)
)
</script>

<style scoped lang="scss">
@import "../../assets/scss/constants";

.heading {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: flex-end;
}

.post.clickable:not(.template) {
  cursor: pointer;
}

.title {
  font-size: 24px;
  margin: 0;
}

.button-box {
  display: flex;
  flex-direction: row;
  gap: 6px;

  span {
    padding: 6px;
  }

  .spacer {
    margin: auto;
  }
}

.contents {
  max-height: 4.6rem;
  text-overflow: ellipsis;
  overflow: hidden;
  transition: 300ms max-height ease-in-out;

  .template > & {
    max-height: 1rem;
  }
}
</style>