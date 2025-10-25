'use client';

import { useRouter } from 'next/navigation';

import { useForm } from '@tanstack/react-form';
import { toast } from 'sonner';
import { z } from 'zod';

import { Button } from '@styles/components/ui/button';
import { Field, FieldError, FieldGroup, FieldLabel, FieldTitle } from '@styles/components/ui/field';

import { useLogin } from '@api/tanstack/challengers.queries';

import { ROUTES } from '@common/constants/routes.constants';

import { useSetUser } from '@features/auth/hooks/useAuthStore';

import SchoolComboBox from '@features/auth/components/SchoolComboBox';
import InputFormField from '@features/projects/components/forms/InputFormField';

const loginSchema = z.object({
  school: z.object({
    name: z.string().min(1, '학교를 선택하세요'),
    id: z.string().min(1, '학교를 선택하세요'),
  }),
  studentId: z.string().min(1, '학번을 입력하세요'),
  password: z.string().min(1, '비밀번호를 입력하세요'),
});

const LoginForm = () => {
  const { mutate: login, isPending, isError, error } = useLogin();
  const router = useRouter();
  const setUser = useSetUser();

  const form = useForm({
    defaultValues: {
      school: {
        id: '',
        name: '',
      },
      studentId: '',
      password: '',
    },
    onSubmit: ({ value }) => {
      login(
        {
          studentId: value.studentId,
          schoolId: value.school.id,
          gisu: '9', // 9기 고정
          password: value.password,
        },
        {
          onSuccess: (data) => {
            if (!data) throw new Error('사용자 정보가 없습니다.');

            setUser({
              accessToken: data.accessToken,
              info: data.challengerInfo,
            });

            router.push(ROUTES.HOME);
            // 성공 시 처리 (예: 페이지 이동)
          },
          onError: (error) => {
            // console.error('로그인 실패:', error);
            // 에러 처리
            toast.error('로그인에 실패했습니다. 다시 시도해주세요.', {
              description: error.message,
            });
          },
        },
      );
    },
    validators: {
      onSubmit: loginSchema,
      onChange: loginSchema,
      // onChange, onBlur 제거 - 자동완성 시 문제 방지
    },
  });

  return (
    <div>
      <form
        onSubmit={async (e) => {
          e.preventDefault();
          e.stopPropagation();
          await form.handleSubmit();
        }}
        className="min-w-md"
      >
        <FieldGroup>
          {/* ...existing code... */}
          <FieldTitle className={'text-2xl'}>UPMS</FieldTitle>
          <form.Field
            name={'school'}
            children={(field) => {
              return (
                <Field className={'w-full'}>
                  <FieldLabel htmlFor={field.name}>학교</FieldLabel>
                  <SchoolComboBox value={field.state.value} onValueChange={field.handleChange} />
                  {field.state.meta.isTouched && !field.state.value.id && (
                    <FieldError errors={[{ message: '학교를 선택해 주세요.' }]} />
                  )}
                </Field>
              );
            }}
          />
          <InputFormField
            tanstackForm={form}
            name="studentId"
            label="학번"
            placeholder="학번을 입력해주세요."
          />
          <InputFormField
            tanstackForm={form}
            name="password"
            label="비밀번호"
            type="password"
            placeholder="비밀번호를 입력해주세요."
          />
          {isError && (
            <div className="text-sm text-red-500">
              로그인에 실패했습니다. {error?.message || '다시 시도해주세요.'}
            </div>
          )}
          <Button type="submit" disabled={isPending}>
            {isPending ? '로그인 중...' : '로그인하기'}
          </Button>
        </FieldGroup>
      </form>
    </div>
  );
};

export default LoginForm;
