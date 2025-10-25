'use client';

import { ReactNode } from 'react';

import { useRedirectToHomeIfNotAnyAdmin } from '@common/hooks/check-challenger-permissions';

const AdminLayout = ({ children }: { children?: ReactNode }) => {
  useRedirectToHomeIfNotAnyAdmin();

  return <>{children}</>;
};

export default AdminLayout;
