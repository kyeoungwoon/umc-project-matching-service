import { ReactNode } from 'react';

import type { Metadata } from 'next';
import localFont from 'next/font/local';

import { GoogleAnalytics } from '@next/third-parties/google';
import { Analytics } from '@vercel/analytics/next';
import { SpeedInsights } from '@vercel/speed-insights/next';
import clsx from 'clsx';

import { Toaster } from '@styles/components/ui/sonner';
import '@styles/globals.css';

import QueryClientProviders from '@app/query-client-providers';

const pretendard = localFont({
  src: '../assets/fonts/pretendard/PretendardVariable.woff2',
  variable: '--font-pretendard',
  display: 'swap',
  weight: '45 920',
});

export const metadata: Metadata = {
  title: process.env.NODE_ENV === 'production' ? 'UPMS' : '[DEV] UPMS',
  description: 'UMC Project Matching Service',
};

const RootLayout = ({
  children,
}: Readonly<{
  children: ReactNode;
}>) => {
  const GA_ID = process.env.NEXT_PUBLIC_GA_ID;

  return (
    <html lang="ko">
      <body className={clsx('flex min-h-screen flex-col', pretendard.variable)}>
        <QueryClientProviders>
          <SpeedInsights />
          <Analytics />
          <Toaster />
          {children}
        </QueryClientProviders>
      </body>
      {GA_ID && <GoogleAnalytics gaId={GA_ID} />}
    </html>
  );
};

export default RootLayout;
