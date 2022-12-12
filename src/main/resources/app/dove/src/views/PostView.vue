<template>
  <View>
    <Searchbar/>
    <Post class="fill-view open" :post="post" dismissible/>
    <PostList :loader="comments"></PostList>
  </View>
</template>

<script setup>
import {useRoute} from "vue-router";
import Post from "@/views/layouts/Post.vue";
import {computed, onMounted} from "vue";
import PostApi from "../api/Post"
import PostList from "@/views/layouts/PostList.vue";
import Clamp from "@/views/components/containers/Clamp.vue";
import View from "@/views/components/containers/View.vue";
import Searchbar from "@/views/components/Searchbar.vue";

const route = useRoute()
const id = +route.params.id
const post = computed(() => PostApi.getById(id))

function comments() {
  return PostApi.getCommentsOf(id)
}

</script>

<style scoped>

</style>