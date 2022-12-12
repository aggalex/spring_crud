<template>
  <Transition class="transition" name="slide-in" mode="out-in">
    <div class="sidebar" v-if="state.show">
      <button class="menu-button fab-style centered-icon" @click="hide">
        <Icon>menu</Icon>
      </button>
      <UserMural/>
      <List>
        <ListItem>Item 1</ListItem>
        <ListItem>Item 2</ListItem>
      </List>

    </div>
    <div class="sidebar-closed" v-else></div>
  </Transition>
  <button class="centered-icon top-left fab-style" @click="show">
    <Icon>menu</Icon>
  </button>
</template>

<script setup lang="ts">
import ListItem from "@/views/components/Sidebar/ListItem.vue";
import List from "@/views/components/Sidebar/List.vue";
import UserMural from "@/views/components/Sidebar/UserMural.vue";
import {reactive} from "vue";
import Icon from "@/views/components/Icon.vue";

let state = reactive({
  show: true
})

function hide() {
  state.show = false;
}

function show() {
  state.show = true;
}

</script>

<style scoped lang="scss">
@import "@/assets/scss/constants.scss";
.menu-button {
  position: absolute;
  top: 0;
  //left: 0;
  z-index: 1;
}

.sidebar {
  height: 100vh;
  background: $background-special;
  overflow: scroll;
  width: 15rem;
  z-index: 300;

  @media (max-width: 50rem) {
    width: 100vw;
  }
}

.top-left {
  z-index: 299;
  position: fixed;

  @media (max-width: 50rem) {
    margin-top: 1.5rem;
    margin-left: 1.5rem;
  }
}
</style>