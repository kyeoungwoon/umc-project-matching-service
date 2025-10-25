import AuthStore from '@features/auth/stores/auth-store';

export const useGetUser = () =>
  AuthStore((state) => {
    return state.user;
  });

export const useSetUser = () => {
  return AuthStore((state) => state.actions.setUser);
};

export const useSetUserInfo = () => {
  return AuthStore((state) => state.actions.setUserInfo);
};

export const useClearUser = () => AuthStore((state) => state.actions.clearUser);
