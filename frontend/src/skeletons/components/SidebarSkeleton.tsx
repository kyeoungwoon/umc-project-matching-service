import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarGroup,
  SidebarGroupContent,
  SidebarGroupLabel,
  SidebarHeader,
  SidebarMenu,
  SidebarMenuItem,
} from '@styles/components/ui/sidebar';
import { Skeleton } from '@styles/components/ui/skeleton';

const SidebarSkeleton = () => {
  return (
    <Sidebar>
      {/* Header: Logo */}
      <SidebarHeader className={'m-3 flex items-start'}>
        <Skeleton className="h-10 w-32" />
      </SidebarHeader>

      {/* Content: Menu Items */}
      <SidebarContent>
        {/* Simulate two menu groups */}
        {Array.from({ length: 2 }).map((_, groupIndex) => (
          <SidebarGroup key={groupIndex}>
            <SidebarGroupLabel>
              <Skeleton className="h-4 w-20" />
            </SidebarGroupLabel>
            <SidebarGroupContent>
              <SidebarMenu>
                {/* Simulate 3 menu items per group */}
                {Array.from({ length: 3 }).map((_, itemIndex) => (
                  <SidebarMenuItem key={itemIndex}>
                    <div className="flex items-center gap-3 px-4 py-2">
                      <Skeleton className="h-5 w-5" />
                      <Skeleton className="h-5 w-24" />
                    </div>
                  </SidebarMenuItem>
                ))}
              </SidebarMenu>
            </SidebarGroupContent>
          </SidebarGroup>
        ))}
      </SidebarContent>

      {/* Footer: User Info */}
      <SidebarFooter className={'mb-5 px-4'}>
        <div className={'flex w-full flex-row items-center gap-3 px-3 py-2'}>
          <Skeleton className="h-12 w-12 rounded-full" />
          <div className={'flex w-full flex-col gap-2'}>
            <Skeleton className="h-5 w-32" />
            <Skeleton className="h-4 w-24" />
          </div>
        </div>
      </SidebarFooter>
    </Sidebar>
  );
};

export default SidebarSkeleton;
