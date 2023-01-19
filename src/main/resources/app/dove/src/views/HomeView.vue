<template>
  <View class="content" keep-scroll>
    <Searchbar/>
    <PostList :baseCreatePostDto="null"/>
    <div class="fab-holder">
      <button class="fab">
        <Icon>add</Icon>
      </button>
    </div>
  </View>
</template>

<script setup lang="ts">
import Card from "@/views/components/containers/Card.vue";
import Searchbar from "@/views/components/Searchbar.vue";
import Post from "@/views/layouts/Post.vue";
import Icon from "@/views/components/Icon.vue";
import PostList from "@/views/layouts/PostList.vue";
import Clamp from "@/views/components/containers/Clamp.vue";
import View from "@/views/components/containers/View.vue";
import {onActivated, onDeactivated, reactive} from "vue";
import Editor from "@/views/layouts/Editor.vue";
import PostApi from "@/api/post/api";
import type {CreatePostDto} from "@/api/post/dto/CreatePostDto";
</script>

<style scoped lang="scss">
@import "../assets/scss/constants";


</style>