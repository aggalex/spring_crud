<template>
  <View>
    <div class="list">
      <h1>Register</h1>
      <input placeholder="username" v-model="state.username"/>
      <input placeholder="email" v-model="state.email" type="email"/>
      <input placeholder="password" v-model="state.password" type="password">
      <UserViewButton @click="register" :loading="state.loading">Register</UserViewButton>
      <h4 class="error" v-if="state.error">{{ state.error }}</h4>
    </div>
  </View>
</template>

<script setup lang="ts">
import {reactive} from "vue";
import View from "@/views/components/containers/View.vue";
import {useUserStore} from "@/store/userStore";
import {useRouter} from "vue-router";
import UserViewButton from "@/views/components/UserViewButton.vue";
import userApi from "@/api/user/api";

const state = reactive({
  username: "",
  email: "",
  password: "",
  loading: false,
  error: null
});

const router = useRouter()

function register() {
  state.loading = true
  userApi.register({
    username: state.username,
    email: state.email,
    password: state.password
  })
      .then(_ => router.push("/login"))
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