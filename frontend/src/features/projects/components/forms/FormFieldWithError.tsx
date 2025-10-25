import { ReactNode } from 'react';

import { Field, FieldError, FieldLabel } from '@styles/components/ui/field';

// TODO: 이거 완성합시다

const FormFieldWithError = ({
  tanstackForm,
  name,
  label,
  children,
}: {
  tanstackForm: any;
  name: string;
  label?: string;
  type?: string;
  placeholder?: string;
  children: ReactNode;
}) => {
  return (
    <tanstackForm.Field
      name={name}
      children={(field: any) => {
        const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
        return (
          <Field>
            <FieldLabel htmlFor={field.name}>{label}</FieldLabel>
            {children}
            {isInvalid && <FieldError errors={field.state.meta.errors} />}
          </Field>
        );
      }}
    />
  );
};

export default FormFieldWithError;
