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
    <button class="fab-style centered-icon non-templatable"
            v-if="dismissible"
            @click="closePost">
      <Icon>close</Icon>
    </button>
  </div>
  <span class="poster-section hidden-template">
    <button class="poster-button">
      <Icon>
        account_circle
      </Icon>
      Poster
    </button>
    <hr class="non-templatable">
  </span>
  <p class="contents text">
    {{ state.post?.body || "Title" }}
  </p>
  <div class="button-box small">
    <button>
      <Icon>thumb_up</Icon>
      {{ state.post?.likes || "Content" }}
    </button>
    <slot/>
    <div class="spacer"/>
    <span class="last">
        <Icon>visibility</Icon>
        {{ state.post?.views || "0" }}
      </span>
  </div>
</Card>
</template>

<script setup lang="ts">
import {computed, onMounted, reactive} from "vue";
import Card from "@/views/components/containers/Card.vue";
import Icon from "@/views/components/Icon.vue";
import PostList from "@/views/layouts/PostList.vue";
import PostApi from "@/api/post/api";
import {useRouter} from "vue-router";
import type {PostDto} from "@/api/post/dto/PostDto";

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
  gap: 1rem;
  align-items: flex-start;

  .fab-style {
    flex-shrink: 0;
  }
}

.post.clickable:not(.template) {
  cursor: pointer;
}

.title {
  font-size: 1.5rem;
  margin: 0;
}

.button-box {
  display: flex;
  flex-direction: row;
  gap: .5rem;

  span {
    padding: .5rem;
  }

  .spacer {
    margin: auto;
  }
}

.poster-section {
  display: flex;
  flex-direction: row;
  align-items: center;
  margin-top: .5rem;

  hr {
    width: 100%;
    height: 0;
    margin-left: .5rem;
  }

  .poster-button {
    display: flex;
    flex-direction: row;
    gap: .5rem;
    align-items: center;

    * {
      font-size: 1rem;
    }
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