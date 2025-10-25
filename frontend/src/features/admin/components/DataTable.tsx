'use client';

import { useState } from 'react';

import {
  ColumnDef,
  ColumnFiltersState,
  PaginationState,
  SortingState,
  VisibilityState,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  useReactTable,
} from '@tanstack/react-table';
import { ChevronDownIcon, ChevronUpIcon, ChevronsUpDownIcon } from 'lucide-react';

import { Button } from '@styles/components/ui/button';
import { Input } from '@styles/components/ui/input';
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@styles/components/ui/select';
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@styles/components/ui/table';

export type FilterType = 'text' | 'select';

export interface FilterConfig {
  columnId: string;
  placeholder: string;
  type?: FilterType; // 'text' | 'select'
  options?: Array<{ label: string; value: string }>; // select용
}
interface DataTableProps<TData, TValue> {
  columns: ColumnDef<TData, TValue>[];
  data: TData[];
  filterConfigs?: FilterConfig[];
}

export function DataTable<TData, TValue>({
  columns,
  data,
  filterConfigs,
}: DataTableProps<TData, TValue>) {
  const [sorting, setSorting] = useState<SortingState>([]);
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([]);
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({});
  const [pagination, setPagination] = useState<PaginationState>({
    pageIndex: 0, // 첫 페이지 인덱스
    pageSize: 20, // 페이지당 보여줄 행의 수
  });

  const table = useReactTable({
    data,
    columns,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    onSortingChange: setSorting,
    getSortedRowModel: getSortedRowModel(),
    onColumnFiltersChange: setColumnFilters,
    getFilteredRowModel: getFilteredRowModel(),
    onColumnVisibilityChange: setColumnVisibility,
    onPaginationChange: setPagination,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      pagination,
    },
  });

  return (
    <div className="flex flex-col gap-4">
      {/*{filterConfigs && filterConfigs.length > 0 && (*/}
      {/*  <div className="flex items-center gap-2">*/}
      {/*    {filterConfigs.map((config) => (*/}
      {/*      <Input*/}
      {/*        key={config.columnId}*/}
      {/*        placeholder={config.placeholder}*/}
      {/*        value={(table.getColumn(config.columnId)?.getFilterValue() as string) ?? ''}*/}
      {/*        onChange={(event) =>*/}
      {/*          table.getColumn(config.columnId)?.setFilterValue(event.target.value)*/}
      {/*        }*/}
      {/*        className="w-1/8 max-w-sm min-w-50"*/}
      {/*      />*/}
      {/*    ))}*/}
      {/*  </div>*/}
      {/*)}*/}

      {/* 필터 영역 */}
      <div className="flex gap-4 py-4">
        {filterConfigs &&
          filterConfigs.map((config) => {
            const column = table.getColumn(config.columnId);

            if (!column) return null;

            // 텍스트 입력 필터
            if (config.type === 'text' || !config.type) {
              return (
                <Input
                  key={config.columnId}
                  placeholder={config.placeholder}
                  value={(column.getFilterValue() as string) ?? ''}
                  onChange={(event) => column.setFilterValue(event.target.value)}
                  className="max-w-sm"
                />
              );
            }

            // 드롭다운 필터
            if (config.type === 'select' && config.options) {
              return (
                <Select
                  key={config.columnId}
                  value={(column.getFilterValue() as string) ?? ''}
                  onValueChange={(value) => column.setFilterValue(value === 'all' ? '' : value)}
                >
                  <SelectTrigger className="w-[180px]">
                    <SelectValue placeholder={config.placeholder} />
                  </SelectTrigger>
                  <SelectContent>
                    <SelectItem value="all">전체</SelectItem>
                    {config.options.map((option) => (
                      <SelectItem key={option.value} value={option.value}>
                        {option.label}
                      </SelectItem>
                    ))}
                  </SelectContent>
                </Select>
              );
            }

            return null;
          })}
      </div>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {/*{header.isPlaceholder*/}
                      {/*  ? null*/}
                      {/*  : flexRender(header.column.columnDef.header, header.getContext())}*/}
                      {header.isPlaceholder ? null : (
                        <div
                          {...{
                            className: header.column.getCanSort()
                              ? 'cursor-pointer select-none flex items-center gap-1'
                              : '',
                            onClick: header.column.getToggleSortingHandler(),
                          }}
                        >
                          {flexRender(header.column.columnDef.header, header.getContext())}
                          {{
                            asc: <ChevronUpIcon className="ml-2 h-4 w-4" />,
                            desc: <ChevronDownIcon className="ml-2 h-4 w-4" />,
                          }[header.column.getIsSorted() as string] ??
                            (header.column.getCanSort() ? (
                              <ChevronsUpDownIcon className="text-muted-foreground/50 ml-2 h-4 w-4" />
                            ) : null)}
                        </div>
                      )}
                    </TableHead>
                  );
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => {
                return (
                  <TableRow key={row.id} data-state={row.getIsSelected() && 'selected'}>
                    {row.getVisibleCells().map((cell) => (
                      <TableCell key={cell.id}>
                        {flexRender(cell.column.columnDef.cell, cell.getContext())}
                      </TableCell>
                    ))}
                  </TableRow>
                );
              })
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  결과가 없습니다.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-between px-2">
        <div className="text-muted-foreground flex-1 text-sm">
          총 {table.getFilteredRowModel().rows.length}개의 결과가 있습니다.
        </div>
        <div className="flex items-center space-x-6 lg:space-x-8">
          <div className="flex items-center space-x-2">
            <p className="text-sm font-medium">페이지당 행</p>
            <Select
              value={`${table.getState().pagination.pageSize}`}
              onValueChange={(value) => {
                table.setPageSize(Number(value));
              }}
            >
              <SelectTrigger className="h-8 w-[70px]">
                <SelectValue placeholder={table.getState().pagination.pageSize} />
              </SelectTrigger>
              <SelectContent side="top">
                {[10, 20, 30, 40, 50].map((pageSize) => (
                  <SelectItem key={pageSize} value={`${pageSize}`}>
                    {pageSize}
                  </SelectItem>
                ))}
              </SelectContent>
            </Select>
          </div>
          <div className="flex w-[100px] items-center justify-center text-sm font-medium">
            {table.getState().pagination.pageIndex + 1} / {table.getPageCount()} 페이지
          </div>
          <div className="flex items-center space-x-2">
            <Button
              variant="outline"
              size="sm"
              onClick={() => table.previousPage()}
              disabled={!table.getCanPreviousPage()}
            >
              이전
            </Button>
            <Button
              variant="outline"
              size="sm"
              onClick={() => table.nextPage()}
              disabled={!table.getCanNextPage()}
            >
              다음
            </Button>
          </div>
        </div>
      </div>
    </div>
  );
}
