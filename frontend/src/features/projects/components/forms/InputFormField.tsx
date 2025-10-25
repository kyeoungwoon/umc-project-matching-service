import { clsx } from 'clsx';

import { Field, FieldError, FieldLabel } from '@styles/components/ui/field';
import { Input } from '@styles/components/ui/input';

const InputFormField = ({
  tanstackForm,
  name,
  label,
  type = 'text',
  placeholder,
}: {
  tanstackForm: any;
  name: string;
  label?: string;
  type?: string;
  placeholder?: string;
}) => {
  return (
    <tanstackForm.Field
      name={name}
      children={(field: any) => {
        const isInvalid = field.state.meta.isTouched && !field.state.meta.isValid;
        return (
          <Field>
            <FieldLabel htmlFor={field.name}>{label}</FieldLabel>
            <Input
              type={type}
              id={field.name}
              name={field.name}
              value={field.state.value}
              onBlur={field.handleBlur}
              onChange={(e) => field.handleChange(e.target.value)}
              placeholder={placeholder}
              className={clsx(isInvalid && 'border-red-500')}
            />
            {isInvalid && <FieldError errors={field.state.meta.errors} />}
          </Field>
        );
      }}
    />
  );
};

export default InputFormField;
