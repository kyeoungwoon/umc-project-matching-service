'use client';

import { useState } from 'react';

import { clsx } from 'clsx';
import { CheckIcon, ChevronsUpDownIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import {
  Command,
  CommandEmpty,
  CommandGroup,
  CommandInput,
  CommandItem,
  CommandList,
} from '@styles/components/ui/command';
import { Popover, PopoverContent, PopoverTrigger } from '@styles/components/ui/popover';
import { Skeleton } from '@styles/components/ui/skeleton';

import { useGetSchools } from '@api/tanstack/umc.queries';
import { School } from '@api/types/umc';

export type PartialSchool = Pick<School, 'id' | 'name'>;

interface SchoolComboBoxProps {
  value: PartialSchool;
  onValueChange: (value: PartialSchool) => void;
}

const SchoolComboBox = ({ value, onValueChange }: SchoolComboBoxProps) => {
  const [open, setOpen] = useState(false);

  const { data: schools, isLoading } = useGetSchools();

  if (isLoading || !schools) {
    return <Skeleton className={'h-5 w-full'} />;
  }

  return (
    <Popover open={open} onOpenChange={setOpen}>
      <PopoverTrigger asChild>
        <Button
          variant="outline"
          role="combobox"
          aria-expanded={open}
          className="w-full justify-between"
          type="button"
        >
          {value.name ? value.name : '학교를 선택하세요.'}
          <ChevronsUpDownIcon className="ml-2 h-4 w-4 shrink-0 opacity-50" />
        </Button>
      </PopoverTrigger>
      <PopoverContent className="w-[var(--radix-popover-trigger-width)] p-0">
        <Command>
          <CommandInput placeholder="학교명 검색" />
          <CommandList>
            <CommandEmpty>해당하는 학교가 존재하지 않습니다.</CommandEmpty>
            <CommandGroup>
              {schools
                .sort((a, b) => a.name.localeCompare(b.name, 'ko'))
                .map((school) => (
                  <CommandItem
                    key={school.id}
                    value={school.name}
                    onSelect={() => {
                      // 선택된 학교를 다시 선택하면, 빈 값으로 변경
                      if (value.id === school.id) {
                        onValueChange({ id: '', name: '' });
                      } else {
                        onValueChange(school);
                      }
                      setOpen(false);
                    }}
                  >
                    <CheckIcon
                      className={clsx(
                        'mr-2 h-4 w-4',
                        value.id === school.id ? 'opacity-100' : 'opacity-0',
                      )}
                    />
                    {school.name}
                  </CommandItem>
                ))}
            </CommandGroup>
          </CommandList>
        </Command>
      </PopoverContent>
    </Popover>
  );
};

export default SchoolComboBox;
