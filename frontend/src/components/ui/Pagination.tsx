import { ChevronLeft, ChevronRight } from '@/icons'
import { cn } from '@/lib/utils'

interface PaginationProps {
  page: number
  totalPages: number
  totalElements: number
  size: number
  onPageChange: (page: number) => void
  className?: string
}

export function Pagination({
  page,
  totalPages,
  totalElements,
  size,
  onPageChange,
  className,
}: PaginationProps) {
  const from = totalElements === 0 ? 0 : page * size + 1
  const to = Math.min((page + 1) * size, totalElements)

  return (
    <div className={cn('flex items-center justify-between text-sm text-slate-600', className)}>
      <p>
        Showing <span className="font-medium text-slate-900">{from}–{to}</span> of{' '}
        <span className="font-medium text-slate-900">{totalElements}</span> results
      </p>
      <div className="flex items-center gap-1">
        <button
          disabled={page === 0}
          onClick={() => onPageChange(page - 1)}
          className={cn(
            'flex h-8 w-8 items-center justify-center rounded-lg border border-slate-200 transition-colors',
            page === 0
              ? 'cursor-not-allowed opacity-40'
              : 'hover:bg-slate-100',
          )}
        >
          <ChevronLeft className="h-4 w-4" />
        </button>
        <span className="px-3 font-medium">
          {page + 1} / {totalPages || 1}
        </span>
        <button
          disabled={page >= totalPages - 1}
          onClick={() => onPageChange(page + 1)}
          className={cn(
            'flex h-8 w-8 items-center justify-center rounded-lg border border-slate-200 transition-colors',
            page >= totalPages - 1
              ? 'cursor-not-allowed opacity-40'
              : 'hover:bg-slate-100',
          )}
        >
          <ChevronRight className="h-4 w-4" />
        </button>
      </div>
    </div>
  )
}
