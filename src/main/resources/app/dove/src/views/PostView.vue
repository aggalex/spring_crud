<template>
  <View>
    <Searchbar/>
    <Post class="fill-view open" :post="post" dismissible></Post>
    <PostList :loader="comments" :base-crate-post-dto="{ parent: id }"></PostList>
  </View>
</template>

<script setup>
import {useRoute} from "vue-router";
import Post from "@/views/layouts/Post.vue";
import {computed, onMounted, reactive} from "vue";
import PostApi from "../api/post/api"
import PostList from "@/views/layouts/PostList.vue";
import Clamp from "@/views/components/containers/Clamp.vue";
import View from "@/views/components/containers/View.vue";
import Searchbar from "@/views/components/Searchbar.vue";
import Icon from "@/views/components/Icon.vue";
import Editor from "@/views/layouts/Editor.vue";

const route = useRoute()
const id = +route.params.id
const post = computed(() => {
  return PostApi.getById(id)
})

const state = reactive({
  loaded: false
})

function onSubmit({title, body}) {
  PostApi.post({
    title,
    body,
    parent: id
  })
}

function comments() {
  console.log("Getting Comments")
  return PostApi.getCommentsOf(id)
}

onMounted(() => post.value.then((_) => state.loaded = true))
</script>

<style scoped>

</style>