'use client';

import { ReactNode } from 'react';

const HomeLayout = ({ children }: { children?: ReactNode }) => {
  return (
    <>
      {/*<Header section={HEADER_SECTION.MAIN} />*/}
      {children}
    </>
  );
};

export default HomeLayout;
