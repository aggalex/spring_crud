<template>
  <Transition class="transition" name="slide-in" mode="out-in">
    <div class="sidebar" v-if="state.show">
      <button class="menu-button fab-style centered-icon" @click="hide">
        <Icon>menu</Icon>
      </button>
      <UserMural/>
      <List>
        <ListItem v-if="info !== null" @click="login">Switch User</ListItem>
        <ListItem v-if="info !== null" @click="logout">Log Out</ListItem>

        <ListItem v-if="info === null" @click="login">Log In</ListItem>
        <ListItem v-if="info === null" @click="register">Register</ListItem>
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
import {computed, reactive} from "vue";
import Icon from "@/views/components/Icon.vue";
import {useUserStore} from "@/store/userStore";
import {UserInfoDto} from "@/api/user/dto/UserInfoDto";
import {useRouter} from "vue-router";

let router = useRouter()

let userStore = useUserStore();
let info = computed(() => userStore.info as UserInfoDto | null)

let state = reactive({
  show: true
})

function hide() {
  state.show = false;
}

function show() {
  state.show = true;
}

function login() {
  router.push("/login")
}

function register() {
  router.push("/register")
}

function logout() {
  userStore.logout()
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