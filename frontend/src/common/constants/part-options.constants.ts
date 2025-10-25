import { ComboboxOption } from '@styles/components/ui/combobox';

import { ChallengerPartEnum } from '@api/types/common';

import { parsePart } from '@common/utils/parse-userinfo';

export const partOptionLabels: ComboboxOption[] = Object.values(ChallengerPartEnum).map((part) => ({
  value: part,
  label: parsePart(part),
}));
