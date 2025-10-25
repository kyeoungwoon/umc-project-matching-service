import { create } from 'zustand';
import { devtools, persist } from 'zustand/middleware';
import { immer } from 'zustand/middleware/immer';

import { Challenger } from '@api/types/challenger';

type UserInfo = Challenger;

// TODO: BE 구현 후에 Optional 전부 제거하기
export interface User {
  accessToken: string;
  info: UserInfo;
}

interface AuthStoreState {
  user: User | null;
  actions: {
    setUser: (newUser: User) => void;
    setUserInfo: (userInfo: UserInfo) => void;
    clearUser: () => void;
  };
}

const AuthStore = create<AuthStoreState>()(
  devtools(
    persist(
      immer((set) => ({
        user: null,
        actions: {
          setUser: (newUser: User) =>
            set((state) => {
              state.user = { ...state.user, ...newUser };
              // 상태가 null인 경우에도 새로운 유저 정보를 설정할 수 있도록 함
            }),
          clearUser: () => {
            set((state) => {
              state.user = null;
            });
          },
          setUserInfo: (userInfo: UserInfo) =>
            set((state) => {
              if (state.user) {
                state.user.info = userInfo;
              }
            }),
        },
      })),
      {
        name: 'auth-store',
        partialize: (state) => ({
          user: state.user,
        }),
      },
    ),
  ),
);

export default AuthStore;
