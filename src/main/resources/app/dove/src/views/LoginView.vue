<template>
  <View>
    <div class="list">
      <h1>Login</h1>
      <input placeholder="email" v-model="state.email" type="email"/>
      <input placeholder="password" v-model="state.password" type="password">
      <UserViewButton @click="login" :loading="state.loading">Login</UserViewButton>
      <h4 class="error" v-if="state.error">{{ state.error }}</h4>
    </div>
  </View>
</template>

<script setup lang="ts">
import type {UserLoginDto} from "@/api/user/dto/UserLoginDto";
import {reactive} from "vue";
import View from "@/views/components/containers/View.vue";
import {useUserStore} from "@/store/userStore";
import {useRouter} from "vue-router";
import { FlowerSpinner, HollowDotsSpinner, RadarSpinner } from 'epic-spinners';
import UserViewButton from "@/views/components/UserViewButton.vue";

const state = reactive({
  email: "",
  password: "",
  loading: false,
  error: null
});

const store = useUserStore()
const router = useRouter()

function login() {
  state.loading = true
  store.login({
    email: state.email,
    password: state.password
  })
      .then(_ => router.push("/"))
      .catch(err => {
        state.loading = false;
        state.error = err;
      })
}

</script>

<style scoped lang="scss">
.list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.error {
  background-color: #6b3131;
  border-radius: .2rem;
  padding: 1rem;
}
</style>