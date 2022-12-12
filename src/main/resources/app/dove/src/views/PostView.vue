<template>
  <View>
    <Searchbar/>
    <Post class="fill-view open" :post="post" dismissible>
      <PostList :loader="() => PostApi.getCommentsOf(id)"></PostList>
    </Post>
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

let route = useRoute()
let id = +route.params.id
let post = computed(() => PostApi.getById(id))

</script>

<style scoped>

</style>